package javax.simple;
/**
 * The PageModeClientListener interface defines the listener methods by which an
 * application receives and processes incoming SIMPLE/SIP instant messages, that is,
 * incoming messages with the MESSAGE method.
 * To receive incoming SIMPLE/SIP messages, an application implements this interface
 * and registers the instance with a
 * <A href="PageModeClient.html">PageModeClient</A>
 * using the
 * 
 * <A href="UserAgent.html">UserAgent</A>
 * .setListener method.
 */
public interface PageModeClientListener extends javax.simple.SimpleListener {
    /**
     * Process a binary-body MESSAGE request passed from a
     * <A href="PageModeClient.html">PageModeClient</A>
     * . Any MESSAGE
     * request that can not be passed to the processTextMessage is regarded as a
     * binary-body MESSAGE request and is processed by this method.
     * @param messageRequest the incoming MESSAGE reqeust
     * @param body message body
     * @param tid the transaction ID for the MESSAGE request
     * @param pageClient the <A href="PageModeClient.html">PageModeClient</A> associated with where the MESSAGE request is from.
     *  In the case there is no <A href="PageModeClient.html">PageModeClient</A> instance with the right context, the
     *  pageClient parameter is set to the default <A href="PageModeClient.html">PageModeClient</A> instance.
     */
    void processMessage(javax.simple.RequestMessage messageRequest, byte[] body, java.lang.String tid, javax.simple.PageModeClient pageClient);

    /**
     * Process a text-body MESSAGE request passed from a
     * <A href="PageModeClient.html">PageModeClient</A>
     * .
     * A
     * <A href="PageModeClient.html">PageModeClient</A>
     * invokes this method when the incoming
     * MESSAGE meets the following requirements:
     * 
     * Content-Type header value is text
     * There is no Content-Encoding header.
     * The body text is encoded in a charset supported by the Java platform.
     * 
     * @param messageRequest the incoming MESSAGE reqeust
     * @param body the text body in the MESSAGE request
     * @param tid the transaction ID for the MESSAGE request.
     * @param pageClient the <A href="PageModeClient.html">PageModeClient</A> associated with where the MESSAGE request is from, i.e.,
     *  the <A href="PageModeClient.html">PageModeClient</A> with the corresponding context of the MESSAGE request.
     *  In the case there is no <A href="PageModeClient.html">PageModeClient</A> instance with the right context, the
     *  pageClient parameter is set to the default <A href="PageModeClient.html">PageModeClient</A> instance.
     */
    void processTextMessage(javax.simple.RequestMessage messageRequest, java.lang.String body, java.lang.String tid, javax.simple.PageModeClient pageClient);

}
