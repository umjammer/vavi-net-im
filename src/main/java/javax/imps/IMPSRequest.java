package javax.imps;
/**
 * A IMPSRequest is a message of a specified type
 * exchanged between server and client, in either direction.
 * <p>
 * A IMPSRequest object is generated in two circumstances.
 * First,when an API method sends a protocol request to the server.
 * In this case, the API call returns a
 * {@link javax.imps.IMPSStatus}
 * which references the sent request.
 * The
 * {@link javax.imps.IMPSStatus#getRequest()}
 * method can be used to
 * access the original request:
 * </p>
 * <pre>
 * IMPSTid tid = connection.login(user, password);
 * // joinTransaction is application method defined in
 * // <A href="../../overview-summary.html#_JOIN_TRANS_">Overview</A>
 * // section
 * IMPSStatus status = joinTransaction(tid);
 * if (status.getResultCode() == IMPSStatus.OK) {
 *   IMPSRequest r = status.getRequest();
 *   if (r.getType() == IMPSRequest.LOGIN) {
 *     // update application state indicating logged in
 *   }
 * }
 * </pre>
 * 
 * Second, when a request or asynchronous event is received from the
 * server, the
 * {@link javax.imps.IMPSConnectionListener}
 * processRequest
 * method is invoked with a IMPSRequest object.
 * 
 * <pre>
 * public void processRequest(IMPSConnection c, IMPSRequest request)
 * {
 *   switch (request.getType()) {
 *   // Use the request type to determine what kind of data
 *   // has been received from the requestor
 *   case IMPSRequest.PR_UPDATE :
 *     PresenceInformation pinfo = (PresenceInformation)request.getData();
 *     break;
 *   }
 * }
 * </pre>
 * 
 * A IMPSRequest has two attributes:
 * 
 * <pre>
 * Type: the type of the event (PRESENCE_NOTICE, MESSAGE_NOTICE,
 * SERVER_DISCONNECT, asynchronous responses)
 * Data: content that must be converted to an application
 * object in order to be received.
 * </pre>
 * 
 * This class also defines static request types
 * managed by the implementation of the API.
 * 
 */
public interface IMPSRequest {
    /**
     * add a member to a existing contact list
     * direction: client to server
     * request method:
     * {@link javax.imps.IMPSContactList#addContactListMember(javax.imps.IMPSAddress, javax.imps.IMPSAddress)}
     * .
     */
    int CL_ADD_MEMBER=85;

    /**
     * add a new contact list
     * direction: client to server
     * request method:
     * {@link javax.imps.IMPSContactList#setContactList(javax.imps.IMPSAddress, java.lang.String, boolean, javax.imps.IMPSAddress[])}
     * .
     */
    int CL_CREATE=81;

    /**
     * delete an existing contact list
     * direction: client to server
     * request method:
     * {@link javax.imps.IMPSContactList#deleteContactList(javax.imps.IMPSAddress)}
     * .
     */
    int CL_DELETE=82;

    /**
     * get addresses of a contact list members
     * direction: client to server
     * request method:
     * {@link javax.imps.IMPSContactList#getListMembersAddress(javax.imps.IMPSAddress)}
     * .
     */
    int CL_GET_MEMBERS=84;

    /**
     * retrieve the list of contact lists owned by the caller
     * direction: client to server
     * request method:
     * {@link javax.imps.IMPSContactList#getContactListIDs()}
     * .
     * response data:
     * {@link javax.imps.IMPSAddress}
     * [] or null if no contact list is present.
     */
    int CL_GETIDS=80;

    /**
     * retrieve a contact list, including members and properties
     * request method:
     * {@link javax.imps.IMPSContactList#removeContactListMember(javax.imps.IMPSAddress, javax.imps.IMPSAddress)}
     * .
     */
    int CL_REMOVE_MEMBER=83;

    /**
     * set contact list properties
     * direction: client to server
     * request method:
     * {@link javax.imps.IMPSContactList#setContactListProperties(javax.imps.IMPSAddress, java.lang.String, boolean)}
     * .
     */
    int CL_SET_PROP=86;

