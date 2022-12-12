package javax.simple;
/**
 * This interface represents a header in a SIP/SIMPLE message as defined in RFC 3261.
 * 
 * A message header consists of two parts: name and field.
 * The
 * <I>field</I>
 * includes a
 * <I>value</I>
 * and an optional
 * <I>parameter</I>
 * sequence,
 * where each parameter is a [parameter name = parameter value] pair.
 * 
 * 
 * Header example: "From: Bob ;tag=a6c85cf"
 * 
 * 
 * <TABLE>
 * <TR>getField()<TD>=&gt;</TD><TD>“Bob ;tag=a6c85cf”
 * </TD></TR><TR><TD>getName()</TD><TD>=&gt;</TD><TD>“From”
 * </TD></TR><TR><TD>getValue()</TD><TD>=&gt;</TD><TD>“Bob ”
 * </TD></TR><TR><TD>getParameterNames()</TD><TD>=&gt;</TD><TD>{“tag”}
 * </TD></TR><TR><TD>getParameter(“tag”)</TD><TD>=&gt;</TD><TD>“a6c85cf”
 * </TD></TR></TABLE>
 * 
 * For certain header names, there may be multiple headers of the same header name in a message.
 * A header name may take one of two forms, the complete form or the compact form.
 * In the API, only the complete form is allowed regardless which form is used in
 * the transmitted/received SIP/SIMPLE messages.
 * 
 * 
 * Example 1: Create and set a header field
 * using the setField(String field) method.
 * 
 */
public interface Header extends java.lang.Cloneable {
    /**
     * Create a clone (deep copy) of this Header
     * @return a clone copy of this Header
     */
    java.lang.Object clone();

    /**
     * Compare this header with another Header
     * @param header the Header object to which this Header is compared
     * @return true if header is a Header instance and has the same header name,
     *  header value and header parameters, or false otherwise
     */
    boolean equals(java.lang.Object header);

    /**
     * Obtain the header field (header value and header parameters)
     * @return header field
     * @throws java.lang.IllegalArgumentException if paraName is invalid.
     */
    java.lang.String getField() throws java.lang.IllegalArgumentException;

    /**
     * Obtain the unique string name of this Header
     * @return the name of this specific Header
     */
    java.lang.String getName();

    /**
     * Obtain the named parameter from a Header.
     * @return the parameter value, or a null if the parameter is not present
     * @throws java.lang.IllegalArgumentException if paraName is invalid
     */
    java.lang.String getParameter(java.lang.String paraName) throws java.lang.IllegalArgumentException;

    /**
     * Obtain the header parameter names
     * @return a String array of all the parameter names present in the header
     *  field, or null if no parameter is present
     */
    java.lang.String[] getParameterNames();

    /**
     * Get the header value (without parameters)
     * @return header value
     */
    java.lang.String getValue();

    /**
     * Remove the named header parameter
     * @param paraName parameter name
     * @throws java.lang.IllegalArgumentException
     */
    void removeParameter(java.lang.String paraName) throws java.lang.IllegalArgumentException;

    /**
     * Set the header field
     * @param field header field
     * @throws java.lang.IllegalArgumentException if field is invalid
     */
    void setField(java.lang.String field);

    /**
     * Set value of the named parameter, replacing any previous value.
     * @param paraName parameter name
     * @param paraValue parameter value
     * @throws java.lang.IllegalArgumentException if paraName is invalid.
     */
    void setParameter(java.lang.String paraName, java.lang.String paraValue) throws java.lang.IllegalArgumentException;

    /**
     * Set the header value without changing the parameters
     * @param value header value
     * @throws java.lang.IllegalArgumentException if value is invalid
     */
    void setValue(java.lang.String value) throws java.lang.IllegalArgumentException;

    /**
     * Return the String representation of the header
     * @return the String representation of the header
     */
    java.lang.String toString();

}
