package javax.imps;
/**
 * Used to send and receive InstantMessages.
 * There are two models of message delivery: Push and Notify-Get.
 * In the Push model, messages are directly sent to the recipient.
 * In the Notify-Get model,
 * the recipient is notified about the messages stored on a server
 * and the recipient can retrieve the messages later.
 * Push is the default delivery mode.
 * 
 * 
 * <pre>
 * // send a IM in PUSH mode
 * IMPSConnection connection = factory.createIMPSConnection();
 * connection.addListener(this);
 * IMPSTid tid = connection.login(user, password);
 * // joinTransaction is application method defined in
 * section
 * IMPSStatus status = joinTransaction(tid);
 * try {
 *   imclient = factory.createIMClient(connection);
 *   imclient.setDeliveryMethod(IMClient.IM_PUSH,1000,null);
 * 
 *   InstantMessage im = imclient.createMessage("hello world!");
 *   IMPSAddress to = factory.createIMPSAddress("sip:hugo@example.com",
 *   IMPSAddress.ADDRESS_USER);
 *   im.addRecipient(to);
 *   // sender not set, so is implied and configuration value is used
 *   imclient.sendMessage(im, false);  // no delivery report
 * } catch ( … ) { }
 * </pre>
 * 
 * The following example retrieves the message IDs of the stored
 * and undelivered messages.  It then iterates through the list,
 * forwarding selected ids to another device, and rejecting the rest.
 * 
 * <pre>
 * // retrieve messages and forward those that from "friends"
 * // and reject others
 * IMPSConnection connection = factory.createIMPSConnection();
 * connection.addListener(this);
 * IMPSTid tid = connection.login(user, password);
 * // joinTransaction is application method defined in
 * // <A href="../../overview-summary.html#_JOIN_TRANS_">Overview</A>
 * // section
 * IMPSStatus status = joinTransaction(tid);
 * 
 * try {
 *   imclient = factory.createIMClient(connection);
 *   // get the message list from the server
 *   tid = imclient.getMessageList(null,100);
 *   // joinTransaction is application method defined in
 *   <A href="../../overview-summary.html#_JOIN_TRANS_">Overview</A>
 *   section
 *   status = joinTransaction(tid);
 *   if(status.getResultCode() == IMPSStatus.OK) {
 *     // list of ids is embedded in the status
 *     StoredMessageRecord [] messageRecs = status.getStoredMessageRecords();
 *     if(messageRecs != null) {
 *       // iterate over the message list
 *       for(int i=0; i < messageRecs.length; i++) {
 *         if(messageRecs[i] != null) {
 *           if(isFriend(messageRecs[i].getSender())) {
 *             imclient.forwardIM(messageRecs[i].getMessageID(),myCellPhone);
 *           } else {
 *             imclient.rejectMessage(messageRecs[i].getMessageID());
 *           }
 *         }
 *       }
 *     }
 *   }
 * } catch ( … ) { }
 * </pre>
 * 
 */
public interface IMClient {
    /**
     * Notify-Get delivery method
     */
    int IM_NOTIFY_GET=2;

    /**
     * Push delivery method
     */
    int IM_PUSH=1;

    /**
     * Create a IM Message instance for sending arbitrary MIME-encoding content.
     * The default values are as follows: content is empty,
     * recipient is empty, subject is empty, sender is the configured
     * address of record for the user.
     * @param content content value
     * @param contentType MIME-type
     * @param contentEncoding encoding used for the MIME-type
     */
    javax.imps.InstantMessage createMessage(byte[] content, java.lang.String contentType, java.lang.String contentEncoding);

    /**
     * Create a IM Message instance for sending text messages.
     * The default values are as follows: content is empty,
     * recipient is empty, subject is empty, sender is the configured
     * address of record for the user.
     * @param messageText -
     */
    javax.imps.InstantMessage createMessage(java.lang.String messageText);

