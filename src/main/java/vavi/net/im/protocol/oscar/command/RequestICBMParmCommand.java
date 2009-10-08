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
public class RequestICBMParmCommand extends Command {
    /**
     * Constructor
     *
     * @param seqNum sequence number for the FlapHeader
     */
    public RequestICBMParmCommand(int seqNum) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, seqNum);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_MESSAGING,
                                    SNACConstants.REQUEST_ICBM_PARMIFO);
    }

    /* (non-Javadoc)
     * @see vavi.net.im.protocol.oscar.command.Command#writeCommandData(java.io.OutputStream)
     */
    public void writeCommandData(OutputStream os) throws IOException {
    }
}
