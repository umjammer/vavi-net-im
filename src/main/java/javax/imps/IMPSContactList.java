package javax.imps;
/**
 * A contact list is a server-stored named collection of addresses referred to
 * as contacts.
 * IMPSContactList provides the functions by which the
 * application manages one or more collections of contacts.
 * Each IMPSContactList has a unique address of type
 * IMPSAddress.ADDRESS_CONTACTLIST, and an optional display
 * name.  The IMPSContactList address is also called
 * the contact list id.
 * The contact list id is assigned by the application at the time the list is
 * created and cannot be changed.
 * The address of the contact list must be unique for a
 * IMPSContactList - IMPSConnection association.
 * <p>
 * Each contact is represented as an
 * {@link javax.imps.IMPSAddress}
 * .
 * Any legal IMPSAddress is permitted as a contact list
 * entry.
 * </p><p>
 * The application obtains an instance of IMPSContactList
 * using
 * {@link javax.imps.IMPSFactory}
 * createIMPSContactList(IMPSConnection c)
 * method.  (An implementation may return the same object instance
 * for multiple calls to createIMPSContactList() which have the same
 * IMPSConnection instance.)  An instance of
 * IMPSContactList is a client-side proxy for sending requests
 * to the server which stores the contact list.
 * Each IMPSContactList instance is associated with a
 * IMPSConnection instance.
 * An application can only create and manage its own contact lists.
 * There is no capability for a public contact list or a shared contact list.
 * Beyond the connection login method, there is no contact list
 * specific authorization requirement for editing, creating, or deleting
 * contact lists.
 * There is no subscription - notification mechanism for
 * an application to watch contact list changes.
 * </p><p>
 * Each contact list has a display name.  The contents of the
 * display name are opaque to the contact list server.
 * Display names need not be unique.
 * </p><p>
 * There is a single default contact list.  The initial contact
 * list is default.
 * </p><p>
 * Maximum size of a contact list and the maximum number of contact lists
 * is implementation dependent.  In some environments, one or more
 * initial contact lists may be administratively pre-configured for
 * the user.  Contact lists may contain duplicate entries.
 * Maximum display name length is implementation dependent.
 * </p><p>
 * Implementation note:
 * Some protocols (e.g., XMPP) automatically update the contact list
 * when a subscription is made.  This specification permits such auto-updating
 * but does not require notification to the application when such an
 * event occurs.
 * </p>
 * 
 * 
 * 
 * <pre>
 * // Creates a contact list. Retrieves list members addresses from
 * // the server and sends welcome messages to all the members.
 * String displayName = "friends"
 * IMPSAddress contactListID
 * IMPSStatus status;
 * IMPSAddress[] contactListAddresses;
 * IMPSAddress[] serverListAddresses;
 * IMPSContactList contactList;
 * IMClient imClient;
 * try {
 *   contactList = factory.createIMPSContactList(connection);
 *   imClient = factory.createIMClient(connection);
 *   contactListID = factory.createIMPSAddress("wv:alice/friends@somedomain.com",
 *   IMPSAddress.ADDRESS_CONTACTLIST);
 *   contactListAddresses[0] = factory.createIMPSAddress("wv:bob@example.com",
 *   IMPSAddress.ADDRESS_USER);
 *   contactListAddresses[1] = factory.createIMPSAddress("wv:xyz@example.com",
 *   IMPSAddress.ADDRESS_USER);
 *   contactListAddresses[2] = factory.createIMPSAddress("wv:abc@example.com",
 *   IMPSAddress.ADDRESS_USER);
 * } catch ( IMPSException e) {}
 * 
 * InstantMessage im = imClient.createMessage("Welcome to Alice's friends list");
 * IMPSTid tid = contactList.setContactList(contactListID, displayName,
 * false, contactListAddresses);
 * // joinTransaction is application method defined in
 * // <A href="../../overview-summary.html#_JOIN_TRANS_">Overview</A>
 * // section
 * status = joinTransaction(tid);
 * if(status.getResultCode() == IMPSStatus.OK){
 *   // get contact list memebers addresses
 *   tid = contactList.getListMembersAddress();
 *   // joinTransaction is application method defined in
 *   // <A href="../../overview-summary.html#_JOIN_TRANS_">Overview</A>
 *   // section
 *   status = joinTransaction(tid);
 *   if(status.getResultCode() == IMPSStatus.OK){
 *     if(status.getRequest().getRequestType() == IMPSRequest.CL_GET_MEMBERS_ADDRESS){
 *       serverListAddresses = (IMPSAddress[])status.getData();
 *       for(int i=0; i < serverListAddresses.length; i++){
 *         imClient.sendMessage(im, false);
 *       }
 *     }
 *   }
 * }
 * </pre>
 */
