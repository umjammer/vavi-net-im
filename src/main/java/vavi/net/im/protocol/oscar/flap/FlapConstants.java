/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.flap;


/**
 * @author mikem
 */
public class FlapConstants {
    // Static fields/initializers ---------------------------------------------
    // 1st byte of every flap
    public static byte FLAP_PREAMBLE = 0x2a;

    // flap channel values
    public static byte FLAP_CHANNEL_CONNECT = 0x01;
    public static byte FLAP_CHANNEL_SNAC = 0x02;
    public static byte FLAP_CHANNEL_ERROR = 0x03;
    public static byte FLAP_CHANNEL_DISCONNECT = 0x04;
    public static byte FLAP_CHANNEL_KEEP_ALIVE = 0x05;

    // max length of a FLAP Packet
    public static int FLAP_MAX_DATA_LENGTH = 0xffff;
}

/* */
