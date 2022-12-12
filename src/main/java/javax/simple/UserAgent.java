package javax.simple;
/**
 * A
 * <I>UserAgent</I>
 * is responsible for a logical set of functions defined in
 * the SIP protocol (RFC 3261).
 * <I>UserAgents</I>
 * defined in this API are:
 * 
 * <A href="PageModeClient.html">PageModeClient</A>
 * ,
 * 
 * <A href="PresenceAgent.html">PresenceAgent</A>
 * ,
 * 
 * <A href="PresenceUserAgent.html">PresenceUserAgent</A>
 * , and
 * 
 * <A href="Watcher.html">Watcher</A>
 * .
 * An application-implemented
 * <A href="SimpleListener.html">SimpleListener</A>
 * instance can be registered with a
 * 
 * <I>UserAgent</I>
 * . A
 * <I>UserAgent</I>
 * passes incoming messages to its registered
 * 
 * <A href="SimpleListener.html">SimpleListener</A>
 * after its internal processing.
 * 
 * A
 * <I>UserAgent</I>
 * sends out certain SIP/SIMPLE messages. A
 * <I>UserAgent</I>
 * may send
 * messages upon the application's invocation, or automatically do so when triggered
 * by its internal status.
 * 
 * <I>UserAgent</I>
 * registers the user (by sending REGISTER request)
 * and refreshes the user's registration until the application terminates it.
 * A
 * <A href="PresenceAgent.html">PresenceAgent</A>
 * may deduce a user's presence
 * status based on the user's REGISTER message. An application registers the presentity using
 * UserAgent.register method and may specify the local
 * 
 * <A href="PresenceAgent.html">PresenceAgent</A>
 * which are to be notified about UserAgent
 * state after registration has completed.
 * 
 * If an outgoing message is challenged by the remote user agent, the
 * <I>UserAgent</I>
 * may
 * either resend the challenged request automatically if local credentials are
 * available (via
 * <A href="Configuration.html">Configuration</A>
 * .addLocalCredentials) or otherwise
 * notify its
 * <A href="SimpleListener.html">SimpleListener</A>
 * by invoking the
 * 
 * <I>processChallenge</I>
 * method.
 * In the latter case after collecting the necessary credentials for the challenge, the application can
 * ask the
 * <I>UserAgent</I>
 * to resend the challenged message by invoking UserAgent.respondToChallenge.
 * 
 * An incoming request may be challenged by calling UserAgent.challenge method
 * within processRequest method of
 * <A href="SimpleListener.html">SimpleListener</A>
 * .
 * The application can also force authentication for certain remote clients
 * resulting in automatic challenging incoming requests from those remote clients
 * if proper credentials are not provided within request
 * (see
 * <A href="Configuration.html">Configuration</A>
 * .forceAuthentication method).
 * <pre>
 * By default the
 * <I>UserAgents</I>
 * in this API automatically set the following headers
 * in all outgoing request messages:
 * From
 * To
 * Via
 * Call-ID
 * CSeq
 * Max-Forwards
 * Event
 * </pre>
 * Request-URI is always set to the To address unless specified otherwise. Usually,
 * the From address is set to the preconfigured local address-of-record,
 * which can be retrieved with the
 * 
 * <A href="Configuration.html">Configuration</A>
 * .getLocalAOR method. However,
 * in some cases anonymous From address is also allowed.
 * The application can set extra headers in outgoing request messages. However,
 * the API does not allow the application to set the above headers.
 * 
 * In addition, depending on the message type, the implementation is also responsible for the following
 * headers, although it may rely on values provided directly or indirectly by the application:
 * 
 * <pre>
 * Content-Type
 * Content-Language
 * Content-Encoding
 * Content-Length
 * MIME-Version
 * Contact
 * Subscription-State
 * SIP-ETag
 * SIP-If-Match
 * WWW-Authenticate
 * Authorization
 * Proxy-Authorization
 * Proxy-Authenticate
 * </pre>
 * The following information is intended to clarify the activities performed by the
 * implementation on behalf of the application with respect to SIP/SIMPLE message exchange.
 * The SIP/SIMPLE message flow in this API is as follows:
 * 
 * (I) Application initiated request
 * 
 * Application invokes a UserAgent method to send a request.
 * If the request can not be sent, an exception is thrown. end.
 * If no response is received for a period of time, the transaction times out.
 * An application may find out about the time out condition
 * via getTransactionStatus or joinTransaction,
 * which should return a timeout flag, or via
 * .processTimeout
 * call-back method invocation. end.
 * A response has been received. If the request is challenged by a 401
 * (Unauthorized) or 407 (Proxy Authentication Required) response, goto (6).
 * 
 * .processResponse
 * is invoked after the UserAgent's internal processing
 * and status update. end.
 * 
 * If preset local credentials are available for the named realm, the request is
 * automatically resent using the preset local credentials and then goto (8).
 * If preset credentials are not available.
 * .processChallenge is invoked.
 * In that method, or later in another method, UserAgent.respondToChallenge can be
 * invoked to resend the request. if UserAgent.respondToChallenge is never invoked, end.
 * Otherwise, when UserAgent.respondToChallenge is invoked, start over from (1).
 * If a response is received for the resent request, goto (5).
 * If the resent request timed out, goto (3).
 * 
 * 
 * (II) UserAgent initiated request
 * 
 * A UserAgent sends a request out automatically when triggered by its internal status.
 * For example, a refreshing REGISTER or SUBSCRIBE request may be issued by
 * 
 * and
 * .
 * Another example is a
 * that sends a NOTIFY request when a subscription expires.
 * 
 * 
 * (III) Received Request
 * 
 * A request arrives.
 * If the request has not been authenticated yet
 * 
 * and force authentication is not set for request originator(see
 * ),
 * 
 * .processRequest method is
 * invoked to make an authentication decision. If within
 * 
 * .processRequest
 * UserAgent.challenge is called, then a 401 Unauthorized response is sent and
 * the request is considered as unauthenticated and no further processing happens. end.
 * If UserAgent.challenge method is not invoked within
 * 
 * .processRequest
 * then then incoming request is considered to be successfully authenticated, and further
 * processing takes place. continue
 * and forced authentication is set and proper credentials are available within
 * request goto previous section. If proper credentials are not available request is
 * challenged. end.
 * 
 * 
 * After the UserAgent's internal processing of the request, a listener's
 * processNotify, processSubscribe,
 * processTextMessage, processMessage or other method are invoked.
 * Within those methods, UserAgent.sendResponse can be invoked to send an application defined
 * response to the request. Usually, this response indicates some abnormal condition that
 * prevents the application's processing of the request. For example, a "415 Unsupported
 * Media Type" response may be sent when the Content-Type is not supported. The application may
 * also invoke UserAgent.challenge to challenge the request with a 401 Unauthorized response.
 * If an application defined response is sent (via UserAgent.sendResponse),
 * then start over from (1) end.
 * If a challenge response is sent, after the resent request is authenticated, start over from (1).
 * If application has not sent any response, the UserAgent sends the default 200 OK response.
 * If a response is not sent for a certain period of time, the transaction
 * timed out and a subsequent call of UserAgent.sendResponse
 * throws SimpleException with error code TRANSACTION_EXPIRES end.
 * 
 * 
 * Example 1: Set default headers to be included in outgoing REGISTER requests.
 * 
 */
