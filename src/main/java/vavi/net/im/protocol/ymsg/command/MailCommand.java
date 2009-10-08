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
import vavi.util.Debug;


/**
 * ÉÅÅ[ÉãíÖêM.
 *
 * @event 11
 *
 * @see YmsgEventName#systemMessageReceived
 * @see YmsgEventName#incUnreadMailCount
 * @see YmsgEventName#setUnreadMailCount
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class MailCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String who = null;
        String email = null;
        String subj = null;
        int count = -1;

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 9:
                count = Integer.parseInt(yd.getValue());
                break;
            case 43:
                who = yd.getValue();
                break;
            case 42:
                email = yd.getValue();
                break;
            case 18:
                subj = yd.getValue();
                break;
            default:
Debug.println("key: " + yd.getKey() + ": " + yd.getValue());
                break;
            }
        }

        if (who != null && (who.length() > 0) &&
            email != null && (email.length() > 0) &&
            subj != null && (subj.length() > 0)) {

            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, MessageFormat.format(rb.getString("MAIL_RECEIVED"), "TODO", who, email, subj)));
            listeners.eventHappened(new IMEvent(this, YmsgEventName.incUnreadMailCount));
        } else if (count >= 0) {
//    	    String mesStr = MessageFormat.format(rb.getString("MAIL_STATUS_NOTICE"), username, count);
//    	    systemBuddy.setStatus("http://mail.yahoo.co.jp");
            listeners.eventHappened(new IMEvent(this, YmsgEventName.setUnreadMailCount, count));
        } else {
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, MessageFormat.format(rb.getString("MAIL_JUST_NOTICE"), "TODO")));
        }
    }
}

/* */
