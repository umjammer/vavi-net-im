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
import vavi.net.im.protocol.oscar.util.ByteUtils;
import vavi.net.im.protocol.oscar.util.TLV;
import vavi.net.im.protocol.oscar.util.TLVConstants;


/**
 * @author mikem
 */
public class LoginCommand extends Command {
    public static final String VERSION = "$Id: LoginCommand.java,v 1.1 2003/07/23 03:15:55 mikemil Exp $";
    private static final byte[] ID_NUMBER = { 0x00, 0x00, 0x00, 0x01 };
    private static final byte[] MAJOR_VER = { 0x00, 0x01 };
    private static final byte[] MINOR_VER = MAJOR_VER;

    public LoginCommand(String screenName, String password) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_CONNECT, 1);

        addTLV(new TLV(TLVConstants.TLV_TYPE_SCREEN_NAME, screenName));
        addTLV(new TLV(TLVConstants.TLV_TYPE_ROASTED_PASSWORD,
                       ByteUtils.roast(password)));
        addTLV(new TLV(TLVConstants.TLV_TYPE_CLIENT_ID_STRING,
                       "HAMSAM T400 Client"));
        addTLV(new TLV(TLVConstants.TLV_TYPE_CLIENT_ID, MAJOR_VER));
        addTLV(new TLV(TLVConstants.TLV_TYPE_CLIENT_MAJOR_VERSION, MAJOR_VER));
        addTLV(new TLV(TLVConstants.TLV_TYPE_CLIENT_MINOR_VERSION, MINOR_VER));
        addTLV(new TLV(TLVConstants.TLV_TYPE_CLIENT_LESSOR_VERSION, MINOR_VER));
        addTLV(new TLV(TLVConstants.TLV_TYPE_CLIENT_BUILD_NUMBER, MINOR_VER));
        addTLV(new TLV(TLVConstants.TLV_TYPE_DISTRIBUTION_NUMBER, ID_NUMBER));
        addTLV(new TLV(TLVConstants.TLV_TYPE_CLIENT_LANGUAGE, "en"));
        addTLV(new TLV(TLVConstants.TLV_TYPE_CLIENT_COUNTRY, "us"));
    }

//    /* (non-Javadoc)
//     * @see vavi.net.im.protocol.oscar.Command#getBytes()
//     */
//    public byte[] getBytes() {
//        byte[] bytes = new byte[10];
//        byte[] hdrBytes = flapHdr.getBytes();
//        System.arraycopy(hdrBytes, 0, bytes, 0, FlapHeader.FLAP_HDR_LENGTH);
//        System.arraycopy(ID_NUMBER, 0, bytes, FlapHeader.FLAP_HDR_LENGTH, ID_NUMBER.length);    
//        return bytes;
//    }
//    

    /**
     * Write the command data to the output stream (not including the FlapHeader or TLVs)
     * @see vavi.net.im.protocol.oscar.command.Command#writeCommandData(java.io.OutputStream)
     */
    public void writeCommandData(OutputStream os) throws IOException {
        os.write(ID_NUMBER);
    }
}
