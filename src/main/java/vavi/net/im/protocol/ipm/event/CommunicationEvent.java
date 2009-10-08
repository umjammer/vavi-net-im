/*
 * 1997/10/16 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm.event;

import java.net.InetSocketAddress;
import java.util.EventObject;

import vavi.net.im.protocol.ipm.IpmPacket;


/**
 * CommunicationEvent.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050802 nsano initial version <br>
 */
public class CommunicationEvent extends EventObject implements Comparable<CommunicationEvent> {
    /** */
    protected int port;

    /** */
    protected IpmPacket packet;

    /** */
    protected InetSocketAddress address;

    /** */
    public CommunicationEvent(Object source, int port, IpmPacket packet, InetSocketAddress address) {
        super(source);
        this.port = port;
        this.packet = packet;
        this.address = address;
    }

    /** */
    public int getLocalPort() {
        return port;
    }

    /** */
    public IpmPacket getPacket() {
        return packet;
    }

    /** */
    public InetSocketAddress getAddress() {
        return address;
    }

    /** */
    public int compareTo(CommunicationEvent target) {
        return address.getAddress().getHostName().compareTo(target.address.getAddress().getHostName());
    }
}

/* */
