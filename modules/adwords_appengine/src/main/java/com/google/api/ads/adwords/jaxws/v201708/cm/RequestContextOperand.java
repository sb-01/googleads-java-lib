// Copyright 2017 Google Inc. All Rights Reserved.
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


package com.google.api.ads.adwords.jaxws.v201708.cm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             An operand in a function referring to a value in the request context.
 *           
 * 
 * <p>Java class for RequestContextOperand complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestContextOperand">
 *   &lt;complexContent>
 *     &lt;extension base="{https://adwords.google.com/api/adwords/cm/v201708}FunctionArgumentOperand">
 *       &lt;sequence>
 *         &lt;element name="contextType" type="{https://adwords.google.com/api/adwords/cm/v201708}RequestContextOperand.ContextType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestContextOperand", propOrder = {
    "contextType"
})
public class RequestContextOperand
    extends FunctionArgumentOperand
{

    @XmlSchemaType(name = "string")
    protected RequestContextOperandContextType contextType;

    /**
     * Gets the value of the contextType property.
     * 
     * @return
     *     possible object is
     *     {@link RequestContextOperandContextType }
     *     
     */
    public RequestContextOperandContextType getContextType() {
        return contextType;
    }

    /**
     * Sets the value of the contextType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestContextOperandContextType }
     *     
     */
    public void setContextType(RequestContextOperandContextType value) {
        this.contextType = value;
    }

}