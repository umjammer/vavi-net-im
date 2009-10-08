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
 * @author mike
 */
public class KeyRequestCommand extends Command {
    private String screenName;

    /**
     *
     */
    public KeyRequestCommand(String screenName) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_CONNECT, 1);
        snacPacket = new SNACPacket(SNACConstants.SNAC_FAMILY_AUTHENTICATION,
                                    SNACConstants.SNAC_SUBTYPE_KEY_REQUEST);
        this.screenName = screenName;
        addTLV(new TLV(TLVConstants.TLV_TYPE_SCREEN_NAME, screenName));
    }

    /**
     *
     * @see vavi.net.im.protocol.oscar.command.Command#writeCommandData(java.io.OutputStream)
     */
    public void writeCommandData(OutputStream os) throws IOException {
        //todo write snacpacket data - TLVs handled by parent class
    }
}
