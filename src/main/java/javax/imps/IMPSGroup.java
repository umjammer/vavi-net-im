package javax.imps;
/**
 * Used to access and manage information associated with a server-side group
 * and with this user's membership in the group.  IMPSGroup is a client-side agent or
 * proxy by which the application issues requests to manage group properties
 * and life cycle.
 * <p>
 * A group represents a multi-party messaging session.
 * This type of addressing allows users to send content to all
 * subscribed members of a group in one step rather than to each individual entity.
 * </p>
 * 
 * 
 * This example shows the ability to join and leave a predefined IMPSGroup,
 * and obtain the joined-list and group membership changes.
 * <pre>
 * try {
 *   IMPSGroup group = factory.createIMPSGroup(connection);
 *   IMPSAddress groupIMPSAddress = factory.createIMPSAddress("wv:pluto/work-group@example.com",
 *   IMPSAddress.ADDRESS_GROUP);
 *   IMPSAddress myScreenName = factory.createIMPSAddress("flash", IMPSAddress.ADDRESS_SCREENNAME);
 *   // get joined list, subscribe to changes
 *   group.join(groupIMPSAddress, myScreenName, true, true);
 * 
 *   // send message to group
 * 
 *   // leave the group
 * } catch (IMPSException e) {
 *   e.printStackTrace();
 * }
 * 
 * // ... handle joined list and group changes here
 * public void processRequest(IMPSConnection c, IMPSRequest request)
 * {
 *   switch (request.getType()) {
 *   // Use the request type to determine what kind of data
 *   // has been received from the requestor
 *   case IMPSRequest.GROUP_GETJND :
 *     IMPSAddress [] grp_members = (IMPSAddress [])request.getData();
 *     // show members to user
 *     break;
 *   case IMPSRequest.GROUP_CHANGED_ADD :
 *     IMPSAddress [] grp_members = (IMPSAddress [])request.getData();
 *     // add to members shown to user
 *     break;
 *   case IMPSRequest.GROUP_CHANGED_REMOVE :
 *     IMPSAddress [] grp_members = (IMPSAddress [])request.getData();
 *     // remove from members shown to user
 *     break;
 *   }
 * }
 * </pre>
 * 
 * The following example shows the creation of a group
 * and setting group properties using
 * {@link javax.imps.Extensible}
 * .
 * 
 * <pre>
 * // Example of how to manipulate Extensible Object
 * // In particular this example shows how to set different
 * // values for groupProperties
 * try {
 *   Extensible groupProperties = factory.createExtensible();
 *   IMPSGroup group = factory.createIMPSGroup(connection);
 *   IMPSAddress groupIMPSAddress = factory.createIMPSAddress("wv:member/movie-group@example.com",
 *   IMPSAddress.ADDRESS_GROUP);
 * } catch (IMPSException e) {
 *   e.printStackTrace();
 * }
 * 
 * groupProperties.setText("Name", "MOVIE-GROUP");
 * groupProperties.setText("AccessType", "RESTRCITED");
 * groupProperties.setText("Type", "PRIVATE");
 * groupProperties.setText("PrivateMessaging", "TRUE");
 * groupProperties.setText("Searchable", "TRUE");
 * groupProperties.setText("Topic", "Discuss Oscar Movies");
 * IMPSTid tid;
 * // screen name same as groupAddress
 * try {
 *   tid = group.setGroup(groupAddress, groupProperties, groupAddress, true, true);
 * } catch (IMPSException e) { }
 * // joinTransaction is application method defined in
 * // <A href="../../overview-summary.html#_JOIN_TRANS_">Overview</A>
 * // section
 * IMPSStatus status = joinTransaction(tid);
 * if(status.getResultCode() == IMPSStatus.OK){
 *   // group  was created successfully
 * }
 * </pre>
 */
