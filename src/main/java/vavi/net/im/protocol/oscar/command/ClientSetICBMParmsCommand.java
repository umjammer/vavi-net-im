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


/**
 * @author mike
 */
public class ClientSetICBMParmsCommand extends Command {
    /**
     * Constructor
     *
     * @param seqNum sequence number for the FlapHeader
     */
    public ClientSetICBMParmsCommand(int seqNum, int flags, byte[] snacData) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, seqNum);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_MESSAGING,
                                    SNACConstants.SET_ICBM_PARMS);
        snacData[5] = (byte) flags;

        // set max msg len to 8000 (ox1f40)
        snacData[6] = 0x1f;
        snacData[7] = 0x40;
        snacPacket.setSNACData(snacData);
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        os.write(this.getSNACData());
    }
}
