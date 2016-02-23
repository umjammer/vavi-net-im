/*
 * 1997/10/3 (C) Copyright T.Kazawa
 */

package vavi.net.im.protocol.ipm;

import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import vavi.net.im.protocol.ipm.event.CommunicationEvent;
import vavi.net.im.protocol.ipm.event.CommunicationListener;
import vavi.net.im.protocol.ipm.event.IpmEvent;
import vavi.net.im.protocol.ipm.event.IpmListener;
import vavi.net.im.protocol.ipm.event.ProxyEvent;


/**
 * IP Messenger Main Class
 */
public class Ipmessenger implements CommunicationListener {

    public enum Constant {
        /** */
        NOOPERATION(0x00000000L),
        /** */
        BR_ENTRY(0x00000001L),
        /** */
        BR_EXIT(0x00000002L),
        /** */
        ANSENTRY(0x00000003L),
        /** */
        BR_ABSENCE(0x00000004L),
        /** */
        BR_ISGETLIST(0x00000018L),
        /** */
        OKGETLIST(0x00000015L),
        /** */
        GETLIST(0x00000016L),
        /** */
        ANSLIST(0x00000017L),
        /** */
        SENDMSG(0x00000020L),
        /** */
        RECVMSG(0x00000021L),
        /** */
        READMSG(0x00000030L),
        /** */
        DELMSG(0x00000031L),
        /** */
        GETINFO(0x00000040L),
        /** */
        SENDINFO(0x00000041L),
        /** */
        GETPUBKEY(0x00000072L),
        /** */
        ANSPUBKEY(0x00000073L),

        // other opt
        /** */
        ABSENCEOPT(0x00000100L),
        /** */
        SERVEROPT(0x00000200L),
        /** */
        DIALUPOPT(0x00010000L),
        /** */
        ENCRYPTOPT(0x00400000L),
        /** */
        ENCRYPTOPTOLD(0x00800000L),

        // send opt
        /** */
        SENDCHECKOPT(0x00000100L),
        /** */
        SECRETOPT(0x00000200L),
        /** */
        BROADCASTOPT(0x00000400L),
        /** */
        MULTICASTOPT(0x00000800L),
        /** */
        NOPOPUPOPT(0x00001000L),
        /** */
        AUTORETOPT(0x00002000L),
        /** */
        RETRYOPT(0x00004000L),
        /** */
        PASSWORDOPT(0x00008000L),
        /** */
        NOLOGOPT(0x00020000L),
        /** */
        NEWMUTIOPT(0x00040000L),

        // encrypt opt
        /** */
        RSA_512(0x00000001L),
        /** */
        RSA_1024(0x00000002L),
        /** */
        RSA_2048(0x00000004L),
        /** */
        RC2_40(0x00001000L),
        /** */
        RC2_128(0x00004000L),
        /** */
        RC2_256(0x00008000L),
        /** */
        BLOWFISH_128(0x00020000L),
        /** */
        BLOWFISH_256(0x00040000L);

        /** */
        static final long COMMASK = 0x000000ffL;
        /** */
        public static final long OPTMASK = 0xffffff00L;
        /** */
        private long value;
        /** */
        Constant(long value) {
            this.value = value;
        }
        /** */
        public long getValue() {
            return value;
        }
        static Constant commandOf(long value) {
            for (int i = NOOPERATION.ordinal(); i < ANSPUBKEY.ordinal(); i++) {
                if (values()[i].value == (value & COMMASK)) {
                    return values()[i];
                }
            }
            return null;
        }
    };

    public static final String encoding = "Windows-31J";

    public static final int MAXBUF = 8192;

    // end
    private static final String P_FILE = "runtime.properties";

    /** */
    private static ResourceBundle rb = ResourceBundle.getBundle("vavi.net.im.protocol.ipm.resources");

    /** 設定ファイル */
    private static Preferences userPrefs = Preferences.userNodeForPackage(Ipmessenger.class);

