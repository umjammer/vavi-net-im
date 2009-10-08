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
public class ClientReadyCommand extends Command {
    private static final byte[] FAMILY = {
        0x01, 0x13, 0x02, 0x03, 0x15, 0x04, 0x06, 0x09, 0x0a, 0x0b
    };

    private static final byte[] VERSION = {
        0x03, 0x02, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01
    };

    private static final byte[] DLLVERSION = {
        0x01, 0x10, 0x04, 0x7b
    };

    /**
     * Constructor
     */
    public ClientReadyCommand() {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, 10);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS,
                                    SNACConstants.CLIENT_READY);
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        for (int i = 0; i < FAMILY.length; i++) {
            os.write(ByteUtils.getUShort(FAMILY[i]));
            os.write(ByteUtils.getUShort(VERSION[i]));
            os.write(DLLVERSION);
        }
    }
}
