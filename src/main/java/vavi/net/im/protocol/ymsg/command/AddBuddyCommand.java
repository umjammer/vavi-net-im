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
 * ÉÅÉìÉoí«â¡.
 * 
 * @event 131
 *
 * @see YmsgEventName#buddyAdded
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class AddBuddyCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String who = null;
        String where = null;
        String me = null;
        int status = 0;

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 1:
                me = yd.getValue();
                break;
            case 7:
                who = yd.getValue();
                break;
            case 65:
                where = yd.getValue();
                break;
            case 66: // 0 = ok, 2 = already on serv list
                status = Integer.parseInt(yd.getValue());
                break;
            default:
Debug.println("key: " + yd.getKey() + ": " + yd.getValue());
                break;
            }
        }

        listeners.eventHappened(new IMEvent(this, YmsgEventName.buddyAdded, who, where));
    }
}

/* */
