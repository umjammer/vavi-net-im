/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import vavi.net.im.protocol.oscar.util.ByteUtils;
import vavi.net.im.protocol.oscar.util.TLV;
import vavi.util.Debug;
import vavi.util.StringUtil;
//import vavi.util.win32.DateUtil;


/**
 * ServerICBM.
 *
 * @author mikem
 */
public class ServerICBM extends Command {
    private static final int TLV_MSG_DATA = 0x0002;
    private static final int TLV_MSG_TEXT = 0x0101;

    // this plus the screen name length should point to the beginning of the TLVs
    private static final int TLV_PARTIAL_OFFSET = 8 + 7;

    /**
     * The simple type, MSG_NORM only, non-acknowledged, internationalized
     * (note: the encoding information is lost for offline messages).
     */
//    private static final int MSGFORMAT_SIMPLE = 0x0001;

    /**
     * The generic type, acknowledged, online messages only,
     * can be internationalized.
     */
//    private static final int MSGFORMAT_ADVANCED = 0x0002;

    /**
     * The server type, used for messages the server needs to see,
     * non-acknowledged, no internationalization possible.
     */
//    private static final int MSGFORMAT_SERVER = 0x0004;

    /** */
    private long time;

    /** */
    private long id;
    private int msgChannel;
    private byte screenNameLength;
    private String screenName;
    private short senderWarningLevel;
    private short tlvCount;
    private List<TLV> tlvList;

    /** */
    private String message = null;

    /** */
    private static final DateFormat sdf = new SimpleDateFormat("[HH:mm] ");

    /** */
    static {
        sdf.setTimeZone(TimeZone.getTimeZone("etc/GMT"));
    }

    /**
     * <pre>
     * Data type    Content                 Name            Description
     * WTIME        xx xx xx xx             TIME            The time this message was sent.
     * DWORD        xx xx xx xx             ID              A seemingly random ID generated for each message.
     * WORD.B       xx xx                   FORMAT          The message transport format used.
     * BUIN         xx ...                  UIN             The sender's UIN.
     * WORD.B       xx xx                   WARNING         A warning level. Usually 0.
     * WORD.B       xx xx                   COUNT           The number of following TLVs.
     * TLV(1)       00 01 00 02 00 50       UNKNOWN         Unknown: 0x50 = 80.
     * TLV(4)       00 04 00 02 00 00       AUTORESPONSE    Marks this message as an AIM autoresponse. Probably unused for ICQ.
     * TLV(6)       00 06 00 04 xx xx xx xx STATUS          The sender's online status.
     * TLV(15)      00 0f 00 04 TIME         TIME           online time in seconds
     * TLV(2)       00 02 00 04 TIME         TIME           The member since time.
     * TLV(3)       00 03 00 04 TIME         TIME           user login timestamp
     * TLV(2)       00 02 xx xx ...          MESSAGE1       TYPE-1 ONLY. Yes, TLV(2) again. The message data in format 1.
     * TLV(5)       00 05 xx xx ...          MESSAGE2       TYPE-2 ONLY. The message data in format 2.
     * TLV(5)       00 05 xx xx ...          MESSAGE4       TYPE-4 ONLY. The message data in format 4.
     * </pre>
     */
    public ServerICBM(Command cmd) {
        flapHdr = cmd.getFlapHdr();
        snacPacket = cmd.getSNAC();

        byte[] data = cmd.getSNACData();

//Debug.print("snacData:\n" + StringUtil.getDump(data));
        time = ByteUtils.getUInt(data, 0);
        id = ByteUtils.getUInt(data, 4);
        msgChannel = ByteUtils.getUShort(data, 8);

//Debug.print("msgChannel: " + msgChannel);
        screenNameLength = data[10];
        screenName = new String(data, 11, screenNameLength);

//Debug.print(sdf.format(new Date(DateUtil.filetimeToLong(time))) + new Date(time) + ", " + screenName + ", " + StringUtil.toHex8((int) time) + ", " + id);
        senderWarningLevel = (short) ByteUtils.getUShort(data,
                                                         11 + screenNameLength);

//Debug.print("senderWarningLevel: " + senderWarningLevel);
        tlvCount = (short) ByteUtils.getUShort(data, 13 + screenNameLength);

//Debug.print("tlvs: " + tlvCount);
        // compute tlv offset and the rest is TLVs
        int tlvOffset = TLV_PARTIAL_OFFSET + screenNameLength;
        tlvList = TLV.getTLVs(data, tlvOffset);
    }

    /** */
    public String getMessageText() {
        if ((tlvList != null) && !tlvList.isEmpty()) {
            for (TLV tlv : tlvList) {

                if (tlv.getType() == TLV_MSG_DATA) { // 0x0002
                    processMessageData(tlv);
                } else {
                    // 0x01: UNKNOWN (constant)
                    // 0x0f: TIME online time in seconds
                    // 0x1d: UNKNOWN (???)
                    // 0x03: TIME user login timestamp
                    Debug.print("OUTER: " + tlv.toString());
                }
            }
        }

        return message;
    }

    /** */
    private void processMessageData(TLV tlv) {
        byte[] innerTlvs = tlv.getValue();
        List<TLV> list = TLV.getTLVs(innerTlvs, 0);

        if ((list != null) && !list.isEmpty()) {
            for (TLV element : list) {

                if (element.getType() == 0x0501) { // CAPABILITIES
                    processCapabilities(element);
                } else if (element.getType() == TLV_MSG_TEXT) { // 0x0101
                    processMessageText(element);
                } else {
                    Debug.print("INNER: " + element.toString());
                }
            }
        }
    }

    /**
     * Each byte describes a capability-like feature of the message.
     * Usually 01 is sent, or 01 and 06, if the receiver has CAP_UTF-8.
     * In the latter case, the message may be encoded in UCS-2BE (or UTF-16?),
     * otherwise the local encoding will be used and specified.
     */
    private void processCapabilities(TLV element) {
//Debug.print("INNER: " + element.toString());
    }

    /** */
    private void processMessageText(TLV element) {
        byte[] value = element.getValue();
Debug.print("<<< RECEIVED:\n" + StringUtil.getDump(value));

        byte[] text = new byte[value.length - 4];
        int encoding = ByteUtils.getUShort(value, 0);
        int unknown = ByteUtils.getUShort(value, 2);
        System.arraycopy(value, 4, text, 0, value.length - 4);

        try {
            switch (encoding) {
            case 0x0000:
                message = new String(text, "US-ASCII");
                break;
            case 0x0002:
                message = new String(text, "UTF-16BE");
                break;
            case 0x0003:
            default: 
                message = new String(text);
                break;
            }
        } catch (UnsupportedEncodingException e) {
Debug.printStackTrace(e);
            message = new String(text);
        }
    }

    /**
     *
     */
    public void writeCommandData(OutputStream os) throws IOException {
        // nothing to write - we receive this message/command
    }
}

/* */
