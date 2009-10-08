/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;


/**
 * YmsgMessageDigestProvider.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050315 nsano initial version <br>
 */
public final class YmsgMessageDigestProvider extends java.security.Provider {

    /** */
    public YmsgMessageDigestProvider() {
        super("YmsgMessageDigest", 1.0, "YmsgMessageDigestProvider implements Ymsg SHA-1 MessageDigest");
        put("MessageDigest.SHA-1", "vavi.net.im.protocol.ymsg.auth.YmsgSHA1");
    }
}

/* */
