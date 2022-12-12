package javax.imps;
/**
 * IMPSRequest other users to participate in an instant messaging and presence activity
 * such as joining a group, subscribing to presence information, or accessing content
 * via a URL.
 * <p>
 * The recipient of an invitation can accept, reject, or ignore the invitation.
 * An invitation shows intent to share an activity with other users, but is not sufficient
 * by itself to provide the shared resources.  Similarly, an acceptance of an invitation
 * shows intent to participate in a shared activity or resource, but doesn't by itself perform the
 * access to the activity or resource.
 * </p>
 * It is the responsibility of the inviting application to establish that the resources
 * referred to in the invitation are available.
 * 
 * 
 * 
 * <pre>
 * // Shows how to send and cancel an invite request.
 * String type = "SharedContent";
 * String id = "SC";
 * String reason = "Share some interesting content";
 * IMPSStatus status = null;
 * Extensible inviteResources;
 * IMPSInvitation invitation;
 * IMPSAddress[] recipients;
 * IMPSAddress ownScreenName;
 * 
 * try {
 *   invitation = factory.createIMPSInvitation(connection);
 *   inviteResources = factory.createExtensible();
 *   recipients[0] = factory.createIMPSAddress("wv:member/movie-group@example.com",
 *   IMPSAddress.ADDRESS_USER);
 *   ownScreenName = factory.createIMPSAddress("wv:john.smith/friends@example.com",
 *   IMPSAddress.ADDRESS_GROUP);
 * } catch ( IMPSException e) {
 *   e.printStackTrace();
 * }
 * 
 * // set up the URLs for content
 * inviteResources.setText("URI","bob@somedomain.com");
 * inviteResources.setText("URI","alice@somedomain.com");
 * inviteResources.setText("URI","zzz@somedomain.com");
 * 
 * // tell recipients about the shared content for 400 seconds
 * IMPSTid tid = invitation.invite(type, id, recipients, inviteResources,
 *   400, reason, ownScreenName);
 * // joinTransaction is application method defined in
 * // section
 * status = joinTransaction(tid);
 * if(status.getResultCode == STATUS.OK){
 *   // invitation was successful
 *   // now let's cancel the invitation
 *   String cancelReason = "Content can not be shared anymore";
 *   IMPSTid tid = invitation.cancelInvitation(id, recipients, cancelReason,
 *   inviteResources, ownScreenName);
 *   // joinTransaction is application method defined in
 *   // section
 *   status = joinTransaction(tid);
 *   if(status.getResultCode() == STATUS.OK) {
 *     // cancellation was successful
 *   }
 * }
 * </pre>
 * 
 */
public interface IMPSInvitation {
    /**
     * invitation to access content at a URI
     */
    int INVITATION_CONTENT=3;

    /**
     * invitation to participate in a group chat
     */
    int INVITATION_GROUP=0;

    /**
     * invitation to participate in instant message exchange
     */
    int INVITATION_IM=1;

    /**
     * invitation to join a group
     */
    int INVITATION_MEMBERSHIP=4;

    /**
     * invitation to subscribe to presence
     */
    int INVITATION_PRESENCE=2;

    /**
     * Cancels a previously sent invitation request, using the invitation id as the reference.
     * @param id the ID of invitation.
     * @param recipients the list of users to receive the cancellation
     *  including (1) {@link javax.imps.IMPSAddress#ADDRESS_USER}, (2) {@link javax.imps.IMPSAddress#ADDRESS_CONTACTLIST},
     *  (3) {@link javax.imps.IMPSAddress#ADDRESS_SCREENNAME}, or (4) {@link javax.imps.IMPSAddress#ADDRESS_GROUP}.
     * @param reason the reason for the cancellation
     * @param inviteResources the list of related presence attributes and URL List. Content type is {@link javax.imps.Extensible}.
     * @param ownScreenName user's screen name in a group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_SCREENNAME})
     * @return an instance of IMPSTid (transaction id) that identifies this request.
     */
    javax.imps.IMPSTid cancelInvitation(java.lang.String id, javax.imps.IMPSAddress[] recipients, java.lang.String reason, javax.imps.Extensible inviteResources, javax.imps.IMPSAddress ownScreenName);

