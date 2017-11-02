/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

import vavi.util.Debug;


/**
 * ChallengeResponseV11.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 061105 nsano initial version <br>
 */
public final class ChallengeResponseV11 implements ChallengeResponse {

    /** */
    public String[] getResponses(String account,
                                 String password,
                                 String seed) {
//Debug.println("\naccount: " + account + "\npassword: " + password + "\nseed: " + seed);
        try {
            return getResponsesInternal(account, password, seed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchProviderException e) {
            throw new IllegalStateException(e);
        }
    }

    /** */
    private String[] getResponsesInternal(String account,
                                          String password,
                                          String seed)
        throws NoSuchAlgorithmException, NoSuchProviderException {

        boolean[] doModify = new boolean[1];
        byte[] magicKey = getMagicKey(seed, doModify);
        return getResponses(magicKey, password, doModify[0]);
    }

    /**
     * 
     * @param doModify will be changed
     */
    private byte[] getMagicKey(String seed, boolean[] doModify) throws NoSuchAlgorithmException {
        byte[] magic = new byte[64];
        int magicLength = doPhase1(seed, magic);
        doPhase2(magic, magicLength);
        byte[] comparison = doPhase3(magic, magicLength);
        return doPhase4(comparison, doModify);
    }

    /**
     * Magic: Phase 1. Generate what seems to be a 30
     * byte value (could change if base64
     * ends up differently?  I don't remember and I'm
     * tired, so use a 64 byte buffer.
     *
     * @param magic will be updated
     */
    private int doPhase1(String seed, byte[] magic) {
        int magicCount = 0;

        int magicPointer = 0;
        int magicWork = 0;

        final String challengeLookup = "qzec2tb3um1olpar8whx4dfgijknsvy5";
        final String operandLookup = "+|&%/*^-";

        while (magicPointer < seed.length()) {

            // Ignore parentheses.
            if ((seed.charAt(magicPointer) == '(') ||
                (seed.charAt(magicPointer) == ')')) {
                magicPointer++;

                continue;
            }

            // Byteacters and digits verify against
            // the challenge lookup.
            if (Character.isLetterOrDigit(seed.charAt(magicPointer))) {

                int loc = challengeLookup.indexOf(seed.charAt(magicPointer));
                if (loc == -1) {
Debug.println("This isn't good");
                    continue;
                }

                // Get offset into lookup table and lsh 3.
                magicWork = loc; // challenge_lookup
                magicWork <<= 3;

                magicPointer++;

                continue;
            } else {

                int loc = operandLookup.indexOf(seed.charAt(magicPointer));
                if (loc == -1) {
Debug.println("Also not good.");
                    continue;
                }

                int localStore = loc; // operand_lookup

                // Oops; how did this happen?
                if (magicCount >= 64) {
                    break;
                }

                magic[magicCount++] = (byte) (magicWork | localStore);
                magicPointer++;

                continue;
            }
        }
//dump("P1", magic);

        return magicCount;
    }
    
    /**
     * Magic: Phase 2. Take generated magic value and
     * sprinkle fairy dust on the values.
     *
     * @param magic will be updated
     */
    private void doPhase2(byte[] magic, int magicLength) {
        int magicCount = 0;

        for (magicCount = magicLength - 2; magicCount >= 0; magicCount--) {

            // Bad. Abort.
            if (((magicCount + 1) > magicLength) || (magicCount > magicLength)) {
                break;
            }

            byte byte1 = magic[magicCount];
            byte byte2 = magic[magicCount + 1];

            byte1 *= 0xcd;
            byte1 ^= byte2;

            magic[magicCount + 1] = byte1;
        }
//dump("P2", magic);
    }

    /**
     * Magic: Phase 3.
     * This computes 20 bytes. The first 4 bytes are used as our magic
     * key (and may be changed later); the next 16 bytes are an MD5 sum of
     * the magic key plus 3 bytes. The 3 bytes are found by looping, and
     * they represent the offsets into particular functions we'll later
     * call to potentially alter the magic key.
     * %-)
     */
    private byte[] doPhase3(byte[] magic, int magicLength) throws NoSuchAlgorithmException {
        int magicCount = 1;
        int x = 0;

        byte[] comparison = new byte[20];
        do {
            int bl = 0;
            int cl = magic[magicCount++] & 0xff;

            if (magicCount >= magicLength) {
                break;
            }

            if (cl > 0x7f) {
                if (cl < 0xe0) {
                    bl = cl = (cl & 0x1f) << 6;
                } else {
                    bl = magic[magicCount++] & 0xff;
                    cl = (cl & 0x0f) << 6;
                    bl = ((bl & 0x3f) + cl) << 6;
                }

                cl = magic[magicCount++] & 0xff;
                bl = (cl & 0x3f) + bl;
            } else {
                bl = cl;
            }

            comparison[x++] = (byte) ((bl & 0xff00) >> 8);
            comparison[x++] = (byte)  (bl & 0xff);
        } while (x < 20);
//dump("P3.1", comparisonSource);

        return comparison;
    }

    /**
     * Magic: Phase 4.  Determine what function to use later by getting outside/inside
     * loop values until we match our previous buffer.
     * 
     * @param doModify will be changed
     */
    private byte[] doPhase4(byte[] comparison, boolean[] doModify) throws NoSuchAlgorithmException {
        // First four bytes are magic key.
        byte[] magicKey = new byte[4];
        for (int i = 0; i < 4; i++) {
            magicKey[i] = comparison[i];
        }

        int depth = 0;
        int table = 0;

depthLoop:
        for (depth = 0; depth < 65535; depth++) {
tableLoop:
            for (table = 0; table < 5; table++) {
                byte[] test = new byte[3];
                
                // Calculate buffer.
                test[0] = (byte) depth;
                test[1] = (byte) (depth >> 8);
                test[2] = (byte) table;
                
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(magicKey);
                md5.update(test);
                byte[] result = md5.digest();
                
                for (int i = 0; i < 16; i++) {
                    if (result[i] != comparison[4 + i]) {
                        continue tableLoop;
                    }
                }

                break depthLoop;
            }
        }

//System.out.println("P3.2: " + x + " " + y + ": ");
//Debug.println("x: " + x + ", y: " + y);

        // If y != 0, we need some help.
        if (table != 0) {
            // Update magic stuff.   Call it twice because Yahoo's encryption is super bad ass.
            long updatedKey = (magicKey[0] & 0xff) | ((magicKey[1] & 0xff) << 8) | ((magicKey[2] & 0xff) << 16) | ((magicKey[3] & 0xff) << 24);
//System.err.printf("%02x %02x %02x %02x\n", (magicKeyChar[0] & 0xff), ((magicKeyChar[1] & 0xff) << 8), ((magicKeyChar[2] & 0xff) << 16), ((magicKeyChar[3] & 0xff) << 24));
//System.err.printf("%016x\n", magic4);

//System.out.printf("P3.3.a: %d, %016x\n", magic4, magic4);
            updatedKey = ChallengeResponseTable.yahoo_auth_finalCountdown(updatedKey, 0x60, table, depth);
//System.out.printf("P3.3.b: %d, %016x\n", updated_key, updated_key);
            updatedKey = ChallengeResponseTable.yahoo_auth_finalCountdown(updatedKey, 0x60, table, depth);
//System.out.printf("P3.3.c: %d, %016x\n", updated_key, updated_key);
            
            magicKey[0] = (byte) (updatedKey & 0xff);
            magicKey[1] = (byte) ((updatedKey >> 8) & 0xff);
            magicKey[2] = (byte) ((updatedKey >> 16) & 0xff);
            magicKey[3] = (byte) ((updatedKey >>> 24) & 0xff);
        } 
//System.err.printf("%02x %02x %02x %02x\n", magicKeyChar[0], magicKeyChar[1], magicKeyChar[2], magicKeyChar[3]);

        if (table >= 3) {
            doModify[0] = true;
        }

        return magicKey;
    }

    /**
     * Get password and crypt hashes as per usual.
     */
    private String[] getResponses(byte[] magicKey, String password, boolean doModify) throws NoSuchAlgorithmException, NoSuchProviderException {
        // Our first authentication response is based off
        // of the password hash.
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] result = md5.digest(password.getBytes());
        byte[] hash = Base64.toYmsgBase64(result);
//System.out.println("P4.1 " + new String(hash));

        byte[] digest2 = getDigest(magicKey, hash, doModify);
        String response6 = getResponse(digest2);

        // Our second authentication response is based off
        // of the crypto hash.
        md5 = MessageDigest.getInstance("MD5");
        result = Crypt.crypt(password, "$1$_2S43d5f$");
        result = md5.digest(result);
        hash = Base64.toYmsgBase64(result);

        digest2 = getDigest(magicKey, hash, doModify);
        String response96 = getResponse(digest2);
//System.out.println("P4.1 " + new String(hash));

//System.out.println("FINAL " + response6 + " " + response96);
        return new String[] { response6, response96 };
    }

