/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * ChallengeResponseV10.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040801 nsano initial version <br>
 */
public final class ChallengeResponseV9 implements ChallengeResponse {

    /** */
    public String[] getResponses(String account,
                                 String password,
                                 String seed) {
        try {
            return getResponsesInternal(account, password, seed);
        } catch (NoSuchAlgorithmException e) {
            throw (RuntimeException) new IllegalStateException().initCause(e);
        }
    }

    /** */
    private String[] getResponsesInternal(String account,
                                          String password,
                                          String seed)
        throws NoSuchAlgorithmException {

        int cnt = 0;
        int magic_cnt = 0;

        // Magic: Phase 1. Generate what seems to be a 30
        // byte value (could change if base64
        // ends up differently?  I don't remember and I'm
        // tired, so use a 64 byte buffer.
        int magic_ptr = 0;
        int magic_work = 0;
        byte[] magic = new byte[64];

        final String challenge_lookup = "qzec2tb3um1olpar8whx4dfgijknsvy5";
        final String operand_lookup = "+|&%/*^-";

        while (magic_ptr < seed.length()) {

            // Ignore parentheses.
            if ((seed.charAt(magic_ptr) == '(') ||
                (seed.charAt(magic_ptr) == ')')) {
                magic_ptr++;

                continue;
            }

            // Byteacters and digits verify against
            // the challenge lookup.
            if (Character.isUpperCase(seed.charAt(magic_ptr)) ||
                Character.isLowerCase(seed.charAt(magic_ptr)) ||
                Character.isDigit(seed.charAt(magic_ptr))) {

                int loc = challenge_lookup.indexOf(seed.substring(magic_ptr));
                if (loc == -1) {
                    // This isn't good
                }

                // Get offset into lookup table and lsh 3.
                magic_work = loc;
                magic_work <<= 3;

                magic_ptr++;

                continue;
            } else {

                int loc = operand_lookup.indexOf(seed.substring(magic_ptr));
                if (loc == -1) {
                    // Also not good.
                }

                int local_store = loc;

                // Oops; how did this happen?
                if (magic_cnt >= 64) {
                    break;
                }

                magic[magic_cnt++] = (byte) (magic_work | local_store);
                magic_ptr++;

                continue;
            }
        }

        int magic_len = magic_cnt;
        magic_cnt = 0;

        // Magic: Phase 2.  Take generated magic value and
        // sprinkle fairy dust on the values.
        for (magic_cnt = magic_len - 2; magic_cnt >= 0; magic_cnt--) {

            // Bad.  Abort.
            if (((magic_cnt + 1) > magic_len) || (magic_cnt > magic_len)) {
                break;
            }

            byte byte1 = magic[magic_cnt];
            byte byte2 = magic[magic_cnt + 1];

            byte1 *= 0xcd;
            byte1 ^= byte2;

            magic[magic_cnt + 1] = byte1;
        }

        // Magic: Phase 3.
        // This computes 20 bytes. The first 4 bytes are used as our magic
        // key (and may be changed later); the next 16 bytes are an MD5 sum of
        // the magic key plus 3 bytes. The 3 bytes are found by looping, and
        // they represent the offsets into particular functions we'll later
        // call to potentially alter the magic key.
        // %-)
        magic_cnt = 1;
        int x = 0;

        byte[] comparison_src = new byte[20];
        do {
            int bl = 0;
            int cl = magic[magic_cnt++];

            if (magic_cnt >= magic_len) {
                break;
            }

            if (cl > 0x7f) {
                if (cl < 0xe0) {
                    bl = cl = (cl & 0x1f) << 6;
                } else {
                    bl = magic[magic_cnt++];
                    cl = (cl & 0x0f) << 6;
                    bl = ((bl & 0x3f) + cl) << 6;
                }

                cl = magic[magic_cnt++];
                bl = (cl & 0x3f) + bl;
            } else {
                bl = cl;
            }

            comparison_src[x++] = (byte) ((bl & 0xff00) >> 8);
            comparison_src[x++] = (byte)  (bl & 0xff);
        } while (x < 20);

        // First four bytes are magic key.
        byte[] magic_key_char = new byte[4];
        for (x = 0; x < 4; x++) {
            magic_key_char[x] = comparison_src[x];
        }

        // Get password and crypt hashes as per usual.
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] result = md5.digest(password.getBytes());
        byte[] password_hash = Base64.toYmsgBase64(result);

        md5 = MessageDigest.getInstance("MD5");
        byte[] crypt_result = Crypt.crypt(password, "$1$_2S43d5f$");
        result = md5.digest(crypt_result);
        byte[] crypt_hash = Base64.toYmsgBase64(result);

        // Our first authentication response is based off
        // of the password hash.
        byte[] pass_hash_xor1 = new byte[64];
        for (x = 0; x < password_hash.length; x++) {
            pass_hash_xor1[cnt++] = (byte) (password_hash[x] ^ 0x36);
        }

        if (cnt < 64) {
            for (int i = cnt; i < 64 - cnt; i++) {
                pass_hash_xor1[i] = 0x36;
            }
        }

        cnt = 0;

