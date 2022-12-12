package javax.simple;
/**
 * This interface represents a message body.
 * An application can invoke
 * 
 * <A href="SimpleFactory.html">SimpleFactory</A>
 * .createMessageBody to create an empty
 * MessageBody instance (with Content-Length set to 0).
 * When MessageBody is a parameter for a UserAgent method
 * that sends messages out, the header values in MessageBody
 * always overwrite the same headers specified by other means.
 * 
 * When using MessageBody the charset parameter of Content-Type header
 * 
 * Please pay attention to setting correct charset parameter of Content-Type header within
 * message body. You have to set appropriate charset parameter manually before you send
 * your message body out. The charset parameter is not set to default value automatically because
 * the default value may be changed by invoking
 * 
 * <A href="Configuration.html">Configuration</A>
 * .setDefaultCharset method
 * and that subsequently may cause unexpected side effect.
 * 
 * 
 * 
 * Example 1: A
 * <A href="PageModeClient.html">PageModeClient</A>
 * obtains the Content-Type header from an incoming message, using the
 * getHeader method
 * in Message Body
 * 
 */
public interface MessageBody {
    /**
     * Obtain names of all present body-related headers
     * @return a String array of all the present header names.
     *  At least Content-length header should be present
     */
    java.lang.String[] getHeaderNames();

    /**
     * Obtain a named body-related header
     * @param headerName the name of the header. If headerName is
     *         null, the full header set is returned.
     * @return an array of Header objects for the named header,
     *  or null if no such header is present
     * @throws java.lang.IllegalArgumentException if headerName is not one of the following:
     *   Content-Type (mandatory)
     *       Content-Encoding
     *        Content-Language
     *        MIME-Version
     *       Content-Length
     *       null value
     */
    javax.simple.Header[] getHeaders(java.lang.String headerName) throws java.lang.IllegalArgumentException;

    /**
     * Retrieves the image type body. This method is only valid on a
     * MessageBody with Content-Type value
     * "image/xxxx". The returned
     * Object instance is an instance of java.awt.Image
     * on J2SE, J2ME Personal Profile, or J2ME Personal Basis Profile platform,
     * and an instance of
     * javax.microedition.lcdui.Image on J2ME MIDP/CLDC platform.
     * This method throws an UnsupportedEncodingException if the
     * implementation is unable to decode the image.
     * @return the decoded image as an Object instance
     * @throws javax.simple.InvalidOperationException if the Content-Type value is not "image/xxxx".
     * @throws java.io.UnsupportedEncodingException if the implementation is unable to decode the image body into a
     *  platform dependent image instance
     */
    java.lang.Object getImageBody() throws javax.simple.InvalidOperationException, java.io.UnsupportedEncodingException;

    /**
     * Obtain the raw bytes in the body.
     * If the message body is an image body, then the raw body is image binary data
     * in appropriate image format (PNG, JPG and so on). The name of the image format
     * is provided in the Content-Type header.
     * If the message body is a text body, then the raw body must contains text
     * string decoded to binary data according to appropriate charset as specified by
     * the charset parameter of Content-Type header, if preset,
     * or the default charset otherwise.
     * See Configuration.setDefaultCharset method for further details.
     * @return a byte array for the raw message body
     */
    byte[] getRawBody();

    /**
     * Obtain the text type body as a String. This method is only valid on text
     * type MessageBody. A MessageBody with the following
     * Content-Type values are considered as text type:
     * all text/xxxx types
     * application/sdp
     * application/pidf+xml
     * 
     * This method encodes the message body's raw data into text string according to
     * the charset parameter set in Content-type header.
     * If the charset  is not
     * present the default charset is used instead. See
     * Configuration.setDefaultCharset method for details.
     * When Content-Encoding header is present, this method may throw
     * an UnsupportedEncodingException if the implementation is not able
     * to decode the specified encoding.
     * @return text representation of body
     * @throws javax.simple.InvalidOperationException if the Content-Type is not text
     *  type
     * @throws java.io.UnsupportedEncodingException if the character encoding
     *  ("charset" parameter) or the Content-Encoding is not supported.
     */
    java.lang.String getTextBody() throws javax.simple.InvalidOperationException, java.io.UnsupportedEncodingException;

    /**
     * Check whether the body is of text type
     * @return true if body is text, false otherwise
     */
    boolean isText();

    /**
     * Set the image type body. This method is only valid on a MessageBody with
     * Content-Type value "image/xxxx". The supplied
     * Object parameter must be an instance of java.awt.Image
     * on J2SE, J2ME Personal Profile, or J2ME Personal Basis Profile platforms,
     * and an instance of javax.microedition.lcdui.Image on J2ME MIDP/CLDC platform.
     * @param image the image source, which is an  object of the Java platform
     *  Image instance (depending on the platform java.awt.Image
     *  or javax.microedition.lcdui.Image)
     * @throws java.lang.IllegalArgumentException if the image object is not an instance of
     *  java.awt.Image on J2SE, J2ME Personal Profile, or J2ME Personal Basis
     *  platforms, or not an instance of
     *  javax.microedition.lcdui.Image on J2ME MIDP/CLDC platform
     * @throws javax.simple.InvalidOperationException if the Content-Type value is not "image/xxxx".
     * @throws java.io.UnsupportedEncodingException if the implementation is unable to
     *  encode an javax.simple.Image instance into the message body according to
     *  the Content-Type and Content-Encoding values.
     */
    void setImageBody(java.lang.Object image) throws javax.simple.InvalidOperationException, java.io.UnsupportedEncodingException;

    /**
     * Set the raw body with a byte array. Content-Length will be updated.
     * The application is responsible to insure that the content matches the Content-Type
     * header.  For example, if the raw body has Content-Type header set to image/PNG
     * the binary data must be of PNG image binary format.
     * @param body the raw message body in a byte array
     */
    void setRawBody(byte[] body);

    /**
     * Set the text body. This method is only valid on text type MessageBody (See getTextBody()
     * for what is considered text type.) This method applies proper character encoding automatically.
     * (See Configuration.setDefaultCharset(String) for how character encoding is chosen.)
     * When Content-Encoding header is present, this method may throw an UnsupportedEncodingException
     * if the implementation is not able to perform the specified encoding.
     * @param text a String text to set as a body
     * @throws javax.simple.InvalidOperationException if the Content-Type is not text type of
     * @throws java.io.UnsupportedEncodingException if the character encoding ("charset" parameter)
     *  or the Content-Encoding is not supported.
     */
    void setTextBody(java.lang.String text) throws javax.simple.InvalidOperationException, java.io.UnsupportedEncodingException;

}
