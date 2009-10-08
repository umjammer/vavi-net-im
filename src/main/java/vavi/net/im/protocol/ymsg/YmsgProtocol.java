/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg;

import java.io.IOException;
import java.util.Date;
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
import vavi.net.im.protocol.Protocol;
import vavi.net.im.protocol.ymsg.YmsgPacketHeader.Status;
import vavi.util.Debug;


/**
 * YmsgProtocol.
 *
 * <specification>
 * メンバのいないグループは存在しない
 * 正確にはセッションの概念はない
 * </specification>
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040221 nsano initial version <br>
 */
public class YmsgProtocol extends Protocol {

    /** */
    public static final String mailUrl = "http://mail.yahoo.co.jp";

    /** */
    public YmsgProtocol() {

        statusMap.put("I'm available", Status.AVAILABLE);
        statusMap.put("Be Right Back", Status.BRB);
        statusMap.put("Busy", Status.BUSY);
        statusMap.put("Not At Home", Status.NOTATHOME);
        statusMap.put("Not At My Desk", Status.NOTATDESK);
        statusMap.put("Not In The Office", Status.NOTINOFFICE);
        statusMap.put("On The Phone", Status.ONPHONE);
        statusMap.put("On Vacation", Status.ONVACATION);
        statusMap.put("Out To Lunch", Status.OUTTOLUNCH);
        statusMap.put("Stepped Out", Status.STEPPEDOUT);
        statusMap.put("Offline", Status.OFFLINE);

        features.put(Feature.BuddyAddRequestSupported, true);
        features.put(Feature.BuddyNameAliasSupported, false);
        features.put(Feature.BuddyGroupSupported, true);
        features.put(Feature.IgnoreSupported, true);
        features.put(Feature.OfflineMessageSupported, true);
        features.put(Feature.TypingNotifySupported, true);
        features.put(Feature.ConferenceSupported, true);
        features.put(Feature.MailNotifySupported, true);
        features.put(Feature.InvisibleSupported, true);

        //
        this.listeners = new HookedIMSupport(super.listeners);
    }

    /** */
    private YmsgConnection connection;

    /** */
    private String[] toStringArray(Buddy[] buddies) {
        String[] buddyNames = new String[buddies.length];
        for (int i = 0; i < buddies.length; i++) {
            buddyNames[i] = buddies[i].getUsername();
        }
        return buddyNames;
    }

    //----

    /**
     * @param status
     */
    protected void changeStatusInternal(String status) throws IOException {
        connection.setStatus((Status) localizeStatus(status));
    }

    /** */
    protected Object localizeStatusInternal(String status) {
        return Status.CUSTOM;
    }

    /**
     * @param props "username", "password"
     */
    protected void connectInternal(Properties props) throws IOException {

        String username = props.getProperty("username");
        String password = props.getProperty("password");
        this.connection = new YmsgConnection(username, password, listeners);

        this.self = new Buddy(username);
    	self.setStatus(normalizeStatus(Status.OFFLINE));
Debug.println("status: " + self.getStatus());

        connection.requestAuth();
    }

    /** */
    protected void disconnectInternal() throws IOException {
        connection.close();
    }

    /** */
    public String getProtocolName() {
        return "YMSG";
    }

    /** */
    protected void startSessionInternal(Session session) throws IOException {
    }

    /** rb.getString("CONF_INVITEMSG") */
    protected void startSessionInternal(Session session, String message) throws IOException {
        connection.inviteBuddiesToGroupSession(session.getName(), toStringArray(session.getParticipants()), message);
    }

    /**
     */
    protected void quitSessionInternal(Session session) throws IOException {
        if (session.isGroupSession()) {
            connection.closeGroupSession(session.getName(), toStringArray(session.getParticipants()));
        }
    }

    /**
     * @param message invitation
     */
    protected void sendSessionMessageInternal(Session session, Message message) throws IOException {
        connection.sendMessageToGroupSession(message.toString(), session.getName(), toStringArray(session.getParticipants()));
    }

    /**
     * 
     */
    protected void addToBuddyListInternal(Buddy buddy) throws IOException {
        connection.addBuddyIntoGroup(buddy.getUsername(), findGroup(buddy).getName());
    }

    /**
     * 
     */
    protected void deleteFromBuddyListInternal(Buddy buddy) throws IOException {
        connection.removeBuddyFromList(buddy.getUsername(), defaultGroup.getName());
    }

    /**
     * 
     */
    protected void ignoreBuddyInternal(Buddy buddy) throws IOException {
        connection.ignoreBuddy(buddy.getUsername());
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        connection.removeBuddyFromList(buddy.getUsername(), findGroup(buddy).getName());
    }

