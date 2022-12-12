/*
 *
 * Hamsam - Instant Messaging API
 *
 * Copyright (C) 2003 Mike Miller <mikemil@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
package vavi.net.im.protocol.oscar.util;

import java.util.LinkedList;
import java.util.List;


/**
 * @author mike
 */
public class TLV {
    private static int BASE_LENGTH = 4;
    private int type;
    private int length;
    private byte[] value;

    /**
     * TLV constructor
     * @param type TLV defined type
     * @param byteArray byte array
     */
    public TLV(int type, byte[] byteArray) {
        this.type = type;
        value = byteArray;

        if (byteArray == null) {
            length = 0;
        } else {
            length = byteArray.length;
        }
    }

    /**
     * TLV Constructor
     * @param type TLV defined type
     * @param str String data value
     */
    public TLV(int type, String str) {
        this.type = type;
        value = str.getBytes();

        if (value == null) {
            length = 0;
        } else {
            length = value.length;
        }
    }

    /**
     * Construct a TLV from byte array
     * @param inputdata byte array read from network
     * @param pos starting position within the byte array
     */
    public TLV(byte[] inputdata, int pos) {
        type = ByteUtils.getUShort(inputdata, pos);
        length = ByteUtils.getUShort(inputdata, pos + 2);
        value = new byte[length];

        if (length > 0) {
            System.arraycopy(inputdata, pos + 4, value, 0, length);
        }
    }

    /** Get the data portion of the the TLV
     * @return byte array of the data
     */
    public byte[] getValue() {
        return value;
    }

    /** Get the data portion of the the TLV as a string
     * @return String value
     */
    public String getStringValue() {
        return new String(value);
    }

    /** Ge the length of the data component
     * @return length of the data component
     */
    public int getLength() {
        return length;
    }

    /** Get the TLV type
     * @return TLV type
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the total length of the TLV.
     * @return total length of the TLV, include type and length fields.
     */
    public int getTotalLength() {
        if (value == null) {
            return BASE_LENGTH;
        } else {
            return BASE_LENGTH + value.length;
        }
    }

    /**
     * Gets a byte array representing the TLV
     * @return byte array representing the TLV
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[BASE_LENGTH + length];
        bytes[0] = (byte) ((type >> 8) & 0xff);
        bytes[1] = (byte) (type & 0xff);
        bytes[2] = (byte) ((length >> 8) & 0xff);
        bytes[3] = (byte) (length & 0xff);

        for (int i = BASE_LENGTH; i < bytes.length; i++) {
            bytes[i] = value[i - BASE_LENGTH];
        }

        return bytes;
    }

    /**
     * Creates a list of TLV objects from a byte array
     * @param byteArray byte array read from the network
     * @param pos starting position in the
     * @return tlvs
     */
    public static List<TLV> getTLVs(byte[] byteArray, int pos) {
        return getTLVs(byteArray, pos, byteArray.length);
    }

//    public static List getTLVs(byte[] byteArray, int pos) {
//
//        List list = null;
//
//        if (byteArray != null) {
//            list = new LinkedList();
//
//            int length = byteArray.length - pos;
//
//            for (int i = pos; i < length;) {
//
//                int tlvlen = ByteUtils.getUShort(byteArray, i + 2);
//                list.add(new TLV(byteArray, i));
//                i += (4 + tlvlen);
//            }
//        }
//
//        return list;
//    }

    /**
     * Creates a list of TLV objects from a byte array
     * @param byteArray byte array read from the network
     * @param pos starting position in the
     * @param len length of the data to process
     * @return tlvs
     */
    public static List<TLV> getTLVs(byte[] byteArray, int pos, int len) {
        List<TLV> list = null;

        if (byteArray != null) {
            list = new LinkedList<>();

            int length = len - pos;

            //for (int i = pos; i < pos+len;) {
            for (int i = pos; i < length;) {
                int tlvlen = ByteUtils.getUShort(byteArray, i + 2);
                list.add(new TLV(byteArray, i));
                i += (4 + tlvlen);
            }
        }

        return list;
    }

    /**
     * Returns string representation of the TLV
     * @return String representation of the TLV
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String sb = "\nTLV : type=0x" + Integer.toHexString(type) + " length=" +
                length + " string value=" + ByteUtils.toString(value) +
                " value=" + ByteUtils.toHexString(value);

        return sb;
    }
}
