package javax.imps;
/**
 * An opaque reference to implementation generated transaction ID.
 * associated with a protocol request-response pair.
 */
public interface IMPSTid {
    /**
     * Test if 2 transaction ids represent the same transaction
     * @return true if the two tids are the same transaction
     */
    boolean equals(javax.imps.IMPSTid tid);

}
