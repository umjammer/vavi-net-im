package javax.imps;
/**
 * Allows a presence client (i.e., PresenceWatcher or PresencePublisher) or IM client to create a session with a presence or IM
 * server, respectively. It also allows the presence or IM client to
 * negotiate service and capability information with the server
 * 
 * 
 * 
 * <pre>
 * // Example showing use of IMPSConnection and IMPSConnectionListener
 * import javax.imps.*;
 * 
 * public class ConnectionExample implements IMPSConnectionListener, Runnable
 * {
 * 
 *   boolean shutdown = false;
 *   IMPSConnection connection;
 * 
 *   public IMPSConnectionExample(IMPSConnection connection, String user, String password) {
 *     this.connection = connection;
 *     connection.addListener(this);
 *     IMPSTid tid = connection.login(user, password);
 *     // joinTransaction is application method defined in
 *     // section
 *     IMPSStatus status = joinTransaction(tid);
 *     if (status.getResultCode() != IMPSStatus.OK){
 *       System.exit(1);
 *     }
 *   }
 * 
 *   // Callback when a disconnect message is received from the server.
 *   public void connectionClosed(IMPSConnection c)
 *   {
 *     System.out.println("IMPSConnection closed by the server");
 *     // do other functions to logout the user.
 *     shutdown = true;
 *   }
 * 
 *   public void processRequest(IMPSConnection c, IMPSRequest request)
 *   {
 *      switch (request.getType()) {
 *      // do regular processing for different types of request
 *      default :
 *      }
 *   }
 * 
 *   public void processResponse(IMPSConnection c, IMPSStatus status)
 *   {
 *     if (shutdown) return;
 * 
 *     switch (status.getRequest().getType()) {
 *     case IMPSRequest.LOGIN :
 *       // Authentication completed
 *       // The client is logged in.
 *       break;
 * 
 *     case IMPSRequest.LOGOUT :
 *       shutdown = true;
 *       break;
 * 
 *     default :
 *     }
 *   }
 * 
 *   public void run()
 *   {
 *     while (!shutdown) {
 *       // do regular processing
 *     }
 *     // shutdown
 *     System.exit(1);
 *   }
 * 
 *   public static void main(String arg[])
 *   {
 *     try {
 *       IMPSConnection connection = factory.createIMPSConnection();
 *       ConnectionExample connectionexample = new ConnectionExample(connection,
 *         arg[0], arg[1]);
 *       Thread t = new Thread(connectionexample);
 *       t.start();
 *       t.join();
 *     } catch (Exception e) {
 *       e.printStackTrace();
 *     }
 *   }
 * }
 * </pre>
 */
public interface IMPSConnection {
    /**
     * Add an application defined IMPSConnectionListener instance to the list of IMPSConnectionListeners
     * maintained by this IMPSConnection. The processIncomingMessage of that
     * instance will be called when the JAIN IMPS implementation receives a
     * message from the service element.
     * @param listener an application defined instance of IMPSConnectionListener.
     */
    void addListener(javax.imps.IMPSConnectionListener listener);

    /**
     * Discover the possible services in the server after a successful
     * login by a  client, such as invitation features, search features,
     * IM features, presence features, group features and access control
     * features.
     * @return an instance of IMPSTid (transaction id) that identifies this request. The type of the request is
     *  {@link javax.imps.IMPSRequest#SVC_DISCOVER}.  Upon successful completion, the
     *  response from the server contains a IMPSServiceFeatures instance.
     * @throws javax.imps.IMPSException
     */
    javax.imps.IMPSTid discoverService() throws javax.imps.IMPSException;

    /**
     * Send a request to the server for information regarding
     * the IMPSService.
     * @return an instance of IMPSTid (transaction id) that identifies this request. upon success, the
     *  response contains a {@link javax.imps.IMPSService} object filled in with the available
     *  service information.
     * @throws javax.imps.IMPSException
     */
    javax.imps.IMPSTid getServiceInformation() throws javax.imps.IMPSException;

    /**
     * Authenticate the user to the service using the
     * client's user ID and password. The presence and IM servers may ask for
     * capability negotiations in the login response message.
     * @param userID indicates the ID of the user
     * @param password indicates the password of the user
     * @return an instance of IMPSTid (transaction id) that identifies this request.  Upon successful
     *  completion, the response may contain {@link javax.imps.IMPSServiceFeatures}.
     * @throws javax.imps.IMPSException
     */
    javax.imps.IMPSTid login(java.lang.String userID, java.lang.String password) throws javax.imps.IMPSException;

    /**
     * Terminate an authenticated session with the service.
     * This disposes local and service resources allocated for
     * the previously authenticated user.
     * The IMPSConnection may not be used after this method has been called,
     * except to login again.
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     * @throws javax.imps.IMPSException
     */
    javax.imps.IMPSTid logout() throws javax.imps.IMPSException;

    /**
     * Negotiate the services supported by the server after a successful connection
     * (and Login if such is required).  If no service features are negotiable for
     * a given protocol, then an implementation can return a fixed IMPSServiceFeatures
     * object appropriate for the given protocol.
     * @param features indicates the desired set of service features
     *  and advertized client capabilities.  Can be set to null to
     *  indicate to the service that the previously received service
     *  features and capabilities are acceptable.
     * @return an instance of IMPSTid (transaction id) that identifies this request. upon
     *  successful completion of the request, the response contains
     *  a {@link javax.imps.IMPSServiceFeatures} object.  If the associated object,
     *  it signifies that the server has agreed to provide the requested
     *  service features within the requested capabilities.
     * @throws javax.imps.IMPSException
     */
    javax.imps.IMPSTid negotiateService(javax.imps.IMPSServiceFeatures features) throws javax.imps.IMPSException;

    /**
     * Remove an application-defined IMPSConnectionListener instance from the list of
     * IMPSConnectionListeners maintained by this IMPSConnection.
     * @param listener an application defined instance of IMPSConnectionListener.
     */
    void removeListener(javax.imps.IMPSConnectionListener listener);

}
