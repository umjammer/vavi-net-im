/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.command;

import java.io.IOException;
import java.io.OutputStream;

import vavi.net.im.Buddy;
import vavi.net.im.protocol.oscar.flap.FlapConstants;
import vavi.net.im.protocol.oscar.flap.FlapHeader;
import vavi.net.im.protocol.oscar.snac.SNACConstants;
import vavi.net.im.protocol.oscar.snac.SNACPacket;
import vavi.net.im.protocol.oscar.util.ByteUtils;


/**
 * @author mike
 */
public class UnignoreBuddyCommand extends Command {
    private String buddyName;

    /**
     * Constructor
     *
     * @param seqNum sequence number for the FlapHeader
     * @param buddy Buddy to be ignored
     */
    public UnignoreBuddyCommand(int seqNum, Buddy buddy) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, seqNum);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_PRIVACY_MANAGEMENT,
                                    SNACConstants.SNAC_SUBTYPE_DEL_INVISIBLE);

        buddyName = buddy.getUsername();
    }

    /* (non-Javadoc)
     * @see vavi.net.im.protocol.oscar.command.Command#writeCommandData(java.io.OutputStream)
     */
    public void writeCommandData(OutputStream os) throws IOException {
        os.write(buddyName.length());
        os.write(ByteUtils.getBytes(buddyName));
    }
}
