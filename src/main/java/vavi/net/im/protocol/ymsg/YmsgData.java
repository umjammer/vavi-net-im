/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import vavi.util.Debug;
import vavi.util.StringUtil;


/**
 * YmsgData.
 * <pre>
 * key ... 0x0c 0x80 value ... 0x0c 0x80
 * </pre>
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	040221	nsano	initial version <br>
 */
public class YmsgData {

    /** */
    public static final int KEY_YAHOO_ID = 0;
    /** */
    public static final int KEY_NICKNAME = 1;
    /** */
    public static final int KEY_CHALLENGE_STRING = 94;
    /** */
    public static final int KEY_RESPONSE_VALUE2 = 96;
    /** */
    public static final int KEY_RESPONSE_VALUE1 = 6;
    /** */
    public static final int KEY_UNKNOWN_002 = 2;
    /** */
    public static final int KEY_UNKNOWN_013 = 13;

    /** */
    private int key;
    /** value */
    private String value;
    /** length of value (encoded) */
    private int length;

    /** */
    private String encoding = "Windows-31J";

    /** */
    public YmsgData(int key, String value) {
        this.key = key;
        int keyLength = String.valueOf(key).getBytes().length;
        this.value = value;
        int valueLength = 0;
        try {
            valueLength = value.getBytes(encoding).length;
        } catch (UnsupportedEncodingException e) {
Debug.printStackTrace(e);
            valueLength = value.getBytes().length;
        }
        this.length = keyLength + 2 + valueLength + 2;
    }

    /** */
    public int getKey() {
        return key;
    }

    /** */
    public String getValue() {
        return value;
    }

    /** Returns whole length of value. */
    public int getLength() {
        return length;
    }

    /** */
    public byte[] toByteArray() {
        // key
        byte[] keyBytes;
        keyBytes = String.valueOf(key).getBytes();
        // value
        byte[] valueBytes;
        try {
            valueBytes = value.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
Debug.printStackTrace(e);
            valueBytes = value.getBytes();
        }

        // construct
        byte[] buffer = new byte[keyBytes.length + 2 + valueBytes.length + 2];
        int p = 0;

        System.arraycopy(keyBytes, 0, buffer, p, keyBytes.length);
        p += keyBytes.length;

        buffer[p++] = (byte) 0xc0;
        buffer[p++] = (byte) 0x80;

        System.arraycopy(valueBytes, 0, buffer, p, valueBytes.length);
        p += valueBytes.length;

        buffer[p++] = (byte) 0xc0;
        buffer[p++] = (byte) 0x80;

        return buffer;
    }

    /**
     * @throws IOException
     */
    public static YmsgData readFrom(InputStream is) throws IOException {

        int p = 0;
        byte[] buffer = new byte[Math.max(is.available(), 4096)]; // TODO check spec.

        int c1 = 0;
        do {
            c1 = is.read();
            if (c1 == -1) {
                throw new EOFException("key bytes not found");
            }
            buffer[p++] = (byte) c1;
        } while (c1 != 0xc0);

        int c2 = is.read();
        if (c2 != 0x80) {
Debug.println("key is not closed: " + StringUtil.toHex2(c2));
            throw new EOFException("data bytes not found");
        }
        buffer[p++] = (byte) c2;

        int key = Integer.parseInt(new String(buffer, 0, p - 2));
        int keyLength = p - 2;
//Debug.println("key: " + key);

        do {
            c1 = is.read();
            if (c1 == -1) {
                throw new EOFException("data bytes not found");
            }
            buffer[p++] = (byte) c1;
        } while (c1 != 0xc0);

        c2 = is.read();
        if (c2 != 0x80) {
            throw new EOFException("data is not closed: " + StringUtil.toHex2(c2));
        }
        buffer[p++] = (byte) c2;
//Debug.println("length: " + p);
//System.err.println("received:\n" + StringUtil.getDump(buffer, p));
        YmsgData yd = new YmsgData(key, new String(buffer, keyLength + 2, p - 2 - keyLength - 2));
        return yd;
    }
}

/* */
