/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * A <code>Connection</code> represents some sort of network connection
 * between the client machine and a server which provides instant messaging
 * services.
 *
 * @author  Raghu
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.1
 */
public interface Connection {
    /**
     * Returns the number of bytes that can be read from this connection
     * without blocking by the next caller of a method for this connection.
     * The next caller might be the same thread or or another thread.
     *
     * <p>
     * This method has exactly the same semantics as that of
     * <code>java.io.InputStream.available()</code>.
     *
     * @return the number of bytes that can be read from this connection without blocking.
     * @throws IOException if an I/O error occurs.
     */
    int available() throws IOException;

    /**
     * Reads the next byte of data from the input stream. The value byte is returned as an
     * int in the range 0 to 255. If no byte is available because the end of the stream has
     * been reached, the value -1 is returned. This method blocks until input data is
     * available, the end of the stream is detected, or an exception is thrown.
     *
     * @return the next byte of data, or -1 if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    int read() throws IOException;

    /**
     * Reads some number of bytes from the connection and stores them into
     * the buffer array b. The number of bytes actually read is returned as
     * an integer. If no bytes are available because of EOF, returns -1.
     * This call has exactly the same semantics as that of
     * <code>java.io.InputStream.read(byte[] b)</code>.
     *
     * @param b             The buffer to which data is read.
     * @return              The total number of bytes read into the buffer, or -1 if there
     *                      is no more data because the end of the stream has been reached.
     * @throws IOException  If an I/O error occurs.
     */
    int read(byte[] b) throws IOException;

    /**
     * Reads up to len bytes of data from the connection into an array of bytes.
     * An attempt is made to read as many as len bytes, but a smaller number may
     * be read, possibly zero. The number of bytes actually read is returned as
     * an integer. If no bytes are available because of EOF, returns -1.
     * This call has exactly the same semantics as that of
     * <code>java.io.InputStream.read(byte[] b, int off, int len)</code>.
     *
     * @param b             The buffer to which data is read.
     * @param off           The start offset in array <code>b</code> at which data is written.
     * @param len           The maximum number of bytes to be read.
     * @return              The total number of bytes read into the buffer, or -1 if there
     *                      is no more data because the end of the stream has been reached.
     * @throws IOException  If an I/O error occurs.
     */
    int read(byte[] b, int off, int len) throws IOException;

    /**
     * Writes b.length bytes from the specified byte array to this connection.
     *
     * @param b The data to be sent through this connection.
     * @throws IOException If an I/O error occurs.
     */
    void write(byte[] b) throws IOException;

    /**
     * Flushes this connection and forces any buffered output bytes to be
     * written out. The general contract of flush is that calling it is an
     * indication that, if any bytes previously written have been buffered by
     * the implementation of the connection, such bytes should immediately be
     * written to their intended destination.
     *
     * @throws IOException If an I/O error occurs.
     */
    void flush() throws IOException;

    /**
     * Closes this connection and releases any resources associated with it.
     *
     * @throws IOException If an I/O error occurs.
     */
    void close() throws IOException;

    /**
     * Returns an InputStream that can be used to indirectly call the
     * read methods.
     *
     * @return An InputStream for reading from this connection.
     */
    InputStream getInputStream();

    /**
     * Returns an OutputStream that can be used to indirectly call the
     * read methods.
     *
     * @return An OutputStream for writing to this connection.
     */
    OutputStream getOutputStream();
}

/* */
