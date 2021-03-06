// Copyright 2018 Google LLC
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


package com.google.api.ads.adwords.jaxws.v201806.cm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             Error indicating that a reviewable was not saved because it contained a policy summary with
 *             a PROHIBITED or FULLY_LIMITED policy topic entry.
 *           
 * 
 * <p>Java class for PolicyFindingError complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PolicyFindingError">
 *   &lt;complexContent>
 *     &lt;extension base="{https://adwords.google.com/api/adwords/cm/v201806}ApiError">
 *       &lt;sequence>
 *         &lt;element name="reason" type="{https://adwords.google.com/api/adwords/cm/v201806}PolicyFindingError.Reason" minOccurs="0"/>
 *         &lt;element name="policySummary" type="{https://adwords.google.com/api/adwords/cm/v201806}PolicySummary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyFindingError", propOrder = {
    "reason",
    "policySummary"
})
public class PolicyFindingError
    extends ApiError
{

    @XmlSchemaType(name = "string")
    protected PolicyFindingErrorReason reason;
    protected PolicySummary policySummary;

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link PolicyFindingErrorReason }
     *     
     */
    public PolicyFindingErrorReason getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicyFindingErrorReason }
     *     
     */
    public void setReason(PolicyFindingErrorReason value) {
        this.reason = value;
    }

    /**
     * Gets the value of the policySummary property.
     * 
     * @return
     *     possible object is
     *     {@link PolicySummary }
     *     
     */
    public PolicySummary getPolicySummary() {
        return policySummary;
    }

    /**
     * Sets the value of the policySummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicySummary }
     *     
     */
    public void setPolicySummary(PolicySummary value) {
        this.policySummary = value;
    }

}
