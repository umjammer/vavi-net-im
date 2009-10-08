/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;


/**
 * ChallengeResponse.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040801 nsano initial version <br>
 */
public interface ChallengeResponse {

    /**
     * @return 0: for key 6, 1: for key 96
     */
    String[] getResponses(String account, String password, String seed);
}

/* */