    /**
     * 
     */
    protected void unignoreBuddyInternal(Buddy buddy) throws IOException {
        connection.unignoreBuddy(buddy.getUsername());
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        connection.addBuddyIntoList(buddy.getUsername(), defaultGroup.getName());
    }

    /**
     * 
     */
    protected void sendInstantMessageInternal(Session session, Message message) throws IOException {
Debug.println("here 3");
        connection.sendMessage(message.toString(), session.getParticipants()[0].getUsername());
    }

    /**
     * Notify this buddy that you have started typing.
     */
    protected void startTypingInternal(Buddy buddy) throws IOException {
        connection.beginTyping(buddy.getUsername());
    }

    /**
     * Notify this buddy that you have stopped typing.
     */
    protected void stopTypingInternal(Buddy buddy) throws IOException {
        connection.endTyping(buddy.getUsername());
    }

    /** */
    public SmileyComponent[] getSupportedSmileys() {
        return null;
    }

    /** */
    protected void changeBuddyAliasInternal(Buddy buddy, String alias) throws IOException {
        // TODO
    }

    /** */
    protected void addGroupInternal(Group group) throws IOException {
        // TODO
    }

    /** */
    protected void removeGroupInternal(Group group) throws IOException {
        // TODO
    }

    /** */
    protected void changeGroupNameInternal(Group group, String newName) throws IOException {
        connection.changeGroupName(group.getName(), newName);
    }

    /** */
    protected void changeGroupOfBuddyInternal(Buddy buddy, Group oldGroup, Group newGroup) throws IOException {
        connection.addBuddyIntoGroup(buddy.getUsername(), newGroup.getName());
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        connection.removeBuddyFromList(buddy.getUsername(), oldGroup.getName());
    }

    //----

    /** あんましきれくない... */
    private class HookedIMSupport extends IMSupport {
        IMSupport backupListeners;
        HookedIMSupport(IMSupport backupListeners) {
            this.backupListeners = backupListeners;
        }
        public void addIMListener(IMListener l) {
            backupListeners.addIMListener(l);
        }
        public void removeIMListener(IMListener l) {
            backupListeners.removeIMListener(l);
        }
        /**
         * ここでは YmsgConnection のデータ型から IM library のデータ型への変換のみを行うこと。
         * 実際の処理は YmsgConnection#defaultIMListener#eventHappened(IMEvent) で行う。
         */
        public void eventHappened(IMEvent event) throws IOException {
            Name name = event.getName();

            if (name instanceof YmsgEventName) {
                YmsgEventName eventName = (YmsgEventName) name;

                // 変換
                event = eventName.toIMEvent(event);

                // 上で変換しきれないやつ
                switch (eventName) {
                case messageReceived: {
                    String text = (String) event.getArguments()[0];
                    String buddyName = (String) event.getArguments()[1];
                    Date date = (Date) event.getArguments()[2];
                    Buddy buddy = findBuddyForce(buddyName);
                    Session session = findSessionForce(buddy);
                    Message message = new Message(text);
                    message.setDate(date);
                    event = new IMEvent(event.getSource(), IMEventName.instantMessageReceived, session, message);
                } break;
                case confMessageReceived: {
                    String buddyName = (String) event.getArguments()[0];
                    String sessionName = (String) event.getArguments()[1];
                    String text = (String) event.getArguments()[2];
                    Buddy buddy = findBuddyForce(buddyName);
                    Session session = findSessionForce(sessionName, buddy);
                    Message message = new Message(text);
                    event = new IMEvent(event.getSource(), IMEventName.conferenceMessageReceived, session, buddy, message);
                } break;
                case statusChanged: {
                    String buddyName = (String) event.getArguments()[0];
                    Status status = (Status) event.getArguments()[1];
                    Buddy buddy = new Buddy(buddyName);
                    String statusString = normalizeStatus(status);
                    event = new IMEvent(event.getSource(), IMEventName.buddyStatusChanged, buddy, statusString);
                } break;
                }
            } else {
                IMEventName eventName = (IMEventName) name;

                switch (eventName) {
                case protocolMessageReceived: {
                    String text = (String) event.getArguments()[0];
                    Message message = new Message(text);
                    event = new IMEvent(event.getSource(), IMEventName.protocolMessageReceived, message);
                } break;
                }
            }

            backupListeners.eventHappened(event);
        }
    };

    //----
    
    /** */
    public static void main(String[] args) throws Exception {
        YmsgProtocol yp = new YmsgProtocol();
        Properties props = new Properties();
        props.setProperty("username", args[0]);
        props.setProperty("password", args[1]);
        yp.connect(props);
        Thread.sleep(10000);
        yp.disconnect();
        System.exit(0);
    }
}

/* */