public interface UserAgent {
    /**
     * Transaction status bit. Transaction is failed. Transaction status is
     * returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_IS_FAILED=1073741824;

    /**
     * SIP is a transactional protocol.  See RFC 3261 sec. 17 Transactions for formal
     * description of transactions states.  The following flags represent SIP transaction state.
     * 
     * Transaction status bit. Transaction is server initiated. Transaction status is
     * returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_IS_SERVER=-2147483648;

    /**
     * Transaction status bit. Transaction is in COMPLETED state (see rfc3261 "Transaction").
     * Transaction status is returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_IS_STATE_COMPLETED=134217728;

    /**
     * Transaction status bit. Transaction is in TERMINATED or COMPLETED state
     * (see rfc3261 "Transaction").
     * Transaction status is returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_IS_STATE_FINISHED=201326592;

    /**
     * Transaction status bit. Transaction is in PROCEEDING state (see rfc3261 "Transaction").
     * Transaction status is returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_IS_STATE_PROCEEDING=268435456;

    /**
     * Transaction status bit. Transaction is in TERMINATED state (see rfc3261 "Transaction").
     * Transaction status is returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_IS_STATE_TERMINATED=67108864;

    /**
     * Transaction status bit. Transaction is in TRYING state (see rfc3261 "Transaction").
     * Transaction status is returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_IS_STATE_TRYING=536870912;

    /**
     * Transaction status bit. Transaction status is unknown. Transaction status is
     * returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_IS_UNKNOWN=33554432;

    /**
     * Transaction response mask, used to obtain response code from status value. Transaction status is
     * returned by getTransactionStatus and joinTransaction methods
     */
    int TRANSACTION_RESPONSE_CODE=65535;

