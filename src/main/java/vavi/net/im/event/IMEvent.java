/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.event;

import java.util.EventObject;

import vavi.net.im.Buddy;
import vavi.net.im.protocol.Protocol;


/**
 * IM のイベントです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class IMEvent extends EventObject {

    /** IM イベント名を表すインターフェースです。 */
    public interface Name {
    }

    /** IM イベントの基本 ID 名です。 */
    public enum IMEventName implements Name {
        /**
         * Invoked when a protocol attempts start up.
         */
        connecting,

        /**
         * Invoked when a protocol completes the start up successfully.
         */
        connected,

        /**
         * Invoked when a protocol is disconnected.
         */
        disconnected,

        /**
         * @arguments Group
         */
        groupAdded,

        /**
         * Invoked when a buddy was successfully added to your buddy list.
         * 
         * @arguments Buddy
         */
        buddyAdded,

        /**
         * Invoked when an attempt to add a buddy failed because the buddy
         * rejected your request.
         * 
         * @arguments Buddy, String
         */
        buddyAddRejected,

        /**
         * Invoked when a buddy was successfully deleted from your buddy list.
         * 
         * @arguments Buddy
         */
        buddyDeleted,

        /**
         * Invoked when a buddy was successfully ignored. Ignored buddies cannot
         * send messages to you.
         * <p>
         * Not all protocols support ignoring buddies. If a specific protocol
         * does not support ignoring, this method will not be invoked. To
         * identify whether this method is supported, use
         * {@link Protocol.Feature#IgnoreSupported} feature.
         * 
         * @arguments Buddy
         */
        buddyIgnored,

        /**
         * Invoked when a buddy was successfully unignored. Unignoring a
         * previously ignored buddy permits him / her to send messages to you.
         * <p>
         * Not all protocols support ignoring buddies. If a specific protocol
         * does not support ignoring, this method will not be invoked. To
         * identify whether this method is supported, use
         * {@link Protocol.Feature#IgnoreSupported} feature.
         * 
         * @arguments Buddy
         */
        buddyUnignored,

        /**
         * Invoked when your buddy list is received after a successful login. If
         * this method is not invoked, the listener can assume that the buddy
         * list is empty.
         * 
         * @arguments Buddy[] buddies
         */
        buddyListReceived,

        /**
         * Invoked when a list of buddies whom you have ignored is received,
         * after a successful login. If this method is not invoked, the listener
         * can assume that the ignore list is empty.
         * <p>
         * Not all protocols support ignoring buddies. If a specific protocol
         * does not support ignoring, this method will not be invoked. To
         * identify whether this method is supported, use
         * {@link Protocol.Feature#IgnoreSupported} feature.
         * 
         * @arguments Buddy[] buddies
         */
        ignoreListReceived,

        /**
         * Invoked when the status information of a buddy is changed. The new
         * status can be accessed using the {@link Buddy#getStatus()} method on
         * the buddy object.
         * 
         * @arguments Buddy buddy
         */
        buddyStatusChanged,

        /**
         * Invoked when an instant message is received.
         * 
         * @arguments Session session, Message message
         */
        instantMessageReceived,

        /**
         * Invoked when an offline message is received. Not all protocols
         * support offline messages. If a specific protocol does not support
         * offline messages, this method will not be invoked. To check whether a
         * protocol supports offline messages or not, use the
         * {@link Protocol.Feature#OfflineMessageSupported} method.
         * 
         * @arguments Buddy buddy, Date date, Message message
         */
        offlineMessageReceived,

        /**
         * Invoked when the underlying protocol system sends a notification to
         * the user.
         * 
         * @arguments Message message
         */
        protocolMessageReceived,

        /**
         * Invoked when a buddy starts typing. Not all protocols support typing
         * notifications. If a specific protocol does not support typing
         * notifications, this method will not be invoked. To check whether a
         * protocol supports typing notifications or not, use
         * {@link Protocol.Feature#TypingNotifySupported} feature.
         * 
         * @arguments Buddy
         */
        typingStarted,

        /**
         * Invoked when a buddy stops typing. Not all protocols support typing
         * notifications. If a specific protocol does not support typing
         * notifications, this method will not be invoked. To check whether a
         * protocol supports typing notifications or not, use
         * {@link Protocol.Feature#TypingNotifySupported} feature.
         * 
         * @arguments Buddy
         */
        typingStopped,

        /**
         * Invoked when a buddy invites you for a conference. The listener
         * method overriding this method must return a <code>Response</code>
         * object which describes whether the request was allowed or disallowed,
         * and if it was disallowed, an explanation for that.
         * 
         * <p>
         * Not all protocols support conferences. If a specific protocol does
         * not support conferences, this method will not be invoked. To check
         * whether a protocol supports conferences or not, use
         * {@link Protocol.Feature#ConferenceSupported} method. // (Session
         * session, String message
         */
        conferenceInvitationReceived,

        /**
         * Invoked when a buddy declines a conference invitation. Not all
         * protocols support conferences. If a specific protocol does not
         * support conferences, this method will not be invoked. To check
         * whether a protocol supports conferences or not, use
         * {@link Protocol.Feature#ConferenceSupported} feature.
         * 
         * @arguments Session session, Buddy buddy, String message
         */
        conferenceInvitationDeclined,

        /**
         * Invoked when a buddy accepts a conference invitation. This indicates
         * that this buddy has joined the conference.
         * 
         * <p>
         * Not all protocols support conferences. If a specific protocol does
         * not support conferences, this method will not be invoked. To check
         * whether a protocol supports conferences or not, use
         * {@link Protocol.Feature#ConferenceSupported} feature.
         * 
         * @arguments Session session, Buddy buddy
         */
        conferenceInvitationAccepted,

        /**
         * Invoked when an instant message arrives at a conference. Not all
         * protocols support conferences. If a specific protocol does not
         * support conferences, this method will not be invoked. To check
         * whether a protocol supports conferences or not, use
         * {@link Protocol.Feature#ConferenceSupported} feature.
         * 
         * @arguments Session session, Buddy buddy, Message message
         */
        conferenceMessageReceived,

        /**
         * Invoked when a buddy joins a conference. Not all protocols support
         * conferences. If a specific protocol does not support conferences,
         * this method will not be invoked. To check whether a protocol supports
         * conferences or not, use {@link Protocol.Feature#ConferenceSupported}
         * feature.
         * 
         * @arguments Session, Buddy
         */
        conferenceParticipantJoined,

        /**
         * Invoked when a buddy leaves from a conference. Not all protocols
         * support conferences. If a specific protocol does not support
         * conferences, this method will not be invoked. To check whether a
         * protocol supports conferences or not, use
         * {@link Protocol.Feature#ConferenceSupported} feature.
         * 
         * @aruemnts Session, Buddy
         */
        conferenceParticipantLeft,

        /**
         * Invoked when a conference session is closed, either by the protocol
         * or by a buddy who is authorized to do so. Who all can close a
         * conference session is protocol dependant.
         * 
         * @arguments Session session
         */
        conferenceClosed,

        /**
         * Invoked when an e-mail notification arrives. Many protcols have
         * associated e-mail accounts. When an e-mail arrives in that account, a
         * notification is sent. If a specific protocol does not support e-mail
         * notifications, this method will not be invoked. To check wheter a
         * protocol supports e-mail notifications, use
         * {@link Protocol.Feature#MailNotifySupported} feature.
         * 
         * <p>
         * The parameter <code>count</code> specifies the number of mails. If
         * this is -1, it means that the number of mails is not known. The
         * parameter <code>from</code> is a list of e-mail addresseswho are
         * the senders of each e-mail message, in order. The parameter
         * <code>subject</code> is the subject line of each e-mail message, in
         * order. Both these parameters can be null, indicating that the from
         * addresses or subject lines are not known.
         * 
         * @arguments int count, String[] froms, String[] subjects
         */
        mailNotificationReceived,

        /**
         * @arguments String message, boolean result
         * @see IMAdapter#doConfirmation(String)
         */
        doConfirmation,

        /**
         * for session (invisible at IMAdapter)
         * 
         * @arguments Session
         * @see "Protocol#defaultImListener"
         */
        quitSession,

        /**
         * for session (invisible at IMAdapter)
         * 
         * @arguments Session, Message
         * @see "Protocol#defaultImListener"
         */
        sendSessionMessage,

        /**
         * for session (invisible at IMAdapter)
         * 
         * @arguments Session, Buddy[]
         * @see "Protocol#defaultImListener"
         */
        addParticipant,

        /**
         * for session (invisible at IMAdapter)
         * 
         * @arguments Session, Message
         * @see "Protocol#defaultImListener"
         */
        sendInstantMessage
    }

    /** イベントの ID 名 */
    private Name name;

    /** イベントの引数 */
    private Object[] arguments;

    /**
     * Creates a IM event.
     * 
     * @param source the event source
     * @param name the event name
     * @param arguments the event argument
     */
    public IMEvent(Object source, Name name, Object... arguments) {
        super(source);

        this.name = name;
        this.arguments = arguments;
    }

    /** */
    public Name getName() {
        return name;
    }

    /** */
    public Object[] getArguments() {
        return arguments;
    }
}

/* */
