/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;

import vavi.net.im.protocol.ymsg.YmsgData;
import vavi.net.im.protocol.ymsg.YmsgPacket;
import vavi.util.Debug;


/**
 * P2PCommand.
 *
 * @event 77
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051103 nsano initial version <br>
 */
public class P2PCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {

        String who = null;
        String base64 = null;

        for (YmsgData yd : yp.getDataList()) {

            switch (yd.getKey()) {
            case 5: // our identity
                break;
            case 4:
                who = yd.getValue();
                break;
            case 1: // who again, the master identity this time?
                break;
            case 12:
                base64 = yd.getValue();
                break;
//            case 61: //          Value: 0
//            case 2: //   Value:
//            case 13: //          Value: 0
//            case 49: //          Value: PEERTOPEER
//            case 140: //         Value: 1
//            case 11: //          Value: -1786225828
            default:
Debug.println("key: " + yd.getKey() + ", " + yd.getValue());
                break;
            }
        }
        
        if (base64 != null) {
        }
    }
}

/* */
