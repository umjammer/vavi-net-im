/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.command;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import vavi.net.im.protocol.oscar.flap.FlapConstants;
import vavi.net.im.protocol.oscar.flap.FlapHeader;
import vavi.net.im.protocol.oscar.snac.SNACConstants;
import vavi.net.im.protocol.oscar.snac.SNACPacket;
import vavi.net.im.protocol.oscar.util.ByteUtils;
import vavi.net.im.protocol.oscar.util.TLV;
import vavi.net.im.protocol.oscar.util.TLVConstants;


/**
 * Revision 1.1  2003/09/20 03:23:45  mikemil
 *
 * @author mikem
 */
public class ClientSetStatusCommand extends Command {
    /** */
    private int modeFlags;
    /** */
    private int statusFlags;

    /** fields after the DC type in the Type 0x0C TLV */
    private static final byte[] unknownFields = new byte[] {
        0x00, 0x08, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x50,
        0x00, 0x00, 0x00, 0x03, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00
    };

    /**
     * 
     */
    public ClientSetStatusCommand(int seqNum, int statusflag) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, seqNum);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS,
                                    SNACConstants.CLIENT_STATUS);

        statusFlags |= statusflag;

        byte[] flags = ByteUtils.getUInt(modeFlags | statusFlags);
        addTLV(new TLV(TLVConstants.TLV_TYPE_USER_STATUS_FLAGS, flags));

        // allocate bytes for the DC Info block
        byte[] dcinfo = new byte[0x25];

        InetAddress addr;

        try {
            addr = InetAddress.getLocalHost();

            byte[] ipAddr = addr.getAddress();
            System.arraycopy(ipAddr, 0, dcinfo, 0, ipAddr.length);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
        }

        dcinfo[8] = 0x04; // dc_type_normal
        System.arraycopy(unknownFields, 0, dcinfo, 9, unknownFields.length);

        addTLV(new TLV(TLVConstants.TLV_TYPE_DC_INFO, dcinfo));
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
    }
}
