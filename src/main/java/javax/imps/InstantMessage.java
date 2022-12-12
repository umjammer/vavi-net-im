package javax.imps;
/**
 * Used to create and access the content of instant messages that are sent
 * or received using IMPSRequest.
 * This class provides easy access to the content and key fields
 * of an InstantMessage. Content types are represented using
 * MIME types.
 * [IETF RFC 2045-2048].
 * 
 * When sending an IM, an application can control the following fields:
 * sender, recipient(s), and subject.  The content can be set, with
 * content type and encoding.
 * 
 * When receiving an IM, an application can access the following fields:
 * sender, recipient(s), and subject.  The content, content type, and
 * content encoding can be read. For XMPP, there is no MIME encoding, so
 * implementations supporting XMPP must return "text/plain" for mime-type.
 * 
 * Received messages may have message ids.  For example, message ids are branch ids or Message URIs in SIP.
 * XMPP messages don't have ids (although there are thread ids in XMPP). So XMPP implementations
 * must compute a local id that is unique per connection.
 * 
 * 
 * 
 * <pre>
 * // sending an instant message
 * IMPSConnection connection = factory.createIMPSConnection();
 * connection.addListener(this);
 * IMPSTid tid = connection.login(user, password);
 * // joinTransaction is application method defined in
 * // section
 * IMPSStatus status = joinTransaction(tid);
 * try {
 *   imclient = factory.createIMClient(connection);
 *   InstantMessage im = imclient.createMessage("hello world!");
 *   IMPSAddress to = factory.createIMPSAddress("sip:hugo@example.com",
 *   IMPSAddress.ADDRESS_USER);
 *   im.setRecipient(to);
 *   // sender not set, so is implied and configuration value is used
 *   imclient.sendMessage(im, false);  // no delivery report
 * } catch ( … ) { }
 * </pre>
 */
public interface InstantMessage extends javax.imps.Extensible {
    /**
     * Get the  content of a non-text instant message.
     * @return binary content as a byte array
     */
    byte[] getBinaryContent();

    /**
     * Get the  content of a text instant message.
     * @return text content of the instant message
     */
    java.lang.String getContent();

    /**
     * Get the MIME content-encoding.
     * @return content encoding
     */
    java.lang.String getContentEncoding();

    /**
     * Return the size of the serialized message content in bytes.
     * If the message content is mime-encoded, then the content size
     * is size of the content as transported.
     * @return message content size in bytes.
     */
    int getContentSize();

    /**
     * Get the MIME type for the message content.  [IETF RFC 2045-2048].
     * The content-type is the MIME content type for MIME-based protocols.
     * For XMPP, an implementation should use "text/plain".
     * @return the MIME content-type.
     */
    java.lang.String getMessageContentType();

    /**
     * Return the message ID.  Only valid for received messages, null otherwise.
     * @return the message id, or null
     */
    java.lang.String getMessageID();

    /**
     * Get the list of recipients.
     * @return the IMPSAddress for each recipient
     */
    javax.imps.IMPSAddress[] getRecipients();

    /**
     * Get the IMPSAddress for the sender of the instant message.
     * @return the address of the source of the instant message
     */
    javax.imps.IMPSAddress getSender();

    /**
     * Get the subject.
     * @return the subject text, or null if empty
     */
    java.lang.String getSubject();

    /**
     * Set the  content of a text instant message.
     * @param content text content
     */
    void setContent(java.lang.String content);

    /**
     * Set the MIME content-encoding.
     * @param encoding encoding identifier according to MIME.
     */
    void setContentEncoding(java.lang.String encoding);

    /**
     * Set the MIME content-type.
     * Applies to MIME message data, ignored otherwise.
     * @param ct the MIME type of the message content
     */
    void setContentType(java.lang.String ct);

    /**
     * Set the recipient for the instanct messages, replacing any previous recipient.
     * @param address address of recipient to be added
     */
    void setRecipient(javax.imps.IMPSAddress address);

    /**
     * Set the sender of the instant message.  If not specified, the default sender
     * address defaults to the configured address.  May be used to specify an anonymous
     * sender.
     * @param address the IMPSAddress for the sender of the instant message.
     */
    void setSender(javax.imps.IMPSAddress address);

    /**
     * Set the subject of the message.  If not specified, the subject is empty.
     * @param text the value of the subject of the message
     */
    void setSubject(java.lang.String text);

}
