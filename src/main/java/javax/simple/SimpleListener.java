package javax.simple;
/**
 * SimpleListener defines the callback methods that allow an application
 * to receive incoming SIP/SIMPLE request and response messages.
 * An application implements this interface and registers the instance
 * with a
 * <A href="UserAgent.html">UserAgent</A>
 * using
 * 
 * <A href="UserAgent.html">UserAgent</A>
 * .setListener.
 * There are a number of
 * <A href="UserAgent.html">UserAgent</A>
 * in the API (see
 * 
 * <A href="PresenceAgent.html">PresenceAgent</A>
 * ,
 * 
 * <A href="PresenceUserAgent.html">PresenceUserAgent</A>
 * ,
 * 
 * <A href="PageModeClient.html">PageModeClient</A>
 * , and
 * 
 * <A href="Watcher.html">Watcher</A>
 * .
 * After processing an incoming message, a
 * <A href="UserAgent.html">UserAgent</A>
 * passes
 * the message to its registered SimpleListener instance by invoking the
 * approriate SimpleListener methods. A
 * <A href="UserAgent.html">UserAgent</A>
 * chooses which
 * method to invoke based on the type of the incoming message and sometimes its content.
 * 
 * All call back methods defined in this interface are expected to return promptly without blocking
 * the invoker for an extended period of time.
 * 
 * The API supports request authentication features that allow the application to authenticate
 * each incoming request. The processRequest method is intended for this purpose.
 * processRequest is called before any other call-back method is called.
 * Within this method the application determines whether to authenticate this request or not. To
 * challenge the request the application calls
 * <A href="UserAgent.html">UserAgent</A>
 * .challenge or
 * sends a failed response by invoking
 * <A href="UserAgent.html">UserAgent</A>
 * .sendResponse
 * with appropriate response code.
 * 
 * processRequest is called for each incoming request regardless
 * of whether it has already been authenticated by means of force authentication facilities (see
 * 
 * <A href="Configuration.html">Configuration</A>
 * .forceAuthentication). Incoming requests
 * that are processed automatically by the implementation and that are not passsed to
 * the application also have to be authenticated within this method.
 * 
 * SimpleListener is directly used by only
 * <A href="PresenceUserAgent.html">PresenceUserAgent</A>
 * .
 * All others user agents use descendants of this interface such as
 * <A href="WatcherListener.html">WatcherListener</A>
 * .
 * 
 * A transaction ID is passed to the SimpleListener along with each message. A
 * SimpleListener instance can use this ID to distinguish received messages.
 * This ID is also needed to invoke certain methods in the API.
 * The API implementation must ensure the global uniqueness of a transaction ID across time and space.
 * 
 */
public interface SimpleListener {
    /**
     * Processes a digest challenge. A
     * <A href="UserAgent.html">UserAgent</A>
     * invokes this method when an outgoing
     * request is challenged with a 401 (Unauthorized) or 407 (Proxy Authentication Required)
     * response and there is no preset local credential (see
     * 
     * <A href="Configuration.html">Configuration</A>
     * .addLocalCredentials)
     * to resend the request automatically. This method must not be blocking, for example,
     * by prompting for the user's input.
     * 
     * This method may also be called to process challenges of requests which
     * have been internally originated by the implementation, for example, during
     * subscription refreshing.
     * @param request original outgoing request message that has been challenged
     * @param realm realm in the challenging response
     * @param tid transaction id for the challenged request
     * @param ua A <A href="UserAgent.html">UserAgent</A> that originated the challenged request
     */
    void processChallenge(javax.simple.RequestMessage request, java.lang.String realm, java.lang.String tid, javax.simple.UserAgent ua);

    /**
     * Authenticate a request if required by the application.
     * This method is called first, before other listener methods such
     * as
     * <A href="WatcherListener.html">WatcherListener</A>
     * .processNotify and the like.
     * The application MUST not do any message processing within this method except for
     * authentication.
     * 
     * To challenge an incoming request, the application calls
     * 
     * <A href="UserAgent.html">UserAgent</A>
     * .challenge
     * within this listener method. In this case the incoming request will be challenged and 401
     * (Unauthorized) response will be sent to remote user agent. Further processing of
     * this request is not done and the application's other listener methods are not invoked
     * for this request.
     * 
     * To accept the incoming request, the application exits this method without generating a response
     * message, in which case the implementation will generate a response, or the application
     * if it chooses may send success response using
     * <A href="UserAgent.html">UserAgent</A>
     * .sendResponse.
     * @param request unauthenticated incoming request
     * @param tid the ID of the incoming transaction
     * @param ua <A href="UserAgent.html">UserAgent</A> that originated the notification
     */
    void processRequest(javax.simple.RequestMessage request, java.lang.String tid, javax.simple.UserAgent ua);

    /**
     * Process a SIP response passed from a
     * <A href="UserAgent.html">UserAgent</A>
     * .
     * A
     * <A href="UserAgent.html">UserAgent</A>
     * invokes
     * this method on its registered
     * <I>SimpleListener</I>
     * when a relevant SIP response arrives.
     * This method is called only for responses to those requests
     * that have been issued by the application.  Responses related to
     * automatic refreshes are not passed to this method.
     * @param response incoming SIMPLE/SIP response message
     * @param tid transaction ID for the response
     * @param ua <A href="UserAgent.html">UserAgent</A> where the response message is from
     */
    void processResponse(javax.simple.ResponseMessage response, java.lang.String tid, javax.simple.UserAgent ua);

    /**
     * A
     * <A href="UserAgent.html">UserAgent</A>
     * (client) invokes this method on
     * its registered
     * <I>SimpleListener</I>
     * when the server does not produce a response within a suitable amount of
     * time, for example, if it could not determine the location of the user
     * in time.  The client MAY repeat the request without modifications at
     * any later time.
     * 
     * Internal implementation-initiated transactions, such as the refreshing SUBSCRIBE transactions
     * originated by a
     * <A href="Watcher.html">Watcher</A>
     * , are not covered by this method.
     * 
     * The application may also discover timeout status when obtaining transaction
     * status via
     * <A href="UserAgent.html">UserAgent</A>
     * .getTransactionStatus
     * and
     * <A href="UserAgent.html">UserAgent</A>
     * .joinTransaction.
     * @param request request message that initiated the timeout transaction
     * @param tid ID of the timeout transaction
     * @param ua <A href="UserAgent.html">UserAgent</A> that has originated the notification
     */
    void processTimeOut(javax.simple.RequestMessage request, java.lang.String tid, javax.simple.UserAgent ua);

}
