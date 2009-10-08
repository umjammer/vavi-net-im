/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;
import java.text.MessageFormat;

import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.ymsg.YmsgData;
import vavi.net.im.protocol.ymsg.YmsgEventName;
import vavi.net.im.protocol.ymsg.YmsgPacket;


/**
 * コンタクト.
 *
 * @event 15
 *
 * @see YmsgEventName#systemMessageReceived
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class ContactCommand extends StatusCommand {

    /** */
    public void exec(YmsgPacket yp) throws IOException {

        int status = yp.getHeader().getStatus();
        switch (status) {
        case 1:
            super.exec(yp);
            return;
        case 3:
            buddyAddedUs(yp);
            break;
        case 7:
            buddyDeniedOurAddRequest(yp);
            break;
        default:
            break;
        }
    }

    /** */
    private void buddyAddedUs(YmsgPacket yp) throws IOException {
        String id = null;
        String who = null;
        String msg = null;

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 1:
                id = yd.getValue();
                break;
            case 3:
                who = yd.getValue();
                break;
            case 15: // time, for when they add us and we're offline
                break;
            case 14:
                msg = yd.getValue();
                break;
            default:
                break;
            }
        }

        if (id != null && msg != null) {
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, msg));
        }
    }

    /** */
    private void buddyDeniedOurAddRequest(YmsgPacket yp) throws IOException {
        String who = null;
        String msg = null;

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 3:
                who = yd.getValue();
                break;
            case 14:
                msg = yd.getValue();
                break;
            default:
                break;
            }
        }

        if (msg != null) {
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, MessageFormat.format(rb.getString("NOTIFY_DENIED"), who)));
        } else {
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, msg));
        }
    }
}

/* */
