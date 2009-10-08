/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;

import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.ymsg.YmsgData;
import vavi.net.im.protocol.ymsg.YmsgEventName;
import vavi.net.im.protocol.ymsg.YmsgPacket;
import vavi.util.Debug;


/**
 * ‹ÖŽ~’Ê’m.
 * 
 * @event 133
 *
 * @see YmsgEventName#buddyIgnored
 * @see YmsgEventName#buddyUnignored
 * @see YmsgEventName#systemMessageReceived
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class IgnoreCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String who = null;
        String me = null;
        int status = 0;
        int unIgnore = 0;

        for (YmsgData yd : yp.getDataList()) {

            if (yd.getKey() == 0) {
                who = yd.getValue();
            } else if (yd.getKey() == 1) {
                me = yd.getValue();
            } else if (yd.getKey() == 13) {
                unIgnore = Integer.parseInt(yd.getValue());
            } else if (yd.getKey() == 66) {
                status = Integer.parseInt(yd.getValue());
            }
        }

        switch (status) {
        case 0: // TODO
            if (unIgnore == 1) {
                listeners.eventHappened(new IMEvent(this, YmsgEventName.buddyIgnored, who));
            } else if (unIgnore == 2) {
                listeners.eventHappened(new IMEvent(this, YmsgEventName.buddyUnignored, who));
            }
            break;
        case 2:
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, rb.getString("BLOCK_ALREADY")));
            break;
        case 3:
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, rb.getString("BLOCK_CANT_REMOVE")));
            break;
        case 12: // TODO add confirmation
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, rb.getString("BLOCK_CANT_MOVE_BUDDY_TO_BLOCKLIST")));
            break;
        default:
Debug.println("unknown login status: " + status);
            break;
        }
    }
}

/* */
