/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg.auth;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Buffer. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 061105 nsano initial version <br>
 */
final class Buffer {

    /** */
    public int start;
    /** */
    public byte[] buffer;

    /** */
    private Buffer(int start, byte[] buffer) {
        this.start = start;
        this.buffer = buffer;
    }

    /** */
    public static Buffer[] type3list;
    /** */
    public static Buffer[] type4list;
    /** */
    public static Buffer[] type5list;

    /* TODO もうちょっとキレイにする */
    static {
        String[] names = {
            "type3.dat",
            "type4.dat",
            "type5.dat"
        };
        try {
            int n = 0;
            for (String name : names) {
//Debug.println("data: " + name);
                InputStream is = Buffer.class.getResourceAsStream("/vavi/net/im/protocol/ymsg/auth/" + name);
                Scanner scanner = new Scanner(is);
                int l = 0;
                List<Buffer> tmpBuffer = new ArrayList<Buffer>();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
//System.err.println("line: " + line);
                    Scanner scanner2 = new Scanner(line);
                    scanner2.useDelimiter("[,\\s]+");
                    int start = 0;
                    if (scanner2.hasNextInt(16)) {
                        start = scanner2.nextInt(16);
//System.err.printf("line %04d: %08x, ", l, start);
                    } else {
                        assert false : "no start";
                    }
                    List<Integer> tmpBuf = new ArrayList<Integer>();
                    while (scanner2.hasNextInt(16)) {
                        tmpBuf.add(scanner2.nextInt(16));
                    }
                    byte[] buf = new byte[tmpBuf.size()];
                    int c = 0;
                    for (int i : tmpBuf) {
                        buf[c] = (byte) i;
//System.err.printf("%02x, ", buf[c]);
                        c++;
                    }
                    Buffer buffer = new Buffer(start, buf);
                    tmpBuffer.add(buffer); 
//System.err.println();
                    l++;
                }
                switch (n) {
                case 0:
//System.err.println("type 3: " + tmpBuffer.size());
                    type3list = tmpBuffer.toArray(new Buffer[tmpBuffer.size()]);
                    break;
                case 1:
//System.err.println("type 4: " + tmpBuffer.size());
                    type4list = tmpBuffer.toArray(new Buffer[tmpBuffer.size()]);
                    break;
                case 2:
//System.err.println("type 5: " + tmpBuffer.size());
                    type5list = tmpBuffer.toArray(new Buffer[tmpBuffer.size()]);
                    break;
                };
                n++;
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    /** */
    public static void main(String[] args) {
    }
}

/* */
