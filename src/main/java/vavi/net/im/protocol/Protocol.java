/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import vavi.net.im.Buddy;
import vavi.net.im.Group;
import vavi.net.im.Message;
import vavi.net.im.Session;
import vavi.net.im.SmileyComponent;
import vavi.net.im.event.IMEvent;
import vavi.net.im.event.IMListener;
import vavi.net.im.event.IMSupport;
import vavi.net.im.event.IMEvent.IMEventName;
import vavi.net.im.event.IMEvent.Name;
import vavi.util.Debug;


/**
 * A protocol is a specific implementation of instant messaging services.
 * Popular examples of instant messaging services are MSN, Yahoo!, NIM etc.
 * 
 * <p>
 * Hamsam provides an implementation independent way of dealing with protocols
 * through this interface. Each of the protocols implements the methods in this
 * interface, so that a user of Hamsam library can use these methods to talk to
 * any protocol available.
 * 
 * <p>
 * This also makes Hamsam a plug-and-play library. The <code>Protocol</code>
 * interface along with the <code>ProtocolManager</code> class, helps to
 * easily add new protocols, without any change to the source code. This means
 * that, if you have an application coded properly, you don't need to change
 * that code, as and when new versions of Hamsam library (with new protocol
 * additions) are released.
 * 
 * @author Raghu
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public abstract class Protocol {

    /** */
    protected Protocol() {
        listeners.addIMListener(defaultImListener);
    }

    /** */
    protected Map<String, Object> statusMap = new HashMap<>();

    /**
     * Change the status message for the user logged in for this protocol. If
     * <code>status</code> is <code>null</code>, the status is changed to
     * invisible.
     * <p>
     * A protocol may support only a limited range of status messages. If you
     * pass a status message which is not supported by the underlying protocol,
     * it will throw an IllegalArgumentException. In order to get a complete
     * list of supported status messages, use the getSupportedStatusMessages()
     * method.
     * 
     * @param status the new status message.
     * @see Feature#InvisibleSupported
     * @see #getSupportedStatusMessages()
     */
    protected abstract void changeStatusInternal(String status) throws IOException;

    /**
     * @param status
     */
    public void changeStatus(String status) throws IOException {
        self.setStatus(status);
        changeStatusInternal(status);
    }

    /**
     * @param localStatus local status
     * @return string status
     */
    protected String normalizeStatus(Object localStatus) {
        for (String key : statusMap.keySet()) {
            if (statusMap.get(key).equals(localStatus)) {
                return key;
            }
        }
        return self.getStatus();
    }

    /** */
    protected abstract Object localizeStatusInternal(String status);

    /**
     * @param status local status
     * @return string status
     */
    protected Object localizeStatus(String status) {
        Object localStatus = statusMap.get(status);
        if (localStatus == null) {
            return localizeStatusInternal(status);
        }
        return localStatus;
    }

    /**
     * Connect to the instant messaging server. This is equivalent to the
     * logging in to the service.
     * 
     * @param props
     */
    protected abstract void connectInternal(Properties props) throws IOException;

    /** */
    public void connect(Properties props) throws IOException {
        listeners.eventHappened(new IMEvent(this, IMEventName.connecting));
        connectInternal(props);
        listeners.eventHappened(new IMEvent(this, IMEventName.connected));
    }

    /**
     * Disconnect from the instant messaging service. This is equivalent to
     * logging out from the service.
     * 
     * @throws IllegalStateException if the protocol is not connected.
     */
    protected abstract void disconnectInternal() throws IOException;

    /** */
    public void disconnect() throws IOException {
        disconnectInternal();
        listeners.eventHappened(new IMEvent(this, IMEventName.disconnected));
    }

    /**
     * Returns the name of this protocol. This method is used to distinguish
     * each of the protocols supported by the API. No two protocols will have
     * the same name. The name is a symbolic one like Yahoo or MSN that may be
     * displayed to the end-user to identify this protocol.
     * 
     * @return the identifying name of this protocol.
     */
    public abstract String getProtocolName();

    /** */
    public enum Feature {
        /**
         * Determines whether this protocol will notify you if a user attempts
         * to add you to his / her buddy list.
         */
        BuddyAddRequestSupported,

        /**
         * Determines whether this protocol supports buddy name aliases. Some
         * protocols supports a nickname (friendly name) for buddies.
         */
        BuddyNameAliasSupported,

        /**
         * Determines whether this protocol supports arranging buddies in
         * different groups.
         */
        BuddyGroupSupported,

        /**
         * Determines whether this protocol supports ignoring buddies. Ignored
         * buddies can not send messages to the person who ignored them.
         */
        IgnoreSupported,

        /**
         * Determines whether this protocol supports sending and receiving
         * offline messages. Offline messages are messages which are sent when
         * the recipient is not connected to the instant messaging system.
         * These messages are stored by the instant messaging server and later
         * delivered to the recipient when he / she connects to the system.
         */
        OfflineMessageSupported,

        /**
         * Determines whether this protocol supports typing notifications. In an
         * instant messaging session, some protocols inform one user that the
         * other person starts or stops typing.
         */
        TypingNotifySupported,

        /**
         * Determines whether this protocol supports conferences.
         * 
         * @see vavi.net.im.Session Session
         */
        ConferenceSupported,

        /**
         * Determines whether this protocol supports e-mail alerts. Many
         * protocols have associated e-mail accounts. When an e-mail arrives in
         * that account, a notification is sent.
         */
        MailNotifySupported,

        /**
         * Determines whether this protocol supports setting the status of the
         * user to invisible. Invisible users appear as offline to other users
         * although they can send and receive messages and participate in any
         * other operations.
         */
        InvisibleSupported,
    }

    /** */
    protected Map<Feature, Boolean> features = new HashMap<>();

    /** */
    public boolean getFeature(Feature feature) {
        return features.get(feature);
    }

    /** */
    public void setFeature(Feature feature, boolean enabled) {
        features.put(feature, enabled);
    }

    /** <String(Buddy|Session#name), Session> */
    private Map<Object, Session> sessions = new HashMap<>();
    /** */
    protected Buddy self;
    /** グループ */
    protected List<Group> groups = new ArrayList<>();
    /** グループ */
    protected Group defaultGroup = new Group("<default>");
    /** 禁止リスト */
    protected Group ignored = new Group("<ignored>");

    /** */
    public Buddy getUser() {
        return self;
    }

    /** */
    public Group getDefaultGroup() {
        return defaultGroup;
    }

    /** */
    public Iterable<Buddy> getIgnoredBuddies() {
        return ignored;
    }

    /** */
    public List<Group> getGroups() {
        return groups;
    }

    /** @return null when not found */
    protected Buddy findBuddy(String name) {
        for (Buddy buddy : defaultGroup) {
            if (buddy.getUsername().equals(name)) {
                return buddy;
            }
        }
        for (Group group : groups) {
            for (Buddy buddy : group) {
                if (buddy.getUsername().equals(name)) {
                    return buddy;
                }
            }
        }
Debug.println("buddy not found: " + name);
        return null;
    }

    /** @return new buddy when not found */
    protected Buddy findBuddyForce(String name) {
        Buddy buddy = findBuddy(name);
        if (buddy == null) {
            buddy = new Buddy(name);
        }
        return buddy;
    }

    /** @return null when not found */
    protected Group findGroup(Buddy buddy) {
        if (defaultGroup.contains(buddy)) {
            return defaultGroup;
        }
        for (Group group : groups) {
            if (group.contains(buddy)) {
                return group;
            }
        }
Debug.println("group not found: " + buddy.getUsername());
        return null;
    }

    /** @return null when not found */
    protected Session findSession(Buddy buddy) {
        for (Session session : sessions.values()) {
            if (!session.isGroupSession() && session.getParticipants()[0].equals(buddy)) {
                return session;
            }
        }
Debug.println("session not found: " + buddy.getUsername());
        return null;
    }

    /** creates new session when not found */
    protected Session findSessionForce(Buddy buddy) {
        Session session = findSession(buddy);
        if (session == null) {
            session = new Session(listeners, self, buddy);

            sessions.put(buddy, session);
        }
        return session;
    }

    /**
     * creates new group session when not found.
     */
    protected Session findSessionForce(String sessionName, Buddy buddy) {
        Session session = sessions.get(sessionName);
        if (session == null) {
            session = new Session(listeners, self, new Buddy[] { buddy });
            session.setName(sessionName);

            sessions.put(sessionName, session);
        }
        return session;
    }

    /** すでに Protocol#defaultListener が追加されています。 */
    protected IMSupport listeners = new IMSupport();

    /**
     * IMListener を追加します．
     */
    public void addIMListener(IMListener listener) {
        listeners.addIMListener(listener);
    }

    /** IMListener を削除します． */
    public void removeIMListener(IMListener listener) {
        listeners.removeIMListener(listener);
    }

    /**
     * Start a new session
     * 
     * @param session the conference which is to be started.
     */
    protected abstract void startSessionInternal(Session session) throws IOException;

    /** */
    public Session startSession(Buddy buddy) throws IOException {
        Session session = findSession(buddy);
        if (session == null) {
            session = new Session(listeners, self, buddy);
            startSessionInternal(session);

            sessions.put(buddy, session);
        }
        return session;
    }

    /** */
    protected abstract void startSessionInternal(Session session, String message) throws IOException;

    /**
     * @param message invitation message
     */
    public Session startSession(String sessionName, Buddy[] buddies, String message) throws IOException {
        Session session = sessions.get(sessionName);
        if (session == null) {
            session = new Session(listeners, self, buddies, message);
            session.setName(sessionName);

            startSessionInternal(session, message);
            sessions.put(sessionName, session);
        }
        return session;
    }

    /**
     * Disconnect the current user from a conference.
     * 
     * @param session the conference from which the user has to disconnect.
     */
    protected abstract void quitSessionInternal(Session session) throws IOException;

    /**
     * Send a conference message.
     * 
     * @param session the conference to which the message is to be sent.
     * @param message the instant message to be sent.
     */
    protected abstract void sendSessionMessageInternal(Session session, Message message) throws IOException;

    /**
     * Add a buddy to your buddy list. The result of this operation will be
     * notified to the registered <code>IMListener</code>.
     * 
     * @param buddy the buddy to be added.
     */
    protected abstract void addToBuddyListInternal(Buddy buddy) throws IOException;

    /**
     * 
     */
    public void addToBuddyList(Buddy buddy) throws IOException {
        defaultGroup.addBuddy(buddy);
        addToBuddyListInternal(buddy);
    }

    /**
     * Delete a buddy from your buddy list. The result of this operation will be
     * notified to the registered <code>IMEventName</code>.
     * 
     * @param buddy the buddy to be deleted.
     */
    protected abstract void deleteFromBuddyListInternal(Buddy buddy) throws IOException;

    /**
     * 
     */
    public void deleteFromBuddyList(Buddy buddy) throws IOException {
        defaultGroup.removeBuddy(buddy);
        deleteFromBuddyListInternal(buddy);
    }

    /**
     * Prevent a buddy from sending you messages. The result of this operation
     * will be notified to the registered <code>IMEventName</code>.
     * 
     * @param buddy the buddy to be ignored.
     */
    protected abstract void ignoreBuddyInternal(Buddy buddy) throws IOException;

    /**
     * 
     */
    public void ignoreBuddy(Buddy buddy) throws IOException {
        ignored.addBuddy(buddy);
        ignoreBuddyInternal(buddy);
    }

    /**
     * Undo a previous ignore operation for a buddy. The result of this
     * operation will be notified to the registered <code>IMEventName</code>.
     * 
     * @param buddy the buddy to be ignored.
     */
    protected abstract void unignoreBuddyInternal(Buddy buddy) throws IOException;

    /**
     * 
     */
    public void unignoreBuddy(Buddy buddy) throws IOException {
        ignored.removeBuddy(buddy);
        unignoreBuddyInternal(buddy);
    }

    /**
     * Send an instant message to a buddy.
     * 
     * @param session the buddy to whom the message should be sent.
     * @param message the message to be sent.
     */
    protected abstract void sendInstantMessageInternal(Session session, Message message) throws IOException;

    /**
     * タイピングを開始する場合に使用します。
     */
    public void startTyping() throws IOException {
        for (Session session : sessions.values()) {
            for (Buddy buddy : session.getParticipants()) {
                startTypingInternal(buddy);
            }
        }
    }

    /**
     * Notify this buddy that you have started typing.
     */
    protected abstract void startTypingInternal(Buddy buddy) throws IOException;

    /**
     * タイピングを終了する場合に使用します。
     */
    public void stopTyping() throws IOException {
        for (Session session : sessions.values()) {
            for (Buddy buddy : session.getParticipants()) {
                stopTypingInternal(buddy);
            }
        }
    }

    /**
     * Notify this buddy that you have stopped typing.
     */
    protected abstract void stopTypingInternal(Buddy buddy) throws IOException;

    /**
     * Returns an array of all SmileyComponents supported by this protocol.
     * 
     * @return an array of all SmileyComponents supported by this protocol.
     */
    public abstract SmileyComponent[] getSupportedSmileys();

    /**
     * Returns an array of status messages supported by this protocol. Some
     * protocols may support any status message, in that case a
     * <code>null</code> is returned.
     * 
     * @return array of status messages supported by this protocol, or
     *         <code>null</code> if this protocol supports any status message.
     */
    /** */
    public String[] getSupportedStatusMessages() {
        return statusMap.keySet().toArray(new String[0]);
    }

    /**
     * Changes the alias of a buddy.
     * 
     * @param buddy the buddy whose alias needs to be changed.
     * @param alias the new alias of this buddy.
     * @see Feature#BuddyNameAliasSupported
     */
    protected abstract void changeBuddyAliasInternal(Buddy buddy, String alias) throws IOException;

    /** */
    public void changeBuddyAlias(Buddy buddy, String alias) throws IOException {
        changeBuddyAlias(buddy, alias);
        buddy.setAlias(alias);
    }

    /** */
    protected abstract void addGroupInternal(Group group) throws IOException;

    /** */
    public void addGroup(Group group) throws IOException {
        addGroupInternal(group);
        groups.add(group);
    }

    /** */
    protected abstract void removeGroupInternal(Group group) throws IOException;

    /** */
    public void removeGroup(Group group) throws IOException {
        removeGroupInternal(group);
        groups.remove(group);
    }

    /** */
    protected abstract void changeGroupNameInternal(Group group, String newName) throws IOException;

    /** */
    public void changeGroupName(Group group, String newName) throws IOException {
        changeGroupNameInternal(group, newName);
        group.setName(newName);
    }

    /** */
    protected abstract void changeGroupOfBuddyInternal(Buddy buddy, Group oldGroup, Group newGroup) throws IOException;

    /** */
    public void changeGroupOfBuddy(Buddy buddy, Group oldGroup, Group newGroup) throws IOException {
        changeGroupOfBuddyInternal(buddy, oldGroup, newGroup);
        oldGroup.removeBuddy(buddy);
        newGroup.addBuddy(buddy);
    }

    //---

    /** for Session */
    private IMListener defaultImListener = event -> {
        Name name = event.getName();
        if (name instanceof IMEventName) {
            IMEventName eventName = (IMEventName) name;
            switch (eventName) {
            case quitSession: {
                Session session = (Session) event.getArguments()[0];
                quitSessionInternal(session);
            }
                break;
            case sendSessionMessage: {
                Session session = (Session) event.getArguments()[0];
                Message message = (Message) event.getArguments()[1];
                sendSessionMessageInternal(session, message);
            }
                break;
            case sendInstantMessage: {
//Debug.println("here 2");
                Session session = (Session) event.getArguments()[0];
                Message message = (Message) event.getArguments()[1];
                sendInstantMessageInternal(session, message);
            }
                break;
            case addParticipant: {
                Session session = (Session) event.getArguments()[0];
                Buddy[] buddies = (Buddy[]) event.getArguments()[1];
                // TODO
            }
                break;
            }
        }
    };
}

/* */
