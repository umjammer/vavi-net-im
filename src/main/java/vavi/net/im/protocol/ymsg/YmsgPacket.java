/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import vavi.net.im.protocol.ymsg.YmsgPacketHeader.Event;
import vavi.util.Debug;


/**
 * YmsgPacket.
 * <pre>
 * header (20 bytes)
 * data1 ↑
 * data2 ｜
 *  :    {@link YmsgPacketHeader#getLength()}
 * dataN ↓
 * </pre>
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040221 nsano initial version <br>
 */
public class YmsgPacket {

    /** */
    private YmsgPacketHeader header;

    /** <YmsgData> */
    private List<YmsgData> dataList = new ArrayList<YmsgData>();

    /** 
     * @param version
     * @param id
     * @param service
     * @param status
     */
    public YmsgPacket(int version,
                      int id,
                      Event service,
                      int status) {

        this.header = new YmsgPacketHeader(version, id, service, status);
    }

    /** */
    private YmsgPacket() {
    }

    /** */
    public YmsgPacketHeader getHeader() {
        return header;
    }

    /** Returns whole length of a packet */
    public int getLength() {
        return YmsgPacketHeader.LENGTH + header.getLength();
    }

    /** */
    public void addData(YmsgData yd) {
        dataList.add(yd);
        header.setLength(header.getLength() + yd.getLength());
//Debug.println("header length: " + header.getLength());
    }

    /** */
    public List<YmsgData> getDataList() {
        return dataList;
    }

    /**
     * throws NoSuchElementException
     * @return null When data does not exists.
     */
    public String getDataValueByKey(int key) {
        for (int i = 0; i < dataList.size(); i++) {
            YmsgData yd = dataList.get(i);
            
            if (yd.getKey() == key) {
                return yd.getValue();
            }
        }
        
//	throw new NoSuchElementException(String.valueOf(key));
        return null;
    }
    
    /** */
    public static YmsgPacket readFrom(InputStream is) throws IOException {

        YmsgPacket yp = new YmsgPacket();

        yp.header = YmsgPacketHeader.readFrom(is);
//Debug.println("header: " + StringUtil.paramString(yp.header));

        int l = 0;
        while (l < yp.header.getLength()) {
            YmsgData yd = YmsgData.readFrom(is);
            l += yd.getLength();
            if (yd == null) {
                continue;
            }
//Debug.println("data: " + StringUtil.paramString(yd) + ", " + l + "/" + yp.header.getLength());
            yp.dataList.add(yd);
            if ((yp.header.getLength() - l) < 4) {
Debug.println("garbage: " + (yp.header.getLength() - l) + " bytes");
                break;
            }
        }

        // garbage
        for (int i = 0; i < yp.header.getLength() - l; i++) {
            is.read();
        }

        return yp;
    }

    /** */
    public byte[] toByteArray() {
        int p = 0;
        byte[] buffer = new byte[getLength()];
//Debug.println("packet buffer: " + buffer.length);

        // ヘッダ
        byte[] headerBuffer = header.toByteArray();
        System.arraycopy(headerBuffer, 0, buffer, p, headerBuffer.length);
        p += headerBuffer.length;
//Debug.println("p: header: " + p);
        // パケット本体
        for (int i = 0; i < dataList.size(); i++) {
            YmsgData yd = dataList.get(i);
//Debug.println("p: data(" + i + "): " + p + ", " + yd.getLength() + "/" + buffer.length);
            System.arraycopy(yd.toByteArray(), 0, buffer, p, yd.getLength());
            p += yd.getLength();
//Debug.println("p: data(" + i + "): " + p);
        }

//Debug.println("sending:\n" + StringUtil.paramStringDeep(this));
//Debug.println("sending:\n" + StringUtil.getDump(buffer));
        return buffer;
    }
}

/* */
