/*
 * 1997/10/27 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import vavi.net.im.protocol.ipm.Ipmessenger.Constant;


/**
 * Broadcast Packet Send Class
 */
public class BroadcastSender extends Sender {
    /** */
    private static final int wait = 500;

    /** */
    private InetSocketAddress[] broadcastAddress;

    /** */
    public void run() {
        try {
            if (broadcastAddress.length > 0) {
                int limitedb = 0;
                for (; limitedb < broadcastAddress.length; limitedb++) {
                    try {
                        if (broadcastAddress[limitedb] == null) {
                            continue;
                        } else if (broadcastAddress[limitedb].getAddress().equals(InetAddress.getByName("255.255.255.255"))) {
                            send(socket, packet, broadcastAddress[limitedb]);
                        } else {
                            break;
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace(System.err);
                    }
                }

                long command = packet.getCommand();
                packet.setCommand(Constant.NOOPERATION.getValue());
                for (int i = limitedb; i < broadcastAddress.length; i++) {
                    send(socket, packet, broadcastAddress[i]);
                }
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                }
                packet.setCommand(command);
                for (int i = limitedb; i < broadcastAddress.length; i++) {
                    send(socket, packet, broadcastAddress[i]);
                }
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                }
                for (int i = limitedb; i < broadcastAddress.length; i++) {
                    send(socket, packet, broadcastAddress[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /** */
    public BroadcastSender(DatagramSocket socket, IpmPacket packet, InetSocketAddress[] address) {
        super(socket, packet, null);
        broadcastAddress = address;
    }
}

/* */
