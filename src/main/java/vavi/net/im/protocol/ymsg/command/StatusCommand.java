/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;
import java.util.Date;

import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.ymsg.YmsgConnection;
import vavi.net.im.protocol.ymsg.YmsgData;
import vavi.net.im.protocol.ymsg.YmsgEventName;
import vavi.net.im.protocol.ymsg.YmsgPacket;
import vavi.net.im.protocol.ymsg.YmsgPacketHeader.Event;
import vavi.net.im.protocol.ymsg.YmsgPacketHeader.Status;
import vavi.util.Debug;


/**
 * ステータス通知.
 *
 * @event 1
 * @event 2
 * @event 3
 * @event 4
 * @event 5
 * @event 7
 * @event 8
 * @event 9
 *
 * @see YmsgEventName#statusChanged
 * @see YmsgEventName#customStatusChanged
 * @see YmsgEventName#systemMessageReceived
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class StatusCommand extends Command {

    private class YmsgFriend {
        /** */
        public YmsgFriend(String name) {
            this.name = name;
        }
        String name;
        Status status;
        int away;
        long idle;
        public int sms;
    }

    /** */
    public void exec(YmsgPacket yp) throws IOException {

        YmsgFriend friend = null;
        
        if (yp.getHeader().getEvent() == Event.LOGOFF && yp.getHeader().getStatus() == -1) {
            listeners.eventHappened(new IMEvent(this, YmsgEventName.processLoginResponse, YmsgConnection.STATUS_LOGIN_DUPL));
            return;
        }

        String msg = null;
        
        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 0: // we won't actually do anything with this
                break;
            case 1: // we don't get the full buddy list here.
//                if (connection.getStatus().equals(Status.OFFLINE)) {
                    // TODO picture upload
//                }
                break;
            case 8: // how many online buddies we have
                break;
            case 7: // the current buddy
                String name = yd.getValue();
                friend = new YmsgFriend(name);
                break;
            case 10: // state
                if (friend == null) {
                    break;
                }
                friend.status = Status.valueOf(Integer.parseInt(yd.getValue()));
                if (friend.status.getValue() >= Status.BRB.getValue() && friend.status.getValue() <= Status.STEPPEDOUT.getValue()) {
                    friend.away = 1;
                } else {
                    friend.away = 0;
                }
                if (friend.status == Status.IDLE) {
                    friend.idle = new Date().getTime();
                } else {
                    friend.idle = 0;
                }
                if (friend.status != Status.CUSTOM) {
                    listeners.eventHappened(new IMEvent(this, YmsgEventName.statusChanged, friend.name, friend.status));
                }
                friend.sms = 0;
                break;
            case 19: // custom message
                msg = yd.getValue();
                listeners.eventHappened(new IMEvent(this, YmsgEventName.customStatusChanged, friend.name, msg));
                break;
            case 11: // this is the buddy's session id
                break;
            case 17: // in chat?
                break;
            case 47: // is custom status away or not? 2=idle
                if (friend == null) {
                    break;
                }
                // I have no idea what it means when this is
                // set when someone's available, but it doesn't
                // mean idle.
                if (friend.status == Status.AVAILABLE) {
                    break;
                }
                friend.away = Integer.parseInt(yd.getValue());
                if (friend.away == 2) {
                    friend.idle = new Date().getTime();
                }
                break;
            case 138: // either we're not idle, or we are but won't say how long
                if (friend == null) {
                    break;
                }
                if (friend.idle != 0) {
                    friend.idle = -1;
                }
                break;
            case 137: // usually idle time in seconds, sometimes login time
                if (friend == null) {
                    break;
                }
                if (friend.status != Status.AVAILABLE) {
                    friend.idle = -1;
                }
                break;
            case 13: // bitmask, bit 0 = pager, bit 1 = chat, bit 2 = game
                Event event = yp.getHeader().getEvent();
                int value = Integer.parseInt(yd.getValue());
                if (event == Event.LOGOFF || value == 0) {
                    listeners.eventHappened(new IMEvent(this, YmsgEventName.statusChanged, friend.name, Status.OFFLINE));
                    return;
                } else if (friend.status == Status.CUSTOM && msg != null) {
                    if (friend.away != 0) {
                        listeners.eventHappened(new IMEvent(this, YmsgEventName.statusChanged, friend.name, Status.IDLE));
                    } else {
                        listeners.eventHappened(new IMEvent(this, YmsgEventName.statusChanged, friend.name, Status.AVAILABLE));
                    }
                    listeners.eventHappened(new IMEvent(this, YmsgEventName.customStatusChanged, friend.name, msg));
                } else {
                    listeners.eventHappened(new IMEvent(this, YmsgEventName.statusChanged, friend.name, Status.AVAILABLE));
                }
                break;
            case 197: // Avatars 
                break;
            case 192: // Pictures, aka Buddy Icons, checksum
//                int cksum = Integer.parseInt(yd.getValue());
                break;
            case 60: // SMS
                break;
            case 16: // Custom error message
                listeners.eventHappened(new IMEvent(this, YmsgEventName.systemMessageReceived, "YMSG: " + yd.getValue()));
                break;
            default:
Debug.println("key: " + yd.getKey() + ": " + yd.getValue());
                break;
            }
        }
    }
}

/* */
