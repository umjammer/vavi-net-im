/*
 * 1997/10/16 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;

import vavi.net.im.protocol.ipm.event.CommunicationEvent;
import vavi.net.im.protocol.ipm.event.CommunicationListener;
import vavi.net.im.protocol.ipm.event.ProxyEvent;


/**
 * IP Messenger Proxy Class
 */
public class IpmProxy implements CommunicationListener {
    private static final long KEEPALIVE_INTERVAL = 30000;

    public static final int PROXY_PORT = 2425;

    Ipmessenger ipmsg;

    private boolean active = false;

    private boolean broadcastAll = false;

    private InetAddress localAddress;

    private InetAddress proxyAddress;

    private DataOutputStream dos;

    private Map<DataOutputStream, DataOutputStream> doss = new HashMap<DataOutputStream, DataOutputStream>();

    private Map<Integer, DatagramSocket> sockets = new HashMap<Integer, DatagramSocket>();

    private BidiMap IPtoPORT = new TreeBidiMap();

    class Accept extends Thread {
        public Accept() {
            this.start();
        }

        public void run() {
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(PROXY_PORT, 1024, localAddress);
                while (true) {
                    Socket sock = ss.accept();
                    connect(sock);
                    ipmsg.refreshList();
                }
            } catch (IOException e) {
            }
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException e) {
            }
        }
    }

    class Listen extends Thread {
        private DataInputStream dis;

        public Listen(DataInputStream dis) {
            this.dis = dis;
            this.start();
        }

        public void run() {
            byte[] buf = new byte[8192];
            IpmByteBuffer bb = new IpmByteBuffer();
            outer: while (!isInterrupted()) {
                while (!bb.eop()) {
                    int count = 0;
                    try {
                        count = dis.read(buf);
                    } catch (IOException ex) {
                        break outer;
                    }
                    if (count == -1) {
                        break outer;
                    }
                    bb.append(buf, 0, count);
                }

                byte[] tmpbuf = bb.getBytes();
                if (tmpbuf.length == 0) {
                    continue; // keep alive.
                }

                ProxyEvent event = new ProxyEvent(this, tmpbuf);
                accept(event);
            }
            try {
                dis.close();
            } catch (IOException e) {
            }
        }
    }

    class KeepAlive extends Thread {
        public KeepAlive() {
            this.start();
        }

        public void run() {
            try {
                while (!isInterrupted()) {
                    sleep(KEEPALIVE_INTERVAL);
                    while (active) {
                        try {
                            if (dos != null) {
                                synchronized (dos) {
                                    dos.write(new byte[] {
                                        0, 0, 0
                                    });
                                    dos.flush();
                                }
                                break;
                            }
                        } catch (IOException ex) {
                            try {
                                if (dos != null) {
                                    dos.close();
                                }
                            } catch (IOException exx) {
                            }
                            dos = null;
                        }
                        connectProxy();
                    }
                }
            } catch (Throwable ex) {
            }
        }
    }

    void connect(Socket socket) throws IOException {
        DataOutputStream tmpDos = new DataOutputStream(socket.getOutputStream());
        doss.put(tmpDos, tmpDos);

        DataInputStream tmpDis = new DataInputStream(socket.getInputStream());
        new Listen(tmpDis);
    }

    synchronized void accept(ProxyEvent event) {
        DatagramSocket socket;
        if (IPtoPORT.containsValue(event.getFromAddress().toString())) {
            socket = sockets.get(IPtoPORT.getKey(event.getFromAddress().toString()));
        } else {
            int port = 0;
            Random random = new Random(System.currentTimeMillis());
            while (true) {
                /* 49154 - 65534 */
                port = (int) ((random.nextLong() % 8192) + 57342);
                if (IPtoPORT.containsKey(new Integer(port))) {
                    continue;
                }
                try {
                    socket = new DatagramSocket(port);
                } catch (SocketException e) {
                    continue;
                }
                break;
            }
            IPtoPORT.put(new Integer(port), event.getFromAddress().toString());
            sockets.put(new Integer(port), socket);

            Receiver tmpReceiver = new Receiver(socket);
            tmpReceiver.addIPMComListener(this);
            new Thread(tmpReceiver).start();
        }
        try {
            if (event.getToAddress().getAddress().equals(InetAddress.getByName("255.255.255.255")) && broadcastAll) {
                InetSocketAddress[] addresses = ipmsg.getBroadcastAddr();
                for (int i = 0; i < addresses.length; i++) {
                    DatagramPacket packet = new DatagramPacket(event.getPacket().getBytes(), event.getPacket().getBytes().length, addresses[i].getAddress(), addresses[i].getPort());
                    socket.send(packet);
                }
            } else {
                DatagramPacket packet = new DatagramPacket(event.getPacket().getBytes(), event.getPacket().getBytes().length, event.getToAddress().getAddress(), event.getToAddress().getPort());
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    synchronized void connectProxy() throws IOException {
        if (dos != null) {
            return;
        }

        Socket sock = new Socket(proxyAddress, PROXY_PORT, localAddress, PROXY_PORT);
        dos = new DataOutputStream(sock.getOutputStream());

        DataInputStream tmpdin = new DataInputStream(sock.getInputStream());
        new Listen(tmpdin);
    }

    synchronized void write(byte[] buf) {
        boolean done = false;
        while ((dos != null) && !done) {
            synchronized (dos) {
                try {
                    dos.write(buf);
                    dos.flush();
                    done = true;
                } catch (IOException e) {
                    try {
                        dos.close();
                    } catch (IOException e2) {
                    }
                    dos = null;
                }
            }
            if (active && !done) {
                try {
                    connectProxy();
                } catch (Exception e) {
                    break;
                }
            }
        }

        for (DataOutputStream tmpDos : doss.keySet()) {
            try {
                tmpDos.write(buf);
                tmpDos.flush();
            } catch (IOException e) {
                try {
                    tmpDos.close();
                } catch (IOException e2) {
                }
                doss.remove(tmpDos);
            }
        }
    }

    public IpmProxy(Ipmessenger ipmsg, InetAddress proxy, boolean broadcastAll) {
        this.ipmsg = ipmsg;
        this.broadcastAll = broadcastAll;
        localAddress = ipmsg.getLocalAddress();
        new Accept();
        if (proxy == null) {
            active = false;
        } else {
            active = true;
            proxyAddress = proxy;
            try {
                connectProxy();
            } catch (Exception e) {
            }
            new KeepAlive();
        }
    }

    public IpmProxy(Ipmessenger argipmsg) {
        this(argipmsg, null, false);
    }

    public void addBroadcastPort(int port) {
        IPtoPORT.put(new Integer(port), "255.255.255.255:" + port);
    }

    public void receive(CommunicationEvent event) {
        try {
            if (event.getAddress().getAddress().equals(InetAddress.getLocalHost()) && (IPtoPORT.get(new Integer(event.getAddress().getPort())) != null)) {
                return;
            }
        } catch (UnknownHostException e) {
        }
        if (IPtoPORT.getKey(event.getAddress().toString()) != null) {
            return;
        }

        ByteBuffer bb = ByteBuffer.allocate(1024);
        String prefix = event.getAddress().toString() + ":" + (String) IPtoPORT.get(new Integer(event.getLocalPort())) + ":";
        bb.put(prefix.getBytes());
        bb.put(event.getPacket().getBytes());
        bb.put(new byte[] {
            0, 0, 0
        });
        write(bb.array());
    }
}

/* */
