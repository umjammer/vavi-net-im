package javax.imps;
/**
 * IMPSService represents non-negotiable information about the
 * service.  IMPSService attributes relate to the
 * client implementation (protocol) and connection-specific attributes.
 * It includes service provider information, protocol information,
 * and transport information.
 * <p>
 * An application obtains connection-specific service information
 * by creating a IMPSConnection instance and
 * using the IMPSConnection's getServiceInformation() method.
 * </p>
 * 
 * <pre>
 * // IMPSService example. Shows how to receive IMPSService information
 * try{
 *   factory = IMPSFactory.getInstance();
 *   connection = factory.createIMPSConnection();
 *   connection.addListener(this);
 * } catch ( IMPSException e) { }
 * 
 * IMPSTid tid = connection.getServiceInformation();
 * // joinTransaction is application method defined in
 * // section
 * IMPSStatus status = joinTransaction(tid);
 * if(status.getResultCode() == STATUS.OK) {
 *   IMPSService service = (IMPSService)status.getData();
 *   System.out.println("Server IMPSAddress :" + service.getServerAddress());
 *   System.out.println("Server Version :" + service.getServerVersion());
 *   System.out.println("Protocol Name :" + service.getProtocolName());
 *   System.out.println("Protocol Version :" + service.getProtocolVersion());
 * }
 * </pre>
 * 
 */
public interface IMPSService {
    /**
     * Get a protocol specific string describing the server
     * asociated with the connection.  For example, a SIP
     * registrar would return "registrar" and a SIP proxy
     * would return "proxy".
     * 
     * @return description of the server associated with the connection
     */
    java.lang.String getDescription();

    /**
     * Get the name of the underlying protocol for the
     * client implementation.  Possible values include:
     * 
     * SIP
     * XMPP
     * WV
     * 
     * @return the name of the protocol supported by the client
     *  implementation
     */
    java.lang.String getProtocolName();

    /**
     * Get the version identifier of the protocol specification
     * which the underlying protocol for the client implementation
     * conforms to.  For example, an implementation using SIP 2.0
     * would return "2.0" and an implementation using Wireless
     * Village 1.2 would return "1.2".
     * 
     * @return the version identifier of the protocol specification
     *  which the underlying protocol for the client implementation
     *  adhers to
     */
    java.lang.String getProtocolVersion();

    /**
     * Get the network address of the server for this connection.
     * 
     * @return a FQDN or IP address for the server
     */
    java.lang.String getServerAddress();

    /**
     * Get the version number of the server.
     * The meaning of the version number is vendor specific.
     * 
     * @return the version number of the server associated with this
     *  connection.
     */
    java.lang.String getServerVersion();

    /**
     * Get the name of the underlying transport protocol for
     * this connection.  Possible values are:
     * 
     * TCP
     * UDP
     * TLS
     * http
     * SMS
     * WSP
     * 
     * @return the name of the transport protocol used in this connection
     */
    java.lang.String getTransport();

}
