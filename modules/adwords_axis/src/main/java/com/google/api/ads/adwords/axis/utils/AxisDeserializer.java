// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.ads.adwords.axis.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.DeserializerFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.client.AxisClient;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.SerializerFactory;
import org.apache.axis.encoding.TypeMapping;
import org.apache.axis.encoding.TypeMappingRegistryImpl;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.SOAPEnvelope;
import org.xml.sax.InputSource;

/** Utility for deserializing XML to Axis objects. */
public class AxisDeserializer {
  private static final String SOAP_START_BODY =
      "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
          + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
          + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
          + "<soapenv:Body>";

  private static final String SOAP_END_BODY = "</soapenv:Body></soapenv:Envelope>";

  private static final String TAG_MUTATE_RESPONSE = "mutateResponse";

  private static final String TAG_RVAL = "rval";

  private static final String INDENT_AMOUNT = "4";

  public <ResultT> List<ResultT> deserializeBatchJobMutateResults(
      URL url, List<TypeMapping> serviceTypeMappings, Class<ResultT> resultClass, QName resultQName)
      throws Exception {

    return deserializeBatchJobMutateResults(
        url, serviceTypeMappings, resultClass, resultQName, 0, Integer.MAX_VALUE);
  }

  public <ResultT> List<ResultT> deserializeBatchJobMutateResults(
      URL url,
      List<TypeMapping> serviceTypeMappings,
      Class<ResultT> resultClass,
      QName resultQName,
      int startIndex,
      int numberResults)
      throws Exception {

    List<ResultT> results = Lists.newArrayList();

    // Build a wrapped input stream from the response.
    InputStream wrappedStream = buildWrappedInputStream(url, startIndex, numberResults);

    // Create a MessageContext with a new TypeMappingRegistry that will only
    // contain deserializers derived from serviceTypeMappings and the
    // result class/QName pair.
    MessageContext messageContext = new MessageContext(new AxisClient());
    TypeMappingRegistryImpl typeMappingRegistry = new TypeMappingRegistryImpl(true);
    messageContext.setTypeMappingRegistry(typeMappingRegistry);

    // Construct an Axis deserialization context.
    DeserializationContext deserializationContext =
        new DeserializationContext(
            new InputSource(wrappedStream), messageContext, Message.RESPONSE);

    // Register all type mappings with the new type mapping registry.
    TypeMapping registryTypeMapping =
        typeMappingRegistry.getOrMakeTypeMapping(messageContext.getEncodingStyle());
    registerTypeMappings(registryTypeMapping, serviceTypeMappings);

    // Parse the wrapped input stream.
    deserializationContext.parse();

    // Read the deserialized mutate results from the parsed stream.
    SOAPEnvelope envelope = deserializationContext.getEnvelope();
    MessageElement body = envelope.getFirstBody();

    for (Iterator<?> iter = body.getChildElements(); iter.hasNext(); ) {
      Object child = iter.next();
      MessageElement childElm = (MessageElement) child;
      @SuppressWarnings("unchecked")
      ResultT mutateResult = (ResultT) childElm.getValueAsType(resultQName, resultClass);
      results.add(mutateResult);
    }
    return results;
  }

  /**
   * Returns a new input stream that wraps the download URL into a SOAP body with pagination, so it
   * can be parsed by Axis.
   */
  private InputStream buildWrappedInputStream(URL url, int startIndex, int numberResults)
      throws IOException, XMLStreamException, TransformerException {
    Preconditions.checkArgument(startIndex >= 0, "startIndex must not be negative");
    Preconditions.checkArgument(numberResults > 0, "numberResults must be positive");

    StringWriter stringWriter = new StringWriter();

    XMLEventReader xmlEventReader =
        XMLInputFactory.newFactory().createXMLEventReader(url.openStream());
    XMLEventWriter xmlEventWriter =
        XMLOutputFactory.newFactory().createXMLEventWriter(stringWriter);
    XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();

    int index = 0;
    boolean included = false;

    while (xmlEventReader.hasNext()) {
      XMLEvent event = xmlEventReader.nextEvent();

      if (event.isStartElement()) {
        String name = ((StartElement) event).getName().getLocalPart();
        if (name.equals(TAG_MUTATE_RESPONSE)) {
          // Write the <mutateResponse> tag.
          xmlEventWriter.add(event);
          continue;
        } else if (name.equals(TAG_RVAL) && ++index > startIndex) {
          // Included the eligible <rval> tags and sub tags by setting the 'included' flag.
          included = true;
        }
      } else if (event.isEndElement()) {
        String name = ((EndElement) event).getName().getLocalPart();
        if (name.equals(TAG_MUTATE_RESPONSE)) {
          // Write the </mutateResponse> tag.
          xmlEventWriter.add(event);
          break;
        } else if (included && name.equals(TAG_RVAL) && index == startIndex + numberResults) {
          // Write the last </rval> tag and append a </mutateResponse> tag, if this has written
          // the specified number of results.
          xmlEventWriter.add(event);
          xmlEventWriter.add(xmlEventFactory.createEndElement("", "", TAG_MUTATE_RESPONSE));
          break;
        }
      }

      if (included) {
        // Write the included tags and events.
        xmlEventWriter.add(event);
      }
    }
    xmlEventReader.close();
    xmlEventWriter.close();

    InputStream truncatedInputStream =
        ByteSource.wrap(stringWriter.toString().getBytes(UTF_8)).openStream();

    // Pass the input stream through a Transformer that removes the XML
    // declaration. Create a new TransformerFactory and Transformer on each invocation
    // since these objects are <em>not</em> thread safe.
    Transformer omitXmlDeclarationTransformer = TransformerFactory.newInstance().newTransformer();
    omitXmlDeclarationTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
    omitXmlDeclarationTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    omitXmlDeclarationTransformer.setOutputProperty(
        "{http://xml.apache.org/xslt}indent-amount", INDENT_AMOUNT);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    StreamResult streamResult = new StreamResult(outputStream);
    Source xmlSource = new StreamSource(truncatedInputStream);
    omitXmlDeclarationTransformer.transform(xmlSource, streamResult);

    return ByteSource.concat(
            ByteSource.wrap(SOAP_START_BODY.getBytes(UTF_8)),
            ByteSource.wrap(outputStream.toByteArray()),
            ByteSource.wrap(SOAP_END_BODY.getBytes(UTF_8)))
        .openStream();
  }

  /** Adds the type mappings in the list to {@code registryTypeMapping}. */
  private void registerTypeMappings(
      TypeMapping registryTypeMapping, List<TypeMapping> typeMappings) {
    Preconditions.checkNotNull(registryTypeMapping, "Null registry type mapping");
    Preconditions.checkNotNull(typeMappings, "Null type mappings");
    Preconditions.checkArgument(!typeMappings.isEmpty(), "Empty type mappings");
    for (TypeMapping typeMapping : typeMappings) {
      for (Class<?> mappingClass : typeMapping.getAllClasses()) {
        QName classQName = typeMapping.getTypeQName(mappingClass);
        DeserializerFactory deserializer = typeMapping.getDeserializer(mappingClass, classQName);
        if (deserializer != null && !registryTypeMapping.isRegistered(mappingClass, classQName)) {
          registryTypeMapping.register(
              mappingClass, classQName, (SerializerFactory) null, deserializer);
        }
      }
    }
  }
}
