/*
 * 1997/10/17 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


/**
 * IP Messenger Packet Send Abstract Class.
 */
public abstract class Sender implements Runnable {
    /* */
    protected DatagramSocket socket;
    /* */
    protected IpmPacket packet;
    /* */
    protected InetSocketAddress address;

    /**
     * @param socket
     * @param packet
     * @param address
     */
    public static void send(DatagramSocket socket, IpmPacket packet, InetSocketAddress address) throws IOException {
// System.out.println("Send(" + argsock.getLocalPort() + "):" + argaddr.toString() + ":" + new String(argpack.getBytes()));
        DatagramPacket datagramPacket = new DatagramPacket(packet.getBytes(),
                                                           packet.getBytes().length,
                                                           address.getAddress(),
                                                           address.getPort());
        socket.send(datagramPacket);
    }

    /**
     * 
     * @param socket
     * @param packet
     * @param address
     */
    public Sender(DatagramSocket socket, IpmPacket packet, InetSocketAddress address) {
        this.socket = socket;
        this.packet = packet;
        this.address = address;
    }
}

/* */