public interface IMPSContactList {
    /**
     * Add a member to a contact list indicated by the contact list id.
     * If a contact already exists in the contact list, the existing
     * entry is replaced with the new entry.
     * Error status is returned if the indicated contact list does not exist.
     * 
     * @param contactListID indicates the ID of the contact list to which the member is
     *  to be added. (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_CONTACTLIST})
     * @param addMember the address to be added to the contact list.
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#CL_ADD_MEMBER} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid addContactListMember(javax.imps.IMPSAddress contactListID, javax.imps.IMPSAddress addMember) throws javax.imps.IMPSException;

    /**
     * Delete the indicated contact list.
     * Upon successfull deletion, the contact list is  removed from the server
     * associated with the connection.
     * Error status is returned if the indicated contact list does not exist.
     * @param contactListID indicates the ID of the list to be deleted.
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#CL_DELETE} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid deleteContactList(javax.imps.IMPSAddress contactListID) throws javax.imps.IMPSException;

    /**
     * Retrieve the list of all existing contact list IDs for this connection.
     * If there are no contact lists for this connection, then null is returned.
     * To obtain the contact list ids from the status, do:
     * IMPSAddress [] contactListID = (IMPSAddress [])status.getRequest.getData()
     * 
     * @return an instance of IMPSTid (transaction id) that identifies this request.  If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#CL_GETIDS}.
     */
    javax.imps.IMPSTid getContactListIDs();

    /**
     * Retrieve the default contact list for this connection.
     * If there are no contact lists for this connection, then null is returned.
     * 
     * @return an instance of IMPSTid (transaction id) that identifies this request.  If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#CL_GETIDS}.
     */
    javax.imps.IMPSTid getDefaultContactList();

    /**
     * Get the contact list's display name.
     * @param contactListID address of the contact list
     * @return the contact list's display name
     */
    java.lang.String getDisplayName(javax.imps.IMPSAddress contactListID);

    /**
     * Retrieve the list of members belonging to the indicated contact list.
     * Returns error status if there is no list corresponding to the id.
     * 
     * To obtain the contact list from the status, do:
     * IMPSAddress [] contactList = status.getRequest.getData()
     * 
     * @param contactListID indicates the ID of the contact list.
     *  (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_CONTACTLIST})
     * @return an instance of IMPSTid (transaction id) that identifies this request. If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#CL_GET_MEMBERS} is returned in the status of the response.
     *  <P/>
     */
    javax.imps.IMPSTid getListMembersAddress(javax.imps.IMPSAddress contactListID);

    /**
     * Remove contact list member from the indicated contact list.
     * If the contact to be removed is not found in the contact list,
     * it is ignored.
     * 
     * Note: In some protocols (e.g., XMPP) removal of a contact list
     * member causes subscriptions from the member to be automatically
     * cancelled.  Implementations should follow the protocol rules
     * in such cases.
     * 
     * @param contactListID indicates the ID of the contact list.
     *  (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_CONTACTLIST})
     * @param removeMember address of the member whose membership is to be removed.
     *  (IMPSAddress type is {@link javax.imps.IMPSAddress#ADDRESS_USER} and
     *  {@link javax.imps.IMPSAddress#ADDRESS_NICKNAME})
     * @return An instance of IMPSTid.  If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#CL_REMOVE_MEMBER} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid removeContactListMember(javax.imps.IMPSAddress contactListID, javax.imps.IMPSAddress removeMember) throws javax.imps.IMPSException;

    /**
     * Create a server-side contact list.  The server is identified
     * according to the IMPSConnection object associated with the
     * IMPSContactList.  If a contact list having the same id
     * already exists for the associated connection, the existing contact list
     * is replaced with the new one, and the new display name is used.
     * More than one contact list may be created.
     * @param contactListID indicates the address of a contact list. Must be unique
     *     per connection
     *    (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_CONTACTLIST})
     * @param displayName indicates the display name for this contact list.
     * @param defaultContactList indicates whether this particular contact
     *  list is a default contact list.  If this list is the initial list
     *  for this connection, then this flag is ignored and the list is
     *  automatically the default list.
     * @param contacts a list of addresses, stored in the server
     * @return an instance of IMPSTid (transaction id) that identifies this request.If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#CL_CREATE} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid setContactList(javax.imps.IMPSAddress contactListID, java.lang.String displayName, boolean defaultContactList, javax.imps.IMPSAddress[] contacts) throws javax.imps.IMPSException;

    /**
     * Set the display name of a contact list and optionally change
     * this to be the default contact list for the connection.
     * Error status is returned if the indicated contact list does not exist.
     * 
     * @param contactListID indicates the ID of the contact list.
     *  (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_CONTACTLIST})
     * @param displayName indicates the display name for this contact list.
     * @param defaultContactList if true the indicated contact
     *  list becomes the default list.  if false no change
     *  is made.
     * @return an instance of IMPSTid (transaction id) that identifies this request.  If the request is successful,
     *  the content type is {@link javax.imps.IMPSRequest#CL_SET_PROP} with null data.
     * @throws javax.imps.IMPSException with the error code NOT_IMPLEMENTED when the
     *  this feature is not supported.
     */
    javax.imps.IMPSTid setContactListProperties(javax.imps.IMPSAddress contactListID, java.lang.String displayName, boolean defaultContactList) throws javax.imps.IMPSException;

}
