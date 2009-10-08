/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.handler;

import java.io.IOException;
import java.util.logging.Logger;

import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.oscar.OscarEventName;
import vavi.net.im.protocol.oscar.command.Command;
import vavi.net.im.protocol.oscar.flap.FlapConstants;
import vavi.net.im.protocol.oscar.util.ByteUtils;
import vavi.net.im.protocol.oscar.util.TLV;
import vavi.net.im.protocol.oscar.util.TLVConstants;


/**
 * ReconnectingHandler. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051117 nsano initial version <br>
 */
public class ConnectingHandler extends Handler {

    /** */
    private static Logger log = Logger.getLogger(ConnectingHandler.class.getName());

    /** */
    public void exec(Command command) throws IOException {
        if (command.getChannel() == FlapConstants.FLAP_CHANNEL_DISCONNECT) {
            // get the server and cookie from the command tlv
            TLV tlvServer = command.getTLV(TLVConstants.TLV_TYPE_SERVER);
            TLV tlvCookie = command.getTLV(TLVConstants.TLV_TYPE_COOKIE);
            TLV tlvError = command.getTLV(TLVConstants.TLV_TYPE_ERROR_CODE);
    
            if (tlvServer != null && tlvCookie != null) {
                // hack the port to 22 now that work firewall blocks real port
                int port = 5190; // 22;
    
                String server = null;
                String serverCookie = tlvServer.getStringValue();
                int pos = serverCookie.indexOf(':');
    
                if (pos != -1) {
                    server = serverCookie.substring(0, pos);
                    port = Integer.parseInt(serverCookie.substring(pos + 1));
                } else {
                    server = serverCookie;
                }
    
                listeners.eventHappened(new IMEvent(this, OscarEventName.reconnect, server, port, tlvCookie.getValue()));
            } else if (tlvError != null) {
                // 00 18: too fast relogin, wait a minutes.
log.severe("Authentication error - error code=0x" + ByteUtils.toHexString(tlvError.getValue()));
            }
        }
    }
}

/* */