    /** */
    private byte[] getDigest(byte[] magicKey, byte[] hash, boolean doModify) throws NoSuchAlgorithmException, NoSuchProviderException {
        int i;
        byte[] hashXor1 = new byte[64];
        for (i = 0; i < hash.length; i++) {
            hashXor1[i] = (byte) (hash[i] ^ 0x36);
        }
        Arrays.fill(hashXor1, i, 64, (byte) 0x36);

        byte[] hashXor2 = new byte[64];
        for (i = 0; i < hash.length; i++) {
            hashXor2[i] = (byte) (hash[i] ^ 0x5c);
        }
        Arrays.fill(hashXor2, i, 64, (byte) 0x5c);

//dump("P4.2", hashXor1);
//dump("P4.2", hashXor2);
//dump("P4.2", magicKey);

        // The first context gets the password hash XORed
        // with 0x36 plus a magic value
        // which we previously extrapolated from our
        // challenge.
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1", "YmsgMessageDigest");
        sha1.update(hashXor1, 0, 64);
        if (doModify) {
            ((vavi.net.im.protocol.ymsg.auth.YmsgSHA1) sha1).setBitCount(0x1ff);
        }
        sha1.update(magicKey, 0, 4);
        byte[] digest1 = sha1.digest();

        // The second context gets the password hash XORed
        // with 0x5c plus the SHA-1 digest
        // of the first context.
        MessageDigest sha2 = MessageDigest.getInstance("SHA");
        sha2.update(hashXor2, 0, 64);
        sha2.update(digest1, 0, 20);
        byte[] digest2 = sha2.digest();
//dump("P4.3", digest1);
//dump("P4.3", digest2);
        return digest2;
    }