    /**
     * Server initiated disconnect
     * direction: from server to client
     */
    int DISCONNECT=6;

    /**
     * add a member to the group
     * request method:
     * {@link javax.imps.IMPSGroup#addGroupMember(javax.imps.IMPSAddress, javax.imps.IMPSAddress[])}
     * .
     */
    int GROUP_ADDMEMBER=42;

    /**
     * group member change notification.  from server to client.
     * direction: server to client
     * request method:
     * {@link javax.imps.IMPSGroup#join(javax.imps.IMPSAddress, javax.imps.IMPSAddress, boolean, boolean)}
     * response data:
     * {@link javax.imps.IMPSAddress}
     * []
     */
    int GROUP_CHANGED=31;

    /**
     * request method:
     * {@link javax.imps.IMPSGroup#setGroup(javax.imps.IMPSAddress, javax.imps.Extensible, javax.imps.IMPSAddress, boolean, boolean)}
     * .
     * response data:
     * {@link javax.imps.IMPSGroup}
     */
    int GROUP_CREATE=32;

    /**
     * Delete a group
     * request method:
     * {@link javax.imps.IMPSGroup#deleteGroup(javax.imps.IMPSAddress)}
     * .
     * response data: none
     */
    int GROUP_DELETE=33;

    /**
     * get list of member
     * request method:
     * {@link javax.imps.IMPSGroup#getJoinedUser(javax.imps.IMPSAddress)}
     * .
     * response data:
     * {@link javax.imps.IMPSAddress}
     * []
     */
    int GROUP_GETJND=35;

    /**
     * get the members of the group
     * request method:
     * {@link javax.imps.IMPSGroup#getGroupMembers(javax.imps.IMPSAddress)}
     * .
     * response data:
     * {@link javax.imps.IMPSAddress}
     * []
     */
    int GROUP_GETMEMBERS=43;

    /**
     * Get a group's properties.
     * direction: client to server.
     * request method:
     * {@link javax.imps.IMPSGroup#getGroupProperties(javax.imps.IMPSAddress)}
     * .
     * response data: Modified
     * {@link javax.imps.IMPSGroup}
     */
    int GROUP_GETPRP=37;

    /**
     * Join a group.  or notification that someone joined the group.
     * direction: both
     * request method:
     * {@link javax.imps.IMPSGroup#join(javax.imps.IMPSAddress, javax.imps.IMPSAddress, boolean, boolean)}
     * .
     * response data: null
     */
    int GROUP_JOIN=34;

    /**
     * Leave a group.  or notification that someone left a group.
     * direction: both
     * request method:
     * {@link javax.imps.IMPSGroup#leave(javax.imps.IMPSAddress)}
     * request data:
     * {@link javax.imps.IMPSAddress}
     * []
     */
    int GROUP_LEAVE=30;

    /**
     * modify a group's access control list.
     * direction: both (server to client or client to server).
     * request method:
     * {@link javax.imps.IMPSGroup#setAccessControlList(javax.imps.IMPSAddress, javax.imps.IMPSAccessControlList)}
     * .
     * response or requrest data:
     * {@link javax.imps.IMPSAccessControlList}
     * .
     */
    int GROUP_MBRAC=36;

    /**
     * change the properties of a group.
     * direction: both
     * request method:
     * {@link javax.imps.IMPSGroup#setGroupProperties(javax.imps.IMPSAddress, javax.imps.Extensible)}
     * request/response data:
     * {@link javax.imps.IMPSGroup}
     */
    int GROUP_SETPRP=38;

    /**
     * subscribe to group changed notifications.
     * direction: client to server
     * request method:
     * {@link javax.imps.IMPSGroup#subscribeGroupChange(javax.imps.IMPSAddress)}
     * .
     * response data: none
     */
    int GROUP_SUBGRC=39;

    /**
     * subscribe to group changed notifications.
     * direction: client to server
     * request method:
     * {@link javax.imps.IMPSGroup#unsubscribeGroupChange(javax.imps.IMPSAddress)}
     * .
     * response data: none
     */
    int GROUP_UNSUBG=40;