        byte[] pass_hash_xor2 = new byte[64];
        for (x = 0; x < password_hash.length; x++) {
            pass_hash_xor2[cnt++] = (byte) (password_hash[x] ^ 0x5c);
        }

        if (cnt < 64) {
            for (int i = cnt; i < 64 - cnt; i++) {
                pass_hash_xor2[i] = 0x5c;
            }
        }

        final String alphabet1 = "FBZDWAGHrJTLMNOPpRSKUVEXYChImkwQ";
        final String alphabet2 = "F0E1D2C3B4A59687abcdefghijklmnop";

        final String delimit_lookup = ",;";

        // The first context gets the password hash XORed
        // with 0x36 plus a magic value
        // which we previously extrapolated from our
        // challenge.
        MessageDigest sha1 = MessageDigest.getInstance("SHA");
        sha1.update(pass_hash_xor1, 0, 64);
        sha1.update(magic_key_char, 0, 4);
        byte[] digest1 = sha1.digest();

        // The second context gets the password hash XORed
        // with 0x5c plus the SHA-1 digest
        // of the first context.
        MessageDigest sha2 = MessageDigest.getInstance("SHA");
        sha2.update(pass_hash_xor2, 0, 64);
        sha2.update(digest1, 0, 20);
        byte[] digest2 = sha2.digest();

        // Now that we have digest2, use it to fetch
        // characters from an alphabet to construct
        // our first authentication response.
        StringBuilder resp_6 = new StringBuilder();
        for (x = 0; x < 20; x += 2) {

            // First two bytes of digest stuffed
            //  together.
            int val = digest2[x];
            val <<= 8;
            val += digest2[x + 1];

            int lookup = val >> 0x0b;
            lookup &= 0x1f;

            if (lookup >= alphabet1.length()) {
                break;
            }

            resp_6.append(alphabet1.charAt(lookup));
            resp_6.append("=");

            lookup = val >> 0x06;
            lookup &= 0x1f;

            if (lookup >= alphabet2.length()) {
                break;
            }

            resp_6.append(alphabet2.charAt(lookup));

            lookup = val >> 0x01;
            lookup &= 0x1f;

            if (lookup >= alphabet2.length()) {
                break;
            }

            resp_6.append(alphabet2.charAt(lookup));

            lookup = val & 0x01;

            if (lookup >= delimit_lookup.length()) {
                break;
            }

            resp_6.append(delimit_lookup.charAt(lookup));
        }

        // Our second authentication response is based off
        // of the crypto hash.
        cnt = 0;
        for (int i = 0; i < 20; i++) {
            digest1[i] = 0;
            digest2[i] = 0;
        }

        byte[] crypt_hash_xor1 = new byte[64];
        for (x = 0; x < crypt_hash.length; x++) {
            crypt_hash_xor1[cnt++] = (byte) (crypt_hash[x] ^ 0x36);
        }

        if (cnt < 64) {
            for (int i = cnt; i < 64 - cnt; i++) {
                crypt_hash_xor1[i] = 0x36;
            }
        }

        cnt = 0;

        byte[] crypt_hash_xor2 = new byte[64];
        for (x = 0; x < crypt_hash.length; x++) {
            crypt_hash_xor2[cnt++] = (byte) (crypt_hash[x] ^ 0x5c);
        }

        if (cnt < 64) {
            for (int i = cnt; i < 64 - cnt; i++) {
                crypt_hash_xor2[i] = 0x5c;
            }
        }

        // The first context gets the password hash XORed
        // with 0x36 plus a magic value
        // which we previously extrapolated from our
        // challenge.
        sha1 = MessageDigest.getInstance("SHA");
        sha1.update(crypt_hash_xor1, 0, 64);
        sha1.update(magic_key_char, 0, 4);
        digest1 = sha1.digest();

        // The second context gets the password hash XORed
        // with 0x5c plus the SHA-1 digest
        // of the first context.
        sha2 = MessageDigest.getInstance("SHA");
        sha2.update(crypt_hash_xor2, 0, 64);
        sha2.update(digest1, 0, 20);
        digest2 = sha2.digest();

        // Now that we have digest2, use it to fetch
        // characters from an alphabet to construct
        // our first authentication response.
        StringBuilder resp_96 = new StringBuilder();
        for (x = 0; x < 20; x += 2) {

            // First two bytes of digest stuffed
            // together.
            int val = digest2[x];
            val <<= 8;
            val += digest2[x + 1];

            int lookup = val >> 0x0b;
            lookup &= 0x1f;

            if (lookup >= alphabet1.length()) {
                break;
            }

            resp_96.append(alphabet1.charAt(lookup));
            resp_96.append("=");

            lookup = val >> 0x06;
            lookup &= 0x1f;

            if (lookup >= alphabet2.length()) {
                break;
            }

            resp_96.append(alphabet2.charAt(lookup));

            lookup = val >> 0x01;
            lookup &= 0x1f;

            if (lookup >= alphabet2.length()) {
                break;
            }

            resp_96.append(alphabet2.charAt(lookup));

            lookup = val & 0x01;

            if (lookup >= delimit_lookup.length()) {
                break;
            }

            resp_96.append(delimit_lookup.charAt(lookup));
        }

        return new String[] { resp_6.toString(), resp_96.toString() };
    }
}

/* */
