/*
 * Written by Kevin Kurtz and Scott Werndorfer
 *
 * Copyright(c) 2003 Cerulean Studios
 */

package vavi.net.im.protocol.ymsg.auth;

import vavi.util.Debug;


/**
 * ChallengeResponseTable.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 061105 nsano initial version <br>
 */
class ChallengeResponseTable {

    /** */
    private static final int NUM_TYPE_THREES = 105;
    /** */
    private static final int NUM_TYPE_FOURS = 46;
    /** */
    private static final int NUM_TYPE_FIVES = 37;

    /*
     * We've defined the Yahoo authentication functions as having types 1-5; all
     * take either 1 or 2 arguments.
     */
    private static final int type = 0;
    private static final int var1 = 1;
    private static final int var2 = 2;

    /**
     * 
     */
    private static long yahoo_auth_fibonacci(long challenge, int divisor, int outer_loop, int inner_loop) {
        long hash = (challenge & 0xff) * 0x9e3779b1L;

        hash ^= (challenge >> 0x08) & 0xff;
        hash *= 0x9e3779b1L;
        hash ^= (challenge >> 0x10) & 0xff;
        hash *= 0x9e3779b1L;
        hash ^= (challenge >> 0x18) & 0xff;
        hash *= 0x9e3779b1L;

        if (outer_loop > 1) {

            hash = ((((hash ^ (hash >> 0x8)) >> 0x10) ^ hash) ^ (hash >> 0x8)) & 0xff;

            int remainder = (int) (hash % divisor);

            outer_loop--;
            challenge *= 0x10dcd;
            challenge &= 0xffffffffL;
//System.out.println("LOOK3:" + challenge + ":" + hash);

            try {
                int[] ft = FunctionList.values[inner_loop][remainder];

                switch (ft[type]) {
                case 0: // 
                    return challenge;
                case 1: // 
                    return yahoo_auth_typeone(challenge, divisor, outer_loop, inner_loop, ft[var1]);
                case 2: // 
                    return yahoo_auth_typetwo(challenge, divisor, outer_loop, inner_loop, ft[var1], ft[var2]);
                case 3: // 
                    return yahoo_auth_typethree(challenge, divisor, outer_loop, inner_loop, ft[var1]);
                case 4: // 
                case 5:
                    return yahoo_auth_typefourfive(challenge, divisor, outer_loop, inner_loop, ft[var1]);
                default:
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
e.printStackTrace(System.err);
                Debug.println(e + ", " + inner_loop + ", " + remainder);
            }
        }

        return challenge;
    }

    /**
     * 
     */
    private static int yahoo_auth_read45(int buffer, int offset) {

        if (offset > 32) {
            return 0;
        }

        for (int i = 0; i < NUM_TYPE_FOURS; i++) {
            if (Buffer.type4list[i].start == buffer) {
                return (Buffer.type4list[i].buffer[offset] ^ buffer) & 0xff;
            }
        }

        for (int i = 0; i < NUM_TYPE_FIVES; i++) {
            if (Buffer.type5list[i].start == buffer) {
                return (Buffer.type5list[i].buffer[offset] ^ buffer) & 0xff;
            }
        }

        return 0;
    }

    /**
     * 
     */
    private static int yahoo_auth_read3(int buffer, int offset) {

        if (offset > 256) {
            return 0;
        }

        for (int i = 0; i < NUM_TYPE_THREES; i++) {
            if (Buffer.type3list[i].start == buffer) {
                return (Buffer.type3list[i].buffer[offset] ^ buffer) & 0xff;
            }
        }

        return 0;
    }

    /**
     * 
     */
    private static long yahoo_auth_typefourfive(long challenge, int divisor, int outer_loop, int inner_loop, int initial) {
        long final_value = 0;

        // Run through each bit.

        for (int i = 0; i < 32; i++) {
            /* Find the location in the challenge to put the 1/0 bit */
            /* so that we can do a replace of our current value. */
            /* Is this bit 1 or 0? */
            int buffer = yahoo_auth_read45(initial, i) & 0xff;
            int mask = ~(1 << buffer);
            long new_value = (challenge >>> i) & 1;

            final_value = (final_value & mask) | (new_value << buffer);
        }

        return yahoo_auth_fibonacci(final_value, divisor, outer_loop, inner_loop);
    }

    /**
     * 
     */
    private static long yahoo_auth_typethree(long challenge, int divisor, int outer_loop, int inner_loop, int offset) {
        long new_challenge = (long) (yahoo_auth_read3(offset, (int) ((challenge >> 0x18) & 0xff)) & 0xff) << 0x18;

        new_challenge |= (yahoo_auth_read3(offset, (int) (challenge >> 0x10) & 0xff) & 0xff) << 0x10;
        new_challenge |= (yahoo_auth_read3(offset, (int) (challenge >> 0x08) & 0xff) & 0xff) << 0x8;
        new_challenge |= (yahoo_auth_read3(offset, (int) (challenge          & 0xff)) & 0xff);

        return yahoo_auth_fibonacci(new_challenge, divisor, outer_loop, inner_loop);
    }

    /**
     * 
     */
    private static long yahoo_auth_typetwo(long challenge, int divisor, int outer_loop, int inner_loop, int type_two_variable, int type_two_variable2) {
        return yahoo_auth_fibonacci((challenge * type_two_variable) + type_two_variable2, divisor, outer_loop, inner_loop);
    }

    /**
     * 
     */
    private static long yahoo_auth_typeone(long challenge, int divisor, int outer_loop, int inner_loop, int type_one_variable) {
        return yahoo_auth_fibonacci(challenge ^ type_one_variable, divisor, outer_loop, inner_loop);
    }

    /**
     * 
     */
    static long yahoo_auth_finalCountdown(long challenge, int divisor, int inner_loop, int outer_loop) {

        int remainder = (int) ((challenge & 0xffffffffL) % divisor);

        try {
            int[] ft = FunctionList.values[inner_loop][remainder];
//System.out.println("LOOK1:" + inner_loop + " " + outer_loop + " " + challenge + ":" + (challenge & 0xffffffffl) + " " + ft[type]);
            switch (ft[type]) {
            case 0:
                break;
            case 1:
                challenge = yahoo_auth_typeone(challenge, divisor, outer_loop, inner_loop, ft[var1]);
                break;
            case 2:
                challenge = yahoo_auth_typetwo(challenge, divisor, outer_loop, inner_loop, ft[var1], ft[var2]);
                break;
            case 3:
                challenge = yahoo_auth_typethree(challenge, divisor, outer_loop, inner_loop, ft[var1]);
                break;
            case 4:
            case 5:
                challenge = yahoo_auth_typefourfive(challenge, divisor, outer_loop, inner_loop, ft[var1]);
                break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Debug.println(e + ", " + inner_loop + ", " + remainder);
        }

        return challenge;
    }
}

/* */
