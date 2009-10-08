/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar;

import vavi.net.im.event.IMEvent.Name;


/**
 * Oscar のイベント Name です．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public enum OscarEventName implements Name {

    /**
     * @args Map<String, List<String>>
     */
    buddyListReceived,
    /**
     * @args String, int, byte[]
     */
    reconnect,
    /**
     * @args void
     */
    bosConnecting,
    /**
     * @args void
     */
    bosConnected,
    /**
     * @args void
     */
    ackConnectRates,
    /**
     * @args void
     */
    bosServiceList,
    /**
     * @args void
     */
    protocolNegotiation,
    /**
     * @args int, byte[] 
     */
    clientReady;
}

/* */