    /** 設定ファイル */
    private static Preferences systemPrefs = Preferences.systemNodeForPackage(Ipmessenger.class);

    private boolean state = false;

    private InetAddress localAddress;

    private long serial = 0;

    private String user;

    private String host;

    private Map<String, CommunicationEvent> users = new HashMap<String, CommunicationEvent>();

    private Map<String, InetSocketAddress> dialupMember = new HashMap<String, InetSocketAddress>();

    private Map<IpmListener, IpmListener> listeners = new HashMap<IpmListener, IpmListener>();

    private Map<Long, NormalSender> recentSents = new HashMap<Long, NormalSender>();

    private int port;

    private int[] ports;

    private DatagramSocket socket;

    private IpmProxy proxy;

    private int receiveCount = 0;

    private class CryptoInfo {
        private long cap = 0L;

        private PublicKey publicKey = null;

        public CryptoInfo(long cap, PublicKey publicKey) {
            this.cap = cap;
            this.publicKey = publicKey;
        }

        public long getCap() {
            return cap;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }
    }

    private boolean hasJCE = true;

    private Map<String, CryptoInfo> publicKeys = new HashMap<String, CryptoInfo>();

    private PublicKey publicKey = null;

    private PrivateKey privateKey = null;

    private long getCryptoCaps() {
        return Constant.RSA_512.value | Constant.RSA_1024.value | Constant.RC2_40.value | Constant.RC2_128.value | Constant.BLOWFISH_128.value;
    }

    private String makeKey(CommunicationEvent event) {
        IpmPacket packet = event.getPacket();
        String key = event.getAddress() + ":" + packet.getUser() + ":" + packet.getHost();
        return key;
    }

    public void receive(CommunicationEvent event) throws IOException {
        if (!state) {
            return;
        }

        IpmPacket packet = event.getPacket();
        long opt = packet.getCommand() & Constant.OPTMASK;

        switch (Constant.commandOf(packet.getCommand())) {
        case BR_ENTRY:

            String nickname = rb.getString("nickName");
            Sender.send(socket, makePack(Constant.ANSENTRY.value | getEntryOpt(), nickname, true), event.getAddress());
        case ANSENTRY:
            if (publicKey != null) {
                Sender.send(socket, makePack(Constant.GETPUBKEY.value, Long.toString(getCryptoCaps(), 16).toUpperCase(), false), event.getAddress());
            }
        case BR_ABSENCE:
            users.put(makeKey(event), event);
            if (((opt & Constant.DIALUPOPT.value) != 0) && (dialupMember.get(event.getAddress().toString()) == null)) {
                dialupMember.put(event.getAddress().toString(), event.getAddress());
            }

            IpmEvent ie = new IpmEvent(this, IpmEvent.Id.UPDATE_LIST, new Date(System.currentTimeMillis()), event);
            invokeListener(ie);
            break;
        case BR_EXIT:
            users.remove(makeKey(event));
            dialupMember.remove(event.getAddress().toString());
            publicKeys.remove(event.getAddress().toString());
            ie = new IpmEvent(this, IpmEvent.Id.UPDATE_LIST, new Date(System.currentTimeMillis()), event);
            invokeListener(ie);
            break;
        case SENDMSG:
            if ((opt & Constant.SENDCHECKOPT.value) != 0) {
                Sender.send(socket, makePack(Constant.RECVMSG.value | Constant.AUTORETOPT.value, new Long(packet.getNo()).toString(), false), event.getAddress());
            }
            if (new Boolean(rb.getString("absenceState")).booleanValue() && ((opt & Constant.AUTORETOPT.value) == 0)) {
                String tmpmsg = rb.getString("absenceMsg");
                if (!tmpmsg.equals("")) {
                    Sender.send(socket, makePack(Constant.SENDMSG.value | Constant.AUTORETOPT.value, tmpmsg, false), event.getAddress());
                }
            }
            if (!users.containsKey(makeKey(event))) {
                nickname = rb.getString("nickName");
                Sender.send(socket, makePack(Constant.BR_ENTRY.value | Constant.AUTORETOPT.value | getEntryOpt(), nickname, true), event.getAddress());
            }
            if ((opt & Constant.ENCRYPTOPT.value) != 0) {
                packet.setExtra(decryptMessage(packet.getExtra()));
            }
            ie = new IpmEvent(this, IpmEvent.Id.RECEIVE_MESSAGE, new Date(System.currentTimeMillis()), event);
            invokeListener(ie);
            break;
        case RECVMSG:
            try {
                Long tmpLong = new Long(packet.getExtra());
                NormalSender ns = recentSents.get(tmpLong);
                if (ns != null) {
                    ns.receiveReply();
                    recentSents.remove(tmpLong);
                }
            } catch (NumberFormatException ex) {
            }
            break;
        case READMSG:
            ie = new IpmEvent(this, IpmEvent.Id.READ_MESSAGE, new Date(System.currentTimeMillis()), event);
            invokeListener(ie);
            break;
        case DELMSG:
            ie = new IpmEvent(this, IpmEvent.Id.DELETE_MESSAGE, new Date(System.currentTimeMillis()), event);
            invokeListener(ie);
            break;
        case GETPUBKEY:
            Sender.send(socket, makePack(Constant.ANSPUBKEY.value, answerPublicKey(event.getPacket()), false), event.getAddress());
            break;
        case ANSPUBKEY:
            receivePublicKey(event);
            break;
        case GETINFO:
            Sender.send(socket, makePack(Constant.SENDINFO.value, rb.getString("version") + " (" + System.getProperty("java.vendor") + " ver." + System.getProperty("java.version") + "/" + System.getProperty("os.name") + " " + System.getProperty("os.version") + ")", false), event.getAddress());
            break;
        }
    }

