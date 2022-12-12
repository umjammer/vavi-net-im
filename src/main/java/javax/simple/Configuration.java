package javax.simple;
/**
 * Configuration parameters are available to the application.
 * Most parameters are available only in read-only mode.  It is the responsibility of the implementation
 * to initialize those mandatory parameters listed below.  Implementations should throw the
 * InstantiationException during initialization if mandatory parameters are not
 * initialized.
 * 
 * 
 * 
 * 
 * <TABLE>
 * 
 * <TR>SIMPLE Property
 * <TD>Description</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.ADDRESS_OF_RECORD</TD>
 * <TD>Current client Address of Record. For example, sip:miss.marple@example.com.
 * This property is mandatory</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.LOCAL_PORT</TD>
 * <TD>The port number used for receiving SIP messages. For example, 5060.
 * This property is mandatory</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.LOCAL_TRANSPORT</TD>
 * <TD>The transport used to receive SIP messages. For example, UDP.
 * This property is mandatory</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.CONTACT</TD>
 * <TD>The user's contact address. For example, sip:192.19.229.50
 * This property is mandatory</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.LOCAL_REALM</TD>
 * <TD>The local realm for authentication. For example, UK.
 * This property is mandatory</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.REGISTRAR_ADDRESS</TD>
 * <TD>The address of the registrar. For example, sip:panasonic.com.
 * If not specified current host address is used. This property is optional</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.REMOTE_PRESENCE_AGENT_ADDRESS</TD>
 * <TD>The specific address of Presence Agent that is used as Presence Server.
 * If not specified local AOR is used. This property is optional</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.PUBLISH_EXPIRATION_VALUE</TD>
 * <TD>The default expiration value used for Expires header within PUBLISH
 * request, in milliseconds. This property is optional</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.REGISTER_EXPIRATION_VALUE</TD>
 * <TD>The default expiration value used for Expires parameter within REGISTER
 * request, in milliseconds. This property is optional</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.SUBSCRIPTION_EXPIRATION</TD>
 * <TD>The default expiration value used for Expires header within SUBSCRIPTION
 * request, in miliseconds. This property is optional</TD>
 * </TR>
 * <TR>
 * <TD>javax.simple.TRANSACTION_EXPIRATION</TD>
 * <TD>The transaction expiration value in milliseconds. If transaction is not finished within
 * specified time it will be considered as timed out. This property is optional</TD>
 * </TR>
 * </TABLE>
 * 
 * 
 * Example 1:
 * Set, retrieve and remove local credential information for possible
 * digest authentication of the local client.
 * 
 */
public interface Configuration {
    /**
     * Add credentials for possible digest authentication of the local client,
     * overwriting the old credentials for the same realm. When challenged
     * by a 401 (Unauthorized) or 407 (Proxy Authentication Required) response,
     * the API implementation must regenerate the challenged request
     * automatically using the pre-set credentials here, if such credentials exist
     * for the realm. The message regeneration is transparent to the application.
     * 
     * If a response is received for the resent request, the processResponse method
     * is invoked in the
     * <I>SimpleListener</I>
     * of the
     * <I>UserAgent</I>
     * that sent the challenged
     * request. If the resent request timed out without receiving a response,
     * the processTimeOut method is invoked. No matter which method is invoked,
     * the transaction ID for the original request is passed to the method.
     * @param realm the realm component of the credentials
     * @param user the user name componenet of the credentials
     * @param password the password component of the credentials
     * @throws java.lang.IllegalArgumentException if realm, user or
     *  password parameters are null values
     */
    void addLocalCredentials(java.lang.String realm, java.lang.String user, java.lang.String password) throws java.lang.IllegalArgumentException;

    /**
     * Disable the forced authentication for a remote client. Previously forced authentication on all methods is disabled
     * @param remoteClient the remote client for which forced authentication is disabled.
     * @throws java.lang.IllegalArgumentException if remoteClient parameter
     *  is null value.
     */
    void disableAuthentication(javax.simple.Address remoteClient) throws java.lang.IllegalArgumentException;

    /**
     * Force digest authentication of the indicated remote user agent. After this method is invoked,
     * each request received from the specified remoteClient is automatically challenged
     * by the API implementation with a 401 Unauthorized response, if the request
     * method matches one of the indicated methods.
     * If a resent request is received and successfully authenticated, it is passed to
     * the relevant UserAgent. If the authentication fails, a 403 Forbidden response is sent
     * back to the remote user agent by the implementation.
     * This method overwrites any previously set credentials for the same
     * remote client and method.
     * @param remoteClient the remote user agent on which authentication is forced
     * @param user the user name used in the authentication
     * @param password the password used in the authentication
     * @param methods the methods of which requests must be authenticated. If null, all methods
     *  are authenticated.
     * @throws java.lang.IllegalArgumentException if remoteClient,
     *          user, or password parameters are null
     *          values.
     */
    void forceAuthentication(javax.simple.Address remoteClient, java.lang.String user, java.lang.String password, java.lang.String[] methods) throws java.lang.IllegalArgumentException;

    /**
     * Obtain the remote user agents on which authentication is forced
     * @return the addresses of the remote user agents on which authentication is forced,
     *    or null if there are none.
     */
    javax.simple.Address[] getAuthenticationClients();