    /**
     * Challenge a request. A 401 Unauthorized response with a WWW-Authenticate
     * header is sent. If a resent request is received, the request is authenticated
     * before being passed to a UserAgent's listener. If the authentication fails, a 403
     * Forbidden response is sent and the request is not processed any further.
     * 
     * Note: the credentials set here are not cached. So any further requets are not
     * authenticated before being passed to a UserAgent's listener.
     * 
     * Note: this method must be called within
     * 
     * <A href="SimpleListener.html">SimpleListener</A>
     * .processRequest method only
     * @param tid transaction id of the request to be challenged
     * @param user user name used in the authentication
     * @param password password used in the authentication
     * @throws javax.simple.SimpleException if error occurred in sending the response
     * @throws java.lang.IllegalArgumentException tid is the ID of a client or
     *  completed or expired or unknown transaction or user, password or tid is a null value.
     */
    void challenge(java.lang.String tid, java.lang.String user, java.lang.String password) throws javax.simple.SimpleException, java.lang.IllegalArgumentException;

    /**
     * Get default REGISTER request headers.
     * @return the default headers previously set for use in outgoing REGISTER
     *  requests, or null if none.
     */
    javax.simple.Header[] getDefaultRegisterHeaders();

    /**
     * Get the status of the indicated transaction.
     * 
     * Note: method returns response code mixed with transaction status bits as follows
     * (description of these states is in RFC 3261 sec 17 Transactions):
     * TRANSACTION_IS_UNKNOWN if transaction is unknown for UserAgent.
     * TRANSACTION_IS_SERVER named transaction is server transaction
     * TRANSACTION_IS_FAILED error occurred
     * TRANSACTION_IS_STATE_TRYING transaction is in TRYING state
     * TRANSACTION_IS_STATE_PROCEEDING transaction is in PROCEEDING satate
     * TRANSACTION_IS_STATE_COMPLETED transaction is completed
     * TRANSACTION_IS_STATE_TERMINATED transaction is terminated
     * TRANSACTION_IS_STATE_FINISHED transaction is in COMPLETED or TERMINATED states
     * 
     * 
     * 
     * Example:
     * ...
     * int status = ua.getTransactionStatus(tid);
     * if((status & TRANSACTION_IS_STATE_FINISHED) != 0)
     * {
     * // get response code
     * int respCode = status & TRANSACTION_RESPONSE_CODE;
     * ...
     * }
     * ...
     * 
     * @param tid transaction ID
     * @return transaction status bit mixed with response code if any
     */
    int getTransactionStatus(java.lang.String tid);

    /**
     * Wait for named transaction to finish. A transaction finishes for the following reasons:
     * transaction completed
     * transport error occured
     * transaction timeout occured
     * 
     * If
     * <I>UserAgent</I>
     * knows nothing about requested transaction this method simply returns
     * TRANSACTION_UNKNOWN status bit.
     * 
     * joinTransaction method cannot lead to thread blocking or another synchronization problem
     * because its waiting time is determined by the underlying transaction level.
     * @param tid transaction ID to wait
     * @return transaction status bit mixed with response code if any. See getTransactionStatus
     */
    int joinTransaction(java.lang.String tid);

    /**
     * Register the UA by sending a REGISTER request to the registrar.
     * The preconfigured registrar's address can be obtained via
     * 
     * <A href="Configuration.html">Configuration</A>
     * .getRegistrarAddress method call.
     * UserAgent renews the registration automatically until the unregister method is invoked.
     * 
     * @param registrar the address of the required registrar
     * @param extraHeaders the extra headers in addition to the preset default headers
     *  to use in the outgoing REGISTER message. Could be null
     * @param body the message body or null
     * @return the transaction ID for the sent REGISTER request
     * @throws javax.simple.SimpleException if an error occurred in sending the REGISTER message
     * @throws java.lang.IllegalArgumentException if defaultHeaders set includes headers that
     *  are not allowed or if registrar address is null value
     */
    java.lang.String register(javax.simple.Address registrar, javax.simple.Header[] extraHeaders, javax.simple.MessageBody body) throws javax.simple.SimpleException;

