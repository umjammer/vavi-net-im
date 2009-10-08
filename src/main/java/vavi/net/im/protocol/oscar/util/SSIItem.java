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

import java.util.List;


/**
 *
 */
public class SSIItem {
    public static final int TYPE_BUDDY = 0x0000;
    public static final int TYPE_GROUP = 0x0001;
    public static final int TYPE_PERMIT = 0x0002;
    public static final int TYPE_DENY = 0x0003;
    public static final int TYPE_PRIVACY = 0x0004;
    public static final int TYPE_VISIBILITY = 0x0005;
    private String itemName;
    private int groupId;
    private int itemId;
    private int itemType;
    private int additionalDataLen;
    private int itemByteLength;
    private List<?> tlvList;

    /** Constructor
     * @param data SNAC data
     * @param pos starting position/index in the btye array
     * @param len length of the byte array
     */
    public SSIItem(byte[] data, int pos, int len) {
        //todo add defensive checks here
        //  data == null ?
        //  pos not negative or past end of array
        //  len not negative
        if ((pos + 10) > len) {
            System.out.println("potential index error");
        }

        int startPos = pos;
        int nameLen = ByteUtils.getUShort(data, pos);
        pos += 2;
        itemName = ByteUtils.toString(data, pos, nameLen);

        if ((pos + nameLen) > len) {
            System.out.println("potential index error");
        }

        pos += nameLen;
        groupId = ByteUtils.getUShort(data, pos);
        pos += 2;
        itemId = ByteUtils.getUShort(data, pos);
        pos += 2;
        itemType = ByteUtils.getUShort(data, pos);
        pos += 2;
        additionalDataLen = ByteUtils.getUShort(data, pos);
        pos += 2;
        itemByteLength = pos - startPos + additionalDataLen;

        // now - iterate thru the 'additional data' building TLVs
        tlvList = TLV.getTLVs(data, pos, additionalDataLen);
    }

    /** Get the length, in bytes for this SSIItem
     * @return length, in bytes for this SSIItem
     */
    public int getItemByteLength() {
        return itemByteLength;
    }

    /** String representation of the Server Side Item
     * @return String representation of the Server Side Item
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("\nSSIItem: name=").append(itemName);
        sb.append(" groupId=").append(groupId);
        sb.append(" itemId=").append(itemId);
        sb.append(" itemType=").append(getItemTypeDisplay());

        if ((tlvList != null) && !tlvList.isEmpty()) {
            sb.append(" tlvList : ").append(tlvList);
        }

        return sb.toString();
    }

    /*
     * Get the string version of the item type, for display
     */
    private String getItemTypeDisplay() {
        String type = null;

        switch (itemType) {
        case TYPE_BUDDY:
            type = "Buddy";

            break;

        case TYPE_GROUP:
            type = "Group";

            break;

        case TYPE_DENY:
            type = "Deny";

            break;

        case TYPE_PRIVACY:
            type = "Privacy";

            break;

        case TYPE_VISIBILITY:
            type = "Visibility";

            break;

        default:
            type = "unknown(" + Integer.toHexString(itemType) + ")";

            break;
        }

        return type;
    }

    /** Get the group id
     * @return group id of the SSI Item
     */
    public int getGroupId() {
        return groupId;
    }

    /** get the item name
     * @return item name of the SSI Item
     */
    public String getItemName() {
        return itemName;
    }

    /** Get item type
     * @return item type of the SSI Item
     */
    public int getItemType() {
        return itemType;
    }
}
