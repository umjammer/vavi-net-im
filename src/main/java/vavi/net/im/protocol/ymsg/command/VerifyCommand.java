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


/**
 * 認証のレスポンス対応. (for version 11)
 *
 * @event 76
 * 
 * @see YmsgEventName#requestAuthDirect
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class VerifyCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        
        // レスポンスする
        listeners.eventHappened(new IMEvent(this, YmsgEventName.requestAuthDirect));
    }
}

/* */
