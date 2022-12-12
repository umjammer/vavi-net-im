package javax.simple;
/**
 * Obtain implementation objects for each interface defined in the API.
 * By convention, a get method
 * returns a singleton instance for the interface's implementation, and
 * a create method returns a new instance for the interface's implementation.
 * 
 * SimpleFactory is a singleton class, that is, there is only
 * one instance of this class. An application must invoke the getInstance()
 * method to obtain the reference to this singleton instance.
 * 
 * 
 * An implementation of this API must implement all the abstract methods of the
 * SimpleFactory class in a subclass named "javax.simple.SimpleFactoryImpl".
 * A SimpleFactoryImpl must be defined in the "javax.simple"
 * package and must have a constructor that takes no parameters. The SimpleFactory is the
 * only class in this API that requires this fixed implementation package name. The package name of the
 * SimpleFactoryImpl
 * is fixed so that the application need not specify explicitly the package name of the
 * proprietary implementation.
 * 
 * 
 * Example 1: Obtain the singleton instance of SimpleFactory
 * 
 */
public abstract class SimpleFactory {
    public SimpleFactory() {
         //TODO codavaj!!
    }

    /**
     * Create an instance of the
     * <A href="Address.html">Address</A>
     * from a string conformant
     * to the format defined in Section 19.1 of RFC 3261. The display name, the URI
     * port and the URI user are optional, while the URI scheme and the URI host are mandatory.
     * @param address SIP address embodied in a string.
     * @throws javax.simple.SimpleException when address is an invalid address string.
     */
    public abstract javax.simple.Address createAddress(java.lang.String address) throws javax.simple.SimpleException;

    /**
     * Creates an instance of
     * <A href="Address.html">Address</A>
     * from the values of its components.
     * The values null and " "(empty string) are
     * prohibited for the scheme and the host.
     * @param displayName display name (e.g., "Winston Smith"). The value null is allowed.
     * @param scheme the URI scheme name (e.g., "sip"). The value null or " "(empty string) is not allowed.
     * @param user URI user name (e.g., "wsmith"). The value null is allowed.
     * @param host URI host name (e.g., "panasonic.com"). The value null or " "(empty string) is not allowed.
     * @param port URI port number of the URI (e.g., 5060).
     * @return address composed of indicated components
     * @throws java.lang.IllegalArgumentException when there are illegal arguments.
     */
    public abstract javax.simple.Address createAddress(java.lang.String displayName, java.lang.String scheme, java.lang.String user, java.lang.String host, int port) throws java.lang.IllegalArgumentException;

    /**
     * Create a SIP message A
     * <A href="Header.html">Header</A>
     * @param headerName header name, null not allowed
     * @param headerField headerField of the newly created <A href="Header.html">Header</A>,
     *         null not allowed
     * @return created <A href="Header.html">Header</A> object
     * @throws java.lang.IllegalArgumentException if headerName or headerField is an invalid
     */
    public abstract javax.simple.Header createHeader(java.lang.String headerName, java.lang.String headerField) throws java.lang.IllegalArgumentException;

    /**
     * Create empty message body (Content-Length: 0 ) with specified content headers.
     * @param contentHeaders headers relevant to a message body. Allowed headers are:
     *  Content-Type (mandatory)
     *      Content-Encoding
     *      Content-Language
     *      MIME-Version
     *      Content-Length (ignored)
     *  
     * 
     *  Content-Length header will be set automatically to 0.
     *  If the Content-Type is of text type and "charset" parameter is absent,
     *  the global default will be used.
     * @return contentHeaders <A href="MessageBody.html">MessageBody</A> depending on the supplied headers
     * @throws java.io.UnsupportedEncodingException if Content-Encoding is not supported
     * @throws java.lang.IllegalArgumentException if headers are invalid
     */
    public abstract javax.simple.MessageBody createMessageBody(javax.simple.Header[] contentHeaders) throws java.io.UnsupportedEncodingException, java.lang.IllegalArgumentException;

    /**
     * Create a page mode IM client which handles instant messaging exchange with the
     * indicated remote user agent.  If more than one instance of
     * 
     * <A href="PageModeClient.html">PageModeClient</A>
     * exists
     * for a given remote user agent, then the first one created will receive incoming
     * requests and responses from that user agent.
     * @param remoteClient address of the remote SIMPLE client with which this instance will handle
     *  instant message exchange
     * @param isAnonymous true if the outgoing MESSAGE requests will have an anonymous From address
     * @return instance of the <A href="PageModeClient.html">PageModeClient</A> interface.
     *  If the implementation does not support Instant Messaging, this method returns null.
     * @throws java.lang.IllegalArgumentException if remote client's address is null
     */
    public abstract javax.simple.PageModeClient createPageModeClient(javax.simple.Address remoteClient, boolean isAnonymous) throws java.lang.IllegalArgumentException;

    /**
     * Create instance of the
     * <A href="PageModeClient.html">PageModeClient</A>
     * based on incoming message.
     * If more than one instance of
     * <A href="PageModeClient.html">PageModeClient</A>
     * exists
     * for a given remote user agent, then the first one created will receive incoming
     * requests and responses from that user agent.
     * @param incomingMsg the incoming message whose "From" address is used to construct the
     *  <A href="PageModeClient.html">PageModeClient</A> object
     * @param isAnonymous true if the outgoing MESSAGE requests will have an anonymous From address
     * @return instance of the <A href="PageModeClient.html">PageModeClient</A> interface.
     * @throws java.lang.IllegalArgumentException if incomingMsg is invalid
     */
    public abstract javax.simple.PageModeClient createPageModeClient(javax.simple.RequestMessage incomingMsg, boolean isAnonymous) throws java.lang.IllegalArgumentException;

    /**
     * Obtain the singleton
     * <A href="Configuration.html">Configuration</A>
     * object
     * @return singleton <A href="Configuration.html">Configuration</A> object
     * @throws java.lang.InstantiationException if error occurred
     */
    public abstract javax.simple.Configuration getConfiguration() throws java.lang.InstantiationException;

    /**
     * Get the singleton default
     * <A href="PageModeClient.html">PageModeClient</A>
     * which is responsible for remote clients that
     * are not assigned to specific
     * <A href="PageModeClient.html">PageModeClient</A>
     * instances.
     * @return singleton default instance of <A href="PageModeClient.html">PageModeClient</A> interface.
     */
    public abstract javax.simple.PageModeClient getDefaultPageModeClient();

    /**
     * Obtain singleton instance of the SimpleFactory class
     * @return singleton instance of this class
     * @throws javax.simple.SimpleException when the implementation for this API can not be found or
     *          when the initialization fails
     */
    public static javax.simple.SimpleFactory getInstance() throws javax.simple.SimpleException {
        return null; //TODO codavaj!!
    }

}
