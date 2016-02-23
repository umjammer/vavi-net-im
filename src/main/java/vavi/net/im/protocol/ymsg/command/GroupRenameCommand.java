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
import vavi.util.Debug;


/**
 * グループ名変更.
 * 
 * @event 137
 *
 * @see YmsgEventName#groupNameChanged
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class GroupRenameCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String me = null;
        String oldName = null;
        String newName = null;
        int status = 0;

        for (YmsgData yd : yp.getDataList()) {

            if (yd.getKey() == 1) {
                me = yd.getValue();
            } else if (yd.getKey() == 65) {
                newName = yd.getValue();
            } else if (yd.getKey() == 67) {
                oldName = yd.getValue();
            } else if (yd.getKey() == 66) {
                status = Integer.parseInt(yd.getValue());
            }
        }

        switch (status) {
        case 0:
            listeners.eventHappened(new IMEvent(this, YmsgEventName.groupNameChanged, oldName, newName));
            break;
        default:
Debug.println("YMSG: processGroupRename: me=" + me + ", oldName=" + oldName + ", newName=" + newName + ", status=" + status);
            break;
        }
    }
}

/* */
