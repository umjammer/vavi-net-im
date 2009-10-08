/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ipm;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import vavi.net.im.Buddy;
import vavi.net.im.Group;
import vavi.net.im.Message;
import vavi.net.im.Session;
import vavi.net.im.SmileyComponent;
import vavi.net.im.protocol.Protocol;


/**
 * IP Messenger Protocol.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public class IpmProtocol extends Protocol {
    /** */
    static Logger log = Logger.getLogger(IpmProtocol.class.getName());

    /** */
    private static final String DEFAULT_HOST = "";

    /** */
    private static final int DEFAULT_PORT = 5190;

    /**
     * the current user.
     */
    private String username;

    /**
     * Password of the current user.
     */
    private String password;

    /**
     * host.
     */
    private String host;

    /**
     * port.
     */
    private int port;

    /**
     * Indicates whether we are connected to a AIM Server.
     */
    private boolean connected;

    /**
     * Default Constructor
     */
    public IpmProtocol() {
        super();

        try {
            final String path = "ipm.properties";
            Properties props = new Properties();
            props.load(IpmProtocol.class.getResourceAsStream(path));
            host = props.getProperty("ipm.host");
            port = Integer.parseInt(props.getProperty("ipm.port"));
        } catch (IOException e) {
            log.severe(String.valueOf(e));
            host = DEFAULT_HOST;
            port = DEFAULT_PORT;
        }

        connected = false;

        features.put(Feature.BuddyAddRequestSupported, true);
        features.put(Feature.BuddyGroupSupported, true);
        features.put(Feature.IgnoreSupported, true);
        features.put(Feature.OfflineMessageSupported, false);
        features.put(Feature.TypingNotifySupported, true);
        features.put(Feature.ConferenceSupported, false);
        features.put(Feature.MailNotifySupported, false);
        features.put(Feature.InvisibleSupported, true);
        features.put(Feature.BuddyNameAliasSupported, false);
    }

    /**
     */
    protected void changeStatusInternal(String status) throws IllegalArgumentException {
    }

    /** */
    protected Object localizeStatusInternal(String status) {
        return null; // TODO
    }

    /**
     * Connect to the Ipm network
     * 
     * @param props
     */
    protected void connectInternal(Properties props) throws IOException {

        this.username = props.getProperty("username");
        this.password = props.getProperty("password");

        // send the initial command
        connected = true;
    }

    /**
     * Disconnect
     */
    protected void disconnectInternal() throws IllegalStateException {
    }

    /**
     * Gets the protocol name
     * 
     * @return String containing the protocol name
     * @see vavi.net.im.protocol.Protocol#getProtocolName()
     */
    public String getProtocolName() {
        return "IPMessenger";
    }

    /**
     * Starts a conference
     * 
     * @param session Session
     */
    protected void startSessionInternal(Session session) throws IOException {
    }

    /**
     * Starts a conference
     * 
     * @param session Session
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
     * Adds a buddy to the Buddy List.
     * 
     * @param buddy Buddy to be added to the Buddy List
     */
    protected void addToBuddyListInternal(Buddy buddy) throws IOException {
    }

    /**
     * Deletes a buddy from the Buddy List.
     * 
     * @param buddy Buddy to be deleted from the Buddy List
     */
    protected void deleteFromBuddyListInternal(Buddy buddy) throws IOException {
    }

    /**
     * Ignores a buddy.
     * 
     * @param buddy Buddy to ignore
     */
    protected void ignoreBuddyInternal(Buddy buddy) throws IOException {
    }

    /**
     * Unignores a buddy.
     * 
     * @param buddy Buddy to unignore
     * @see vavi.net.im.protocol.Protocol#unignoreBuddy(vavi.net.im.api.Buddy)
     */
    protected void unignoreBuddyInternal(Buddy buddy) throws IOException {
    }

    /**
     * Sends an instant message to a buddy.
     * 
     * @param session Buddy to send message to
     * @param message Message to send
     */
    protected void sendSessionMessageInternal(Session session, Message message) throws IOException {
    }

    /**
     * Sends an instant message to a buddy.
     * 
     * @param session Buddy to send message to
     * @param message Message to send
     */
    protected void sendInstantMessageInternal(Session session, Message message) throws IOException {
    }

    /**
     * @see vavi.net.im.protocol.Protocol#startTyping()
     */
    protected void startTypingInternal(Buddy buddy) throws IOException {
    }

    /**
     * @see vavi.net.im.protocol.Protocol#stopTyping()
     */
    protected void stopTypingInternal(Buddy buddy) throws IOException {
    }

    /**
     */
    public SmileyComponent[] getSupportedSmileys() {
        return null;
    }

    /**
     */
    protected void changeBuddyAliasInternal(Buddy buddy, String alias) throws IOException {
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
