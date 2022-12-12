package javax.imps;
/**
 * An element in an IMPSAccessControlList which assigns
 * an access level that a set of subjects have to a resource.
 * The subjects are identified by IMPSAddress instances.
 * Types of access are:
 * 
 * 
 * AC_ADMIN - administrator
 * AC_MOD - moderator
 * AC_WRITE, AC_GRANT - write
 * AC_READ - read-only
 * AC_NONE, AC_BLOCK - no access
 * 
 * 
 * See
 * {@link javax.imps.IMPSAccessControlList}
 * for an example.
 */
public interface IMPSAccessControlItem {
    /**
     * Administrator access.
     * Subjects can define access rules to the protected resource, access and
     * change resource properties, and remove the resource.
     */
    int AC_ADMIN=8;

    /**
     * No access.  Equivalent to AC_NONE.
     * Subjects can not read, write, or administer the resource.
     * Subjects can not obtain access lists from the resource.
     */
    int AC_BLOCK=0;

    /**
     * Write access.  Equivalent to AC_WRITE.
     * Subjects can write to the resource.  This includes updating
     * presence information for a (hereby protected) presentity,
     * or being able to send messages to a (hereby protected) group or user.
     */
    int AC_GRANT=2;

    /**
     * Moderator access.
     * Subjects can affect or block the content submitted by
     * others to the protected resource. Typically used for defining
     * group moderators.
     */
    int AC_MOD=4;

    /**
     * No access.  Equivalent to AC_BLOCK.
     * Subjects can not read, write, or administer the resource.
     * Subjects can not obtain access lists from the resource.
     */
    int AC_NONE=0;

    /**
     * Read-only access.
     * Subjects can read data from the protected resource, but are not able
     * to write to it or update it.   This includes accessing resources such
     * as an entity's presence information, or acting as a group spectator.
     */
    int AC_READ=1;

    /**
     * Write access.  Equivalent to AC_GRANT.
     * Subjects can write to the resource.  This includes updating
     * presence information for a (hereby protected) presentity,
     * or being able to send messages to a (hereby protected) group or user.
     */
    int AC_WRITE=2;

    /**
     * Add one user to the list of subjects, duplicates are ignored.
     * @param address user address
     */
    void addSubject(javax.imps.IMPSAddress address);

    /**
     * Return the access level for this item.
     * @return one value from the set: AC_NONE, AC_READ, AC_WRITE, AC_MOD, and AC_ADMIN
     */
    int getAccess();

    /**
     * Retrieve the list of subjects, identified by their address
     * to which the access level defined here applies.
     * @return an array of IMPSAddress instances, one for each subject
     */
    javax.imps.IMPSAddress[] getSubjects();

    /**
     * remove one user from the list of subjects, if present
     * @param address user address
     */
    void removeSubject(javax.imps.IMPSAddress address);

    /**
     * Set the access level for this item.
     * @param access value from the set: AC_NONE, AC_READ, AC_WRITE, AC_MOD, and AC_ADMIN
     */
    void setAccess(int access);

    /**
     * replace the entire subject list
     * @param address user addresses
     */
    void setSubjects(javax.imps.IMPSAddress[] address);

}
