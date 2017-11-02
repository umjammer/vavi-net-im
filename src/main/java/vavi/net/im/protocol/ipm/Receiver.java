/*
 * 1997/10/16 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import vavi.net.im.protocol.ipm.event.CommunicationEvent;
import vavi.net.im.protocol.ipm.event.CommunicationListener;


/**
 * IP Messenger Packet Receive Class
 */
public class Receiver implements Runnable {
    /** */
    private static final int MEMORY = 100;

    /** */
    private static final int MAXBUF = 8192;

    /** */
    private DatagramSocket socket;

    /** */
    private Map<CommunicationListener, CommunicationListener> listeners = new HashMap<>();

    /** */
    private IpmPacket[] recentPackets = new IpmPacket[MEMORY];

    /** */
    private Map<String, IpmPacket> flags = new HashMap<>();

    /** */
    public Receiver(DatagramSocket socket) {
        this.socket = socket;
    }

    /** */
    public synchronized void addIPMComListener(CommunicationListener listener) {
        listeners.put(listener, listener);
    }

    /** */
    public synchronized void removeIPMComListener(CommunicationListener listener) {
        listeners.remove(listener);
    }

    /** */
    public void run() {
        int count = 0;
        byte[] packetBuffer = new byte[MAXBUF];
        while (true) {
            DatagramPacket datagramPacket = new DatagramPacket(packetBuffer, packetBuffer.length);
            try {
                socket.receive(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            int port = datagramPacket.getPort();
            InetAddress address = datagramPacket.getAddress();
            byte[] buffer = new byte[datagramPacket.getLength()];
            System.arraycopy(packetBuffer, 0, buffer, 0, buffer.length);

            IpmPacket packet = new IpmPacket(buffer);
            boolean b = false;
            if (flags.containsKey(packet.getKey())) {
                b = true;
            }
            count = (count + 1) % MEMORY;
            if (recentPackets[count] != null) {
                flags.remove(recentPackets[count].getKey());
            }
            recentPackets[count] = packet;
            flags.put(packet.getKey(), packet);
            if (b) {
                continue;
            }

            InetSocketAddress tmpAddress = new InetSocketAddress(address, port);

// System.out.println("Recv(" + dsock.getLocalPort() + "):" + tmpaddr.toString() + ":" + new String(tmppack.getBytes()));
            CommunicationEvent event = new CommunicationEvent(this, socket.getLocalPort(), packet, tmpAddress);
            synchronized (this) {
                for (CommunicationListener listener : listeners.values()) {
                    try {
                        listener.receive(event);
                    } catch (IOException e) {
                        e.printStackTrace(); // TODO
                    }
                }
            }
        }
    }
}

/* */