    /**
     * Obtain the default character encoding
     * (the "charset" parameter in "Content-Type" header)
     * for outgoing messages with a text type body.
     * For a text type message body (see MessageBody for what is considered as
     * "text type" message bodies.), the Content-Type "charset" parameter
     * is chosen according to the following priority order (from low to high):
     * 
     * the Java platform default.
     * the global default set by Configuration.setDefaultCharset.
     * the "charset" parameter in the current "Content-Type" header.
     * 
     * @return the non-null global default character encoding
     */
    java.lang.String getDefaultCharset();

    /**
     * Obtain the local address-of-record, that is, the local user agent's address.
     * This address is precofigured by the API implementation (the implementation may
     * provide an interface for the user to configure it). UserAgents in the API uses
     * this address as the default "From" address for the outgoing requests
     * @return the non-null local address-of-record
     */
    javax.simple.Address getLocalAOR();

    /**
     * Obtain the local contact address. This address is precofigured by the implementation
     * and can be used in the "Contact" header
     * @return the non-null local contact address
     */
    javax.simple.Address getLocalContact();

    /**
     * Obtain all the realms for which local credentials have been set
     * @return a String array of all the realms with preset credentials.
     *          If there are no realms this method returns null.
     */
    java.lang.String[] getLocalCredentialRealms();

    /**
     * Obtain the local contact port. This port is precofigured by the implementation
     * and is used as intput port
     * @return the non-negative number of the local port
     */
    int getLocalPort();

    /**
     * Obtain the local digest realm that is used in challenging remote clients
     * @return the non-null local digest realm that is used in challenging remote clients.
     */
    java.lang.String getLocalRealm();

    /**
     * Obtain the local transport name (
     * <I>TCP, UDP </I>
     * for instance). This transport is precofigured by the implementation
     * and is used as input transport
     * @return the non-null and non-empty name of local transport
     */
    java.lang.String getLocalTransport();

    /**
     * Obtain the registration expiration value. This value is precofigured by the implementation
     * and is used within "expires" parameter of "Contact" header in REGISTER request
     * @return the non-zero expiration value
     */
    long getRegisterExpirationValue();

    /**
     * Obtain the address of the registrar. This address is precofigured by the implementation
     * and is used as
     * <I>Request-URI</I>
     * in REGISTER requests.
     * If registrar address is not set within static configuration properties
     * (javax.simple.REGISTRAR_ADDRESS) the method has to return the host part of the address-of-record
     * @return the non-null registrar address
     */
    javax.simple.Address getRegistrarAddress();

    /**
     * Obtain the name of the SIP/SIMPLE stack used by the API implementation.
     * @return the name of the SIP/SIMPLE stack in a string or null.
     */
    java.lang.String getStackName();

    /**
     * Obtain the version of the SIP/SIMPLE stack used by the API implementation
     * @return the version of the SIP/SIMPLE stack in a string or null.
     */
    java.lang.String getStackVersion();

    /**
     * Obtain the transaction expiration value. This value is precofigured by the implementation
     * and is used to determine transaction expiration time. If response to certain request is not
     * received within specified time then the appropriate transaction is considered as expired.
     * @return the non-zero expiration value
     */
    long getTransactionExpiration();

    /**
     * Obtain the preset local credentials for a realm
     * @param realm the realm for which the local credentials is to be removed
     * @throws java.lang.IllegalArgumentException if realm parameter is null value
     */
    void removeLocalCredentials(java.lang.String realm) throws java.lang.IllegalArgumentException;

    /**
     * Set the global default character encoding (the "charset" parameter in
     * "Content-Type" header) for all outgoing SIP/SIMPLE messages with a text
     * type body. If not set, the default charset is the platform default.
     * For a text type message body (see MessageBody for what is considered a
     * "text type" message body.), the Content-Type "charset" parameter is
     * chosen according to the following priority order (from low to high):
     * 
     * the Java platform default.
     * the global default set by Configuration.setDefaultCharset.
     * the "charset" parameter in the chosen "Content-Type" header.
     * 
     * Here is an example of charset processing using explicit operation on the message body:
     * 
     * 
     * 
     * // creates body content header
     * Header contentHeader = m_simpleFactory.createHeader(HeaderName.CONTENT_TYPE,
     * <I>"text/text"</I>
     * );
     * 
     * // creates message body without charset parameter
     * MessageBody body = m_simpleFactory.createMessageBody(new Header[] {contentHeader});
     * 
     * // set the default charset
     * String newCharset =
     * <I>"UTF-16LE"</I>
     * ;
     * m_configuration.setDefaultCharset(newCharset);
     * 
     * // prepare message text
     * String testString = new String(
     * <I>"Test String"</I>
     * .getBytes(newCharset));
     * 
     * // set the raw body
     * body.setRawBody(testString.getBytes(newCharset));
     * 
     * // get the string that is equal to the testString string
     * String resultString = body.getTextBody();
     * 
     * 
     * @param charset the default character encoding scheme
     * @throws java.io.UnsupportedEncodingException if the character encoding is not supported
     */
    void setDefaultCharset(java.lang.String charset) throws java.io.UnsupportedEncodingException;

}
