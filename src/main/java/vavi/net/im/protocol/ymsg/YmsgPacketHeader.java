/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import vavi.util.Debug;
import vavi.util.StringUtil;


/**
 * パケットヘッダを表すクラスです。
 * 
 * 20 byte
 * 
 * <pre>
 *  00-03	シグニチャ      C4     &quot;YMSG&quot;
 *  04		バージョン      C      9
 *  05-07	?               C3     &quot;\0\0\0&quot;
 *  08-09	ボディ部の長さ  n
 *  10-11	イベント番号    n
 *  12-15	フラグ/返り値   N
 *  16-19	接続ID          N
 * </pre>
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040221 nsano initial version <br>
 */
public class YmsgPacketHeader {

    /** パケットヘッダの長さ (20 bytes) */
    public static final int LENGTH = 4 + 2 + 2 + 2 + 2 + 4 + 4;

    /** */
    private int length;

    /** */
    private static final String SIGNATURE = "YMSG";

    /** */
    static final int VERSION_JP = 0x000c;
    /** */
    static final int VERSION_EN = 0x000c;
    /** */
    static final int VERSION_WEB_EN = 0x0065;

    /** */
    private int version;

    /** */
    public enum Event {
        LOGON(1),
        LOGOFF(2),
        ISAWAY(3),
        ISBACK(4),
        /** 5 (placemarker) */
        IDLE(5),
        MESSAGE(6),
        IDACT(7),
        IDDEACT(8),
        MAILSTAT(9),
        /** 0xa */
        USERSTAT(10),
        NEWMAIL(11),
        CHATINVITE(12),
        CALENDAR(13),
        NEWPERSONALMAIL(14),
        NEWCONTACT(15),
        /** 0x10 */
        ADDIDENT(16),
        ADDIGNORE(17),
        PING(18),
        /** < 1), 36(old)), 37(new) */
        GOTGROUPRENAME(19),
        SYSMESSAGE(0x14),
        PASSTHROUGH2(0x16),
        CONFINVITE(0x18),
        CONFLOGON(0x19),
        CONFDECLINE(0x1a),
        CONFLOGOFF(0x1b),
        CONFADDINVITE(0x1c),
        CONFMSG(0x1d),
        CHATLOGON(0x1e),
        CHATLOGOFF(0x1f),
        CHATMSG(0x20),
        GAMELOGON(0x28),
        GAMELOGOFF(0x29),
        GAMEMSG(0x2a),
        FILETRANSFER(0x46),
        VOICECHAT(0x4a),
        NOTIFY(0x4b),
        VERIFY(0x4c),
        P2PFILEXFER (0x4d),
        /** Checks if P2P possible */
        PEERTOPEER(0x4f),
        WEBCAM(0x50),
        AUTHRESP(0x54),
        LIST(0x55),
        AUTH(0x57),
        ADDBUDDY(0x83),
        REMBUDDY(0x84),
        /** > 1; 7; 13 < 1; 66; 13; 0*/
        IGNORECONTACT(0x85),
        REJECTCONTACT(0x86),
        /* > 1; 65(new); 66(0); 67(old) */
        GROUPRENAME(0x89),
        /* > 109(id); 1; 6(abcde) < 0;1*/
        CHATONLINE(0x96),
        CHATGOTO(0x97),
        /* > 1 104-room 129-1600326591 62-2 */
        CHATJOIN(0x98),
        CHATLEAVE(0x99),
        CHATEXIT(0x9b),
        CHATADDINVITE(0x9d),
        CHATLOGOUT(0xa0),
        CHATPING(0xa1),
        COMMENT(0xa8),
        AVATAR(0xbc),
        PICTURE_CHECKSUM(0xbd),
        PICTURE(0xbe),
        PICTURE_UPDATE(0xc1),
        PICTURE_UPLOAD(0xc2),
        YAHOO6_STATUS_UPDATE(0xc6),
        AVATAR_UPDATE(0xc7),
        AUDIBLE(0xd0),
        WEBLOGIN(0x0226),
        SMS_MSG(0x02ea);
        int value;
        Event(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        static Event valueOf(int value) {
            for (Event event : values()) { 
                if (event.value == value) {
                    return event;
                }
            }
            throw new IllegalArgumentException(String.valueOf(value));
        }
    };

