/*
 * 1997/10/16 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm.event;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.EventObject;

import vavi.net.im.protocol.ipm.IpmPacket;


/**
 * IP Messenger Proxy Event Class
 */
public class ProxyEvent extends EventObject {
    /** */
    protected IpmPacket packet = null;

    /** */
    protected InetSocketAddress fromAddress = null;

    /** */
    protected InetSocketAddress toAddress = null;

    /** */
    public ProxyEvent(Object source, IpmPacket packet, InetSocketAddress from, InetSocketAddress to) {
        super(source);
        this.packet = packet;
        this.fromAddress = from;
        this.toAddress = to;
    }

    /** */
    public ProxyEvent(Object source, byte[] buf) {
        super(source);

        int colons = 0;
        int index = 0;
        int begin = 0;
        int[] indexes = new int[4];
        while (buf[begin] == 0) {
            begin++;
        }
        while (colons < 4) {
            if (buf[index] == ":".getBytes()[0]) {
                indexes[colons] = index;
                colons++;
            }
            index++;
        }
        try {
            fromAddress = new InetSocketAddress(InetAddress.getByName(new String(buf, begin, indexes[0] - begin)), Integer.parseInt(new String(buf, indexes[0] + 1, indexes[1] - indexes[0] - 1)));
            toAddress = new InetSocketAddress(InetAddress.getByName(new String(buf, indexes[1] + 1, indexes[2] - indexes[1] - 1)), Integer.parseInt(new String(buf, indexes[2] + 1, indexes[3] - indexes[2] - 1)));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return;
        }

        byte[] packetBuffer = new byte[buf.length - indexes[3] - 1];
        System.arraycopy(buf, indexes[3] + 1, packetBuffer, 0, packetBuffer.length);
        packet = new IpmPacket(packetBuffer);
    }

    /** */
    public byte[] getBytes() {
        ByteBuffer bb = ByteBuffer.allocate(1024);
        String prefix = fromAddress.toString() + ":" + toAddress.toString() + ":";
        bb.put(prefix.getBytes());
        bb.put(packet.getBytes());

        byte[] suffix = new byte[] {
            0, 0, 0
        };
        bb.put(suffix);
        return bb.array();
    }

    /** */
    public IpmPacket getPacket() {
        return packet;
    }

    /** */
    public InetSocketAddress getFromAddress() {
        return fromAddress;
    }

    /** */
    public InetSocketAddress getToAddress() {
        return toAddress;
    }
}

/* */
