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
import vavi.net.im.protocol.oscar.command.ContactListResponseCommand;
import vavi.net.im.protocol.oscar.snac.SNACConstants;


/**
 * AckConnectRatesHandler. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051117 nsano initial version <br>
 */
public class AckConnectRatesHandler extends Handler {

    /** */
    public void exec(Command command) throws IOException {
        if (command.getFamily() == SNACConstants.SNAC_FAMILY_SERVER_STORED_INFO) {
            switch (command.getSubType()) {
            case SNACConstants.CONTACT_LIST_RESPONSE:

                ContactListResponseCommand contactListResponse = new ContactListResponseCommand(command);
                listeners.eventHappened(new IMEvent(this, OscarEventName.buddyListReceived, contactListResponse.getGroups()));

                listeners.eventHappened(new IMEvent(this, OscarEventName.ackConnectRates));

                break;
            }
        }
    }
}

/* */
