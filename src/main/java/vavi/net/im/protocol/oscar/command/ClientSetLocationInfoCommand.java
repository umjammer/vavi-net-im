/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.command;

import java.io.IOException;
import java.io.OutputStream;

import vavi.net.im.protocol.oscar.flap.FlapConstants;
import vavi.net.im.protocol.oscar.flap.FlapHeader;
import vavi.net.im.protocol.oscar.snac.SNACConstants;
import vavi.net.im.protocol.oscar.snac.SNACPacket;
import vavi.net.im.protocol.oscar.util.TLV;
import vavi.net.im.protocol.oscar.util.TLVConstants;


/**
 * @author mikem
 */
public class ClientSetLocationInfoCommand extends Command {
    /** */
    private static final byte[] CAPABILITIES = {
        0x09, 0x46, 0x13, 0x4B, 0x4C, 0x7F, 0x11, (byte) 0xD1,
        (byte) 0x82, 0x22, 0x44, 0x45, 0x53, 0x54, 0x00, 0x00,

        0x09, 0x46, 0x13, 0x45, 0x4C, 0x7F, 0x11, (byte) 0xD1,
        (byte) 0x82, 0x22, 0x44, 0x45, 0x53, 0x54, 0x00, 0x00
    };
    /** */
    private static final String AWAY_FMT = "text/x-aolrtf; charset=us-ascii";

    /** */
    public ClientSetLocationInfoCommand() {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, 6);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_LOCATION_SERVICES,
                                    SNACConstants.CLIENT_SET_LOCATION_INFO);

        addTLV(new TLV(0x05, CAPABILITIES));
    }

    /** */
    public ClientSetLocationInfoCommand(int seqNum, int statusflag) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, seqNum);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_LOCATION_SERVICES,
                                    SNACConstants.CLIENT_SET_LOCATION_INFO);

        addTLV(new TLV(TLVConstants.TLV_TYPE_AWAY_FMT, AWAY_FMT));

        String away = null;

        switch (statusflag) {
        case 0x0000:
            away = "";
            break;
        case 0x0001:
            away = "I am away!";
            break;
        case 0x0002:
            away = "Do Not Disturb";
            break;
        case 0x0004:
            away = "Not Available";
            break;
        case 0x0010:
            away = "Busy";
            break;
        default:
            away = "";
            break;
        }

        addTLV(new TLV(TLVConstants.TLV_TYPE_AWAY, away));
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        // no snac data for this, other than the TLV above!
    }
}
