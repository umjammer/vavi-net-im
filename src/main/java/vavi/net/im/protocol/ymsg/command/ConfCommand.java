/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import vavi.net.im.event.CallableIMEvent;
import vavi.net.im.event.IMEvent;
import vavi.net.im.event.IMEvent.IMEventName;
import vavi.net.im.protocol.ymsg.YmsgData;
import vavi.net.im.protocol.ymsg.YmsgEventName;
import vavi.net.im.protocol.ymsg.YmsgPacket;
import vavi.net.im.protocol.ymsg.YmsgPacketHeader.Event;
import vavi.util.Debug;
import vavi.util.StringUtil;


/**
 * カンファレンス.
 *
 * @event 24
 * @event 28
 * @event 26
 * @event 25
 * @event 27
 * @event 29
 *
 * @see YmsgEventName#confInvited
 * @see YmsgEventName#acceptBuddiesToGroupSession
 * @see YmsgEventName#declineBuddiesToGroupSession
 * @see YmsgEventName#confLogon
 * @see YmsgEventName#confLogoff
 * @see YmsgEventName#confMessageReceived
 * @see YmsgEventName#systemMessageReceived
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class ConfCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String msg = null;
        String host = null;
        String who = null;
        String room = null;
        String id = null;
        List<String> members = new ArrayList<>();

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 1: // my id
                id = yd.getValue();
                break;
            case 3: // message sender
                who = yd.getValue();
                break;
            case 13:
                break;
            case 14: // decline/conf message
                msg = yd.getValue();
                break;
            case 50:
                host = yd.getValue();
                break;
            case 52: // invite
                who = yd.getValue();
                members.add(who);
                break;
            case 53: // login
                who = yd.getValue();
                break;
            case 54: // decline
                who = yd.getValue();
                break;
            case 56: // logoff
                who = yd.getValue();
                break;
            case 57:
                room = yd.getValue();
                break;
            case 58: // join message
                msg = yd.getValue();
                break;
            default:
Debug.println("key: " + yd.getKey() + ": " + yd.getValue());
                break;
            }
        }

        if (room == null) {
            return;
        }

        if (host != null) {
            members.add(host);
        }

        int connectionId = yp.getHeader().getConnectionId();
        Event event = yp.getHeader().getEvent();
        String[] buddies = members.toArray(new String[members.size()]);
        
        // invite, decline, join, left, message . status == 1
        switch (event) {
        case CONFINVITE:
        case CONFADDINVITE: {
            String dialogMessage = MessageFormat.format(rb.getString("CONF_INVITATION"), host, room);
            CallableIMEvent imEvent = new CallableIMEvent(this, IMEventName.doConfirmation, dialogMessage);
            listeners.eventHappened(imEvent);
            if ((Boolean) imEvent.getResult()) {
                // Ymsg 側の処置
                listeners.eventHappened(new IMEvent(this, YmsgEventName.acceptBuddiesToGroupSession, connectionId, buddies, room, msg));
                // client 側の処置
                for (String buddyName : buddies) {
                    if (buddyName.equals(id)) {
                        continue;
                    }
                    listeners.eventHappened(new IMEvent(this, YmsgEventName.confInvited, buddyName));
                }
            } else {
                listeners.eventHappened(new IMEvent(this, YmsgEventName.declineBuddiesToGroupSession, connectionId, buddies, room));
            }
          } break;
        case CONFDECLINE:
            String message = MessageFormat.format(rb.getString("CONF_NOTIFY_DECLINED"), who);
            listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, message));
            break;
        case CONFLOGON:
            listeners.eventHappened(new IMEvent(this, YmsgEventName.confLogon, who, room));
            break;
        case CONFLOGOFF:
            listeners.eventHappened(new IMEvent(this, YmsgEventName.confLogoff, who, room));
            break;
        case CONFMSG:
            if (who != null) {
                // 受信
                listeners.eventHappened(new IMEvent(this, YmsgEventName.confMessageReceived, who, room, msg));
            }
            break;
        default:
Debug.println("YMSG: processConf: unhandled service 0x" + StringUtil.toHex2(event.getValue()));
            break;
        }
    }
}

/* */
