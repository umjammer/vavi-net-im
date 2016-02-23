/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;
import java.util.StringTokenizer;

import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.ymsg.YmsgData;
import vavi.net.im.protocol.ymsg.YmsgEventName;
import vavi.net.im.protocol.ymsg.YmsgPacket;
import vavi.util.Debug;


/**
 * リスト.
 *
 * @event 85
 *
 * @see YmsgEventName#groupAdded
 * @see YmsgEventName#buddyAdded
 * @see YmsgEventName#buddyIgnored
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class ListCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String buddies = "";
        String ignores = "";
        
        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 87: // regular member
                buddies += yd.getValue();
                break;
            case 88: // ignore member
                ignores += yd.getValue();
                break;
            case 59: // cookie
                processCookie(yd.getValue());
                break;
            default:
Debug.println("key: " + yd.getKey() + ": " + yd.getValue());
            	break;
            }
        }

//Debug.println("buddies: " + buddies);
        // buddy list
        StringTokenizer groups = new StringTokenizer(buddies, "\n");

        while (groups.hasMoreTokens()) {
            String p = groups.nextToken();
            StringTokenizer gm = new StringTokenizer(p, ":");

            if (gm.countTokens() > 1) {
                String groupName = gm.nextToken();
Debug.println("groupName: " + groupName);
                listeners.eventHappened(new IMEvent(this, YmsgEventName.groupAdded, groupName));

                StringTokenizer members = new StringTokenizer(gm.nextToken(), ",");

                while (members.hasMoreTokens()) {
                    String buddyName = members.nextToken();
Debug.println("buddyName: " + buddyName);
                    listeners.eventHappened(new IMEvent(this, YmsgEventName.buddyAdded, buddyName, groupName));
                }
            }
        }

//Debug.println("ignores: " + ignores);
        // ignore list
        StringTokenizer st = new StringTokenizer(ignores, ",");
        while (st.hasMoreTokens()) {
            String buddyName = st.nextToken();
Debug.println("ignore buddyName: " + buddyName);
            listeners.eventHappened(new IMEvent(this, YmsgEventName.buddyIgnored, buddyName));
        }
    }

    /** */
    private void processCookie(String rawCookie) {
        String cookie = getCookie(rawCookie);
Debug.println("cookie: " + cookie);
        switch (rawCookie.charAt(0)) {
        case 'Y':
            break;
        case 'T':
            break;
        }
    }

    /** */
    private String getCookie(String rawCookie) {

        if (rawCookie.length() < 2) {
            return null;
        }

        String cookie = rawCookie.substring(2);
        int cookieEnd = cookie.indexOf(';');
        if (cookieEnd >= 0) {
            cookie.substring(0, cookieEnd);
        }

        return cookie;
    }
}

/* */
