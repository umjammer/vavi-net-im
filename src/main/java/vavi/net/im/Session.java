/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vavi.net.im.event.IMEvent;
import vavi.net.im.event.IMSupport;
import vavi.net.im.event.IMEvent.IMEventName;


/**
 * A conference is a communication mechanism by which more than
 * two buddies can exchange messages with each other.
 * 
 * @see IMEvent.IMEventName#addParticipant
 * @see IMEvent.IMEventName#quitSession
 * @see IMEvent.IMEventName#sendSessionMessage
 * @see IMEvent.IMEventName#sendInstantMessage
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public class Session implements Serializable {
    /**
     * The underlying protocol for this conference.
     */
    protected IMSupport listeners;

    /**
     * The host buddy.
     */
    private Buddy host;

    /**
     * A list of all participants, including the host.
     */
    protected List<Buddy> buddies = new ArrayList<>();

    /** セッションの名前 */
    private String name;

    /** 1 対 1 の場合 false、1 対多の場合 true */
    private boolean groupSession;

    /** */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return is group session or not
     */
    public boolean isGroupSession() {
        return groupSession;
    }

    /**
     * Construct a conference. This constructor is meant for internal use
     * by the library and not for the users of Hamsam. You may use
     * {@link #Session(IMSupport,Buddy,Buddy[],String)} to start a new conference.
     *
     * @param listeners the underlying protocol for this conference.
     * @param host the buddy who is hosting this conference.
     * @param buddy a list of all buddies participating in this conference,
     *                including the host.
     */
    public Session(IMSupport listeners, Buddy host, Buddy buddy) {
        this.listeners = listeners;
        this.host = host;
        this.buddies.add(buddy);
    }

    /**
     * 
     *
     * @param listeners the underlying protocol for this conference.
     * @param host the buddy who is hosting this conference.
     * @param buddies a list of all buddies participating in this conference,
     *                including the host.
     */
    public Session(IMSupport listeners, Buddy host, Buddy[] buddies) {
        this.listeners = listeners;
        this.host = host;
        this.buddies.addAll(Arrays.asList(buddies));
    }

    /**
     * Start a new conference and invite buddies to join. This constructor is
     * used to create new conferences.
     *
     * @param listeners the underlying protocol for this conference.
     * @param host the buddy who is hosting this conference.
     * @param buddies a list of all buddies participating in this conference,
     *                including the host.
     * @param message the invitation message that is to be sent to the participants.
     */
    public Session(IMSupport listeners, Buddy host, Buddy[] buddies, String message) throws IOException {
        this.listeners = listeners;
        this.host = host;
        this.buddies.addAll(Arrays.asList(buddies));

        listeners.eventHappened(new IMEvent(this, IMEventName.addParticipant, this, buddies));
    }

    /**
     * Quit from this conference.
     */
    public void quit() throws IOException {
        listeners.eventHappened(new IMEvent(this, IMEventName.quitSession, this));
    }

    /**
     * Send a message to this conference.
     *
     * @param message the message to be sent.
     */
    public void sendMessage(Message message) throws IOException {
//Debug.println("here 0");
        if (groupSession) {
            listeners.eventHappened(new IMEvent(this, IMEventName.sendSessionMessage, this, message));
        } else {
//Debug.println("here 1");
            listeners.eventHappened(new IMEvent(this, IMEventName.sendInstantMessage, this, message));
        }
    }

    /**
     * Add a new participant to this conference object. This method is meant for
     * internal use by the library and not for the users of Hamsam.
     *
     * @param buddy the buddy to be added to this conference.
     */
    public void addParticipant(Buddy buddy) {
        if (!buddies.contains(buddy)) {
            buddies.add(buddy);
        }
    }

    /**
     * Returns all participants for this conference.
     *
     * @return all participants for this conference.
     */
    public Buddy[] getParticipants() {
        return buddies.toArray(new Buddy[0]);
    }

    /**
     * Returns the host buddy for this conference.
     *
     * @return the host buddy for this conference.
     */
    public Buddy getHost() {
        return host;
    }

    /**
     * Removes a buddy from this conference object. This method is for
     * internal use by Hamsam. As a user of the API, you should never
     * invoke this method. Hamsam does not allow you to block another user
     * from a conference.
     *
     * @param buddy the buddy to be removed.
     */
    public void removeParticipant(Buddy buddy) {
        buddies.remove(buddy);
    }
}

/* */
