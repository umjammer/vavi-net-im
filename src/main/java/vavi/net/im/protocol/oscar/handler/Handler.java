/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.handler;

import java.io.IOException;

import vavi.net.im.event.IMSupport;
import vavi.net.im.protocol.oscar.command.Command;


/**
 * Handler. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051117 nsano initial version <br>
 */
public abstract class Handler {

    /** */
    public abstract void exec(Command command) throws IOException;

    //----

    protected IMSupport listeners;

    /** IMListener を追加します． */
    public void setIMListeners(IMSupport listeners) {
        this.listeners = listeners;
    }
}

/* */
