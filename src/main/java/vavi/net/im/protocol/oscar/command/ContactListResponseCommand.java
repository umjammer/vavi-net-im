/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.command;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vavi.net.im.protocol.oscar.util.ByteUtils;
import vavi.net.im.protocol.oscar.util.SSIItem;


/**
 * @author mikem
 */
public class ContactListResponseCommand extends Command {
    /** */
    private byte ssiVersion;
    /** */
    private int ssiCount;
    /** */
    private List<SSIItem> ssiList;

    public ContactListResponseCommand(Command cmd) {
        flapHdr = cmd.getFlapHdr();
        snacPacket = cmd.getSNAC();

        byte[] data = cmd.getSNACData();
        ssiVersion = data[0];
        ssiCount = ByteUtils.getUShort(data, 1);

        if (ssiCount > 0) {
            ssiList = new ArrayList<>(ssiCount);
        }

        // position of SSI Item data
        int pos = 3;

        for (int i = 0; i < ssiCount; i++) {
            // extract the SSIItems
            SSIItem item = new SSIItem(data, pos, data.length);
            ssiList.add(item);

            // now bump the pos index
            pos += item.getItemByteLength();
        }
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        // we don't write this command, we receive it, so nothing to do here!
    }

    /**
     * Returns a map of group name to list of buddy names
     * @return Map of group name to a list of buddys
     */
    public Map<String,List<String>> getGroups() {
        Map<String,List<String>> groupMap = new HashMap<>();
        Map<Integer,String> groupNameMap = new HashMap<>();

        if ((ssiList != null) && !ssiList.isEmpty()) {
            for (SSIItem item : ssiList) {
                int groupId = item.getGroupId();
                int itemType = item.getItemType();

                if (itemType == SSIItem.TYPE_GROUP) {
                    if (!groupNameMap.containsKey(groupId)) {
                        groupMap.put(item.getItemName(), new LinkedList<>());
                        groupNameMap.put(groupId,
                                item.getItemName());
                    }
                } else if (itemType == SSIItem.TYPE_BUDDY) {
                    String name = groupNameMap.get(groupId);
                    List<String> list = groupMap.get(name);
                    list.add(item.getItemName());
                }
            }
        }

        return groupMap;
    }
}
