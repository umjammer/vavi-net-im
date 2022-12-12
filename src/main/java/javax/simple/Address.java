package javax.simple;
/**
 * Address represents the origin or destination of a SIMPLE/SIP
 * request or response. It consists of two parts, where the syntax for a
 * URI is defined in Section 19.1 of RFC 3261:
 * display name
 * URI
 * 
 * For example, in the address: "Winston Smith
 * sip:wsmith@panasonic.com:5060
 * "
 * "Winston Smith" is the display name
 * "sip:wsmith@panasonic.com:5060" is the URI
 * 
 * 
 * Acceptable forms for a URI for use as addresses (in the From,
 * To, and Request-URI headers) are:
 * 
 * 
 * URI    := scheme:user@host:port;parameters
 * scheme := "sip"|"sips"
 * 
 * 
 * The Address interface has set/get methods for each component of the URI as well as
 * for the display name.
 * A non-null URI scheme and the URI host are required in a valid address.
 * 
 * To create an Address, invoke either of the following factory methods:
 * 
 * SimpleFactory.createAddress(String displayName, String scheme, String user, String host, String port)
 * SimpleFactory.createAddress(String address)
 * 
 */
public interface Address extends java.lang.Cloneable {
    /**
     * Creates a deep copy of the address.
     * @return deep copy of this Address
     */
    java.lang.Object clone();

    /**
     * Checks whether the URI of this address is equivalent to the URI of another address
     * @param address the Address object to which this Address is compared
     * @return true if the URI of this Address is equal to the URI of the 
     *  address parameter, false otherwise
     */
    boolean equals(java.lang.Object address);

    /**
     * Obtain the display name of the address or null if no display name is set.
     * For the example address "Winston Smith
     * sip:wsmith@panasonic.com:5060
     * ", invoking
     * this method would return "Winston Smith".
     * @return the display name, or a null if the display name is not set.
     */
    java.lang.String getDisplayName();

    /**
     * Obtain the URI host part of the address.
     * For the example address "Winston Smith
     * sip:wsmith@panasonic.com:5060
     * ", invoking
     * this method would return "panasonic.com".
     * @return the host
     */
    java.lang.String getHost();

    /**
     * Obtain the value of the named URI parameter
     * @param paraName parameter name
     * @return parameter value, or null if the parameter is not present.
     */
    java.lang.String getParameter(java.lang.String paraName);

    /**
     * Obtain an array of the names of each parameter in the URI of the address.
     * @return array of all parameter names present in the Address,
     *  or null if no parameter is present
     */
    java.lang.String[] getParameterNames();

    /**
     * Obtain the URI port from the address.
     * Returns a 5060 if no URI port is present.
     * For the example address "Winston Smith
     * sip:wsmith@panasonic.com:6000
     * ", invoking
     * this method would return 6000.
     * @return the URI port, or a 5060 if the URI port is not present.
     */
    int getPort();

    /**
     * Obtain the URI scheme part of the address, either sip or sips.
     * For the example address "Winston Smith
     * sip:wsmith@panasonic.com:5060
     * ", invoking
     * this method would return sip.
     * @return the URI scheme
     */
    java.lang.String getScheme();

    /**
     * Obtain the URI without the angle brackets "< >" of the address.
     * For the example address "Winston Smith
     * sip:wsmith@panasonic.com:5060
     * ", invoking
     * this method would return "sip:wsmith@panasonic.com:5060".
     * @return the URI without the brackets.
     */
    java.lang.String getURI();

    /**
     * Obtain the URI user part of the address.
     * For the example address "Winston Smith
     * sip:wsmith@panasonic.com:5060
     * ", invoking
     * this method would return "wsmith".
     * @return the user
     */
    java.lang.String getUser();

    /**
     * Remove the indicated URI parameter from the address
     * @param paraName the parameter name
     * @throws java.lang.IllegalArgumentException if paraName is invalid
     */
    void removeParameter(java.lang.String paraName) throws java.lang.IllegalArgumentException;

    /**
     * Set the display name to the given value
     * @param displayName display name
     * @throws java.lang.IllegalArgumentException if displayName is invalid
     */
    void setDisplayName(java.lang.String displayName) throws java.lang.IllegalArgumentException;

    /**
     * Set the URI host part of the address.  Must be non-null.
     * @param host host name/address
     * @throws java.lang.IllegalArgumentException if host is invalid
     */
    void setHost(java.lang.String host) throws java.lang.IllegalArgumentException;

    /**
     * Set the indicated URI parameter to the specified value.
     * Any previous value is replaced with the new value.
     * @param paraName parameter name
     * @param paraValue parameter value
     * @throws java.lang.IllegalArgumentException if paraName or paraValue is invalid
     */
    void setParameter(java.lang.String paraName, java.lang.String paraValue) throws java.lang.IllegalArgumentException;

    /**
     * Set the URI port number of the address
     * @param port port number
     * @throws java.lang.IllegalArgumentException if port is less than 0
     */
    void setPort(int port) throws java.lang.IllegalArgumentException;

    /**
     * Set the URI scheme part of the address. Only sip and sips are permitted.
     * @param scheme SIP scheme, either sip or sips
     * @throws java.lang.IllegalArgumentException if scheme is invalid
     */
    void setScheme(java.lang.String scheme) throws java.lang.IllegalArgumentException;

    /**
     * Set the URI part of the SIP address (without parameters)
     * @param URI URI of the address
     * @throws java.lang.IllegalArgumentException if URI is invalid.
     */
    void setURI(java.lang.String URI) throws java.lang.IllegalArgumentException;

    /**
     * Set the URI user of the address
     * @param user URI user. A null or blank string is regarded as an absent URI user
     * @throws java.lang.IllegalArgumentException if user is invalid
     */
    void setUser(java.lang.String user) throws java.lang.IllegalArgumentException;

    /**
     * Create a string format of the Address in the form "display name
     * URI
     * ".
     * @return a string representation of the Address
     */
    java.lang.String toString();

}