    /**
     * message delivery report.
     * direction: server to client:
     * request data:
     * {@link javax.imps.Extensible}
     */
    int IM_DELIVERY_REPORT=57;

    /**
     * forward undelivered message.
     * direction: client to server
     * request method:
     * {@link javax.imps.IMClient#forwardIM(java.lang.String, javax.imps.IMPSAddress)}
     * .
     * response data: none
     */
    int IM_FORWARD=55;

    /**
     * get undelivered message ID list
     * direction: client to server
     * request method:
     * {@link javax.imps.IMClient#getMessageList(javax.imps.IMPSAddress, int)}
     * .
     * request data:
     * <A href="../../javax/imps/StoredMessageRecord.html">[]</A>
     */
    int IM_GETMLT=52;

    /**
     * download a message stored on the server
     * direction: client to server
     * request method:
     * {@link javax.imps.IMClient#getMessage(java.lang.String)}
     * .
     * response data:
     * {@link javax.imps.InstantMessage}
     * .
     */
    int IM_GETMSG=54;

    /**
     * New message notification from the server.
     * direction: server to client:
     * request data:
     * {@link javax.imps.InstantMessage}
     */
    int IM_MESSAGE_NOTICE=56;

    /**
     * Discard undelivered messages.
     * direction: client to server
     * request method:
     * {@link javax.imps.IMClient#rejectMessages(java.lang.String[])}
     * .
     * response data: none
     */
    int IM_RJTMSG=53;

    /**
     * send an instant message
     * direction: both
     * request method:
     * {@link javax.imps.IMClient#sendMessage(javax.imps.InstantMessage, boolean)}
     * .
     * request data:
     * {@link javax.imps.InstantMessage}
     * .
     * response data: none
     */
    int IM_SEND=50;

    /**
     * set delivery method
     * direction: client to server
     * request method:
     * {@link javax.imps.IMClient#setDeliveryMethod(int, int, javax.imps.IMPSAddress)}
     * .
     * response data: none
     */
    int IM_SETDEM=51;

    /**
     * Invite another user to a group.  Or sent by the server to relay
     * a group invitation from another user.
     * direction: both
     * request method:
     * {@link javax.imps.IMPSInvitation#invite(int, java.lang.String, javax.imps.IMPSAddress[], javax.imps.Extensible, int, java.lang.String, javax.imps.IMPSAddress)}
     * .
     * request data:
     * {@link javax.imps.IMPSInvitation}
     */
    int INVITE=20;

    /**
     * Cancel a previously sent and yet to be replied invitation.
     * direction: client server
     * request method:
     * {@link javax.imps.IMPSInvitation#cancelInvitation(java.lang.String, javax.imps.IMPSAddress[], java.lang.String, javax.imps.Extensible, javax.imps.IMPSAddress)}
     * .
     * request data:
     * {@link javax.imps.IMPSInvitation}
     */
    int INVITE_CANCEL=32;

    /**
     * Reply to an invitation.  sent by the server to relay the rsvp message
     * of a previously invited user.
     * direction: both
     * requestMethod:
     * {@link javax.imps.IMPSInvitation#replyInvitation(java.lang.String, javax.imps.IMPSAddress, boolean, java.lang.String, javax.imps.IMPSAddress)}
     * .
     * request data:
     * {@link javax.imps.IMPSInvitation}
     */
    int INVITE_REPLY=21;

    /**
     * Authenticate access to the service
     * direction: from client to server
     * request method:
     * {@link javax.imps.IMPSConnection#login(java.lang.String, java.lang.String)}
     * .
     * response data:
     * {@link javax.imps.IMPSServiceFeatures}
     */
    int LOGIN=1;

    /**
     * Logout from the service
     * direction: from client to server
     * request method:
     * {@link javax.imps.IMPSConnection#logout()}
     * .
     */
    int LOGOUT=2;