    /** */
    private Event event;

    /** */
    public enum Status {
        AVAILABLE(0, false),
        BRB(1, true),
        BUSY(2, false),
        NOTATHOME(3, true),
        NOTATDESK(4, true),
        NOTINOFFICE(5, true),
        ONPHONE(6, false),
        ONVACATION(7, true),
        OUTTOLUNCH(8, true),
        STEPPEDOUT(9, true),
        INVISIBLE(12, true),
        CUSTOM(99, false),
        IDLE(999, true),
        WEBLOGIN(0x5a55aa55, false),
        OFFLINE(0x5a55aa56, false), /* don't ask */
        TYPING(0x16, false);
        int value;
        boolean away;
        Status(int value, boolean away) {
            this.value = value;
            this.away = away;
        }
        public int getValue() {
            return value;
        }
        public boolean isAway() {
            return away;
        }
        public static Status valueOf(int value) {
            for (Status event : values()) { 
                if (event.value == value) {
                    return event;
                }
            }
            throw new IllegalArgumentException(String.valueOf(value));
        }
    };

    /** */
    private int status;

    /** */
    private int connectionId;

    /**
     * @param version
     * @param connectionId
     * @param event
     * @param status
     */
    public YmsgPacketHeader(int version,
                            int connectionId,
                            Event event,
                            int status) {

        this.version = version;
        this.event = event;
        this.status = status;
        this.connectionId = connectionId;

        this.length = 0;
    }

    /** */
    private YmsgPacketHeader() {
    }

    /** */
    public static YmsgPacketHeader readFrom(InputStream is)
        throws IOException {

        YmsgPacketHeader yph = new YmsgPacketHeader();

        DataInputStream dis = new DataInputStream(is);

        // YMSG
        int s1 = dis.readByte();
        int s2 = dis.readByte();
        int s3 = dis.readByte();
        int s4 = dis.readByte();

        if (s1 != 'Y' || s2 != 'M' || s3 != 'S' || s4 != 'G') {
Debug.println(StringUtil.toHex2(s1) + " " + StringUtil.toHex2(s2) + " " + StringUtil.toHex2(s3) + " " + StringUtil.toHex2(s4));
            throw new IllegalArgumentException("header: " +
                                               (char) s1 +
                                               (char) s2 +
                                               (char) s3 +
                                               (char) s4);
        }

        yph.version      = dis.readShort();
                           dis.readShort();
        yph.length       = dis.readShort();
//Debug.println("length: " + StringUtil.toHex4(yph.length) + ", " + yph.length);
        yph.event        = Event.valueOf(dis.readShort());
//Debug.println("event: " + StringUtil.toHex4(yph.event) + ", " + yph.event);
        yph.status       = dis.readInt();
//Debug.println("status: " + StringUtil.toHex8(yph.status) + ", " + yph.status);
        yph.connectionId = dis.readInt();
//Debug.println("connectionId: " + StringUtil.toHex8(yph.connectionId) + ", " + yph.connectionId);
        return yph;
    }

    /** */
    public int getVersion() {
        return version;
    }

    /** Returns length in packet. (sum of data length, without header length) */
    public int getLength() {
        return length;
    }

    /** */
    public Event getEvent() {
        return event;
    }

    /** */
    public int getStatus() {
        return status;
    }

    /** */
    public int getConnectionId() {
        return connectionId;
    }

    /** */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @throws java.lang.IllegalStateException
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(LENGTH);
        try {
            DataOutputStream dos = new DataOutputStream(baos);
            dos.write(SIGNATURE.getBytes(), 0, 4);
            dos.writeShort(version);
            dos.writeShort(0x0000);
            dos.writeShort(length);
            dos.writeShort(event.value);
            dos.writeInt(status);
            dos.writeInt(connectionId);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return baos.toByteArray();
    }
}

/* */
