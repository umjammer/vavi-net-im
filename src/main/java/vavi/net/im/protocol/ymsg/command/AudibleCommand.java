/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;
import java.util.Date;

import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.ymsg.YmsgData;
import vavi.net.im.protocol.ymsg.YmsgEventName;
import vavi.net.im.protocol.ymsg.YmsgPacket;
import vavi.util.Debug;


/**
 * AudibleCommand.
 *
 * @event 208
 *
 * @see YmsgEventName#messageReceived
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051103 nsano initial version <br>
 */
public class AudibleCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {

        String who = null;
        String msg = null;

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 4:
                who = yd.getValue();
                break;
            case 5: // us
                break;
            case 230: // the audible, in foo.bar.baz format
                break;
            case 231: // the text of the audible
                msg = yd.getValue();
                break;
            case 232: // weird number (md5 hash?), like 8ebab9094156135f5dcbaccbeee662a5c5fd1420
                break;
            default:
Debug.println("key: " + yd.getKey() + ", " + yd.getValue());
                break;
            }
        }
        
        if (who != null && msg != null) {
            listeners.eventHappened(new IMEvent(this, YmsgEventName.messageReceived, msg, who, new Date()));
        }
    }
}

/* */
