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
public class AckServerRateInfo extends Command {
    /**
     * Constructor
     */
    public AckServerRateInfo() {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, 5);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS,
                                    SNACConstants.ACK_RATE_INFO);
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        for (int i = 1; i < 6; i++) {
            os.write(ByteUtils.getUShort(i));
        }
    }
}

/* */