    /**
     * authorize a user to subscribe to this client's presence information
     * direction: client to server
     * request method: PresencePublisher#authorizeUser.
     */
    int PR_AUTHORIZE=63;

    /**
     * de-authorize a user to subscribe to this client's presence information
     * direction: client to server
     * request method: PresencePublisher#cancelAuthorization.
     */
    int PR_CANCELAUT=64;

    /**
     * fetch presence information
     * direction: client to server
     * request method: PresenceWatcher#fetchPresenceInfo.
     * response data: PresenceInformation[]
     */
    int PR_FETCH=62;

    /**
     * get the Access control list protecting a presence entity.
     * direction: client to server
     * request method: PresencePublisher#getAccessControlList.
     * response data:
     * {@link javax.imps.IMPSAccessControlList}
     */
    int PR_GETAUST=65;

    /**
     * subscribe to presence notifications for specified entities
     * also sent by server to relay a subscription request.
     * direction: both
     * request method: PresenceWatcher#subscribePresenceInfo.
     * response data:  Return a
     * {@link javax.imps.IMPSStatus}
     */
    int PR_SUBSCRIBE=60;

    /**
     * unsubscribe to presence notifications for specified entities
     * direction: both
     * request method: PresenceWatcher#subscribePresenceInfo.
     */
    int PR_UNSUB=61;

    /**
     * publish or notify presence information
     * direction: both
     * request method: PresencePublisher#publishPresenceInfo.
     * request data: PresenceInformation[]
     */
    int PR_UPDATE=66;

    /**
     * Start or continue a search
     * direction: client to server
     * request methods:
     * {@link javax.imps.IMPSSearch#search(java.lang.String, java.lang.String, java.lang.String[], int)}
     * and
     * {@link javax.imps.IMPSSearch#continueSearch(javax.imps.IMPSSearchResult, int)}
     */
    int SEARCH=10;

    /**
     * Results of a search
     * direction: server to client
     * request data:
     * {@link javax.imps.IMPSSearchResult}
     * .
     */
    int SEARCH_RESULT=12;

    /**
     * Stop a search
     * direction: client to server
     * request methods:
     * {@link javax.imps.IMPSSearch#stopSearch(javax.imps.IMPSSearchResult)}
     * .
     */
    int SEARCH_STOP=11;

    /**
     * Get IMPSService information
     * direction: from client to server
     * request method:
     * {@link javax.imps.IMPSConnection#getServiceInformation()}
     * .
     * response data:
     * {@link javax.imps.IMPSService}
     */
    int SPINFO=3;

    /**
     * Get service features and capabilities
     * direction: from client to server
     * request method:
     * {@link javax.imps.IMPSConnection#discoverService()}
     * .
     * response data:
     * {@link javax.imps.IMPSServiceFeatures}
     */
    int SVC_DISCOVER=4;

    /**
     * Send desired features
     * direction: from client to server
     * request method:
     * {@link javax.imps.IMPSConnection#negotiateService(javax.imps.IMPSServiceFeatures)}
     * .
     * response data:
     * {@link javax.imps.IMPSServiceFeatures}
     */
    int SVC_NEGOTIATE=5;

    /**
     * get current watcher lists
     * direction: client to server
     * request method: PresencePublisher#fetchPresenceWatcherList.
     * response data:
     * {@link javax.imps.IMPSAddress}
     * []
     */
    int WL_FETCH=72;

    /**
     * subscribe to watcher list changes
     * direction: client to server
     * request method: PresencePublisher#subscribePresenceWatcherList.
     */
    int WL_SUBSCRIBE=70;

    /**
     * Unsubscribe to the watcher list changes.
     * direction: client to server
     * request method: PresencePublisher#unsubscribePresenceWatcherList
     */
    int WL_UNSUB=71;

    /**
     * returns the object embodying the content of this message.
     * It is the result of the parsing of the message content.
     * The type of the data depends on the request.
     */
    java.lang.Object getData();

    /**
     * Gets the request type.
     * IMPSRequest types are defined in
     * {@link javax.imps.IMPSRequest}
     * @return request type int value.
     */
    int getType();

}
