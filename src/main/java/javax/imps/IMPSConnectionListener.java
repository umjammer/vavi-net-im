package javax.imps;
/**
 * Allows the application to be notified of any incoming requests or asynchronous
 * responses.   The application implements the listener interface and registers
 * it using the IMPSConnection addListener method.
 * <p>
 * Each callback method provides a handle to the connection receiving
 * the event.  This allows the same connection listener to be used
 * for multiple connections.
 */
public interface IMPSConnectionListener {
    /**
     * Notify the listener that the server has closed the connection.
     * This method is not called if the connection closing was initiated
     * by the application through
     * {@link javax.imps.IMPSConnection#logout()}
     * .
     * @param c connection for which this listener is invoked
     */
    void connectionClosed(javax.imps.IMPSConnection c);

    /**
     * Notify the listener about an incoming request or event from the server.
     * When an incoming message arrives, processing is application specific.
     * @param request request received from the server or peer
     * @param c connection for which this listener is invoked
     */
    void processRequest(javax.imps.IMPSConnection c, javax.imps.IMPSRequest request);

    /**
     * Notify the listener of the status of a previously sent request.
     * @param c connection for which this listener is invoked
     * @param status response status for the request previous sent over the connection
     */
    void processResponse(javax.imps.IMPSConnection c, javax.imps.IMPSStatus status);

}
