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
import vavi.net.im.protocol.oscar.command.ContactListResponseCommand;
import vavi.net.im.protocol.oscar.command.ServerICBM;
import vavi.net.im.protocol.oscar.snac.SNACConstants;
import vavi.net.im.protocol.oscar.util.ByteUtils;


/**
 * ReconnectingHandler. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051117 nsano initial version <br>
 */
public class ClientReadyHandler extends Handler {

    /** */
    private static Logger log = Logger.getLogger(ClientReadyHandler.class.getName());

    /** */
    public void exec(Command command) throws IOException {
        if (command.getSubType() == SNACConstants.SNAC_SUBTYPE_ERROR) {
log.severe("error code=" + ByteUtils.toHexString(command.getSNACData()));
        } else if (command.getFamily() == SNACConstants.SNAC_FAMILY_MESSAGING) {
            switch (command.getSubType()) {
            case SNACConstants.SRV_ICBM_ERROR:

//log.error("ICMB error : ");
//ByteUtils.dump(cmd.getSNACData());
                break;
            case SNACConstants.CLIENT_RECV_ICBM:

                ServerICBM serverICBM = new ServerICBM(command);
                log.info("ICMB text=" + serverICBM.getMessageText());

                break;
            case SNACConstants.RESPONSE_ICBM_PARMINFO:

                byte[] snacData = command.getSNACData();
log.fine("ICBM parm info response" + ByteUtils.toHexString(snacData));

                // bit4 should be set to 1 for mini typing notifications
                long flags = ByteUtils.getUInt(snacData, 2);
                flags |= 8;

                listeners.eventHappened(new IMEvent(this, OscarEventName.clientReady, (int) flags, snacData));

                break;

            // test to see if this is how I receive typing notifications!
            // none received as of yet!!!
            case SNACConstants.SNAC_SUBTYPE_MINI_TYPING_NOTIFICATION:

                //System.out.println("Typing notification received!");
log.fine("Typing notification received");

                //TODO: figure out if typing started or stopped and
                //       call the appropriate listener method
                break;
            }
        } else if (command.getFamily() == SNACConstants.SNAC_FAMILY_BUDDY_LIST_MANAGEMENT) {
            switch (command.getSubType()) {
            case SNACConstants.SNAC_SUBTYPE_USER_ONLINE:
            case SNACConstants.SNAC_SUBTYPE_USER_OFFLINE:

                byte[] data = command.getSNACData();
                int nameLen = data[0];
                String name = ByteUtils.toString(data, 1, nameLen);
                String status = (command.getSubType() == SNACConstants.SNAC_SUBTYPE_USER_ONLINE) ? " online" : " offline";
log.info("User " + name + status);

                break;
            }
        } else if (command.getFamily() == SNACConstants.SNAC_FAMILY_SERVER_STORED_INFO) {
            switch (command.getSubType()) {
            case SNACConstants.CONTACT_LIST_RESPONSE:

                ContactListResponseCommand contactListResponse = new ContactListResponseCommand(command);
                listeners.eventHappened(new IMEvent(this, OscarEventName.buddyListReceived, contactListResponse.getGroups()));

                break;
            }
        } else if (command.getFamily() == SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS) {
            switch (command.getSubType()) {
            case SNACConstants.SRV_ONLINE_INFO:
log.info("Server Online Info: ");
                ByteUtils.dump(command.getSNACData());

                break;
            }
        } // todo - include handling for snac(04/0c) - ICBM send ACK
    }
}

/* */