public interface IMPSGroup {
    /**
     * Add a member to the indicated group by providing the ID of the member to be added.
     * Only a user with sufficient access rights may set the properties of a group.
     * 
     * @param group the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @param userList the list of the user IDs to be added (IMPSAddress types are
     *  {@link javax.imps.IMPSAddress#ADDRESS_USER} and {@link javax.imps.IMPSAddress#ADDRESS_SCREENNAME})
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_ADDMEMBER} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid addGroupMember(javax.imps.IMPSAddress group, javax.imps.IMPSAddress[] userList) throws javax.imps.IMPSException;

    /**
     * Delete the indicated group.
     * 
     * Only a user with sufficient access rights can delete a group.
     * After a successful removal of the group, the server also removes all related
     * subscriptions to that group.
     * 
     * @param group indicates the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_DELETE} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid deleteGroup(javax.imps.IMPSAddress group) throws javax.imps.IMPSException;

    /**
     * Get the group's current access control list.
     * 
     * @param group indicates the resource address for which access control is to be retrieved.
     * @return an instance of IMPSTid (transaction id) that identifies this request.If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_SETPRP} with
     *  the group's {@link javax.imps.IMPSAccessControlList} as data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid getAccessControlList(javax.imps.IMPSAddress group) throws javax.imps.IMPSException;

    /**
     * Get the list of  members of the indicated group.
     * 
     * @param group the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_GETMEMBERS} with a list of IMPSAddresses.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid getGroupMembers(javax.imps.IMPSAddress group) throws javax.imps.IMPSException;

    /**
     * Get the properties of the indicated group.
     * 
     * @param group indicates the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_GETPRP} with {@link javax.imps.Extensible} data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid getGroupProperties(javax.imps.IMPSAddress group) throws javax.imps.IMPSException;

    /**
     * Get the list of users currently joined the group.
     * 
     * @param group indicates the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     *  <P>
     *  The corresponding asynchronous return event  type is {@link javax.imps.IMPSRequest#GROUP_GETJND}
     * </P>
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_GETJND} with IMPSAddress [] data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid getJoinedUser(javax.imps.IMPSAddress group) throws javax.imps.IMPSException;

    /**
     * Send a request to the server to join the indicated group.
     * Included in the request are the screen name of the user, and the user's own group properties.
     * This method can be called multiple times for a given group, in order to modify
     * subscriptions or change the screen name or other properties.
     * 
     * @param group address of group to join,  (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @param screenName nick name to use within the group.  Properties of this
     *  address may be used to specify additional properties
     *  associated with this user's membership.
     * @param joinedListFlag if true the current member list of the
     *  group will be sent.
     * @param subscribeFlag if true, an implicit group member change subscription
     *  is requested.
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_JOIN} with null data.
     */
    javax.imps.IMPSTid join(javax.imps.IMPSAddress group, javax.imps.IMPSAddress screenName, boolean joinedListFlag, boolean subscribeFlag);

    /**
     * Sends a request to the server to leave the indicated group.
     * 
     * @param group address of group to leave,  (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_LEAVE} with null data.
     */
    javax.imps.IMPSTid leave(javax.imps.IMPSAddress group);

    /**
     * Set the group's access control list (ACL).
     * This request may only be performed by a group member with admin-level
     * access to this group.  If an ACL is already set, it is replaced by the new ACL.
     * 
     * @param acl describes the access rights of various subject to this group.
     * @param group indicates the resource address for which access control is to be applied
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_MBRAC} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid setAccessControlList(javax.imps.IMPSAddress group, javax.imps.IMPSAccessControlList acl) throws javax.imps.IMPSException;

    /**
     * Sends  message to the server to create a new group.
     * 
     * @param group the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @param groupProperties indicates the initial group properties. The schema of IMPSGroupProperties
     *  is as follows: <P>
     *         </P><P>Name: String
     *         </P><P>AccessType: String (OPEN or RESTRICTED)
     *         </P><P>Type: String (PUBLIC or PRIVATE)
     *         </P><P>PrivateMessaging: String (TRUE or FALSE)
     *         </P><P>Searchable: String (TRUE, FALSE)
     *         </P><P>Topic: String
     *         </P><P>ActiveUser: String (no of users in string)
     *         </P><P>MaxUsers: String (no of users in string)
     *         </P><P>WelcomeNote: String
     *         </P><P>History: String (TRUE or FALSE)
     *         </P><P>AutoDelete: String (TRUE or FALSE)
     *         </P><P>Validity: String (specifies validity time in string)
     *         </P><P>
     *   If a protocol does not support a specific field, then null value is returned.</P>
     * @param screenName the screen name of the user (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_SCREENNAME})
     * @param joinFlag true if the user auto-joins the group when the group is created
     * @param subscribeFlag trueif a subscription to the group changes is made
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_CREATE} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when
     *  this feature is not supported.
     */
    javax.imps.IMPSTid setGroup(javax.imps.IMPSAddress group, javax.imps.Extensible groupProperties, javax.imps.IMPSAddress screenName, boolean joinFlag, boolean subscribeFlag) throws javax.imps.IMPSException;

    /**
     * Set the group properties of the indicated group.
     * Only a user with sufficient access rights may set the properties of a group.
     * 
     * @param group indicates the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @param groupProperties indicates the properties of the group to be set.
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_SETPRP} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid setGroupProperties(javax.imps.IMPSAddress group, javax.imps.Extensible groupProperties) throws javax.imps.IMPSException;

    /**
     * Subscribe to group-change notifications.
     * The subscriber receives notification messages when there is a change in the group status.
     * Notifications are passed to the application via the IMPSConnectionListener with a
     * IMPSRequest with a request type GROUP_CHANGED.
     * 
     * @param group indicates the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_SUBGRC} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid subscribeGroupChange(javax.imps.IMPSAddress group) throws javax.imps.IMPSException;

    /**
     * Unsubscribe for group-change notification messages.
     * After a successful unsubscription, the active subscription is removed.
     * 
     * @param group indicates the address of the group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#GROUP_UNSUBG} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid unsubscribeGroupChange(javax.imps.IMPSAddress group) throws javax.imps.IMPSException;

}
