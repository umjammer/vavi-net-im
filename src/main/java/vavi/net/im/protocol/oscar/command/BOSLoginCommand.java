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
import vavi.net.im.protocol.oscar.util.TLV;
import vavi.net.im.protocol.oscar.util.TLVConstants;


/**
 * @author mikem
 */
public class BOSLoginCommand extends Command {
    /** command data - appears after flap header and before TLVs */ 
    private static final byte[] ID_NUMBER = { 0x00, 0x00, 0x00, 0x01 };

    /**
     * Constructor
     * @param cookie authorization cookie from the server
     */
    public BOSLoginCommand(byte[] cookie) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_CONNECT, 2);
        addTLV(new TLV(TLVConstants.TLV_TYPE_COOKIE, cookie));
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        os.write(ID_NUMBER);
    }
}
