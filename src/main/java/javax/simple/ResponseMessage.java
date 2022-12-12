package javax.simple;
/**
 * ResponseMessage represents a SIP/SIMPLE response message.
 * The application does not directly instantiate response messages, instead
 * it uses UserAgent.sendResponse to indirectly create and send SIP/SIMPLE responses.
 * However, responses to outgoing requests are passed to the application
 * through the SimpleListener.processResponse method.
 * The application uses this interface to process such response messages.
 * 
 * The general format of a SIP response message (per RFC 3261) is as follows:
 * 
 * <pre>
 * SIP-Version SP Status-Code SP Reason-Phrase
 * CRLF
 * message-header-1
 * CRLF
 * message-header-2
 * CRLF
 * ...
 * 
 * CRLF
 * [ message-body ]
 * </pre>
 * 
 * where
 * is the Carriage-Return Line-Feed sequence.
 * 
 * is the SIP version that the message conforms to
 * 
 * is a 3-digit integer status code
 * 
 * is a text phrase identifying the status code
 * 
 * is a space character
 * 
 */
public interface ResponseMessage extends javax.simple.Message {
    int ACCEPTED=202;

    int ADDRESS_INCOMPLETE=484;

    int ALTERNATIVE_SERVICE=380;

    int AMBIGUOUS=485;

    int BAD_EVENT=489;

    int BAD_EXTENSION=420;

    int BAD_GATEWAY=502;

    int BAD_REQUEST=400;

    int BUSY_EVERYWHERE=600;

    int BUSY_HERE=486;

    int CALL_IS_BEING_FORWARDED=181;

    int CALL_OR_TRANSACTION_DOES_NOT_EXIST=481;

    int CONDITIONAL_REQUEST_FAILED=412;

    int DECLINE=603;

    int DOES_NOT_EXIST_ANYWHERE=604;

    int EXTENSION_REQUIRED=421;

    int FORBIDDEN=403;

    int GONE=410;

    int INTERVAL_TOO_BRIEF=423;

    int LOOP_DETECTED=482;

    int MESSAGE_TOO_LARGE=513;

    int METHOD_NOT_ALLOWED=405;

    int MOVED_PERMANENTLY=301;

    int MOVED_TEMPORARILY=302;

    int MULTIPLE_CHOICES=300;

    int NOT_ACCEPTABLE=406;

    int NOT_ACCEPTABLE_HERE=488;

    int NOT_FOUND=404;

    int NOT_IMPLEMENTED=501;

    int OK=200;

    int PROXY_AUTHENTICATION_REQUIRED=407;

    int QUEUED=182;

    int REQUEST_ENTITY_TOO_LARGE=413;

    int REQUEST_PENDING=491;

    int REQUEST_TERMINATED=487;

    int REQUEST_TIMEOUT=408;

    int REQUEST_URI_TOO_LONG=414;

    int SERVER_INTERNAL_ERROR=500;

    int SERVER_TIMEOUT=504;

    int SERVICE_UNAVAILABLE=503;

    int SESSION_NOT_ACCEPTABLE=606;

    int SESSION_PROGRESS=183;

    int TEMPORARILY_UNAVAILABLE=480;

    int TOO_MANY_HOPS=483;

    int TRYING=100;

    int UNAUTHORIZED=401;

    int UNDECIPHERABLE=493;

    int UNSUPPORTED_MEDIA_TYPE=415;

    int UNSUPPORTED_URI_SCHEME=416;

    int USE_PROXY=305;

    int VERSION_NOT_SUPPORTED=505;

    /**
     * Retrieve the method name from CSeq field.
     * @return method name
     */
    java.lang.String getCSeqMethod();

    /**
     * Retrieve the reason phrase of a response message
     * @return the reason phrase
     */
    java.lang.String getReasonPhrase();

    /**
     * Retrieve the status code of a response message.
     * The meaning of status codes are defined in RFC 3261.
     * @return the status code
     */
    int getStatusCode();

    /**
     * Check if status code is failed (that is, 4XX, 5XX, or 6XX)
     * @return true if status code is failed, false otherwise
     */
    boolean isStatusFailed();

    /**
     * Check if status code is provisional (that is, 1XX)
     * @return true if status code is provisional
     */
    boolean isStatusProvisional();

    /**
     * Check if status code is successful (that is, 2XX)
     * @return true if status code is successful
     */
    boolean isStatusSuccessful();

}
