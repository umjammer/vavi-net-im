/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import vavi.net.im.protocol.oscar.flap.FlapConstants;
import vavi.net.im.protocol.oscar.flap.FlapHeader;
import vavi.net.im.protocol.oscar.snac.SNACConstants;
import vavi.net.im.protocol.oscar.snac.SNACPacket;
import vavi.net.im.protocol.oscar.util.ByteUtils;
import vavi.net.im.protocol.oscar.util.TLV;
//import vavi.util.StringUtil;


/**
 * @author mikem
 */
public class ClientICMB extends Command {
    /** */
    private byte[] MSGID_COOKIE = {
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00
    };
    /** */
    private static final byte CHANNEL = 0x01;
    /** */
    private String screenName;
    /** */
    private static final int TLV_MSG_DATA = 0x0002;
    /** */
    private static final int TLV_MSG_ACK = 0x0003;
    /** */
    private static final int TLV_STORE_MSG_IF_OFFLINE = 0x0006;
    /** */
    private static final int TLV_CLIENT_FUTURE = 0x0501;
    /** */
    private static final int TLV_MSG_TEXT = 0x0101;
    /** */
    private static final byte[] CLI_FUTURE = { 0x01, 0x01, 0x02 };

    /**
     * Constructor
     */
    public ClientICMB(int seqNum, String screenName, String msgContent) {
//Debug.println("[" + msgContent + "]");
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, seqNum);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_MESSAGING,
                                    SNACConstants.CLIENT_SEND_ICBM);

        this.screenName = screenName;

        TLV clientFutures = new TLV(TLV_CLIENT_FUTURE, CLI_FUTURE); // 0x0501

        // TODO 振り分け
        int encoding = 0x0002; // UTF-16BE

        byte[] msgTextBytes = null;
        switch (encoding) {
        case 0x0000:
            msgTextBytes = msgContent.getBytes(StandardCharsets.US_ASCII);
            break;
        case 0x0002:
            msgTextBytes = msgContent.getBytes(StandardCharsets.UTF_16BE);
            break;
        case 0x0003:
        default:
            msgTextBytes = msgContent.getBytes();
            break;
        }

        byte[] tlvData = new byte[msgTextBytes.length + 4];

        // format the byte array
        tlvData[0] = (byte) ((encoding >> 8) & 0xff);
        tlvData[1] = (byte) (encoding & 0xff);
        tlvData[2] = (byte) 0x00; // 0xff;
        tlvData[3] = (byte) 0x0b; // 0xff;

        System.arraycopy(msgTextBytes, 0, tlvData, 4, msgTextBytes.length);

        TLV tlvMsgText = new TLV(TLV_MSG_TEXT, tlvData); // 0x0101

        // combine the bytes for the two TLVs above into a single TLV
        int tlvLen = clientFutures.getTotalLength() +
                     tlvMsgText.getTotalLength();
        byte[] msgData = new byte[tlvLen];
        int p = 0;
        System.arraycopy(clientFutures.getBytes(), 0, msgData, p,
                         clientFutures.getTotalLength());
        p += clientFutures.getTotalLength();
        System.arraycopy(tlvMsgText.getBytes(), 0, msgData, p,
                         tlvMsgText.getTotalLength());

        addTLV(new TLV(TLV_MSG_DATA, msgData)); // 0x0002

        // add tlv 03 & 06 as empty TLVs
        byte[] empty = null;
        addTLV(new TLV(TLV_MSG_ACK, empty)); // 0x0003
        addTLV(new TLV(TLV_STORE_MSG_IF_OFFLINE, empty)); // 0x0006

//try { Debug.dump(getBytes()); } catch (IOException e) {}
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        os.write(MSGID_COOKIE);
        os.write(0x00);
        os.write(CHANNEL);
        os.write(screenName.length());
        os.write(ByteUtils.getBytes(screenName));
    }
}

/* */