    private String[] cutCString(String cstr) {
        StringTokenizer tokenizer = new StringTokenizer(cstr, ",");
        String[] tmpstrs = new String[tokenizer.countTokens()];
        for (int i = 0; i < tmpstrs.length; i++) {
            tmpstrs[i] = tokenizer.nextToken().trim();
        }
        return tmpstrs;
    }

    private IpmPacket makePack(long com, String extra, boolean groupflag) {
        if (groupflag) {
            return new IpmPacket(1, getSerial(), user, host, com, extra, rb.getString("groupName"));
        } else {
            return new IpmPacket(1, getSerial(), user, host, com, extra, null);
        }
    }

    private synchronized long getSerial() {
        return (System.currentTimeMillis() >> 10) + serial++;
    }

    public InetSocketAddress[] getBroadcastAddr() {
        InetSocketAddress[] tmpaddr;
        int dialup = dialupMember.size();
        try {
            String tmpstr = rb.getString("broadcastAddr");
            String[] strbroadcasts = cutCString("255.255.255.255," + tmpstr);
            tmpaddr = new InetSocketAddress[(strbroadcasts.length * ports.length) + dialup];
            for (int i = 0; i < strbroadcasts.length; i++) {
                for (int j = 0; j < ports.length; j++) {
                    try {
                        tmpaddr[(i * ports.length) + j] = new InetSocketAddress(InetAddress.getByName(strbroadcasts[i]), ports[j]);
                    } catch (UnknownHostException ex) {
                        tmpaddr[(i * ports.length) + j] = null;
                    }
                }
            }
        } catch (MissingResourceException ex) {
            tmpaddr = new InetSocketAddress[ports.length + dialup];
            for (int i = 0; i < ports.length; i++) {
                try {
                    tmpaddr[i] = new InetSocketAddress(InetAddress.getByName("255.255.255.255"), ports[i]);
                } catch (UnknownHostException exx) {
                    tmpaddr[i] = null;
                }
            }
        }

        Iterator<?> enumeration = dialupMember.values().iterator();
        for (int i = 0; i < dialup; i++) {
            tmpaddr[(i + tmpaddr.length) - dialup] = (InetSocketAddress) enumeration.next();
        }
        return tmpaddr;
    }

