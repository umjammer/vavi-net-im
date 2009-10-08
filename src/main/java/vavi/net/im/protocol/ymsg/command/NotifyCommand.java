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
 * タイピング通知.
 *
 * @event 75
 *
 * @see YmsgEventName#typingStarted
 * @see YmsgEventName#typingStopped
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class NotifyCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String msg = null;
        String from = null;
        int stat = -1;
        String game = null;

        for (YmsgData yd : yp.getDataList()) {

            if (yd.getKey() == 4) {
                from = yd.getValue();
            } else if (yd.getKey() == 49) {
                msg = yd.getValue();
            } else if (yd.getKey() == 13) {
                stat = Integer.parseInt(yd.getValue());
            } else if (yd.getKey() == 14) {
                game = yd.getValue();
            }
        }

        if (msg == null) {
            return;
        }

        if (from != null && "TYPING".equals(msg)) {
            if (stat == 1) {
                listeners.eventHappened(new IMEvent(this, YmsgEventName.typingStarted, from));
            } else {
                listeners.eventHappened(new IMEvent(this, YmsgEventName.typingStopped, from));
            }
        } else if ("GAME".equals(msg)) {
            if (stat == 1) {
                // ゲーム開始？
            } else {
                // ゲーム終了？
            }
        }
    }
}

/* */
