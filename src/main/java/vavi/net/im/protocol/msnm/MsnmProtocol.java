/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.msnm;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.hn.sleek.jmml.Contact;
import org.hn.sleek.jmml.ContactChangeEvent;
import org.hn.sleek.jmml.ContactStatus;
import org.hn.sleek.jmml.IncomingMessageEvent;
import org.hn.sleek.jmml.MSNException;
import org.hn.sleek.jmml.MessengerClientAdapter;
import org.hn.sleek.jmml.MessengerClientListener;
import org.hn.sleek.jmml.MessengerServerManager;

import vavi.net.im.Buddy;
import vavi.net.im.Group;
import vavi.net.im.Message;
import vavi.net.im.Session;
import vavi.net.im.SmileyComponent;
import vavi.net.im.event.IMEvent;
import vavi.net.im.event.IMEvent.IMEventName;
import vavi.net.im.protocol.Protocol;


/**
 * MsnmProtocol. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051222 nsano initial version <br>
 */
public class MsnmProtocol extends Protocol {
    /** */
    static Logger log = Logger.getLogger(MsnmProtocol.class.getName());

    /** */
    private static final String PROTOCOL_NAME = "MSN Instant Messenger";

    /** */
    private static final String DEFAULT_HOST = "msn.com";

    /** */
    private static final int DEFAULT_PORT = 5190; // 

    /**
     * host.
     */
    private String host;

    /**
     * port.
     */
    private int port;

    /** MSN Command Handler */
    private MessengerServerManager connection;

