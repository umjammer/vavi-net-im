/*
 * 1997/10/22 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import vavi.net.im.protocol.ipm.Ipmessenger.Constant;
import vavi.net.im.protocol.ipm.event.CommunicationEvent;
import vavi.net.im.protocol.ipm.event.CommunicationListener;


/**
 * NormalSender. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 971022 initial version <br>
 */
public class NormalSender extends Sender {
    /** */
    private Map<CommunicationListener, CommunicationListener> listeners = new HashMap<>();

    /** */
    private static final int wait = 3000;

    /** */
    private static final int retry = 3;

    /** */
    private boolean reply = false;

    /** */
    public synchronized void addCommunicationListener(CommunicationListener listener) {
        listeners.put(listener, listener);
    }

    /** */
    public synchronized void removeCommunicationListener(CommunicationListener listener) {
        listeners.remove(listener);
    }

    /** */
    public void run() {
        for (int i = 0; i < retry; i++) {
            try {
                send(socket, packet, address);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
            }
            if (reply) {
                return;
            }
            packet.setCommand(packet.getCommand() | Constant.RETRYOPT.getValue());
        }

        CommunicationEvent event = new CommunicationEvent(this, socket.getLocalPort(), packet, address);
        synchronized (this) {
            for (CommunicationListener listener : listeners.values()) {
                try {
                    listener.receive(event);
                } catch (IOException e) {
                    e.printStackTrace(System.err); // TODO
                }
            }
        }
    }

    /** */
    public void receiveReply() {
        reply = true;
    }

    /** */
    public NormalSender(DatagramSocket socket, IpmPacket packet, InetSocketAddress address) {
        super(socket, packet, address);
    }
}

/* */
