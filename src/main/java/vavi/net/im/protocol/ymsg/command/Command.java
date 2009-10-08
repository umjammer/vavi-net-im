/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import vavi.net.im.event.IMSupport;
import vavi.net.im.protocol.ymsg.YmsgPacket;


/**
 * Command.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public abstract class Command {
    /** 文字列リソース */
    protected static final ResourceBundle rb = ResourceBundle.getBundle("vavi.net.im.protocol.ymsg.resources.ymsg", Locale.getDefault());

    /** */
    public abstract void exec(YmsgPacket yp) throws IOException;

    //----

    protected IMSupport listeners;

    /** IMListener を追加します． */
    public void setIMListeners(IMSupport listeners) {
        this.listeners = listeners;
    }
}

/* */
