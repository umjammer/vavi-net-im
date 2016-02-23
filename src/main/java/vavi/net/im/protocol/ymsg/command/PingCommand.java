/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
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
 * PING 受信.
 *
 * @event 18
 *
 * @see YmsgEventName#ping
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 061107 nsano initial version <br>
 */
public class PingCommand extends Command {

    /** TODO implement */
    public void exec(YmsgPacket yp) throws IOException {

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 143:
            case 144:
            default:
Debug.println("key: " + yd.getKey() + ", " + yd.getValue());
                break;
            }
        }
        
        listeners.eventHappened(new IMEvent(this, YmsgEventName.ping));
    }
}

/* */
