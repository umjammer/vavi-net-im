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
public class TypingNotificationCommand extends Command {
    private String buddyName;
    private int typing;
    private byte[] notificationIdCookie = {
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    private int notificationChannel = 1;

    /**
     * Constructor
     * 
     * @param seqNum sequence number for the FlapHeader
     * @param buddy Buddy to be ignored
     */
    public TypingNotificationCommand(int seqNum, Buddy buddy, int typingState) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, seqNum);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_MESSAGING,
                                    SNACConstants.SNAC_SUBTYPE_MINI_TYPING_NOTIFICATION);

        buddyName = buddy.getUsername();
        typing = typingState;
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        os.write(notificationIdCookie);
        os.write(0x00);
        os.write(notificationChannel);
        os.write(buddyName.length());
        os.write(ByteUtils.getBytes(buddyName));
        os.write(0x00);
        os.write(typing);
    }
}
