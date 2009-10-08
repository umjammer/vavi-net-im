/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import vavi.util.StringUtil;


/**
 * One way encryption based on MD5 sum.
 *
 * warmenhoven took this file and made it work with the md5.[ch] we
 * already had. isn't that lovely. people should just use linux or
 * freebsd, crypt works properly on those systems. i hate solaris
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040221 nsano initial version <br>
 */
final class Crypt {

    /**
     * Define our magic string to mark salt for MD5 "encryption"
     * replacement.  This is meant to be the same as for other MD5 based
     * encryption implementations.
     */
    private static final String saltPrefix = "$1$";

    /* Table with characters for base64 transformation. */
    private static final String b64t =
        "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /** */
    public static byte[] crypt(String key, String salt)
        throws NoSuchAlgorithmException {

        int key_len = key.length();

        // Find beginning of salt string.  The prefix should normally always
        // be present.  Just in case it is not.
        if (salt.startsWith(saltPrefix)) {
            // Skip salt prefix.
            salt = salt.substring(saltPrefix.length());
        }

        int salt_len = Math.min(getDollarLessLength(salt), 8);
// 	salt = salt.substring(0, salt_len);
//System.err.println("key: " + key + ", " + key_len);
//System.err.println("salt: " + salt + ", " + salt_len);

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // Add the key string.
        md5.update(key.getBytes());
        // Because the SALT argument need not always have the salt prefix we
        // add it separately.
        md5.update(saltPrefix.getBytes());
        // The last part is the salt string.  This must be at most 8
        // characters and it ends at the first `$' character (for
        // compatibility which existing solutions).
        md5.update(salt.getBytes(), 0, salt_len);

        // Compute alternate MD5 sum with input KEY, SALT, and KEY.  The
        // final result will be added to the first context.  */
        MessageDigest md5_alt = MessageDigest.getInstance("MD5");
        // Add key.
        md5_alt.update(key.getBytes());
//System.err.print(StringUtil.getDump(key.getBytes()));
        // Add salt.
        md5_alt.update(salt.getBytes(), 0, salt_len);
//System.err.print(StringUtil.getDump(salt.getBytes()));
        // Add key again.
        md5_alt.update(key.getBytes());
//System.err.print(StringUtil.getDump(key.getBytes()));
        // Now get result of this (16 bytes) and add it to the other
        // context.
        byte[] altResult = md5_alt.digest();
//System.err.print(StringUtil.getDump(altResult));

        // Add for any character in the key one byte of the alternate sum.
        int cnt;
        for (cnt = key_len; cnt > 16; cnt -= 16) {
            md5.update(altResult, 0, 16);
        }
        md5.update(altResult, 0, cnt);

        // For the following code we need a NUL byte.
        altResult[0] = 0;

        // The original implementation now does something weird: for every 1
        // bit in the key the first 0 is added to the buffer, for every 0
        // bit the first character of the key.  This does not seem to be
        // what was intended but we have to follow this to be compatible.
        for (cnt = key_len; cnt > 0; cnt >>= 1) {
            md5.update(((cnt & 1) != 0) ? altResult : key.getBytes(), 0, 1);
        }

        // Create intermediate result.
        altResult = md5.digest();

        // Now comes another weirdness.  In fear of password crackers here
        // comes a quite long loop which just processes the output of the
        // previous round again.  We cannot ignore this here.
        for (cnt = 0; cnt < 1000; cnt++) {
            // New context.
            md5.reset();

            // Add key or last result.
            if ((cnt & 1) != 0) {
                md5.update(key.getBytes());
            } else {
                md5.update(altResult, 0, 16);
            }

            // Add salt for numbers not divisible by 3.
            if ((cnt % 3) != 0) {
                md5.update(salt.getBytes(), 0, salt_len);
            }

            // Add key for numbers not divisible by 7.
            if ((cnt % 7) != 0) {
                md5.update(key.getBytes());
            }

            // Add key or last result.
            if ((cnt & 1) != 0) {
                md5.update(altResult, 0, 16);
            } else {
                md5.update(key.getBytes());
            }

            // Create intermediate result.
            altResult = md5.digest();
        }

        // Now we can construct the result string.  It consists of three
        // parts.
        StringBuilder sb = new StringBuilder();

        sb.append(saltPrefix);
        sb.append(new String(salt.getBytes(), 0, salt_len));
        sb.append('$');

        sb.append(b64From24bit(altResult[0], altResult[6], altResult[12], 4));
        sb.append(b64From24bit(altResult[1], altResult[7], altResult[13], 4));
        sb.append(b64From24bit(altResult[2], altResult[8], altResult[14], 4));
        sb.append(b64From24bit(altResult[3], altResult[9], altResult[15], 4));
        sb.append(b64From24bit(altResult[4], altResult[10], altResult[5], 4));
        sb.append(b64From24bit((byte) 0, (byte) 0, altResult[11], 2));

        return sb.toString().getBytes();
    }

    /** */
    private static String b64From24bit(byte b2, byte b1, byte b0, int n) {
        byte[] result = new byte[n];
        int w = ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | (b0 & 0xff);
        for (int i = 0; i < n; i++) {
            result[i] = (byte) b64t.charAt(w & 0x3f);
            w >>>= 6;
        }

        return new String(result);
    }

    /** */
    private static int getDollarLessLength(String string) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != '$') {
                count++;
            }
        }
        return count;
    }
}

/* */
