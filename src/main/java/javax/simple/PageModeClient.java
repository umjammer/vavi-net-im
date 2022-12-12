package javax.simple;
/**
 * Page mode instant messaging is instant messaging without the establishment of a network session.
 * This interface represents a page mode IM client with a certain context. The context
 * of a PageModeClient includes the address of the remote client, whether the outgoing
 * MESSAGE requests are anonymous, and the set of default headers to be used
 * in outgoing MESSAGE requests.
 * Incoming MESSAGE requests are passed to that PageModeClient instance with a remote
 * client address equal to the From address in the message, and then to its registered
 * PageModeClientListener. If there is no matching PageModeClient
 * instance, the incoming message is passed to the default PageModeClient instance.
 * The default PageModeClient instance can be obtained via
 * 
 * <A href="SimpleFactory.html">SimpleFactory</A>
 * .getDefaultPageModeClient method.
 * 
 * If more than one instance of PageModeClient exists
 * for a given remote user agent, then the first one created will receive incoming
 * requests and responses from that user agent.
 * 
 * 
 * Example 1: Send an instant message to a user with a known address
 * 
 */
public interface PageModeClient extends javax.simple.UserAgent {
    /**
     * Get the default MESSAGE headers
     * @return the default headers that are included in each outgoing MESSAGE
     */
    javax.simple.Header[] getDefaultMessageHeaders();

    /**
     * Get the remote client's address for this PageModeClient.
     * @return the remote client's address in the context of this PageModeClient
     */
    javax.simple.Address getRemoteClient();

    /**
     * Checks whether the From address in the context of this PageModeClient is anonymous.
     * @return true if the From address in the context is anonymous, false otherwise
     */
    boolean isAnonymous();

    /**
     * Send a message with an arbitrary body.
     * @param body the body of the MESSAGE request to be sent
     * @param extraHeaders the extra headers specific to this particular message to
     *  be sent (for example, Date header as a time stamp of the sent Message). It may be null
     * @return transaction ID of the sent MESSAGE request.
     * @throws java.lang.IllegalArgumentException when extraHeaders includes headers that are not allowed.
     * @throws javax.simple.SimpleException when errors occur in sending the message.
     */
    java.lang.String sendMessage(javax.simple.MessageBody body, javax.simple.Header[] extraHeaders) throws javax.simple.SimpleException, java.lang.IllegalArgumentException;

    /**
     * Sends a String message with default charset. See
     * 
     * <A href="Configuration.html">Configuration</A>
     * .setDefaultCharset
     * for how the character encoding is determined.
     * @param message the text message to be sent as the body of MESSAGE request
     * @param extraHeaders the extra headers specific to this particular message to be sent
     *  (for example, Date header as a time stamp of the sent Message). It may be null;
     * @return transaction ID of the sent MESSAGE request
     * @throws java.lang.IllegalArgumentException if extraHeaders parameter includes headers that are not allowed.
     * @throws javax.simple.SimpleException when errors occurred in sending the message.
     * @throws java.io.UnsupportedEncodingException
     */
    java.lang.String sendMessage(java.lang.String message, javax.simple.Header[] extraHeaders) throws java.io.UnsupportedEncodingException, javax.simple.SimpleException;

    /**
     * Send a text message using the specified character encoding.
     * @param message the text message to be sent as the body of MESSAGE request
     * @param charset the character encoding for the text message. When it is null,
     *  the default character encoding is used.
     *  See <A href="Configuration.html">Configuration</A>.setDefaultCharset
     *  for how the character encoding is determined.
     * @return transaction ID of the sent MESSAGE request
     * @throws java.io.UnsupportedEncodingException when the charset parameter
     *  is not supported by the implementation.
     * @throws javax.simple.SimpleException when errors occur in sending the message.
     */
    java.lang.String sendMessage(java.lang.String message, java.lang.String charset) throws java.io.UnsupportedEncodingException, javax.simple.SimpleException;

    /**
     * Set default headers used in all outgoing MESSAGE requests made via this
     * PageModeClient. Multiple
     * headers of the same header name can be included in defaultHeaders when allowed by the SIP protocol.
     * @param defaultHeaders the default headers that are included in each outgoing MESSAGE
     *  request from this PageModeClient. Each time this method is called, it starts with
     *  the default base set of headers (no accumulation). If defaultHeaders is null,
     *  previously set default headers are cleared.
     * @throws java.lang.IllegalArgumentException if defaultHeaders includes headers that are not allowed
     */
    void setDefaultMessageHeaders(javax.simple.Header[] defaultHeaders);

}
