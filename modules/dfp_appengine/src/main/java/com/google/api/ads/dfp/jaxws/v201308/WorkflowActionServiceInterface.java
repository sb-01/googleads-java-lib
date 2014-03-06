
package com.google.api.ads.dfp.jaxws.v201308;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * 
 *       Provides methods for adding, updating, and retrieving workflow actions.
 *       <p>
 *       To use this service, you need to have the new sales management solution
 *       enabled on your network. If you do not see a "Sales" tab in
 *       <a href="https://www.google.com/dfp">DoubleClick for Publishers (DFP)</a>,
 *       you will not be able to use this service.
 *     
 * 
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.1
 * 
 */
@WebService(name = "WorkflowActionServiceInterface", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WorkflowActionServiceInterface {


    /**
     * 
     *         Returns the {@link WorkflowAction} uniquely identified by the given ID.
     *         
     *         @param workflowActionId the ID of the workflow action, which must already exist
     *         @return the {@code WorkflowAction} uniquely identified by the given ID.
     *       
     * 
     * @param workflowActionId
     * @return
     *     returns com.google.api.ads.dfp.jaxws.v201308.WorkflowAction
     * @throws ApiException_Exception
     */
    @WebMethod
    @WebResult(name = "rval", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308")
    @RequestWrapper(localName = "getWorkflowAction", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308", className = "com.google.api.ads.dfp.jaxws.v201308.WorkflowActionServiceInterfacegetWorkflowAction")
    @ResponseWrapper(localName = "getWorkflowActionResponse", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308", className = "com.google.api.ads.dfp.jaxws.v201308.WorkflowActionServiceInterfacegetWorkflowActionResponse")
    public WorkflowAction getWorkflowAction(
        @WebParam(name = "workflowActionId", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308")
        Long workflowActionId)
        throws ApiException_Exception
    ;

    /**
     * 
     *         Gets a {@link WorkflowActionPage} of {@link WorkflowAction} objects that satisfy the given
     *         {@link Statement#query}. The following fields are supported for filtering:
     *         
     *         <table>
     *         <tr>
     *         <th scope="col">PQL Property</th>
     *         <th scope="col">Object Property</th>
     *         </tr>
     *         <tr>
     *         <td>id</td>
     *         <td>{@link WorkflowAction#id}</td>
     *         </tr>
     *         <tr>
     *         <td>name</td>
     *         <td>{@link WorkflowAction#name}</td>
     *         </tr>
     *         </table>
     *         
     *         @param filterStatement a Publisher Query Language statement used to filter a set of workflow
     *         actions
     *         @return the {@link WorkflowAction workflow actions} that match the given filter
     *       
     * 
     * @param filterStatement
     * @return
     *     returns com.google.api.ads.dfp.jaxws.v201308.WorkflowActionPage
     * @throws ApiException_Exception
     */
    @WebMethod
    @WebResult(name = "rval", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308")
    @RequestWrapper(localName = "getWorkflowActionsByStatement", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308", className = "com.google.api.ads.dfp.jaxws.v201308.WorkflowActionServiceInterfacegetWorkflowActionsByStatement")
    @ResponseWrapper(localName = "getWorkflowActionsByStatementResponse", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308", className = "com.google.api.ads.dfp.jaxws.v201308.WorkflowActionServiceInterfacegetWorkflowActionsByStatementResponse")
    public WorkflowActionPage getWorkflowActionsByStatement(
        @WebParam(name = "filterStatement", targetNamespace = "https://www.google.com/apis/ads/publisher/v201308")
        Statement filterStatement)
        throws ApiException_Exception
    ;

}