    /** */
    private MessengerClientListener msnListener = new MessengerClientAdapter() {
        public void contactPropertyChanged(ContactChangeEvent event) {
            try {
                if (event.getProperty() == Contact.STATUS) {
                    Buddy buddy = findBuddy(event.getUserName());
                    if (buddy != null) {
                        // @listener
                        listeners.eventHappened(new IMEvent(this, IMEventName.buddyStatusChanged, buddy));
                    }
                } else if (event.getProperty() == Contact.FRIENDLY_NAME) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void incomingMessage(IncomingMessageEvent event) {
            try {
                Buddy buddy = findBuddyForce(event.getUserName());
                Message message = new Message(event.getMessage());

                // @listener
                listeners.eventHappened(new IMEvent(this, IMEventName.instantMessageReceived, findSessionForce(buddy), message));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void reverseListChanged(String string) {
        }
        public void serverDisconnected() {
        }
        public void loginError() {
        }
        public void loginAccepted() {
        }
        public void groupReceived(String string) {
        }
        public void contactReceived(Contact contact) {
        }
        public void contactAdded(Contact contact) {
            // TODO 「あなたをコンタクトリストに加えたい」の承認
        }
        public void contactRemoved(Contact contact) {
        }
    };

    /**
     * Default Constructor
     */
    public MsnmProtocol() {
        try {
            Properties props = new Properties();
            props.load(MsnmProtocol.class.getResourceAsStream("msn.properties"));
            host = props.getProperty("msn.host");
            port = Integer.parseInt(props.getProperty("msn.port"));
        } catch (IOException e) {
log.warning(String.valueOf(e));
            host = DEFAULT_HOST;
            port = DEFAULT_PORT;
        }

        // todo setup status flag masks to put in here
        statusMap.put("online", ContactStatus.ONLINE);
        statusMap.put("away", ContactStatus.AWAY);
        statusMap.put("dnd", ContactStatus.BE_RIGHT_BACK);
        statusMap.put("offline", ContactStatus.OFFLINE);
        statusMap.put("busy", ContactStatus.BUSY);
        statusMap.put("invisible", ContactStatus.APPEAR_OFFLINE);
        statusMap.put("idle", ContactStatus.IDLE);
        statusMap.put("onthephone", ContactStatus.ON_THE_PHONE);
        statusMap.put("outtolunch", ContactStatus.OUT_TO_LUNCH);

        features.put(Feature.BuddyAddRequestSupported, true);
        features.put(Feature.BuddyGroupSupported, true);
        features.put(Feature.IgnoreSupported, false);
        features.put(Feature.OfflineMessageSupported, false);
        features.put(Feature.TypingNotifySupported, false);
        features.put(Feature.ConferenceSupported, false);
        features.put(Feature.MailNotifySupported, false);
        features.put(Feature.InvisibleSupported, true);
        features.put(Feature.BuddyNameAliasSupported, false);

        connection = MessengerServerManager.getInstance();
    }

    /**
     * @param status 
     */
    protected void changeStatusInternal(String status) throws IOException {
        String nativeStatus = null;

        if (status == null) {
            nativeStatus = (String) statusMap.get("invisible");
        } else {
            nativeStatus = (String) statusMap.get(status);

            if (nativeStatus == null) {
                throw new IllegalArgumentException("status " + status + " not supported");
            }
        }

        // send the status flag mask down to be processed
        try {
            connection.setStatus(nativeStatus);
        } catch (MSNException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    /** */
    protected Object localizeStatusInternal(String status) {
        return 0;
    }

    /**
     * Connect to the AIM server
     * 
     * @param props
     */
    protected void connectInternal(Properties props) throws IOException {
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        /* send the initial command */
        try {
            connection.addMessengerClientListener(msnListener);

            // @listener
            listeners.eventHappened(new IMEvent(this, IMEventName.connecting));
            connection.signIn(username, password, ContactStatus.ONLINE);
            // @listener
            listeners.eventHappened(new IMEvent(this, IMEventName.connected));

        } catch (MSNException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    /**
     * Disconnect from AIM
     */
    protected void disconnectInternal() throws IOException {
        if (connection.isConnected()) {
            // tell command handler to disconnect/cleanup
            try {
                connection.signOut();
            } catch (MSNException e) {
                throw (IOException) new IOException().initCause(e);
            }
        }
    }

    /**
     * Gets the protocol name
     * 
     * @return String containing the protocol name
     */
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    /**
     * Starts a conference
     */
    protected void startSessionInternal(Session session) throws IOException {
    }

    /**
     * @param message invitation message
     */
    protected void startSessionInternal(Session session, String message) throws IOException {
    }

    /**
     * Quits a conference.
     * 
     * @param session conference to quit
     */
    protected void quitSessionInternal(Session session) throws IOException {
    }

    /**
     * Sends an instant message to the conference.
     * 
     * @param session Session
     * @param message Message to send to the conference
     */
    protected void sendSessionMessageInternal(Session session, Message message) throws IOException {
    }

    /**
     * Adds a buddy to the Buddy List.
     * 
     * @param buddy Buddy to be added to the Buddy List
     */
    protected void addToBuddyListInternal(Buddy buddy) throws IOException {
        String username = buddy.getUsername();
        Contact contact = new Contact(username, Contact.CONTACTLIST_TYPE);
        try {
            connection.addToContactList(contact);
        } catch (MSNException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    /**
     * Deletes a buddy from the Buddy List.
     * 
     * @param buddy Buddy to be deleted from the Buddy List
     */
    protected void deleteFromBuddyListInternal(Buddy buddy) throws IOException {
        String username = buddy.getUsername();
        Contact contact = new Contact(username, Contact.CONTACTLIST_TYPE);
        try {
            connection.removeFromContactList(contact);
        } catch (MSNException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    /**
     * Ignores a buddy.
     * 
     * @param buddy Buddy to ignore
     */
    protected void ignoreBuddyInternal(Buddy buddy) throws IOException {
        // TODO think this means making them invisible - double check this
    }

    /**
     * Unignores a buddy.
     * 
     * @param buddy Buddy to unignore
     */
    protected void unignoreBuddyInternal(Buddy buddy) throws IOException {
        // TODO think this means making them visible again - remove from
        // invisible
    }

    /**
     * Sends an instant message to a buddy.
     * 
     * @param session Buddy to send message to
     * @param message Message to send
     */
    protected void sendInstantMessageInternal(Session session, Message message) throws IOException {
        String username = session.getParticipants()[0].getUsername();
        String text = message.toString();
log.info("to: " + username + ", : " + text);
        try {
            connection.sendMessage(username, text);
        } catch (MSNException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    /** */
    protected void startTypingInternal(Buddy buddy) throws IOException {
    }

    /** */
    protected void stopTypingInternal(Buddy buddy) throws IOException {
    }

    /** */
    public SmileyComponent[] getSupportedSmileys() {
        return null;
    }

    /** */
    protected void changeBuddyAliasInternal(Buddy buddy, String alias) throws IOException {
        // TODO 自分だけ？
        try {
            connection.setFriendlyName(alias);
        } catch (MSNException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    /** */
    protected void addGroupInternal(Group group) throws IOException {
    }

    /** */
    protected void removeGroupInternal(Group group) throws IOException {
    }

    /** */
    protected void changeGroupNameInternal(Group group, String newName) throws IOException {
    }

    /** */
    protected void changeGroupOfBuddyInternal(Buddy buddy, Group oldGroup, Group newGroup) throws IOException {
    }
}

/* */
