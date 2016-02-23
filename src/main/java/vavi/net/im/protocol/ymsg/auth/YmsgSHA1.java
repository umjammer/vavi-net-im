/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;

import java.security.MessageDigest;


/**
 * This class is a port of the SHA-1 algorithm included with GAIM, which is a
 * port of the SHA-1 algorithm included with Mozilla somewhere.
 * 
 * Donated to the jYMSG project by "phinaesgage" (Phinaes Gage) after Yahoo
 * changed their protocol on 24th June 2004 to require direct manipulation of
 * the SHA digest as it was being created. This code enabled jYMSG to regain
 * access to Yahoo within only a few days of the change.
 * 
 * For v0.6 I (FISH) have modified the source to standardise its style with the
 * rest of the code in the jYMSG project (just for consistency), added comments
 * (more for my own benefit than anyone else's) and simplified it to remove test
 * code and objects when not actually being debugged (to reduce its deployment
 * footprint!)
 * 
 * {@link java.security.MessageDigestSpi} を implements していないのは
 * {@link #setBitCount(long)} をユーザがアクセスできるようにするため。
 */
public class YmsgSHA1 extends MessageDigest {
    private int[] h, w;

    private int lenW;

    private long bitCount;

    private static final byte[] pad0x80 = {
        (byte) 0x80
    };

    private static final byte[] pad0x00 = {
        (byte) 0x00
    };

    /**
     * Constructor
     */
    public YmsgSHA1() {
        super("YmsgSHA1");
        h = new int[5];
        w = new int[80];
        initH();
        initW();
    }

    /**
     * Resets the internal state to its initial values.
     */
    public void engineReset() {
        bitCount = 0;
        lenW = 0;
        initH();
        initW();
    }

    /**
     * Adds more data to the hash.
     */
    public void engineUpdate(byte b) {
        engineUpdate(new byte[] { b }, 0, 1);
    }

    /**
     * Read the data into W and process blocks as they get full
     */
    public void engineUpdate(byte[] bytes, int offset, int len) {
        for (int i = offset; i < len; i++) {
            w[lenW / 4] <<= 8;
            w[lenW / 4] |= (bytes[i] & 0xff);
            if ((++lenW) % 64 == 0) {
                hashBlock();
                lenW = 0;
            }
            bitCount += 8;
        }
    }

    /**
     * Complete the hash and get the final digest (resets internal state).
     */
    public byte[] engineDigest() {
        byte[] padLen = new byte[8];

        padLen[0] = (byte) ((bitCount >> 56) & 0xff);
        padLen[1] = (byte) ((bitCount >> 48) & 0xff);
        padLen[2] = (byte) ((bitCount >> 40) & 0xff);
        padLen[3] = (byte) ((bitCount >> 32) & 0xff);
        padLen[4] = (byte) ((bitCount >> 24) & 0xff);
        padLen[5] = (byte) ((bitCount >> 16) & 0xff);
        padLen[6] = (byte) ((bitCount >> 8) & 0xff);
        padLen[7] = (byte) ((bitCount >> 0) & 0xff);

        engineUpdate(pad0x80, 0, 1);

        while (lenW != 56) {
            engineUpdate(pad0x00, 0, 1);
        }

        engineUpdate(padLen, 0, 8);

        // Create output hash
        byte[] hashout = new byte[20];
        for (int i = 0; i < 20; i++) {
            hashout[i] = (byte) (h[i / 4] >> 24);
            h[i / 4] <<= 8;
        }

        engineReset();
        return hashout;
    }

    /**
     * Forces the bit count to be set to a particular value (should not
     * use).
     */
    public void setBitCount(long count) {
        bitCount = count;
    }

    /**
     * Initialize the H and W buffers.
     */
    private void initH() {
        h[0] = 0x67452301;
        h[1] = 0xefcdab89;
        h[2] = 0x98badcfe;
        h[3] = 0x10325476;
        h[4] = 0xc3d2e1f0;
    }

    private void initW() {
        for (int i = 0; i < w.length; i++) {
            w[i] = 0;
        }
    }

    /**
     * SHA rotate left.
     */
    private int shaRotl(int x, int n) {
        return (x << n) | (x >>> (32 - n));
    }

    private void hashBlock() {
        int a, b, c, d, e, temp;
        for (int t = 16; t <= 79; t++) {
            w[t] = shaRotl(w[t - 3] ^ w[t - 8] ^ w[t - 14] ^ w[t - 16], 1);
        }

        a = h[0];
        b = h[1];
        c = h[2];
        d = h[3];
        e = h[4];

        // Round 1
        for (int t = 0; t <= 19; t++) {
            temp = shaRotl(a, 5) + (((c ^ d) & b) ^ d) + e + w[t] + 0x5a827999;
            e = d;
            d = c;
            c = shaRotl(b, 30);
            b = a;
            a = temp;
        }

        // Round 2
        for (int t = 20; t <= 39; t++) {
            temp = shaRotl(a, 5) + (b ^ c ^ d) + e + w[t] + 0x6ed9eba1;
            e = d;
            d = c;
            c = shaRotl(b, 30);
            b = a;
            a = temp;
        }

        // Round 3
        for (int t = 40; t <= 59; t++) {
            temp = shaRotl(a, 5) + ((b & c) | (d & (b | c))) + e + w[t] + 0x8f1bbcdc;
            e = d;
            d = c;
            c = shaRotl(b, 30);
            b = a;
            a = temp;
        }

        // Round 4
        for (int t = 60; t <= 79; t++) {
            temp = shaRotl(a, 5) + (b ^ c ^ d) + e + w[t] + 0xca62c1d6;
            e = d;
            d = c;
            c = shaRotl(b, 30);
            b = a;
            a = temp;
        }

        h[0] += a;
        h[1] += b;
        h[2] += c;
        h[3] += d;
        h[4] += e;
    }
}

/* */
