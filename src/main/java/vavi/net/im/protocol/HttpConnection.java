/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;


/**
 * An <code>HTTPConnection</code> connects to a specified host on a
 * specified port by establishing an HTTP/1.1 tunnel. This uses
 * HTTP CONNECT to create a persistent connection and uses that
 * to send and receive data.
 *
 * @author  Raghu
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.1
 */
public class HttpConnection implements Connection {
    /**
     * The socket representing this connection.
     */
    private Socket sock;

    /**
     * The input stream of the connection.
     */
    private InputStream in;

    /**
     * The output stream of the connection.
     */
    protected OutputStream out;

    /**
     * Construct an HTTP connection to the specified host and port through an
     * HTTP 1.1 proxy. The proxy optionally may require a user name and
     * password for authentication. In that case, use the
     * {@link #HttpConnection(String, int, String, String, String, int)
     * HttpConnection(proxyHost, proxyPort, username, password, host, port)}
     * constructor.
     *
     * @param proxyHost The HTTP proxy host name
     * @param proxyPort The HTTP proxy port number
     * @param host      The host name to be connected to.
     * @param port      The port number to be connected on.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     * @throws IOException          If an I/O error occurs when creating the connection.
     * @throws SecurityException    If a security manager exists and its checkConnect
     *                              method doesn't allow the operation.
     */
    public HttpConnection(String proxyHost, int proxyPort, String host, int port)
        throws UnknownHostException, IOException {
        this(proxyHost, proxyPort, null, null, host, port);
    }

    /**
     * Construct an HTTP connection to the specified host and port through an
     * HTTP 1.1 proxy which requires authentication. The proxy optionally may
     * require a user name and password for authentication. Those can be passed
     * to this constructor. If the proxy does not require a user name and
     * password, use the {@link #HttpConnection(String, int, String, int)
     * HttpConnection(proxyHost, proxyPort, host, port)} constructor.
     *
     * @param proxyHost The HTTP proxy host name
     * @param proxyPort The HTTP proxy port number
     * @param username  The username to connect to the proxy.
     * @param password  The password to connect to the proxy.
     * @param host      The host name to be connected to.
     * @param port      The port number to be connected on.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     * @throws IOException          If an I/O error occurs when creating the connection.
     * @throws SecurityException    If a security manager exists and its checkConnect
     *                              method doesn't allow the operation.
     */
    public HttpConnection(String proxyHost, int proxyPort, String username,
                          String password, String host, int port)
        throws UnknownHostException, IOException {
        // connect to HTTP proxy
        sock = new Socket(proxyHost, proxyPort);
        in = sock.getInputStream();
        out = sock.getOutputStream();

        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        PrintStream wr = new PrintStream(out);

        // send the CONNECT request
        wr.print("CONNECT " + host + ":" + port + " HTTP/1.1\r\n");
        wr.print("Host: " + host + ":" + port + "\r\n");

        if ((username != null) && (password != null)) {
            wr.print("Proxy-Authorization: Basic " +
                    Arrays.toString(Base64.encodeBase64((username + ":" + password).getBytes())) + "\r\n");
        }

        wr.print("\r\n");

        String line = rd.readLine();

        if (line == null) {
            throw new IOException("Proxy closed connection");
        }

        if (!line.startsWith("HTTP/1.0 200") &&
            !line.startsWith("HTTP/1.1 200")) {
            throw new IOException("Cannot CONNECT, Proxy returned " + line);
        }

        // skip the remaining of the HTTP response
        while ((line != null) && !line.equals("")) {
            line = rd.readLine();
        }
    }

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
    public int available() throws IOException {
        return in.available();
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is returned as an
     * int in the range 0 to 255. If no byte is available because the end of the stream has
     * been reached, the value -1 is returned. This method blocks until input data is
     * available, the end of the stream is detected, or an exception is thrown.
     *
     * @return the next byte of data, or -1 if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read() throws IOException {
        return in.read();
    }

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
    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

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
    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    /**
     * Writes b.length bytes from the specified byte array to this connection.
     *
     * @param b The data to be sent through this connection.
     * @throws IOException If an I/O error occurs.
     */
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    /**
     * Flushes this connection and forces any buffered output bytes to be
     * written out. The general contract of flush is that calling it is an
     * indication that, if any bytes previously written have been buffered by
     * the implementation of the connection, such bytes should immediately be
     * written to their intended destination.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void flush() throws IOException {
        out.flush();
    }

    /**
     * Closes this connection and releases any resources associated with it.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void close() throws IOException {
        in.close();
        out.close();
        sock.close();
    }

    /**
     * Returns an InputStream that can be used to indirectly call the
     * read methods.
     *
     * @return An InputStream for reading from this connection.
     */
    public InputStream getInputStream() {
        return in;
    }

    /** */
    public OutputStream getOutputStream() {
        return out;
    }
}

/* */
