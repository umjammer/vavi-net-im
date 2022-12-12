package javax.imps;
/**
 * Message id, sender, and subject for server-stored messages.
 * Message properties are obtained when an IMClient
 * requests getMessageList and are typically used
 * to determine whether a stored message should be retrieved,
 * forwarded, or rejected.
 */
public interface StoredMessageRecord {
    /**
     * get the id by which the message can be retrieved, forwarded, or rejected.
     * @return server assigned id of the message
     */
    java.lang.String getMessageID();

    /**
     * get the sender's address.
     * @return the address of the user who sent the message
     */
    javax.imps.IMPSAddress getSender();

    /**
     * get the subject line of the message
     * @return the subject of the message or null if not present
     */
    java.lang.String getSubject();

}