    /**
     * Remove the registered
     * <A href="SimpleListener.html">SimpleListener</A>
     * . The method has no effect if no
     * 
     * <A href="SimpleListener.html">SimpleListener</A>
     * is registered
     */
    void removeListener();

    /**
     * Respond to a challenge by resending request.
     * Note: the credentials set here are not cached
     * @param realm realm value, must not be null
     * @param user user value, must not be null
     * @param password password value, must not be null
     * @param tid transaction ID of the challenged request, must not be null
     * @return new transaction ID for the regenerated request
     * @throws java.lang.IllegalArgumentException if any of the parameters is null value
     * @throws javax.simple.SimpleException if challenge cannot  be responded to by any reason,
     *  or if realm doesn't correspond to the one requested in challenge response
     */
    java.lang.String respondToChallenge(java.lang.String tid, java.lang.String realm, java.lang.String user, java.lang.String password) throws javax.simple.SimpleException, java.lang.IllegalArgumentException;

    /**
     * Send a response back, only final response (non 1xx response) is allowed.
     * The default headers in the response are:
     * 
     * From (copied from the request)
     * To (copied from the request if the tag is not present. Otherwise, the To address is copied and a tag is added)
     * Via (copied from the request)
     * Call-ID (copied from the request)
     * CSeq (copied from the request)
     * Max-Forwards (implementation default)
     * Record-Route (copied from the request if present)
     * 
     * @param statusCode response status code
     * @param reasonPhrase response phrase that describes status code
     * @param tid the transaction id of the request to be responded
     * @param extraHeaders extra headers in addition to the default response headers. Could be null
     * @param body message body. Could be null
     * @throws javax.simple.SimpleException if error occurred in sending the response
     * @throws java.lang.IllegalArgumentException - if statusCode is invalid, or
     *  extraHeaders contains headers that are not allowed, or tid is the ID of a
     *  client or completed or expired or unknown transaction
     */
    void sendResponse(int statusCode, java.lang.String reasonPhrase, java.lang.String tid, javax.simple.Header[] extraHeaders, javax.simple.MessageBody body) throws javax.simple.SimpleException;

    /**
     * Set default headers to be used in outgoing REGISTER requests.
     * Multiple Headers of the same header name can be included in
     * defaultHeaders when allowed by SIP protocol.
     * The base set of default headers in every outgoing REGISTER request is:
     * Contact
     * Expires
     * 
     * The application is not allowed to overwrite the above two headers.
     * @param defaultHeaders the default headers to be used in outgoing
     *  REGISTER requests. Each time this method is called, it starts with
     *  the default base set of headers (no accumulation). If defaultHeaders
     *  is null, previously set default headers are cleared.
     * @throws java.lang.IllegalArgumentException if defaultHeaders includes headers that are not allowed
     */
    void setDefaultRegisterHeaders(javax.simple.Header[] defaultHeaders) throws java.lang.IllegalArgumentException;

    /**
     * Register a
     * <A href="SimpleListener.html">SimpleListener</A>
     * .
     * The previously registered
     * <A href="SimpleListener.html">SimpleListener</A>
     * , if there is one, is replaced
     * @param listener the listener
     */
    void setListener(javax.simple.SimpleListener listener);

    /**
     * Unregister by sending a REGISTER request with Expires header set to zero
     * @param registrar the registrar address where to send unregister request
     * @param extraHeaders the extra headers in addition to the preset default headers in
     *  the outgoing REGISTER message. Could be null
     * @param body the optional message body. Could be null
     * @return the transaction ID for the sent REGISTER request
     * @throws javax.simple.SimpleException with error code
     *  MESSAGE_NOT_SENT if error occurred in sending the REGISTER message
     * @throws java.lang.IllegalArgumentException if defaultHeaders includes headers that are not allowed
     */
    java.lang.String unregister(javax.simple.Address registrar, javax.simple.Header[] extraHeaders, javax.simple.MessageBody body) throws javax.simple.SimpleException;

}
