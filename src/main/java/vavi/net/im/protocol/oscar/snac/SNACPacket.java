/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.snac;

import java.io.IOException;
import java.io.OutputStream;

import vavi.net.im.protocol.oscar.util.ByteUtils;


/**
 * @author mikem
 */
public class SNACPacket {

    /** */
    private static byte DEFAULT_FLAG = 0x00;
    /** */
    public static final int SNACPACKET_LENGTH = 10;

    private short familyId;
    private short subTypeId;
    private byte flag1;
    private byte flag2;
    private int requestId;
    private byte[] snacData;

    /** */
    public SNACPacket(int family, int subtype) {
        this(family, subtype, DEFAULT_FLAG, DEFAULT_FLAG);
    }

    /** */
    public SNACPacket(int family, int subtype, byte flag1, byte flag2) {
        familyId = (short) family;
        subTypeId = (short) subtype;
        this.flag1 = flag1;
        this.flag2 = flag2;
        requestId = 0;
    }

    /** */
    public SNACPacket(byte[] data) {
        if ((data == null) || (data.length < SNACPACKET_LENGTH)) {
            throw new IllegalArgumentException("SNAC data too short");
        }

        familyId = (short) ByteUtils.getUShort(data, 0);
        subTypeId = (short) ByteUtils.getUShort(data, 2);
        flag1 = data[4];
        flag2 = data[5];
        requestId = (int) ByteUtils.getUInt(data, 6);

        // create byte[] for the snacData
        if (data.length > SNACPACKET_LENGTH) {
            // handle case when bit 8(or 7 counting from 0) is set
            // we need to strip out the 2 bytes for length
            // and length bytes for the unknown data
            if ((flag1 & 0x80) != 0) {
                int unknownDataLen = ByteUtils.getUShort(data, SNACPACKET_LENGTH);
                int pos = unknownDataLen + 2;
                snacData = new byte[data.length - SNACPACKET_LENGTH - 2 -
                           unknownDataLen];
                System.arraycopy(data, SNACPACKET_LENGTH + pos, snacData, 0,
                                 snacData.length);
            } else {
                snacData = new byte[data.length - SNACPACKET_LENGTH];
                System.arraycopy(data, SNACPACKET_LENGTH, snacData, 0,
                                 snacData.length);
            }
        }
    }

    /**
     * Returns the family id of the SNACPacket.
     * @return the family id of the SNACPacket
     */
    public short getFamilyId() {
        return familyId;
    }

    /**
     * Returns the first flag value.
     * @return the first flag value
     */
    public byte getFlag1() {
        return flag1;
    }

    /**
     * Returns the second flag value.
     * @return the second flag value
     */
    public byte getFlag2() {
        return flag2;
    }

    /**
     * Returns the request id of the SNACPacket.
     * @return the request id of the SNACPacket
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Returns the sub type id of the SNACPacket.
     * @return the sub type id of the SNACPacket
     */
    public short getSubTypeId() {
        return subTypeId;
    }

    /** */
    public int getByteLength() {
        return 0;
    }

    /** */
    public byte[] getSNACData() {
        return snacData;
    }

    /** */
    public void setSNACData(byte[] data) {
        snacData = data;
    }

    /** */
    public byte[] getBytes() {
        byte[] bytes = new byte[SNACPACKET_LENGTH];

        bytes[0] = (byte) ((familyId >> 8) & 0xff);
        bytes[1] = (byte) (familyId & 0xff);
        bytes[2] = (byte) ((subTypeId >> 8) & 0xff);
        bytes[3] = (byte) (subTypeId & 0xff);
        bytes[4] = flag1;
        bytes[5] = flag2;
        bytes[6] = (byte) ((requestId >> 24) & 0xff);
        bytes[7] = (byte) ((requestId >> 16) & 0xff);
        bytes[8] = (byte) ((requestId >> 8) & 0xff);
        bytes[9] = (byte) (requestId & 0xff);

        return bytes;
    }

    /**
     * @see vavi.net.im.protocol.oscar.command.Command#writeCommandData(java.io.OutputStream)
     */
    public void write(OutputStream os) throws IOException {
        // write out the SNAC
        os.write(familyId);
        os.write(subTypeId);
        os.write(flag1);
        os.write(flag2);
        os.write(requestId);
    }

    /**
     * Returns a string representation of the SNACPacket.
     * @return a string representation of the SNACPacket
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("SNAC: ");
        sb.append("familyId=0x").append(Integer.toHexString(familyId)).append(" ");
        sb.append("subTypeId=0x").append(Integer.toHexString(subTypeId)).append(" ");
        sb.append("flag1=").append(flag1).append(" ");
        sb.append("flag2=").append(flag2).append(" ");
        sb.append("requestId=").append(requestId);

        return sb.toString();
    }
}

/* */
