package javax.imps;
/**
 * Identifies an error code that gives more specific
 * information about an exception. The application can retrieve the error code using
 * the IMPSException.getErrorCode method. The legal error code values
 * are defined in the final static fields.
 */
public class IMPSException extends java.lang.Exception {
    /**
     * This error code is set when no other error code can describe the
     * exception that occurred.
     */
    public static final int GENERAL_ERROR=0;

    /**
     * This error code is set when incorrect data is specified in the set operation for Content.
     */
    public static final int INCORRECT_DATA_TYPE=11;

    /**
     * This error code is set when the specified named item can not be found in the Content.
     */
    public static final int ITEM_NAME_NOT_FOUND=10;

    /**
     * This error code is set when the sending operation of a message fails due to
     * a transport error.
     */
    public static final int MESSAGE_NOT_SENT=4;

    /**
     * This error code is set when an implementation for this API cannot be found.
     */
    public static final int NOT_IMPLEMENTED=9;

    /**
     * This error code is set when the connection does not not exists.
     */
    public static final int NOT_LOGGED_IN=12;

    /**
     * This error code is set when a parsing error occurs.
     */
    public static final int PARSE_ERROR=8;

    /**
     * This error code is set when a subscription already exists for a presentity
     * and the corresponding subscriber.
     */
    public static final int SUBSCRIPTION_ALREADY_EXISTS=3;

    /**
     * This error code is set when a subscription does not exist for a presentity
     * and the corresponding subscriber.
     */
    public static final int SUBSCRIPTION_DOES_NOT_EXIST=2;

    /**
     * This error code is set when there is no transaction associated with
     * a transaction ID.
     */
    public static final int TRANSACTION_DOES_NOT_EXIST=1;

    /**
     * Constructs a new IMPSException with an error code.
     * @param errorCode the error code for the exception. Its value must be one
     *  of those listed in this class.
     *  An unlisted error code will be treated as a GENERAL_ERROR.
     */
    public IMPSException(int errorCode) {
         //TODO codavaj!!
    }

    /**
     * Constructs a new IMPSException with an error code and a message
     * to describe the error.
     * @param error a string that describes the exception
     * @param errorCode the error code for the exception. Its value must be one
     *  of those listed in this class. An unlisted error code will be treated as
     *  a GENERAL_ERROR.
     */
    public IMPSException(java.lang.String error, int errorCode) {
         //TODO codavaj!!
    }

    /**
     * Retrieves the error code value.
     * @return the error code for the exception.
     */
    public int getErrorCode() {
        return 0; //TODO codavaj!!
    }

}
