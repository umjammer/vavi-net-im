package javax.imps;
/**
 * Defines the access rules to a resource as an access control list (ACL).
 * The resource itself is identified at the time the ACL is assigned to the resource.
 * Access control is support for the following resource types: IMPSGroup,
 * PresencePublisher, IMClient.
 * <p>
 * A subject's access to a resource is determined by a combination of the
 * ACL and the resource's access policy. The first occurrence of a subject
 * in the resource's ACL determines the access rights of that subject.
 * If a subject is not present in the ACL, then the resource's access policy
 * is used.  For example, a group might be private or public.  In the case
 * of a private group, access not explicitly granted to the resource is denied.
 * </p><p>
 * Default policies for groups are protocol specific.  The application can
 * obtain the default setting by using the getDefaultAccess method.
 * </p><p>
 * Access lists may be enforced by the server or by the implementation
 * depending on the resource.  They should not be enforced by
 * the application.
 * </p>
 * <p>
 * The following example shows the creation of a group
 * and its access control settings.
 * </p>
 * <pre>
 * // create a group and assign an access control list (ACL).
 * IMPSGroup group;
 * Extensible groupProperties;
 * IMPSAddress groupAddress;
 * IMPSAddress subjectAddress;
 * IMPSAccessControlItem[] accessItem;
 * IMPSAccessControlList accessList;
 * 
 * try {
 *   group = factory.createIMPSGroup(connection);
 *   groupProperties = factory.createExtensible();
 *   groupAddress = factory.createIMPSAddress("wv:owner/movie-group@example.com",
 *   IMPSAddress.ADDRESS_GROUP);
 *   subjectAddress = factory.createIMPSAddress("wv:alice@somedomain.com", IMPSAddress.ADDRESS_USER);
 * } catch (IMPSException e) {
 *   e.printStackTrace();
 * }
 * // all group properties and their default settings are protocol defined
 * groupProperties.setText("Name", "MOVIE-GROUP");
 * groupProperties.setText("AccessType", "RESTRICTED");
 * groupProperties.setText("Type", "PRIVATE");
 * groupProperties.setText("PrivateMessaging", "TRUE");
 * groupProperties.setText("Searchable", "TRUE");
 * groupProperties.setText("Topic", "Discuss Oscar Movies");
 * 
 * // create a  group with the above properties
 * IMPSTid tid;
 * try {
 *   // immediately after creation, the creator has full access (AC_ADMIN) to
 *   // the group.  since the group access type is RESTRICTED, all other
 *   // subjects have access AC_NONE unless otherwise set.
 *   // @note this is server and/or protocol dependent, and if we can't
 *   // enforce it then ACL should be passed as param to setGroup
 *   tid = group.setGroup(groupAddress, groupProperties, groupAddress, true, true);
 * 
 *   // joinTransaction is application method defined in
 *   // section
 *   IMPSStatus status = joinTransaction(tid);
 *   if(status.getResultCode() == IMPSStatus.OK){
 *     // group object was created successfully
 *     // assign read-only access to Alice
 *     accessItem[0] = factory.createACLItem();
 *     accessItem[0].addSubject(subjectAddress);
 *     accessItem[0].setAccess(IMPSAccessControlItem.AC_READ);
 * 
 *     accessList = factory.createACL();
 *     accessList.setItems(accessItem);
 * 
 *     // if an ACL is previously set, it is replaced by the new ACL
 *     // user must have AC_ADMIN in order to set the ACL for a resource
 *     tid = group.setAccessControlList(groupAddress, accessList);
 *     // joinTransaction is application method defined in
 *     // section
 *     status = joinTransaction(tid);
 *     if(status.getResultCode() == IMPSStatus.OK){
 *       // access control list was set successfully.
 *     }
 *   }
 * } catch (IMPSException e) {
 *   e.printStackTrace();
 * }
 * </pre>
 * 
 */
public interface IMPSAccessControlList {
    /**
     * access level for subjects not matched by any access control item.
     * @return one value from the set: AC_NONE, AC_READ, AC_WRITE, AC_MOD, and AC_ADMIN
     */
    int getDefaultAccess();

    /**
     * get the complete set of items for this ACL.  Each item
     * shows an access type and a list of subjects with the access type.
     * @return ordered list of access control items.
     */
    javax.imps.IMPSAccessControlItem[] getItems();

    /**
     * set the ACL to the array of items, replacing the previous ACL if present.
     * @param items an array of items, each item containing an access type and a set of subjects
     *  with the given access type
     */
    void setItems(javax.imps.IMPSAccessControlItem[] items);

}
