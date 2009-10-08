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
 * @author mikem
 */
public class RequestServerRateCommand extends Command {
    /**
     * Constructor
     */
    public RequestServerRateCommand() {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, 4);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS,
                                    SNACConstants.REQ_SERVER_RATE_INFO);
    }

    /* (non-Javadoc)
     * @see vavi.net.im.protocol.oscar.command.Command#writeCommandData(java.io.OutputStream)
     */
    public void writeCommandData(OutputStream os) throws IOException {
        // nothing to do - empty snac data
    }
}
