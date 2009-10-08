/*
 * Copyright (c) 2009 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.util.Properties;

import vavi.net.im.protocol.Protocol;
import vavi.net.im.protocol.xmpp.JabberProtocol;


/**
 * Test1. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/06/25 nsano initial version <br>
 */
public class Test1 {

    /** */
    public static void main(String[] args) throws Exception {
        String username = "sano-n"; // args[0];
        String password = "12345963"; // args[1];
        String server = "jabber.jp"; // args[2];

        Protocol protocol = new JabberProtocol();
        Properties props = new Properties();
        props.setProperty("username", username);
        props.setProperty("password", password);
        props.setProperty("server", server);
        props.put("isUsessl", false);
        protocol.connect(props);
        protocol.disconnect();
    }
}

/* */
