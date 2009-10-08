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
 * メッセージ受信.
 *
 * @event 20
 *
 * @see YmsgEventName#systemMessageReceived
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051103 nsano initial version <br>
 */
public class SystemMessageCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {

        String msg = null;
        String me = null;

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 5:
                me = yd.getValue();
                break;
            case 14:
                msg = yd.getValue();
                break;
            default:
Debug.println("key: " + yd.getKey() + ", " + yd.getValue());
                break;
            }
        }
        
        if (msg != null) {
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, msg));
        }
    }
}

/* */
