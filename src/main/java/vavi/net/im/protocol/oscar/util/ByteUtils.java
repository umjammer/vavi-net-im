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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


/**
 */
public class ByteUtils {
    
    /** Roaster array */
    private static byte[] ROASTER = {
                                        (byte) 0xF3, (byte) 0x26, (byte) 0x81,
                                        (byte) 0xC4, (byte) 0x39, (byte) 0x86,
                                        (byte) 0xDB, (byte) 0x92, (byte) 0x71,
                                        (byte) 0xA3, (byte) 0xB9, (byte) 0xE6,
                                        (byte) 0x53, (byte) 0x7A, (byte) 0x95,
                                        (byte) 0x7C
                                    };

    /** length of the roaster array */
    private static int ROAST_LEN = ROASTER.length;

    /**
     * Get unsigned short from the byte array starting in the position provided
     * @param byteArray byte array buffer
     * @param pos position to start extracting the unsigned short from
     * @return unsigned short (really an integer)
     */
    public static int getUShort(byte[] byteArray, int pos) {
        if ((byteArray.length - pos) < 2) {
            return -1;
        }

        return ((byteArray[pos] & 0xff) << 8) | (byteArray[pos + 1] & 0xff);
    }

    /**
     * Returns a short represented in a byte array
     *
     * @param value the value to be written to the returned array
     * @return a two-byte binary representation of the given unsigned value
     */
    public static byte[] getUShort(int value) {
        return new byte[] { (byte) ((value >> 8) & 0xff), (byte) (value & 0xff) };
    }

    /**
     * Get unsigned int from the byte array starting at the position provided
     * @param byteArray byte array buffer
     * @param pos position to start extracting the unsigned int from
     * @return unsigned long (need to cast to int)
     */
    public static long getUInt(byte[] byteArray, int pos) {
        if ((byteArray.length - pos) < 4) {
            return -1;
        }

        return ((byteArray[pos] & 0xffL) << 24) |
               ((byteArray[pos + 1] & 0xffL) << 16) |
               ((byteArray[pos + 2] & 0xffL) << 8) |
               ( byteArray[pos + 3] & 0xffL);
    }

    /**
     * Returns a byte array of four bytes representing the given unsigned value in binary
     * format.
     *
     * @param number the value to be written to the returned array
     * @return a four-byte binary representation of the given unsigned value
     */
    public static byte[] getUInt(long number) {
        return new byte[] {
                   (byte) ((number >> 24) & 0xff),
                   (byte) ((number >> 16) & 0xff), (byte) ((number >> 8) &
                   0xff), (byte) (number & 0xff)
               };
    }

    /**
     * Get ascii byte array for the string provided
     * @param str String we need ascii bytes array for
     * @return ascii byte array for the string provided
     */
    public static byte[] getBytes(String str) {
        try {
            return str.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException impossible) {
            return null;
        }
    }

    /**
     * Roast the users password
     * @param password users' password
     * @return byte array containing the roasted password
     */
    public static byte[] roast(String password) {
        byte[] pword = null;

        try {
            pword = password.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
        }

        byte[] roasted = new byte[pword.length];

        for (int i = 0, n = pword.length; i < n; i++) {
            roasted[i] = (byte) (pword[i] ^ ROASTER[i % ROAST_LEN]);
        }

        return roasted;
    }

    /**
     * Dump format the byte array to the output stream
     * @param data
     * @param writer output stream
     */
    public static void dump(byte[] data, Writer writer) {
        if ((data != null) && (writer != null)) {
            try {
                for (int i = 0; i < data.length; i++) {
                    if ((i != 0) && ((i % 16) == 0)) {
                        writer.write("\n");
                    }

                    String hexChar = Integer.toHexString(data[i]);

                    if (hexChar.length() == 1) {
                        writer.write("0");
                        writer.write(hexChar);
                    } else {
                        writer.write(hexChar.substring(hexChar.length() - 2,
                                                       hexChar.length()));
                    }

                    writer.write(" ");
                }
            } catch (IOException e) {
                //todo
            }
        }
    }

    /**
     * dump in hex the byte array using System.out.println
     * @param data
     */
    public static void dump(byte[] data) {
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if ((i != 0) && ((i % 16) == 0)) {
                    System.out.println();
                }

                String hexChar = Integer.toHexString(data[i]);

                if (hexChar.length() == 1) {
                    System.out.print("0");
                    System.out.print(hexChar);
                } else {
                    System.out.print(hexChar.substring(hexChar.length() - 2,
                                                       hexChar.length()));
                }

                System.out.print(" ");
            }
        }
    }

    /**
     * Return the hex string for the byte array provided
     * @param byteArray byte of array of data
     */
    public static String toHexString(byte[] byteArray) {
        if ((byteArray == null) || (byteArray.length == 0)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder(byteArray.length * 3);
            sb.append("\n");

            for (int i = 0; i < byteArray.length; i++) {
                if ((i != 0) && ((i % 16) == 0)) {
                    sb.append("\n");
                }

                String str = Integer.toHexString(byteArray[i]);

                if (str.length() < 2) {
                    sb.append("0");
                    sb.append(str);
                } else {
                    sb.append(str.substring(str.length() - 2, str.length()));
                }

                sb.append(" ");
            }

            return sb.toString();
        }
    }

    /**
     * String representation of a byte array
     * @param byteArray byte array
     */
    public static String toString(byte[] byteArray) {
        if ((byteArray == null) || (byteArray.length == 0)) {
            return "";
        }

        return ByteUtils.toString(byteArray, 0, byteArray.length);

//        if ((byteArray == null) || (byteArray.length == 0)) {
//            return "";
//        } else {
//
//            StringBuilder sb = new StringBuilder(byteArray.length);
//
//            for (int i = 0; i < byteArray.length; i++) {
//                sb.append((char) byteArray[i]);
//            }
//
//            return sb.toString();
//        }
    }

    /**
     * Get a string from the provided byte array, starting at the
     * specified position/offset for the length provided.
     * @param byteArray
     * @param pos
     * @param len
     * @return a string from the provided byte array
     */
    public static String toString(byte[] byteArray, int pos, int len) {
        if ((byteArray == null) || (byteArray.length == 0) || (len == 0)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder(len);

            //for (int i = pos; i < byteArray.length; i++) {
            for (int i = pos; i < (pos + len); i++) {
                sb.append((char) byteArray[i]);
            }

            return sb.toString();
        }
    }
}
