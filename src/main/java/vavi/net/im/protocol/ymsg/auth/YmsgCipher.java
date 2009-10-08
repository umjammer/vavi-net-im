/*
 * Copyright (c) 2009 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;


/**
 * YmsgCipher. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 090222 nsano initial version <br>
 */
public final class YmsgCipher extends CipherSpi {

    /* @see javax.crypto.CipherSpi#engineDoFinal(byte[], int, int) */
    @Override
    protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
        byte[] output = engineUpdate(input, inputOffset, inputLen);
        finalized = true;
        return output;
    }

    /* @see javax.crypto.CipherSpi#engineDoFinal(byte[], int, int, byte[], int) */
    @Override
    protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        int outputLen = engineUpdate(input, inputOffset, inputLen, output, outputOffset);
        finalized = true;
        return outputLen;
    }

    /* @see javax.crypto.CipherSpi#engineGetBlockSize() */
    @Override
    protected int engineGetBlockSize() {
        return 0; // TODO
    }

    /* @see javax.crypto.CipherSpi#engineGetIV() */
    @Override
    protected byte[] engineGetIV() {
        // TODO Auto-generated method stub
        return null;
    }

    /* @see javax.crypto.CipherSpi#engineGetOutputSize(int) */
    @Override
    protected int engineGetOutputSize(int inputLen) {
        return 0; // TODO
    }

    /* @see javax.crypto.CipherSpi#engineGetParameters() */
    @Override
    protected AlgorithmParameters engineGetParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    private boolean finalized = false;

    private int opmode;

    private String key;

    /* @see javax.crypto.CipherSpi#engineInit(int, java.security.Key, java.security.SecureRandom) */
    @Override
    protected void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
        this.opmode = opmode;

        if (YmsgKey.class.isInstance(key)) {
            this.key = YmsgKey.class.cast(key).key;
        } else {
            throw new InvalidKeyException();
        }

        finalized = false;
    }

    /* @see javax.crypto.CipherSpi#engineInit(int, java.security.Key, java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom) */
    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        engineInit(opmode, key, random);
    }

    /* @see javax.crypto.CipherSpi#engineInit(int, java.security.Key, java.security.AlgorithmParameters, java.security.SecureRandom) */
    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        engineInit(opmode, key, random);
    }

    /* @see javax.crypto.CipherSpi#engineSetMode(java.lang.String) */
    @Override
    protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
        // TODO Auto-generated method stub
    }

    /* @see javax.crypto.CipherSpi#engineSetPadding(java.lang.String) */
    @Override
    protected void engineSetPadding(String padding) throws NoSuchPaddingException {
        // TODO Auto-generated method stub
    }

    /* @see javax.crypto.CipherSpi#engineUpdate(byte[], int, int) */
    @Override
    protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
        if (finalized) {
            throw new IllegalStateException("finalized"); // TODO check
        }
        try {
            if (opmode == Cipher.ENCRYPT_MODE) {
                return Crypt.crypt(key, new String(input, inputOffset, inputLen, "ISO_8859-1"));
            } else {
                assert false : opmode;
            }
        } catch (NoSuchAlgorithmException e) {
            assert false : e;
        } catch (UnsupportedEncodingException e) {
            assert false : e;
        }
        return null;
    }

    /* @see javax.crypto.CipherSpi#engineUpdate(byte[], int, int, byte[], int) */
    @Override
    protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
        throw new UnsupportedOperationException("not implemented yet"); // TODO
    }

    /** */
    public static class YmsgKey implements Key {
        /** 16 bytes (128 bit) key */
        String key;
        /** @param key 16 bytes using UTF-8 encoding */
        public YmsgKey(String key) {
            this.key = key;
        }
        public byte[] getEncoded() {
            return key.getBytes();
        }
        public String getAlgorithm() {
            return "Ymsg";
        }
        public String getFormat() {
            return "Ljava.lang.String";
        }
    };
}

/* */

