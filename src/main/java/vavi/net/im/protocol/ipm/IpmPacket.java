/*
 * 1997/10/14 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


/**
 * IP Messenger Packet Class
 */
public class IpmPacket {
    private byte[] pack;
    private long version = 0;
    private long no = 0;
    private long command;
    private String user = null;
    private String host = null;
    private String extra = null;
    private String group = null;

    /**
     * 
     * @param version
     * @param no
     * @param user
     * @param host
     * @param command
     * @param extra
     * @param group
     */
    public IpmPacket(long version, long no, String user, String host, long command, String extra, String group) {
        this.version = version;
        this.no = no;
        this.user = user;
        this.host = host;
        this.command = command;
        this.extra = extra;
        this.group = group;
    }

    /** */
    private void pack() {
        StringBuilder sb = new StringBuilder();
        sb.append(version).append(":");
        sb.append(no).append(":");
        sb.append(user).append(":");
        sb.append(host).append(":");
        sb.append(command).append(":");
        sb.append(extra);

        String tmpString = new String(sb);
        String ls = System.getProperty("line.separator", "\n");
        String cr = "\n";
        tmpString = tmpString.replace(ls, cr);

        ByteBuffer bb = ByteBuffer.allocate(1024);
        try {
            bb.put(tmpString.getBytes(Ipmessenger.encoding));
        } catch (UnsupportedEncodingException e) {
            assert false;
        }

        byte[] nullByte = { 0 };
        bb.put(nullByte);
        if (group != null && !group.equals("")) {
            try {
                bb.put(group.getBytes(Ipmessenger.encoding));
                bb.put(nullByte);
            } catch (UnsupportedEncodingException e) {
                assert false;
            }
        }
        pack = bb.array();
    }

    /** */
    public IpmPacket(byte[] buf) {
        pack = buf;
        unpack();
    }

    /** */
    private void unpack() {
        byte[] buf = pack;
        int j = buf.length - 1;
        if (buf[j] == 0) {
            while (buf[j] == 0) {
                j--;
            }

            byte[] tmpbuf = new byte[j + 1];
            System.arraycopy(buf, 0, tmpbuf, 0, tmpbuf.length);
            buf = tmpbuf;
        }

        int i = 0;
        for (i = 0; i < buf.length; i++) {
            if (buf[i] == 0) {
                break;
            }
        }
        if (i < buf.length) {
            j = buf.length - 1;

            byte[] tmpbuf = new byte[j - i];
            System.arraycopy(buf, i + 1, tmpbuf, 0, j - i);
            try {
                group = new String(tmpbuf, Ipmessenger.encoding);
            } catch (UnsupportedEncodingException ex) {
                group = new String(tmpbuf);
            }
            tmpbuf = new byte[i];
            System.arraycopy(buf, 0, tmpbuf, 0, i);
            buf = tmpbuf;
        }

        String tmpString;
        try {
            tmpString = new String(buf, 0, buf.length, "SJIS");
        } catch (UnsupportedEncodingException ex) {
            tmpString = new String(buf, 0, buf.length);
        }

        String ls = System.getProperty("line.separator", "\n");
        String cr = "\n";
        tmpString = tmpString.replace(ls, cr).trim();

        StringTokenizer tokenizer = new StringTokenizer(tmpString, ":", false);
        try {
            version = Long.parseLong(tokenizer.nextToken());
            no = Long.parseLong(tokenizer.nextToken());
            user = tokenizer.nextToken();
            host = tokenizer.nextToken();
            command = Long.parseLong(tokenizer.nextToken());
        } catch (NoSuchElementException | NumberFormatException e) {
            e.printStackTrace(System.err);
            return;
        }
        if (tokenizer.hasMoreTokens()) {
            extra = tokenizer.nextToken();
        }
        while (tokenizer.hasMoreTokens()) {
            extra = extra + ':' + tokenizer.nextToken();
        }
    }

    /** */
    public boolean equals(Object target) {
        if (target instanceof IpmPacket) {
            IpmPacket packet = (IpmPacket) target;
            if (user.equals(packet.getUser()) && host.equals(packet.getHost()) &&
                (no == packet.getNo()) && (command == packet.getCommand())) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    /** */
    public String getKey() {
        return user + ":" + host + ":" + no + ":" + command;
    }

    public byte[] getBytes() {
        pack();
        return pack;
    }

    public void setBytes(byte[] pack) {
        this.pack = pack;
        unpack();
    }

    public void setVersion(long ver) {
        version = ver;
    }

    public long getVersion() {
        return version;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setCommand(long command) {
        this.command = command;
    }

    public long getCommand() {
        return command;
    }

    public String getExtra() {
        return extra;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}

/* */
