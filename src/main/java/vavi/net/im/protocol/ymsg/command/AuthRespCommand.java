/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;

import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.ymsg.YmsgEventName;
import vavi.net.im.protocol.ymsg.YmsgPacket;
import vavi.util.Debug;


/**
 * 認証の返事のレスポンス対応.
 *
 * @event 84
 * 
 * @see YmsgEventName#processLoginResponse
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class AuthRespCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        
        String id = yp.getDataValueByKey(0);
        String handle = yp.getDataValueByKey(1);
        String url = yp.getDataValueByKey(20);
        int loginStatus = yp.getDataValueByKey(66) != null ? Integer.parseInt(yp.getDataValueByKey(66)) : -1; // TODO
Debug.println("id: " + id);
Debug.println("handle: " + handle);
Debug.println("url: " + url);
Debug.println("loginStatus: " + loginStatus + ", (" + yp.getDataValueByKey(66) + ")");
        if (yp.getHeader().getStatus() == -1) {
            listeners.eventHappened(new IMEvent(this, YmsgEventName.processLoginResponse, loginStatus));
        }
    }
}

/* */
