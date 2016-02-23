/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
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
 * メッセージ受信.
 *
 * @event 6
 * @event 42 game
 *
 * @see YmsgEventName#systemMessageReceived
 * @see YmsgEventName#messageReceived
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class MessageCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {

        String msg = null;
        String from = null;
        int utf8 = 0;
        long tm = 0;

        int status = yp.getHeader().getStatus();

        if (status <= 1 || status == 5) {
            for (YmsgData yd : yp.getDataList()) {
                
                switch (yd.getKey()) {
                case 4:
                    from = yd.getValue();
                    break;
                case 97:
                    utf8 = Integer.parseInt(yd.getValue());
                    break;
                case 15:
                    tm = Long.parseLong(yd.getValue());
                    break;
                case 206:
                    tm = Long.parseLong(yd.getValue());
                    break;
                case 14:
                    msg = yd.getValue();
                    break;
                default:
Debug.println("key: " + yd.getKey() + ", " + yd.getValue());
                    break;
                }
            }
        } else if (status == 2) {
Debug.println("Your Yahoo! message did no get sent.");
        } else {
Debug.println("unknown packet status: " + status);
        }

        if ((status <= 1) || (status == 5)) {

            if (msg != null) {
                // アプリケーションへ通知
                listeners.eventHappened(new IMEvent(this, YmsgEventName.messageReceived, msg, from, new Date(tm)));
            } else if (status == 2) {
                listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, rb.getString("MESSAGE_CANT_SEND")));
            } else if (status == -1) {
                listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, msg));
            }
        }
    }
}

/* */
