package javax.simple;
/**
 * The Message interface and its sub-interfacs
 * 
 * <A href="RequestMessage.html">RequestMessage</A>
 * and
 * 
 * <A href="ResponseMessage.html">ResponseMessage</A>
 * are used to
 * access the components of a message - the message body and message headers.
 * In addition, methods such as getFrom and getTo
 * provide convenience functions for extracting header fields that
 * are frequently needed without requiring the application to parse
 * SIP message headers.
 * 
 * The format of a SIP/SIMPLE message is defined in RFC 3261:
 * 
 * start-line
 * CRLF
 * message-header-1
 * CRLF
 * message-header-2
 * CRLF
 * ...
 * 
 * CRLF
 * [ message-body ]
 * 
 * where
 * CRLF
 * is the Carriage-Return Line-Feed sequence.
 * 
 * An application is not expected to explicitly instantiate the Message interface.
 * SIP message construction is instead handled by the implementation when a SIMPLE message is
 * to be sent.  When a remote node issues a request or a response, the implementation automatically
 * generates an object with the Message interface and conveys this object
 * to the application via the
 * <A href="SimpleListener.html">SimpleListener</A>
 * or
 * 
 * <A href="PageModeClientListener.html">PageModeClientListener</A>
 * methods.
 * 
 * Here is an example SIP message using the INVITE method.
 * <pre>
 * INVITE sip:bob@panasonic.com SIP/2.0
 * Via: SIP/2.0/UDP pc33.panasonic.com;branch=z9hG4bK776asdhds
 * Max-Forwards: 70
 * To: Bob
 * From: Alice ;tag=1928301774
 * Call-ID: a84b4c76e66710@pc33.panasonic.com
 * CSeq: 314159 INVITE
 * Contact:
 * Content-Type: application/sdp
 * Content-Length: 142
 * (SDP message body, not shown here)
 * Here is a possible SIP response method with OK status.
 * 
 * 
 * SIP/2.0 200 OK
 * Via: SIP/2.0/UDP server10.panasonic.com;branch=z9hG4bKnashds8;received=192.0.2.3
 * Via: SIP/2.0/UDP bigbox3.site3.panasonic.com;branch=z9hG4bK77ef4c2312983.1;received=192.0.2.2
 * Via: SIP/2.0/UDP pc33.panasonic.com;branch=z9hG4bK776asdhds;received=192.0.2.1
 * To: Bob ;tag=a6c85cf
 * From: Alice ;tag=1928301774
 * Call-ID: a84b4c76e66710@pc33.panasonic.com
 * CSeq: 314159 INVITE
 * Contact:
 * Content-Type: application/sdp
 * Content-Length: 131
 * (SDP message body, not shown here)
 * </pre>
 * 
 * 
 * Example 1: A
 * listener processes
 * a subscribe request and grants authorization to the requestor.
 * The address of the requestor is obtained by calling the Message.getFrom() method.
 * 
 */
public interface Message {
    /**
     * Obtain the message body
     * @return the body of a SIMPLE/SIP message in a <A href="MessageBody.html">MessageBody</A>.
     *  The value null is
     *  returned if no body is present in the message.
     */
    javax.simple.MessageBody getBody();

    /**
     * Obtain the address in the From header.
     * @return the From address, or null if message is anonymous
     */
    javax.simple.Address getFrom();

    /**
     * Obtain the names of all the headers present in the message.
     * @return a String array of all the present header names.
     */
    java.lang.String[] getHeaderNames();

    /**
     * Obtain the named header. If headerName is null method
     * returns full header set of the message.
     * @param headerName the name of the header. If this parmeter is null,
     *  returns full set of the message headers.
     * @return an <A href="Header.html">Header</A> array for the named header,
     *  or null if there is no such header present
     */
    javax.simple.Header[] getHeaders(java.lang.String headerName);

    /**
     * Obtain the version number of a SIMPLE/SIP message. The version format is
     * SIP/major.minor. If the version number has not
     * been set in a message, the version number of the underlying SIMPLE/SIP
     * protocol stack is returned.
     * @return the SIMPLE/SIP version, for example "SIP/2.0".
     */
    java.lang.String getSIPVersion();

    /**
     * Obtain the address in the To header.
     * @return the To address
     */
    javax.simple.Address getTo();

    /**
     * Check if a message is anonymous, that is, if it doesn't contain
     * the From header or if From
     * @return true if request is from anonymous user agent, false if not
     */
    boolean isAnonymous();

    /**
     * Creates a string representation of a Message. The format of the string is
     * described in Section 7, RFC 3261.
     * @return message string representation
     */
    java.lang.String toString();

}