    /**
     * Get the acceptance value of the reply, where true means that
     * the invitation was accepted.
     * @return the acceptance value of the reply
     */
    boolean getAcceptance();

    /**
     * Get the reason for the cancellation
     * @return the reason for the cancellation
     */
    java.lang.String getCancelReason();

    /**
     * Get the id of the reply
     * @return the id of the reply
     */
    java.lang.String getId();

    /**
     * Get the reason for the invitation
     * @return the reason for the invitation
     */
    java.lang.String getInvitationReason();

    /**
     * Get the note associated with the reply
     * @return the note associated with the reply
     */
    java.lang.String getReplyNote();

    /**
     * Get the resources associated with the invitation or cancellation
     * @return the resources associated with the invitation or cancellation
     */
    javax.imps.Extensible getResources();

    /**
     * Get the screen name of the replier
     * @return the screen name of the replier
     */
    javax.imps.IMPSAddress getScreenName();

    /**
     * Get the sender of the reply
     * @return the address of the sender of the reply
     */
    javax.imps.IMPSAddress getSender();

    /**
     * Get the type of the invitation
     * @return the type of the invitation
     */
    int getType();

    /**
     * Get the validity period of the invitation, in seconds, from the time the invitation was sent.
     * @return the validity period of the invitation
     */
    int getValidityPeriod();

    /**
     * Sends an invitation request to invite others to join a group,
     * or to request group membership or to exchange messages or to share presence
     * attributes.
     * @param type indicates the type of invitation.
     * @param id indicates the ID of the invitation.
     * @param recipients indicates the list of users to be invited, including (1) {@link javax.imps.IMPSAddress#ADDRESS_USER},
     *  (2) {@link javax.imps.IMPSAddress#ADDRESS_CONTACTLIST}, (3) {@link javax.imps.IMPSAddress#ADDRESS_SCREENNAME}, or (4) {@link javax.imps.IMPSAddress#ADDRESS_GROUP}
     * @param inviteResources indicates the list of related presence attribute or related group IDs, or content URLs. Content
     *  type is {@link javax.imps.Extensible}. The data structure of inviteResources can contain any of the
     *  following information:
     *  <P> group: IMPSAddress
     *  </P><P> presenceAttribute: PresenceInformation
     *  </P><P> urlList: String[] - The URL address for content</P>
     * @param validityPeriod indicates the period of validity of the invitation, in seconds
     * @param reason explains the reason for the invitation
     * @param ownScreenName gives own screen name in a group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_SCREENNAME})
     * @return an instance of IMPSTid (transaction id) that identifies this request
     */
    javax.imps.IMPSTid invite(int type, java.lang.String id, javax.imps.IMPSAddress[] recipients, javax.imps.Extensible inviteResources, int validityPeriod, java.lang.String reason, javax.imps.IMPSAddress ownScreenName);

    /**
     * Reply to the invitation request received from another user.
     * The user can accept or reject the invitation by this method call.
     * @param id he ID of the invitation.
     * @param sender the address of the inviting user ({@link javax.imps.IMPSAddress#ADDRESS_USER}
     *  or {@link javax.imps.IMPSAddress#ADDRESS_GROUP})
     * @param acceptance true to accept or false to reject the invitation
     * @param note the reason for the acceptance or rejection
     * @param ownScreenName user's screen name in a group (IMPSAddress type {@link javax.imps.IMPSAddress#ADDRESS_SCREENNAME})
     * @return instance of IMPSTid (transaction id) that identifies this request.
     */
    javax.imps.IMPSTid replyInvitation(java.lang.String id, javax.imps.IMPSAddress sender, boolean acceptance, java.lang.String note, javax.imps.IMPSAddress ownScreenName);

}