    /**
     * Forward a  non-retrieved message to another recipient. The message is
     * forwarded by the server to the forwarding address specified.
     * @param messageID the ID or URI of the message to be forwarded.
     * @param recipient address of type {@link javax.imps.IMPSAddress#ADDRESS_USER}, {@link javax.imps.IMPSAddress#ADDRESS_CONTACTLIST}, {@link javax.imps.IMPSAddress#ADDRESS_SCREENNAME}, or {@link javax.imps.IMPSAddress#ADDRESS_GROUP}
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     *  <P>
     *  The corresponding request type is {@link javax.imps.IMPSRequest#IM_FORWARD}
     * </P>
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid forwardIM(java.lang.String messageID, javax.imps.IMPSAddress recipient) throws javax.imps.IMPSException;

    /**
     * Get the list of  IM users who are in the Block and Grant Lists of
     * for this application.
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     *  If the request is successful, the status data contains an array of
     *  IMPSAccessControlList objects.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid getAccessControlList() throws javax.imps.IMPSException;

    /**
     * Allows an IM Client to retrieve an instant message by its ID from the server.
     * The IDs of all the messages stored in a server can be retrieved by using the
     * getMessageList method call.
     * @param messageID is the message ID of the message to be retrieved.
     * @return an instance of IMPSTid (transaction id) that identifies this request.  upon success,
     *  the status provides a {@link javax.imps.InstantMessage}.
     *  <P>
     *  The corresponding request type is {@link javax.imps.IMPSRequest#IM_GETMSG}
     * </P>
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid getMessage(java.lang.String messageID) throws javax.imps.IMPSException;

    /**
     * Retrieves the list of undelivered messages or group history stored in an IM server.
     * The IM Client can use this list either to reject or get some messages.
     * @param groupID indicates the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @param messageCountLimit indicates the maximum number of message information structure can be returned
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     *  <P>
     *  The corresponding request type is {@link javax.imps.IMPSRequest#IM_GETMLT}
     * </P>
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid getMessageList(javax.imps.IMPSAddress groupID, int messageCountLimit) throws javax.imps.IMPSException;

    /**
     * Rejects a single undelivered instant messages identified
     * by the message ID or message URI.
     * The IDs of all the messages stored in a server can be retrieved by using the
     * getMessageList method call.
     * @param messageID the message ID to be rejected.
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     *  <P>
     *  The corresponding request type is {@link javax.imps.IMPSRequest#IM_RJTMSG}
     * </P>
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid rejectMessage(java.lang.String messageID) throws javax.imps.IMPSException;

    /**
     * Rejects undelivered instant messages identified by the message ID or message URI.
     * The IDs of all the messages stored in a server can be retrieved by using the
     * getMessageList method call.
     * @param messageIDList is the list of the message IDs to be rejected.
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     *  <P>
     *  The corresponding request type is {@link javax.imps.IMPSRequest#IM_RJTMSG}
     * </P>
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid rejectMessages(java.lang.String[] messageIDList) throws javax.imps.IMPSException;

    /**
     * Send an Instant Message to user or group.
     * @param message message to send.  indicates the recipient's address,
     *  sender's address, content type, content.
     * @param deliveryReport indicates whether a delivery report is requested
     * @return an instance of IMPSTid (transaction id) that identifies this request.  No data is expected in the response.
     */
    javax.imps.IMPSTid sendMessage(javax.imps.InstantMessage message, boolean deliveryReport);

    /**
     * Set the application's access control list for receiving messages.
     * Each address in the ACL identifies a user who is either granted or
     * blocked access to send this application instant messages.
     * 
     * @param acl list of users who are either granted or blocked access to
     *  sending this application instant messages
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid setAccessControlList(javax.imps.IMPSAccessControlList acl) throws javax.imps.IMPSException;

    /**
     * Specifies the delivery method (IM_PUSH or IM_NOTIFY_GET) and the maximum message
     * size for Instant Messages. The precondition before calling this method is to
     * establish a session between the IM Client and the server.
     * @param deliveryMethod indicates the desired IM delivery method
     * @param acceptedContentLength indicates the maximum size of the message the
     *  IM Client would accept using IM_PUSH.
     * @param groupID indicates if the delivery method refers to a group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     *  <P>
     *  The corresponding request type is {@link javax.imps.IMPSRequest#IM_SETDEM}
     * </P>
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid setDeliveryMethod(int deliveryMethod, int acceptedContentLength, javax.imps.IMPSAddress groupID) throws javax.imps.IMPSException;

}
