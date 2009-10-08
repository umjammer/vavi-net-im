/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;


/**
 * Base64.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 070316 nsano initial version <br>
 */
final class Base64 {

    /** */
    private static final char[] base64Digits = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', '.', '_'
    };

    /** */
    public static byte[] toYmsgBase64(byte[] buffer) {
        int limit = buffer.length - (buffer.length % 3);
        StringBuilder out = new StringBuilder();
        int[] buf = new int[buffer.length];

        for (int i = 0; i < buffer.length; i++) {
            buf[i] = buffer[i] & 0xff;
        }

        for (int i = 0; i < limit; i += 3) {
            // Top 6 bits of first byte
            out.append(base64Digits[buf[i] >> 2]);
            // Bottom 2 bits of first byte append to top 4 bits of second
            out.append(base64Digits[((buf[i] << 4) & 0x30) | (buf[i + 1] >> 4)]);
            // Bottom 4 bits of second byte appended to top 2 bits of third
            out.append(base64Digits[((buf[i + 1] << 2) & 0x3c) | (buf[i + 2] >> 6)]);
            // Bottom six bits of third byte
            out.append(base64Digits[buf[i + 2] & 0x3f]);
        }

        // Do we still have a remaining 1 or 2 bytes left?
        int i = limit;
        switch (buf.length - i) {
        case 1:
            // Top 6 bits of first byte
            out.append(base64Digits[buf[i] >> 2]);
            // Bottom 2 bits of first byte
            out.append(base64Digits[((buf[i] << 4) & 0x30)]);
            out.append("--");
            break;
        case 2 :
            // Top 6 bits of first byte
            out.append(base64Digits[buf[i] >> 2]);
            // Bottom 2 bits of first byte append to top 4 bits of second
            out.append(base64Digits[((buf[i] << 4) & 0x30) | (buf[i + 1] >> 4)]);
            // Bottom 4 bits of second byte
            out.append(base64Digits[((buf[i + 1] << 2) & 0x3c)]);
            out.append("-");
            break;
        }

        return out.toString().getBytes();
    }
}

/* */