    private synchronized void invokeListener(IpmEvent tmpevent) {
        for (IpmListener listener : listeners.values()) {
            listener.eventOccured(tmpevent);
        }
    }

    private static String bytesToString(byte[] b) {
        StringBuffer strbuf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int tmpb = (b[i] < 0) ? (b[i] + 0x100) : b[i];
            strbuf.append(Integer.toString((tmpb / 16), 16).toUpperCase());
            strbuf.append(Integer.toString((tmpb % 16), 16).toUpperCase());
        }
        return new String(strbuf);
    }

    private byte[] stringToBytes(String src) {
        byte[] buf = new byte[src.length() / 2];
        for (int i = 0; i < src.length(); i += 2) {
            int b = Integer.parseInt(src.substring(i, ((i + 2) > src.length()) ? src.length() : (i + 2)), 16);
            b = (b > 127) ? (b - 0x100) : b;
            buf[i / 2] = (byte) b;
        }
        return buf;
    }

    private byte[] reverseBytes(byte[] src) {
        for (int i = 0; i < (src.length / 2); i++) {
            byte b = src[i];
            src[i] = src[src.length - i - 1];
            src[src.length - i - 1] = b;
        }
        return src;
    }

    private String encryptMessage(CryptoInfo ci, String src) {
        if ((ci == null) || (ci.getPublicKey() == null)) {
            throw new IllegalStateException("publickey unknown.");
        }
        try {
            long flag = 0L;
            int key_length = ci.getPublicKey().getEncoded().length;
            if (key_length > 200) {
                flag |= Constant.RSA_2048.value;
            } else if (key_length > 100) {
                flag |= Constant.RSA_1024.value;
            } else {
                flag |= Constant.RSA_512.value;
            }

            StringBuilder strbuf = new StringBuilder();
            SecretKey secretkey = null;
            String cipher_name = null;
            if ((ci.getCap() & Constant.BLOWFISH_128.value) != 0) {
                flag |= Constant.BLOWFISH_128.value;

                KeyGenerator kg = KeyGenerator.getInstance("Blowfish");
                kg.init(128);
                secretkey = kg.generateKey();
                cipher_name = rb.getString("Cipher2");
            } else if ((ci.getCap() & Constant.RC2_128.value) != 0) {
                flag |= Constant.RC2_128.value;

                KeyGenerator kg = KeyGenerator.getInstance("RC2");
                kg.init(128);
                secretkey = kg.generateKey();
                cipher_name = rb.getString("Cipher3");
            } else if ((ci.getCap() & Constant.RC2_40.value) != 0) {
                flag |= Constant.RC2_40.value;

                KeyGenerator kg = KeyGenerator.getInstance("RC2");
                kg.init(40);
                secretkey = kg.generateKey();
                cipher_name = rb.getString("Cipher3");
            } else {
                throw new IllegalStateException("no cap!");
            }
            strbuf.append(Long.toString(flag, 16).toUpperCase());
            strbuf.append(":");

            Cipher c = Cipher.getInstance(rb.getString("Cipher1"));
            c.init(Cipher.ENCRYPT_MODE, ci.getPublicKey());

            byte[] keydata = c.doFinal(secretkey.getEncoded());
            strbuf.append(bytesToString(keydata));
            strbuf.append(":");
            c = Cipher.getInstance(cipher_name);

            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            c.init(Cipher.ENCRYPT_MODE, secretkey, iv);

            byte[] msgdata = c.doFinal(src.getBytes(encoding));
            strbuf.append(bytesToString(msgdata));
            return strbuf.toString();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return src;
    }

    private String decryptMessage(String src) {
        if (privateKey == null) {
            return src;
        }
        try {
            StringTokenizer token = new StringTokenizer(src, ":");
            long cap = Long.parseLong(token.nextToken().toLowerCase(), 16);
            String cipher_name = rb.getString("Cipher2");
            if ((cap & (Constant.BLOWFISH_128.value | Constant.BLOWFISH_256.value)) == 0) {
                cipher_name = rb.getString("Cipher3");
            }

            byte[] keydata = stringToBytes(token.nextToken());
            byte[] msgdata = stringToBytes(token.nextToken());
            Cipher c = Cipher.getInstance(rb.getString("Cipher1"));
            c.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] skey = c.doFinal(keydata);
            c = Cipher.getInstance(cipher_name);

            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            SecretKeySpec sks1 = new SecretKeySpec(skey, rb.getString("Cipher2").substring(0, rb.getString("Cipher2").indexOf("/")));
            c.init(Cipher.DECRYPT_MODE, sks1, iv);

            byte[] msg = c.doFinal(msgdata);
            return new String(msg, encoding);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "decrypt error.";
        }
    }

    private String answerPublicKey(IpmPacket pack) {
        if (publicKey == null) {
            return "0";
        }

        StringBuffer result = new StringBuffer();
        try {
            result.append(Long.toString(getCryptoCaps(), 16).toUpperCase());
            result.append(":");
            result.append(makeRSAPublicKeyString((RSAPublicKey) publicKey));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    private void receivePublicKey(CommunicationEvent ipme) {
        if (publicKey == null) {
            return;
        }
        try {
            StringTokenizer token = new StringTokenizer(ipme.getPacket().getExtra(), ":");
            long cap = Long.parseLong(token.nextToken(), 16);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keyspec = makePublicKeySpec(token.nextToken());
            PublicKey pubkey = kf.generatePublic(keyspec);
            CryptoInfo ci = new CryptoInfo(cap, pubkey);
            publicKeys.put(ipme.getAddress().toString(), ci);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private RSAPublicKeySpec makePublicKeySpec(String src) {
        StringTokenizer token = new StringTokenizer(src, "-");
        BigInteger exponent = new BigInteger(token.nextToken(), 16);
        BigInteger modulus = new BigInteger(token.nextToken(), 16);
        return new RSAPublicKeySpec(modulus, exponent);
    }

    private RSAPrivateKeySpec makePrivateKeySpec(String src) {
        StringTokenizer token = new StringTokenizer(src, "-");
        BigInteger exponent = new BigInteger(token.nextToken(), 16);
        BigInteger modulus = new BigInteger(token.nextToken(), 16);
        return new RSAPrivateKeySpec(modulus, exponent);
    }

    private String makeRSAPublicKeyString(RSAPublicKey key) {
        return key.getPublicExponent().toString(16).toUpperCase() + "-" + key.getModulus().toString(16).toUpperCase();
    }

    private String makeRSAPrivateKeyString(RSAPrivateKey key) {
        return key.getPrivateExponent().toString(16).toUpperCase() + "-" + key.getModulus().toString(16).toUpperCase();
    }

    private long setupEncryption() {
        if ((publicKey != null) && (privateKey != null)) {
            return Constant.ENCRYPTOPT.value;
        } else if (!hasJCE) {
            return 0L;
        }
        try {
            Class<?> c = Class.forName(rb.getString("jceProvider"));
            Security.addProvider((Provider) c.newInstance());
        } catch (Exception e) {
            System.err.println("Can't instantiate JCE Provider.->" + e);
            hasJCE = false;
            return 0L;
        }
        try {
            try {
                KeyFactory kf = KeyFactory.getInstance("RSA");
                RSAPublicKeySpec pubspec = makePublicKeySpec(rb.getString("publicKey"));
                publicKey = kf.generatePublic(pubspec);

                RSAPrivateKeySpec privspec = makePrivateKeySpec(rb.getString("privateKey"));
                privateKey = kf.generatePrivate(privspec);
            } catch (Exception e) {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(1024);

                KeyPair kp = kpg.generateKeyPair();
                publicKey = kp.getPublic();
                privateKey = kp.getPrivate();
                userPrefs.put("publicKey", makeRSAPublicKeyString((RSAPublicKey) publicKey));
                userPrefs.put("privateKey", makeRSAPrivateKeyString((RSAPrivateKey) privateKey));
            }
        } catch (Exception e) {
            System.err.println("Can't create key pair.->" + e);
            return 0L;
        }
        return Constant.ENCRYPTOPT.value;
    }

    private long getEntryOpt() {
        long absence = Boolean.parseBoolean(rb.getString("absenceState")) ? Constant.ABSENCEOPT.value : 0;
        long dialup = Boolean.parseBoolean(rb.getString("dialupState")) ? Constant.DIALUPOPT.value : 0;
        return absence | dialup | setupEncryption();
    }

    private void makeBroadcastRecvSockets() throws SocketException {
        try {
            String tmpstr = rb.getString("ports");
            String[] strports = cutCString(tmpstr);
            ports = new int[strports.length];
            for (int i = 0; i < ports.length; i++) {
                ports[i] = Integer.parseInt(strports[i]);
            }
        } catch (MissingResourceException e) {
            ports = new int[1];
            ports[0] = 2425;
        }

        DatagramSocket[] tmpds = new DatagramSocket[ports.length];
        for (int i = 0; i < ports.length; i++) {
            tmpds[i] = new DatagramSocket(ports[i]);
        }

        InetAddress proxyAddress = null;
        try {
            String tmpString = rb.getString("proxy");
            proxyAddress = InetAddress.getByName(tmpString);
        } catch (Exception e) {
            proxyAddress = null;
        }
        proxy = new IpmProxy(this, proxyAddress, new Boolean(rb.getString("proxyBroadcastAll")).booleanValue());
        for (int i = 0; i < ports.length; i++) {
            proxy.addBroadcastPort(ports[i]);

            Receiver tmpReceiver = new Receiver(tmpds[i]);
            tmpReceiver.addIPMComListener(this);
            tmpReceiver.addIPMComListener(proxy);
            new Thread(tmpReceiver).start();
        }
    }

    private class Child implements Runnable {

        public void run() {
            DataInputStream dis;
            Socket socket;
            while (true) {
                try {
                    socket = new Socket(localAddress, IpmProxy.PROXY_PORT);
                    dis = new DataInputStream(socket.getInputStream());
                } catch (IOException ex) {
                    return;
                }

                byte[] buf = new byte[8192];
                IpmByteBuffer ipmbb = new IpmByteBuffer();
                outer: while (true) {
                    while (!ipmbb.eop()) {
                        int count = 0;
                        try {
                            count = dis.read(buf);
                        } catch (IOException ex) {
                            break outer;
                        }
                        if (count == -1) {
                            break outer;
                        }
                        ipmbb.append(buf, 0, count);
                    }
                    buf = ipmbb.getBytes();

                    ProxyEvent proxyEvent = new ProxyEvent(this, buf);
                    try {
                        if (!proxyEvent.getToAddress().getAddress().equals(InetAddress.getByName("255.255.255.255"))) {
                            continue;
                        }
                    } catch (UnknownHostException e) {
                        continue;
                    }

                    CommunicationEvent event = new CommunicationEvent(this, proxyEvent.getToAddress().getPort(), proxyEvent.getPacket(), proxyEvent.getFromAddress());
                    try {
                        receive(event);
                    } catch (IOException e) {
                        e.printStackTrace(System.err); // TODO
                    }
                }
                try {
                    makeBroadcastRecvSockets();
                    try {
                        dis.close();
                        socket.close();
                    } catch (IOException e) {
                    }
                    return;
                } catch (SocketException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public Ipmessenger() {
        user = System.getProperty("user.name", "No Name");
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            host = "Unknown Hostname";
        }
        try {
            try {
                localAddress = InetAddress.getByName(rb.getString("localAddress"));
            } catch (MissingResourceException ex) {
                localAddress = InetAddress.getLocalHost();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace(System.err);
        }
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public void entry() {
        DatagramSocket socket;
        Random random = new Random(System.currentTimeMillis());
        while (true) {
            /* 49154 - 65534 */
            port = (int) ((random.nextLong() % 8192) + 57342);
            try {
                socket = new DatagramSocket(port);
            } catch (SocketException e) {
                continue;
            }
            break;
        }
        this.socket = socket;

        Receiver tmpReceiver = new Receiver(socket);
        tmpReceiver.addIPMComListener(this);
        new Thread(tmpReceiver).start();
        try {
            makeBroadcastRecvSockets();
        } catch (SocketException e) {
            e.printStackTrace(System.err);
            new Thread(new Child()).start();
        }
        state = true;

        String nickname = rb.getString("nickName");
        new BroadcastSender(socket, makePack(Constant.BR_ENTRY.value | getEntryOpt(), nickname, true), getBroadcastAddr());
    }

    public synchronized void exit() {
        state = false;

        BroadcastSender sender = new BroadcastSender(socket, makePack(Constant.BR_EXIT.value, "", false), getBroadcastAddr());
        Thread thread = new Thread(sender);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }

    public synchronized void addIPMListener(IpmListener listener) {
        listeners.put(listener, listener);
    }

    public synchronized void removeIPMListener(IpmListener listener) {
        listeners.remove(listener);
    }

    public Map<String, CommunicationEvent> getUsers() {
        return users;
    }

    public synchronized void increaseReceiveCount() {
        receiveCount++;
    }

    public synchronized void decreaseReceiveCount() {
        receiveCount--;
    }

    public boolean lessThanReceiveMax() {
        int receiveMax = userPrefs.getInt("receiveMax", 500);
        return receiveMax > receiveCount;
    }

    public void sendMessage(InetSocketAddress[] address, String message, long flag) throws IOException {
        if (address == null) {
            IpmPacket packet = makePack(Constant.SENDMSG.value | Constant.BROADCASTOPT.value | flag, message, false);
            BroadcastSender sender = new BroadcastSender(socket, packet, getBroadcastAddr());
System.err.println("BroadcastSender: " + sender);
        } else if (address.length == 1) {
            if (publicKeys.containsKey(address[0].toString())) {
                message = encryptMessage(publicKeys.get(address[0].toString()), message);
                flag |= Constant.ENCRYPTOPT.value;
            }

            IpmPacket packet = makePack(Constant.SENDMSG.value | Constant.SENDCHECKOPT.value | flag, message, false);
            NormalSender sender = new NormalSender(socket, packet, address[0]);
            sender.addCommunicationListener(new CommunicationListener() {
                public void receive(CommunicationEvent event) {
                    recentSents.remove(event.getPacket().getNo());

                    IpmEvent ie = new IpmEvent(this, IpmEvent.Id.CANNOT_SEND_MESSAGE, new Date(System.currentTimeMillis()), event);
                    invokeListener(ie);
                }
            });
            recentSents.put(packet.getNo(), sender);
            new Thread(sender).start();
        } else {
            for (int i = 0; i < address.length; i++) {
                long tmpFlag = flag;
                String tmpMessage = message;
                if (publicKeys.containsKey(address[i].toString())) {
                    tmpMessage = encryptMessage(publicKeys.get(address[i].toString()), message);
                    tmpFlag |= Constant.ENCRYPTOPT.value;
                }

                IpmPacket packet = makePack(Constant.SENDMSG.value | Constant.MULTICASTOPT.value | tmpFlag, tmpMessage, false);
                Sender.send(socket, packet, address[i]);
            }
        }
    }

    public void sendReadMessage(IpmEvent event) throws IOException {
        IpmPacket packet = makePack(Constant.READMSG.value | Constant.AUTORETOPT.value, new Long(event.getPacket().getNo()).toString(), false);
        Sender.send(socket, packet, event.getAddress());
    }

    public void sendDeleteMessage(IpmEvent event) throws IOException {
        IpmPacket packet = makePack(Constant.DELMSG.value | Constant.AUTORETOPT.value, new Long(event.getPacket().getNo()).toString(), false);
        Sender.send(socket, packet, event.getAddress());
    }

    public void getInfo() {
        BroadcastSender sender = new BroadcastSender(socket, makePack(Constant.GETINFO.value, "", false), getBroadcastAddr());
System.err.println("BroadcastSender: " + sender);
    }

    public synchronized void refreshList() {
        InetSocketAddress[] tmpAddress = getBroadcastAddr();
        users = new HashMap<String, CommunicationEvent>();
        dialupMember = new HashMap<String, InetSocketAddress>();

        String nickname = rb.getString("nickName");
        new BroadcastSender(socket, makePack(Constant.BR_ENTRY.value | getEntryOpt(), nickname, true), tmpAddress);
    }

    public void absenceStateChanged() {
        String nickname = rb.getString("nickName");
        new BroadcastSender(socket, makePack(Constant.BR_ABSENCE.value | getEntryOpt(), nickname, true), getBroadcastAddr());
    }

    public String makeDateString(Date now) {
        DateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);
        dateFormatter.setTimeZone(DateFormat.getDateInstance().getTimeZone());
        return "at " + dateFormatter.format(now);
    }

    public String makeListString(IpmPacket packet) {
        String tmpUser = null;
        if (packet.getExtra() == null) {
            tmpUser = packet.getUser();
        } else {
            tmpUser = packet.getExtra();
        }

        StringBuilder sb = new StringBuilder();
        if ((packet.getCommand() & Constant.ABSENCEOPT.value) != 0) {
            tmpUser = tmpUser + "*";
        }
        sb.append(tmpUser + " (");
        if (packet.getGroup() != null) {
            sb.append(packet.getGroup() + "/");
        }
        sb.append(packet.getHost() + ")");
        return sb.toString();
    }

    public String[] makeListStrings(IpmPacket packet) {
        String tmpUser = "";
        String tmpGroup = "";
        String tmpHost = "";
        if (packet.getExtra() == null) {
            tmpUser = packet.getUser();
        } else {
            tmpUser = packet.getExtra();
        }
        if ((packet.getCommand() & Constant.ABSENCEOPT.value) != 0) {
            tmpUser = tmpUser + "*";
        }
        if (packet.getGroup() != null) {
            tmpGroup = packet.getGroup();
        }
        tmpHost = packet.getHost();

        String[] result = {
            tmpUser, tmpGroup, tmpHost
        };
        return result;
    }

    public String makeListStrings(String user, String group, String host) {
        StringBuilder sb = new StringBuilder();
        sb.append(user);
        sb.append(" (");
        if ((group != null) && !group.equals("")) {
            sb.append(group);
            sb.append("/");
        }
        sb.append(host);
        sb.append(")");
        return sb.toString();
    }

    public synchronized void writeLog(String str1, String str2, String body) {
        String cr = System.getProperty("line.separator", "\n");
        try {
            String logname = rb.getString("logFilename");
            FileWriter fw = new FileWriter(logname, true);
            String tmpstr = "=====================================" + cr;
            fw.write(tmpstr, 0, tmpstr.length());
            tmpstr = " " + str1 + cr;
            fw.write(tmpstr, 0, tmpstr.length());
            tmpstr = "	" + str2 + cr;
            fw.write(tmpstr, 0, tmpstr.length());
            tmpstr = "-------------------------------------" + cr;
            fw.write(tmpstr, 0, tmpstr.length());
            tmpstr = body.replace("\n", cr) + cr + cr;
            fw.write(tmpstr, 0, tmpstr.length());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
