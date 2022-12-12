package javax.simple;
/**
 * RequestMessage represents a SIP/SIMPLE request message.
 * The application does not directly instantiate request messages, instead
 * it uses various interfaces to indirectly create and send SIP/SIMPLE requests.
 * However, incoming requests and any outgoing requests that are challenged or
 * time out are passed to the application through listener methods.
 * The application uses this interface to process such request messages.
 * 
 * The general format of SIP request message (per RFC 3261) is as follows:
 * 
 * <pre>
 * Method SP Request-URI SP SIP-Version
 * CRLF
 * message-header-1
 * CRLF
 * message-header-2
 * CRLF
 * ...
 * 
 * CRLF
 * [ message-body ]
 * </pre>
 * 
 * where
 * 
 * is the Carriage-Return Line-Feed sequence
 * 
 * is the method field in a SIP request
 * 
 * is a SIP or SIPS URI described in Section 19.1 of RFC 3261 and which
 * indicates the user or service to which this request is addressed
 * 
 * is a space character
 * 
 */
public interface RequestMessage extends javax.simple.Message {
    /**
     * Retrieve the SIP/SIMPLE method of a message.
     * @return the SIP/SIMPLE method
     */
    java.lang.String getMethod();

    /**
     * Retrieve the request-URI of a request message
     * @return the request-URI
     */
    java.lang.String getRequestURI();

    /**
     * Check whether an incoming request is authenticated. Message is authenticated
     * if it includes valid Authorization or Proxy-Authorization headers
     * @return true if message is an authenticated incoming message, false if not
     */
    boolean isAuthenticated();

}
