/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.handler;

import java.io.IOException;

import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.oscar.OscarEventName;
import vavi.net.im.protocol.oscar.command.Command;
import vavi.net.im.protocol.oscar.snac.SNACConstants;


/**
 * ReconnectingHandler. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051117 nsano initial version <br>
 */
public class ProtocolNegotiationHandler extends Handler {

    /** */
    public void exec(Command command) throws IOException {
        if (command.getFamily() == SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS) {
            if (command.getSubType() == SNACConstants.SERVER_RATE_INFO) {
                listeners.eventHappened(new IMEvent(this, OscarEventName.protocolNegotiation));
            }
        }
    }
}

/* */
