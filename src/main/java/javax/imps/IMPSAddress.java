package javax.imps;
/**
 * An interface to manipulate a variety of address types
 * including addresses for users, groups of users, and user agents.
 * The IMP protocol address formats that are supported include SIP, Wireless Village, and XMPP.
 * <p>
 * An example of a SIP address is: John Smith &ltsip:john.smith@somecompany.com:5060
 * where the corresponding field names are:
 * </p><pre>
 * scheme="sip"
 * displayName="John Smith"
 * user="john.smith"
 * domain="somecompany.com"
 * port="5060"
 * </pre>
 * 
 * An example of Wireless Village address is:<pre>
 * wv:john.smith/friends@imps.com where
 * 
 * resource = "friends" (Identifies private or public resources within a domain.)
 * </pre>
 * 
 * An example of an XMPP address is:<pre>
 * romeo@montague.net/orchard where
 * 
 * resource name="orchard"
 * </pre>
 * 
 * There are eight types of addresses:
 * <pre>
 * ADDRESS_DOMAIN :  a named administrative locality in which a set of users are managed
 * ADDRESS_USER : the identifier for a user within a given domain
 * ADDRESS_CLIENT : the identifier for the IMP application used by the user
 * ADDRESS_CONTACTLIST : the identifier for a contact list for a user
 * ADDRESS_GROUP : the identifier for a set of users, group, or user agent addresses
 * ADDRESS_SCREENNAME : In a group IMP session, a name selected by the user combined with the group
 * ADDRESS_FULLUSERSCREEN : The combination of the screenname and username
 * ADDRESS_NICKNAME : an alias for a user, group, or user agent that is used in a client to hide the user
 * </pre>
 * Some types may not be supported by an underlying protocol.
 * An implementation's behavior in using an IMPSAddress with null values is
 * protocol dependent and undefined.
 * 
 * 
 * <pre>
 * // example of how to create an ADDRESS_SCREENNAME
 * // "neo" wv:thomas.a.anderson/matrix@machineworld.com
 * IMPSAddress screenNameAddress;
 * try {
 *   screenNameAddress = factory.createIMPSAddress();
 *   screenNameAddress.setAddressType(IMPSAddress.ADDRESS_SCREENNAME);
 *   screenNameAddress.setUser("thomas.a.anderson");
 *   screenNameAddress.setDomain("machineworld.com");
 *   screenNameAddress.setScheme("wv");
 *   screenNameAddress.setDisplayName("neo");
 *   screenNameAddress.setResourceID("matrix");
 * } catch (IMPSException e) {
 *   e.printStackTrace();
 * }
 * </pre>
 * 
 */
public interface IMPSAddress {
    /**
     * Constant to specify an address type for a IMP application.
     */
    int ADDRESS_CLIENT=3;

    /**
     * Constant to specify an address type for a contact list.
     */
    int ADDRESS_CONTACTLIST=4;

    /**
     * Constant to specify an address type for a service domain.
     */
    int ADDRESS_DOMAIN=1;

    /**
     * Constant to specify an address type combining user's screenname and username.
     */
    int ADDRESS_FULLUSERSCREEN=7;

    /**
     * Constant to specify an address type for a group.
     */
    int ADDRESS_GROUP=5;

    /**
     * Constant to specify an address type of an alias for a user, group, or user agent.
     */
    int ADDRESS_NICKNAME=8;

    /**
     * Constant to specify an address type combining a username and a group name.
     */
    int ADDRESS_SCREENNAME=6;

    /**
     * Constant to specify an unknown address type.
     */
    int ADDRESS_UNKNOWN=0;

    /**
     * Constant to specify an address type for an IMP user.
     */
    int ADDRESS_USER=2;

    /**
     * Get the address type.
     * @return type of the address.
     */
    int getAddressType();

    /**
     * Get the client id from the address.
     * @return id of the client from the address.
     */
    java.lang.String getClientID();

    /**
     * Get the display name of the user which may be a screen name
     * in a group or a nickname in a contact list.
     * @return display name of the address.
     */
    java.lang.String getDisplayName();

    /**
     * Get the domain name from the address (e.g., somecompany.com)
     * @return domain name of the URI address
     */
    java.lang.String getDomain();

    /**
     * Get the port number from an instance of IMPSAddress.
     * In the example above, the port number is: 5060.
     * @return port number in the URI part of the address.
     */
    int getPort();

    /**
     * Get the ResourceID from the URI part of the address.
     * 
     * A ResourceID may be a IMPSContactListID or a IMPSGroupID.
     * 
     * In the Wireless Village address example above, "friends" is the ResourceID.
     * @return ResourceID in the URI part of the address.
     */
    java.lang.String getResourceID();

    /**
     * Retrieve the scheme name from the URI part of the user address. For example, the scheme
     * name is: "sip" for SIP address and the scheme name is "wv" for wireless village address.
     * @return scheme name of the address.
     */
    java.lang.String getScheme();

    /**
     * Retrieve the URI from the address.
     * @return URI in the address: &amp;ltScheme>':'&amp;ltUserID>'/'&amp;ltResourceID>'@'&amp;ltDomain>[':'&amp;ltPort>].
     *  Example URI: &amp;ltsip:john.smith@somecompany.com&amp;gt.
     */
    java.lang.String getURI();

    /**
     * Get the id of a user from the address.
     * For example, for the SIP address &ltsip:john.smith@somecompany.com:5060
     * ,
     * the user name is "john.smith".
     * @return id of the user in the URI part of the address.
     */
    java.lang.String getUserID();

    /**
     * Set the address type.
     * @param type type of the address.
     * @throws java.lang.IllegalArgumentException when an invalid argument is specified
     */
    void setAddressType(int type) throws java.lang.IllegalArgumentException;

    /**
     * Set the client id in the address.
     * @param ClientID id of the client in the address.
     */
    void setClientID(java.lang.String ClientID);

    /**
     * Set the display name in the user's address.
     * @param name the display name in the user's address.
     */
    void setDisplayName(java.lang.String name);

    /**
     * Set the domain name in the IMPSAddress object(e.g., somecompany.com)
     * @param domain domain name in the address of the user.
     * @throws java.lang.IllegalArgumentException when an invalid argument is specified
     */
    void setDomain(java.lang.String domain) throws java.lang.IllegalArgumentException;

    /**
     * Set the port number in the address (e.g.:5060).
     * @param port port number in the URI part of the address.
     */
    void setPort(int port);

    /**
     * Set the ResourceID in the URI part of the address.
     * 
     * In the Wireless Village address example above, "friends" is the ResourceID.
     * @param resource ResourceID in the URI part of the address
     */
    void setResourceID(java.lang.String resource);

    /**
     * Set the scheme name in the URI part of the address, for example,
     * "sip" in case of SIP address, and
     * "wv" in case of Wireless Village address.
     * @param scheme scheme name in the URI part of the address.
     *  throws IllegalArgumentException when an invalid argument is specified
     * @throws java.lang.IllegalArgumentException
     */
    void setScheme(java.lang.String scheme) throws java.lang.IllegalArgumentException;

    /**
     * Set the URI in the address.
     * @param URI URI part of the address.
     * @throws java.lang.IllegalArgumentException when an invalid arugument is specified
     */
    void setURI(java.lang.String URI) throws java.lang.IllegalArgumentException;

    /**
     * Set the user id in the address.
     * @param userID user name in the URI part of the address.
     */
    void setUserID(java.lang.String userID);

}
