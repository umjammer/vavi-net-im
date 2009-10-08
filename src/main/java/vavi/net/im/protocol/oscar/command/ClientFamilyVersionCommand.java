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
import vavi.net.im.protocol.oscar.util.ByteUtils;


/**
 * @author mikem
 */
public class ClientFamilyVersionCommand extends Command {
    private static final short[] CLIENT_FAMILY = {
        0x0001, 0x0002, 0x0003, 0x0004, 0x0006, 0x0009, 0x000a, 0x000b, 0x0013, 0x0015
    };

    private static final short[] CLIENT_VER = {
        0x0003, 0x0001, 0x0001, 0x0001, 0x0001, 0x0001, 0x0001, 0x0001, 0x0002, 0x0001
    };

    /**
     * Constructor
     */
    public ClientFamilyVersionCommand() {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, 3);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS,
                                    SNACConstants.REQ_SRV_SVC_VERSIONS);
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        // write out the family/version pairs
        for (int i = 0; i < CLIENT_FAMILY.length; i++) {
            os.write(ByteUtils.getUShort(CLIENT_FAMILY[i]));
            os.write(ByteUtils.getUShort(CLIENT_VER[i]));
        }
    }
}
