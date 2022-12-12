package javax.imps;
/**
 * General-purpose status that is returned at each API call, or presented at
 * each event, with API-specific or event-specific data.
 * 
 * <p>
 * IMPSStatus has these states:
 * <pre>
 * PENDING: a request has been sent, but the response
 * from the server hasn't yet been
 * received. Only the transaction ID and the outgoing request are
 * accessible at this stage.
 * OK: The response from the server was received and the operation
 * was successful.  The IMPSStatus can be used to retrieve the success resultCode
 * as well as data associated with the response.
 * </pre>
 * {@link javax.imps.IMPSRequest}
 * defines all request types along
 * with associated data.
 * <pre>
 * FAIL: The response from the server was received and the operation
 * failed.  The IMPSStatus can be used to retrieve the error resultCode.
 * 
 * TIMEOUT: The response from the server was not received within
 * the required response time.  The IMPSStatus can then be used to retrieve
 * the context of the failure of the request.
 * </pre>
 * 
 * 
 * 
 * This example shows possible handing of IMPSStatus'
 * resultCode and protocol specific errors.
 * <p>
 * <pre>
 * IMPSTid tid = connection.login(user, password);
 * if (status.getResultCode() == IMPSStatus.OK) {
 *   IMPSRequest r = status.getRequest();
 *   if (r.getRequestType == IMPSRequest.LOGIN) {
 *     // change client status to logged in
 *   }
 * } else if (status.getResultCode() == IMPSStatus.PENDING) {
 *   // show busy indication to client
 * } else if (status.getResultCode == IMPSStatus.FAIL) {
 *   String protocol = connection.getServiceInformation().getProtocolName();
 *   if(protocol.compareTo("SIP")==0) {
 *     // test for SIP specific error code
 *   } else if (protocol.compareTo("XMPP")==0) {
 *     // test for XMPP specific error description
 *   } else if (protocol.compareTo("WV")==0) {
 *     // test for WV specific error code
 *   } else {
 *     // check protocol name for non-standard protocol
 *   }
 * } else if (status.getResultCode == IMPSStatus.TIMEOUT) {
 * }
 * </pre>
 * 
 * This example shows how to retrieve data content from
 * a response to a previous request.
 * 
 * <pre>
 * public void processRequest(IMPSConnection c, IMPSRequest request)
 * {
 *   switch (request.getType()) {
 *   // process notification message containing the presence information
 *   // of a presentiy
 *   case IMPSRequest.PR_UPDATE :
 *     PresenceInformation pinfo = (PresenceInformation)request.getData();
 *     System.out.println("Received a new instant message");
 *     System.out.println("  Presence Entity's IMPSAddress:  " +
 *       pinfo.getAddress().getURI());
 *     System.out.println("  Time Zone of The presence Entity: " +
 *       pinfo.getTimeZone());
 *     System.out.println("  Availability of Presence Entity:    " +
 *       pinfo.getAvailability());
 *     break;
 *   }
 * }
 * </pre>
 */
public interface IMPSStatus {
    /**
     * IMPSRequest has completed with failure.  getResultCode
     * can be used to retrieve the associated error code.
     */
    int FAIL=1;

    /**
     * IMPSRequest has completed with success
     */
    int OK=0;

    /**
     * IMPSRequest is pending.
     */
    int PENDING=2;

    /**
     * IMPSRequest has timed out.  getResultCode
     * can be used to retrieve the associated error code.
     */
    int TIMEOUT=3;

    /**
     * Get the object embodying the content of this message.
     * Some requests produce responses which carry structured data.
     * The data type depends on the request type, and is specified
     * in the definition of each request type found in
     * {@link javax.imps.IMPSRequest}
     * .
     * 
     * The implementation parses the message content to construct the
     * object.  The application casts the return value of getData()
     * according to the type specified in IMPSRequest.
     * Examples of this are found in the descriptions of the following
     * interfaces: IMPSContactList, IMPSGroup, PresenceWatcher, IMPSRequest,
     * IMPSService, IMPSSearch
     * 
     * @return content of a message
     */
    java.lang.Object getData();

    /**
     * Get the protocol level response code.
     * 
     * 
     * SIP: Response codes and descriptive text for SIP are defined in [SIP 3261, sec 21].
     * 
     * 
     * XMPP: Result code is always -1 for XMPP  responses, since there are no numeric
     * encoding in XMPP (see [XMPP Core, sec 4.6]).
     * 
     * 
     * Response codes and descriptive text for Wireless Village are defined in
     * [WV Client Server Protocol Session and Transactions sec 10].
     * 
     * 
     * @return The protocol level result code
     */
    int getProtocolResultCode();

    /**
     * Get the protocol response textual description, if available.
     * 
     * SIP: Response codes and descriptive text for SIP are defined in [SIP 3261, sec 21].
     * 
     * XMPP: For XMPP implementations, textual description consists of two parts:
     * -  (see XMPP Core, sec 4.6.3].
     * Note that [XMPP Core sec 4.6.2] states that the optional
     * text SHOULD NOT be used as the error message presented to the user.
     * 
     * Response codes and descriptive text for Wireless Village are defined in
     * [WV Client Server Protocol Session and Transactions sec 10].
     * 
     * 
     * @return Descriptive text for the result, or null if not available.
     */
    java.lang.String getProtocolResultText();

    /**
     * Get the related UserID, ClientID, GroupID, ScreenName, ContactListID, MessageID etc.
     * @return The list of related IDs
     */
    javax.imps.IMPSAddress[] getRelatedIDs();

    /**
     * Get the request that corresponds to the incoming response.
     * @return the matching request corresponding to this response.
     */
    javax.imps.IMPSRequest getRequest();

    /**
     * Get the result code, either OK, FAIL, PENDING, or TIMEOUT.
     * @return The result code
     */
    int getResultCode();

    /**
     * Gets the related
     * {@link javax.imps.StoredMessageRecord}
     * s.
     * @return list of message properties
     */
    javax.imps.StoredMessageRecord[] getStoredMessageRecords();

    /**
     * Get the transaction id of the response. This id is used to match request-response
     * pairs.
     * @return an instance of IMPSTid representing the transaction id.
     */
    javax.imps.IMPSTid getTid();

}
