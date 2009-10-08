/*
 * 1998/01/28 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm;


/**
 * Byte Buffer Class.
 */
public class IpmByteBuffer {
    static protected final int BUF_SIZE = 8192;
    /** */
    private int eopindex = 0;
    protected byte[] buf = new byte[BUF_SIZE];
    protected int end = 0;

    /** */
    public IpmByteBuffer(byte[] argbuf) {
        System.arraycopy(argbuf, 0, buf, 0, argbuf.length);
        end = argbuf.length;
    }

    /** */
    public IpmByteBuffer() {
        super();
    }

    /** */
    public boolean eop() {
        if (end > 3) {
            for (int i = 2; i < end;) {
                if (buf[i] == 0) {
                    if (buf[i - 1] == 0) {
                        if (buf[i - 2] == 0) {
                            eopindex = i - 2;
                            return true;
                        } else {
                            i++;
                        }
                    } else {
                        i += 2;
                    }
                } else {
                    i += 3;
                }
            }
        }
        return false;
    }

    /** */
    public byte[] getBytes() {
        byte[] tmp = new byte[eopindex];
        end -= (eopindex + 3);
        System.arraycopy(buf, 0, tmp, 0, eopindex);
        System.arraycopy(buf, eopindex + 3, buf, 0, end);
        return tmp;
    }

    public void append(byte[] argbuf, int off, int len) {
        if (len > (buf.length - end)) {
            byte[] tmp = new byte[buf.length + BUF_SIZE];
            System.arraycopy(buf, 0, tmp, 0, end);
            buf = tmp;
        }
        System.arraycopy(argbuf, off, buf, end, len);
        end += len;
    }

    public void append(byte[] argbuf) {
        append(argbuf, 0, argbuf.length);
    }
}

/* */