    private static final String alphabet1 = "FBZDWAGHrJTLMNOPpRSKUVEXYChImkwQ";
    private static final String alphabet2 = "F0E1D2C3B4A59687abcdefghijklmnop";

    private static final String delimitorLookup = ",;";

    /**
     * Now that we have digest2, use it to fetch
     * characters from an alphabet to construct
     * our first authentication response.
     */
    private String getResponse(byte[] digest2) {

        StringBuilder response = new StringBuilder();

        for (int i = 0; i < 20; i += 2) {

            // First two bytes of digest stuffed
            //  together.
            int value = digest2[i];
            value <<= 8;
            value += digest2[i + 1] & 0xff;

            int lookup = value >> 0x0b;
            lookup &= 0x1f;

            if (lookup >= alphabet1.length()) {
                break;
            }

            response.append(alphabet1.charAt(lookup));
            response.append("=");

            lookup = value >> 0x06;
            lookup &= 0x1f;

            if (lookup >= alphabet2.length()) {
                break;
            }

            response.append(alphabet2.charAt(lookup));

            lookup = value >> 0x01;
            lookup &= 0x1f;

            if (lookup >= alphabet2.length()) {
                break;
            }

            response.append(alphabet2.charAt(lookup));

            lookup = value & 0x01;

            if (lookup >= delimitorLookup.length()) {
                break;
            }

            response.append(delimitorLookup.charAt(lookup));
        }

        return response.toString();
    }

//static void dump(String title, byte[] data) {
//    int idx = 0;
//    System.out.println(title);
//    while (idx < data.length) {
//        String s = Integer.toHexString(data[idx] & 0xff);
//        if (s.length() < 2)
//            s = "0" + s;
//        System.out.print(s + " ");
//        idx++;
//        if ((idx % 20) == 0)
//            System.out.print("\n");
//    }
//    if ((idx % 20) != 0)
//        System.out.print("\n");
//}

    /* */
    static {
        Security.addProvider(new vavi.net.im.protocol.ymsg.auth.YmsgMessageDigestProvider());
    }
}

/* */
