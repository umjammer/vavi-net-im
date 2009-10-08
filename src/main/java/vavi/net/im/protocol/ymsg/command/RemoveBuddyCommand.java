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


/**
 * ÉÅÉìÉoçÌèú.
 *
 * @event 132
 * 
 * @see YmsgEventName#buddyDeleted
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class RemoveBuddyCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String who = null;
        String where = null;
        String me = null;
        int status = 0;

        for (YmsgData yd : yp.getDataList()) {

            if (yd.getKey() == 1) {
                me = yd.getValue();
            } else if (yd.getKey() == 7) {
                who = yd.getValue();
            } else if (yd.getKey() == 65) {
                where = yd.getValue();
            } else if (yd.getKey() == 66) {
                status = Integer.parseInt(yd.getValue());
            }
        }

        listeners.eventHappened(new IMEvent(this, YmsgEventName.buddyDeleted, who, where));
    }
}

/* */
