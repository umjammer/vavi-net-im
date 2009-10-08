/*
 * 1997/10/16 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm.event;

import java.net.InetSocketAddress;
import java.util.Date;

import vavi.net.im.protocol.ipm.IpmPacket;


/**
 * IP Messenger Event Class.
 */
public class IpmEvent extends CommunicationEvent {
    /** */
    public enum Id {
        /** */
        NONE,
        /** */
        UPDATE_LIST,
        /** */
        RECEIVE_MESSAGE,
        /** */
        READ_MESSAGE,
        /** */
        DELETE_MESSAGE,
        /** */
        CANNOT_SEND_MESSAGE
    }

    /** */
    private Id id = Id.NONE;

    /** */
    private Date date;

    /** */
    public IpmEvent(Object source, Id id, Date date, int port, IpmPacket packet, InetSocketAddress address) {
        super(source, port, packet, address);
        this.id = id;
        this.date = date;
    }

    /** */
    public IpmEvent(Object source, Id id, Date date, CommunicationEvent event) {
        this(source, id, date, event.getLocalPort(), event.getPacket(), event.getAddress());
    }

    /** */
    public Id getID() {
        return id;
    }

    /** */
    public Date getDate() {
        return date;
    }
}

/* */
