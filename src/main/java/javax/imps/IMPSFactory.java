package javax.imps;
/**
 * Obtain implementation objects for each interface defined in the API.
 * By convention, a get method
 * returns a singleton instance for the interface's implementation, and
 * a create method returns a new instance for the interface's implementation.
 * <p>
 * IMPSFactory is a singleton class, that is, there is only
 * one instance of this class. An application must invoke the getInstance()
 * method to obtain the reference to this singleton instance.
 * </p>
 * 
 * 
 * <pre>
 * try {
 *   // obtain the factory instance
 *   IMPSFactory factory = IMPSFactory.getInstance();
 *   // create a connection instance to the default service
 *   IMPSConnection connection = factory.createIMPSConnection();
 *   connection.addListener(this);
 *   // login to the service configured for this client
 *   IMPSTid tid = connection.login(user, password);
 * } catch (IMPSException e) {
 *   e.printStackTrace();
 * }
 * </pre>
 */
public abstract class IMPSFactory {
    /**
     * Creates an instance of IMPSAddress initialized using the
     * the information provided in the IMPSAddress passed as argument.
     * The values of different components of address are initialized by the
     * corresponding parameter anIMPSAddress.
     * @param anIMPSAddress an instance of IMPSAddress interface that initializes
     *  the created instance of IMPSAddress
     * @return instance of {@link javax.imps.IMPSAddress}
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the IMPSAddress can not be found.
     * @throws java.lang.IllegalArgumentException when the argument is illegal.
     */
    public abstract javax.imps.IMPSAddress cloneIMPSAddress(javax.imps.IMPSAddress anIMPSAddress) throws javax.imps.IMPSException, java.lang.IllegalArgumentException;

    /**
     * Creates an instance of the IMPSAccessControlList interface
     * which is initially empty.
     * @return instance of IMPSAccessControlList which is initialized with no items.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the Extensible can not be found.
     */
    public abstract javax.imps.IMPSAccessControlList createACL() throws javax.imps.IMPSException;

    /**
     * Create an initially empty instance of an item for an ACL
     * @return an instance of AccessControItem which is empty
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the Extensible can not be found.
     */
    public abstract javax.imps.IMPSAccessControlItem createACLItem() throws javax.imps.IMPSException;

    /**
     * Creates an instance of the Extensible interface.
     * The values of different data items of Extensile can be set by the
     * methods provided by Extensible.
     * @return instance of the Extensible interface
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the Extensible can not be found.
     */
    public abstract javax.imps.Extensible createExtensible() throws javax.imps.IMPSException;

    /**
     * Creates an instance of the IMClient interface.
     * @param c the IMPSConnection object associated with the IMClient instance.
     * @return an instance of the IMClient interface
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the IMClient can not be found.
     * @throws java.lang.IllegalArgumentException when the argument is illegal. <P/>
     */
    public abstract javax.imps.IMClient createIMClient(javax.imps.IMPSConnection c) throws javax.imps.IMPSException, java.lang.IllegalArgumentException;

    /**
     * Creates an instance of the IMPSAddress interface.
     * The IMPSAddress object is initially a null address and should
     * be initialized using the set methods provided by IMPSAddress interface.
     * @return instance of the IMPSAddress interface
     */
    public abstract javax.imps.IMPSAddress createIMPSAddress();

    /**
     * Creates an instance of the IMPSAddress interface, with
     * the values of the components of the address initialized by the parameter URI and addressType.
     * @param URI indicates the URI address
     * @param addressType indicates the type of address (defined in IMPSAddress interface)
     * @return instance of the IMPSAddress interface
     * @throws java.lang.IllegalArgumentException when one or more arguments are illegal.
     */
    public abstract javax.imps.IMPSAddress createIMPSAddress(java.lang.String URI, int addressType);

    /**
     * Creates an instance of the IMPSConnection interface. Creation of multiple
     * instances of IMPSConnection is allowed.  The connection is made to the default
     * service specified by the configuration parameters.
     * @return instance of the IMPSConnection interface.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the IMPSConnection can not be found.
     */
    public abstract javax.imps.IMPSConnection createIMPSConnection() throws javax.imps.IMPSException;

    /**
     * Creates an instance of the IMPSConnection interface. Creation of multiple
     * instances of IMPSConnection is allowed.  The destination endpoint of the connection
     * is specified by the IMPSAddress parameter.
     * @param dest the address of the destination endpoint of the connection
     * @return instance of the IMPSConnection interface.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the IMPSConnection can not be found.
     */
    public abstract javax.imps.IMPSConnection createIMPSConnection(javax.imps.IMPSAddress dest) throws javax.imps.IMPSException;

    /**
     * Creates an instance of the IMPSContactList interface.
     * @param c IMPSConnection object associated with the IMPSContactList instance.
     * @return instance of the IMPSContactList interface
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the IMPSContactList can not be found.
     * @throws java.lang.IllegalArgumentException when the argument is illegal.
     */
    public abstract javax.imps.IMPSContactList createIMPSContactList(javax.imps.IMPSConnection c) throws javax.imps.IMPSException, java.lang.IllegalArgumentException;

    /**
     * Creates an instance of the IMPSGroup interface.
     * @param c IMPSConnection object associated with the IMPSGroup instance.
     * @return instance of the IMPSGroup interface.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the IMPSGroup can not be found.
     * @throws java.lang.IllegalArgumentException when the argument is illegal.
     */
    public abstract javax.imps.IMPSGroup createIMPSGroup(javax.imps.IMPSConnection c) throws javax.imps.IMPSException, java.lang.IllegalArgumentException;

    /**
     * Creates an instance of the IMPSInvitation interface.
     * @param c IMPSConnection object associated with the IMPSInvitation instance.
     * @return instance of the IMPSInvitation interface
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the IMPSInvitation can not be found.
     * @throws java.lang.IllegalArgumentException when the argument is illegal. <P>.</P>
     */
    public abstract javax.imps.IMPSInvitation createIMPSInvitation(javax.imps.IMPSConnection c) throws javax.imps.IMPSException, java.lang.IllegalArgumentException;

    /**
     * Creates an instance of the IMPSSearch interface.
     * @param c IMPSConnection object associated with the IMPSSearch instance.
     * @return instance of the IMPSSearch interface.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation of the IMPSSearch can not be found.
     * @throws java.lang.IllegalArgumentException when the argument is illegal.
     */
    public abstract javax.imps.IMPSSearch createIMPSSearch(javax.imps.IMPSConnection c) throws javax.imps.IMPSException, java.lang.IllegalArgumentException;

    /**
     * Create an initially empty instance of a IMPSServiceFeatures
     * @return an instance of IMPSServiceFeatures which is empty
     */
    public abstract javax.imps.IMPSServiceFeatures createIMPSServiceFeatures();

    /**
     * Get the singleton instance of the IMPSFactory class.
     * @return singleton IMPSFactory instance
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  implementation for this API can not be found.
     */
    public static javax.imps.IMPSFactory getInstance() throws javax.imps.IMPSException {
        return null; //TODO codavaj!!
    }

}
