/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import vavi.net.im.event.IMEvent;
import vavi.net.im.event.IMListener;
import vavi.net.im.event.IMSupport;
import vavi.net.im.event.IMEvent.IMEventName;
import vavi.net.im.event.IMEvent.Name;
import vavi.net.im.protocol.Connection;
import vavi.net.im.protocol.DirectConnection;
import vavi.net.im.protocol.ymsg.YmsgPacketHeader.Event;
import vavi.net.im.protocol.ymsg.YmsgPacketHeader.Status;
import vavi.net.im.protocol.ymsg.auth.ChallengeResponse;
import vavi.net.im.protocol.ymsg.auth.ChallengeResponseV11;
import vavi.net.im.protocol.ymsg.auth.ChallengeResponseV7;
import vavi.net.im.protocol.ymsg.auth.ChallengeResponseWeb;
import vavi.util.Debug;
import vavi.util.StringUtil;


/**
 * YmsgConnection.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040221 nsano initial version <br>
 */
public final class YmsgConnection {

    /** key 66 �̒l */
    public static final int STATUS_LOG_OFF = -1;

    public static final int STATUS_LOGIN_OK = 0;

    public static final int STATUS_LOGIN_ERRUNAME = 3;

    public static final int STATUS_LOGIN_PASSWD = 13;

    public static final int STATUS_LOGIN_LOCK = 14;

    public static final int STATUS_LOGIN_DUPL = 99;

    /** network connection */
    private Connection connection;

    /** for {@link YmsgEventHandler} */
    public void close() throws IOException {
        eventHandler.stop();
        connection.close();
    }

    /** */
    private YmsgEventHandler eventHandler;

    /** Yahoo!Id */
    private String username;
    /** */
    private transient String password;

    /** protocol version */
    private int version;

    /** �ݒ�t�@�C�� */
    private static Preferences systemPrefs = Preferences.systemNodeForPackage(YmsgConnection.class);

    /** */
    private static String defaultHost;
    /** */
    private static int defaultPort;

    /** ���O�C���z�X�g */
    static {
//Debug.println(Locale.getDefault().getCountry() + ", " + Locale.JAPANESE.getCountry());
Debug.println(Locale.getDefault().getDisplayLanguage() + ", " + Locale.JAPANESE.getDisplayLanguage());
        if (Locale.JAPAN.getDisplayLanguage().equalsIgnoreCase(Locale.getDefault().getDisplayLanguage())) {
            defaultHost = systemPrefs.get("host", "cs.yahoo.co.jp");
            defaultPort = systemPrefs.getInt("port", 5050);
        } else {
            defaultHost = systemPrefs.get("host", "wcs2.msg.dcn.yahoo.com");
            defaultPort = systemPrefs.getInt("port", 5050);
        }
Debug.println("host: " + defaultHost);
Debug.println("port: " + defaultPort);
    }

    /** ���ǃ��[���� */
    private int unreadMailCount;

    /** */
    private IMSupport listeners;

    /**
     * @param listeners 
     */
    public YmsgConnection(String username, String password, IMSupport listeners)
        throws IOException {

        this.connection = new DirectConnection(defaultHost, defaultPort);

        // ���O�C���A�J�E���g
        int p = username.indexOf('@');
        if (p == -1) {
            this.username = username;
        } else {
            this.username = username.substring(0, p - 1);
        }
//Debug.println("username: " + this.username);
        // �p�X���[�h
        this.password = password;
//Debug.println("password: " + this.password);

        // ���ǃ��[����
        this.unreadMailCount = 0;
        
        // TODO
    	if (Locale.JAPAN.getDisplayLanguage().equalsIgnoreCase(Locale.getDefault().getDisplayLanguage())) {
    	    this.version = YmsgPacketHeader.VERSION_JP;
    	} else {
    	    this.version = YmsgPacketHeader.VERSION_WEB_EN;
    	}

        // ��M�X���b�h�̋N��
        this.listeners = listeners;
        listeners.addIMListener(defaultIMListener);

        this.eventHandler = new YmsgEventHandler(connection);
        eventHandler.setIMListeners(listeners);
        eventHandler.start();
    }

    //----

    /**
     * �F�ؗv�����s���܂��B
     * @see vavi.net.im.protocol.Protocol#connect(java.util.Properties)
     */
    public void requestAuth() throws IOException {
    	if (version == YmsgPacketHeader.VERSION_WEB_EN) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        requestAuthWeb();
                    } catch (IOException e) {
e.printStackTrace(System.err);
                    }
                }
            });
            thread.start();
    	} else {
            // since 2006-02-21
            verify();
    	}
    }

    /**
     * new protocol
     * @since 2006-02-21
     */
    private void verify() throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.VERIFY, 0);
Debug.println("sending:\n" + StringUtil.getDump(yp.toByteArray()));
        connection.write(yp.toByteArray());
    }

    /** �_�C���N�g�ڑ��̔F�؂��s���܂��B */
    private void requestAuthDirect() throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.AUTH, 0);
        yp.addData(new YmsgData(1, username));
Debug.println("sending:\n" + StringUtil.getDump(yp.toByteArray()));
        connection.write(yp.toByteArray());
    }
    
    /** WEB �ł̔F�؃��W�b�N */
    private static final ChallengeResponse challengeResponseWeb = new ChallengeResponseWeb();

    /** WEB �ł̔F�؂��s���܂��B */
    private void requestAuthWeb() throws IOException {
        String[] responses = challengeResponseWeb.getResponses(username, password, null);

        YmsgPacket yp = new YmsgPacket(YmsgPacketHeader.VERSION_WEB_EN, 0, Event.WEBLOGIN, Status.WEBLOGIN.value);
        yp.addData(new YmsgData(0, username));
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(6, responses[0]));
        connection.write(yp.toByteArray());
    }

    /** �F�؃��W�b�N�̃o�[�W���� */
    private static final ChallengeResponse[] challengeResponses = {
        new ChallengeResponseV7(),
        new ChallengeResponseV11()
    };

    /**
     * �F�ؗv���̕ԓ��ɑ΂��鉞�����s���܂��B
     * @param seed
     * @param sn
     * @param type 0: version 9, 1: version 11
     * @see vavi.net.im.protocol.ymsg.command.AuthCommand
     */
    public void responseAuth(String seed, String sn, int type) throws IOException {
Debug.println("type: " + type);
        String[] responses = challengeResponses[type].getResponses(username, password, seed);

        YmsgPacket yp = new YmsgPacket(version, 0, Event.AUTHRESP, Status.WEBLOGIN.value);
        yp.addData(new YmsgData(0, username));
        yp.addData(new YmsgData(6, responses[0]));
        yp.addData(new YmsgData(96, responses[1]));
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(135, "6,0,0,1710"));
Debug.println("sending:\n" + StringUtil.getDump(yp.toByteArray()));
	    connection.write(yp.toByteArray());
Debug.println("result6: " + responses[0]);
Debug.println("result96: " + responses[1]);
    }
    
    /**
     * �o�f�B���X�g�ɒǉ����܂��B
     * @param buddy �ǉ�����o�f�B
     * @param group �ǉ�����O���[�v
     * @see vavi.net.im.protocol.Protocol#addToBuddyList(vavi.net.im.Buddy)
     */
    public void addBuddyIntoList(String buddy, String group) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.ADDBUDDY, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(65, group));
        connection.write(yp.toByteArray());
    }
    
    /**
     * �O���[�v�Ƀo�f�B��ǉ����܂��B
     * @param buddy �ǉ�����o�f�B
     * @param group �ǉ�����o�f�B����������O���[�v
     */
    public void addBuddyIntoGroup(String buddy, String group) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.ADDBUDDY, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(65, group));
        yp.addData(new YmsgData(14, " ")); // TODO ignore -> �̏ꍇ�͖���
        connection.write(yp.toByteArray());
    }

    /**
     * �o�f�B���X�g����폜���܂��B
     * @param buddy �Ώۃo�f�B
     * @param group �Ώۃo�f�B��������O���[�v
     */
    public void removeBuddyFromList(String buddy, String group) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.REMBUDDY, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(65, group));
        connection.write(yp.toByteArray());
    }

    /**
     * �o�f�B���X�g�ɒǉ��v�����p�����܂��B
     * @param buddy �p������o�f�B
     * @param message �p�����b�Z�[�W
     * @see vavi.net.im.protocol.ymsg.command.ContactCommand
     */
    public void rejectContact(String buddy, String message) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.REJECTCONTACT, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(14, message));
        connection.write(yp.toByteArray());
    }

    /**
     * �֎~���X�g�ɒǉ����܂��B
     * @param buddy �Ώۃo�f�B
     */
    public void ignoreBuddy(String buddy) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.IGNORECONTACT, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(13, "1"));
        connection.write(yp.toByteArray());
    }

    /**
     * �֎~���X�g����폜���܂��B
     * @param buddy �Ώۃo�f�B
     */
    public void unignoreBuddy(String buddy) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.IGNORECONTACT, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(13, "2"));
        connection.write(yp.toByteArray());
    }

    /**
     * �X�e�[�^�X��ύX���܂��B
     * @param status �X�e�[�^�X
     */
    public void setStatus(Status status) throws IOException {
        YmsgPacket yp;
        if (status.isAway()) {
            yp = new YmsgPacket(version, 0, Event.ISAWAY, status.value);
        } else {
            yp = new YmsgPacket(version, 0, Event.ISBACK, status.value);
        }
        yp.addData(new YmsgData(Event.USERSTAT.value, String.valueOf(status.getValue())));
        connection.write(yp.toByteArray());
    }

    /**
     * �O���[�v�Z�b�V�����ɒǉ����҂��܂��B
     * @param session �Z�b�V�����̖��O
     * @param buddies ���҂���o�f�B
     * @param message ���҃��b�Z�[�W
     */
    public void inviteMoreBuddiesToGroupSession(String session, String[] buddies, String message) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.CONFADDINVITE, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(51, username));
        yp.addData(new YmsgData(57, session)); // room name
        yp.addData(new YmsgData(58, message));
        yp.addData(new YmsgData(13, "0"));
        for (int i = 0; i < buddies.length; i++) {
            yp.addData(new YmsgData(52, buddies[i]));
            yp.addData(new YmsgData(53, buddies[i]));
        }
        connection.write(yp.toByteArray());
    }

    /**
     * �O���[�v�Z�b�V�����ɏ��҂��܂��B
     * @param session �Z�b�V�����̖��O
     * @param buddies ���҂���o�f�B
     * @param message ���҃��b�Z�[�W
     */
    public void inviteBuddiesToGroupSession(String session, String[] buddies, String message) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.CONFINVITE, 0);
        yp.addData(new YmsgData(1, username)); // id
        yp.addData(new YmsgData(50, username)); // host
        yp.addData(new YmsgData(57, session));
        yp.addData(new YmsgData(58, message));
        yp.addData(new YmsgData(13, "0"));
        for (int i = 0; i < buddies.length; i++) {
            yp.addData(new YmsgData(52, buddies[i]));
        }
        connection.write(yp.toByteArray());
    }

    /**
     * �O���[�v�Z�b�V�������I�����܂��B
     * @param session �Z�b�V�����̖��O
     * @param buddies �Z�b�V�����ɎQ�����̃o�f�B
     */
    public void closeGroupSession(String session, String[] buddies) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.CONFLOGOFF, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(57, session));
        for (int i = 0; i < buddies.length; i++) {
            yp.addData(new YmsgData(3, buddies[i]));
        }
        connection.write(yp.toByteArray());
    }

    /**
     * �O���[�v����ύX���܂��B 
     */
    public void changeGroupName(String oldGroupName, String newGroupName) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.GROUPRENAME, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(65, oldGroupName));
        yp.addData(new YmsgData(67, newGroupName));
        connection.write(yp.toByteArray());
    }

    /**
     * �O���[�v�Z�b�V�����Ƀ��b�Z�[�W�𑗂�܂��B
     * @param message ���b�Z�[�W
     * @param session �Z�b�V�����̖��O
     * @param buddies �Z�b�V�����ɎQ�����̃o�f�B
     */
    public void sendMessageToGroupSession(String message, String session, String[] buddies) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.CONFMSG, 0);
        yp.addData(new YmsgData(1, username));
        for (int i = 0; i < buddies.length; i++) {
            yp.addData(new YmsgData(53, buddies[i]));
        }
        yp.addData(new YmsgData(57, session));
        yp.addData(new YmsgData(14, message));
        connection.write(yp.toByteArray());
    }

    /** 
     * �Z�b�V�����Ƀ��b�Z�[�W�𑗂�܂��B 
     * @param message ���b�Z�[�W
     * @param buddy �����
     */
    public void sendMessage(String message, String buddy) throws IOException {
Debug.println("here 4");
        YmsgPacket yp = new YmsgPacket(version, 0, Event.MESSAGE, Status.OFFLINE.value);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(5, buddy));
        yp.addData(new YmsgData(14, message));
        connection.write(yp.toByteArray());
    }

    /**
     * �^�C�s���O���n�߂�����m�点�܂��B
     * @param buddy �m�点��o�f�B
     * @see vavi.net.im.protocol.Protocol#startTyping()
     */
    public void beginTyping(String buddy) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.NOTIFY, Status.TYPING.value);
        yp.addData(new YmsgData(4, username));
        yp.addData(new YmsgData(5, buddy));
        yp.addData(new YmsgData(14, " "));
        yp.addData(new YmsgData(13, "1"));
        yp.addData(new YmsgData(49, "TYPING"));
        connection.write(yp.toByteArray());
    }

    /**
     * �^�C�s���O���I��������m�点�܂��B
     * @param buddy �m�点��o�f�B
     * @see vavi.net.im.protocol.Protocol#stopTyping()
     */
    public void endTyping(String buddy) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.NOTIFY, Status.TYPING.value);
        yp.addData(new YmsgData(4, username));
        yp.addData(new YmsgData(5, buddy));
        yp.addData(new YmsgData(14, " "));
        yp.addData(new YmsgData(13, "0"));
        yp.addData(new YmsgData(49, "TYPING"));
        connection.write(yp.toByteArray());
    }

    /**
     * �O���[�v�Z�b�V�����ւ̎Q���v����������܂��B
     * @param session �Z�b�V�����̖��O
     * @param message �����b�Z�[�W
     * @see vavi.net.im.protocol.ymsg.command.ConfCommand
     */
    public void acceptBuddiesToGroupSession(int connectionId, String[] buddies, String session, String message) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, connectionId, Event.CONFLOGON, 0);
        yp.addData(new YmsgData(1, username));
        for (int i = 0; i < buddies.length; i++) {
            String buddyName = buddies[i];
            yp.addData(new YmsgData(3, buddyName));
        }
        yp.addData(new YmsgData(57, session));
        yp.addData(new YmsgData(14, message));
        connection.write(yp.toByteArray());
    }

    /**
     * �O���[�v�Z�b�V�����ւ̎Q���v�������ۂ��܂��B
     * @param session �Z�b�V�����̖��O
     * @param message �p�����b�Z�[�W
     * @see vavi.net.im.protocol.ymsg.command.ConfCommand
     */
    public void declineBuddiesToGroupSession(int connectionId, String[] buddies, String session, String message) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, connectionId, Event.CONFDECLINE, 0);
        yp.addData(new YmsgData(1, username));
        for (int i = 0; i < buddies.length; i++) {
            String account = buddies[i];
            yp.addData(new YmsgData(3, account));
        }
        yp.addData(new YmsgData(57, session));
        yp.addData(new YmsgData(14, message));
        connection.write(yp.toByteArray());
    }

    //----

    /** �����񃊃\�[�X */
    private static final ResourceBundle rb = ResourceBundle.getBundle("vavi.net.im.protocol.ymsg.resources.ymsg", Locale.getDefault());

    /** */
    private IMListener defaultIMListener = new IMListener() {

        /** */
        public void eventHappened(IMEvent event) throws IOException {
            Name name = event.getName();

            if (name instanceof YmsgEventName) {
                YmsgEventName eventName = (YmsgEventName) name;

                switch (eventName) {
                case processLoginResponse: {
                    int loginStatus = (Integer) event.getArguments()[0];

                    processLoginResponse(loginStatus);
                }
                    break;

                case responseAuth: {
                    String seed = (String) event.getArguments()[0];
                    String sn = (String) event.getArguments()[1];
                    int m = (Integer) event.getArguments()[2];

                    responseAuth(seed, sn, m);
                }
                    break;

                case acceptBuddiesToGroupSession: {
                    int connectionId = (Integer) event.getArguments()[0];
                    String[] buddies = (String[]) event.getArguments()[1];
                    String room = (String) event.getArguments()[2];
                    String msg = (String) event.getArguments()[3];

                    acceptBuddiesToGroupSession(connectionId, buddies, room, msg);
                }
                    break;

                case declineBuddiesToGroupSession: {
                    int connectionId = (Integer) event.getArguments()[0];
                    String[] buddies = (String[]) event.getArguments()[1];
                    String room = (String) event.getArguments()[2];

                    declineBuddiesToGroupSession(connectionId, buddies, room, rb.getString("CONF_DECLINE_INVITATION"));
                }
                    break;

                case rejectContact: {
                    String buddy = (String) event.getArguments()[0];

                    rejectContact(buddy, rb.getString("CONTACT_DENY_MESSAGE")); // TODO check
                }
                    break;

                case incUnreadMailCount: {
                    unreadMailCount++;
                }
                    break;

                case setUnreadMailCount: {
                    int count = (Integer) event.getArguments()[0];

                    unreadMailCount = count;
                }
                    break;

                // since 2006-02-21
                case requestAuthDirect: {
                    requestAuthDirect();
                }
                    break;

                }
            }
        }
    };

    /** @param loginStatus YmsgData key 66 */
    private void processLoginResponse(int loginStatus) throws IOException {
        String message = null;
        if (loginStatus == YmsgConnection.STATUS_LOGIN_OK) {
            
            setStatus(Status.AVAILABLE); // TODO
        } else {
            if (loginStatus == YmsgConnection.STATUS_LOGIN_ERRUNAME) {
                message = rb.getString("LOGOUT_INVALID_USER");
            } else if (loginStatus == YmsgConnection.STATUS_LOGIN_PASSWD) {
                message = rb.getString("LOGOUT_CHECK_USER_PASS");
            } else if (loginStatus == YmsgConnection.STATUS_LOGIN_LOCK) {
                message = rb.getString("LOGOUT_ACCOUNT_LOCKED");
            } else if (loginStatus == YmsgConnection.STATUS_LOGIN_DUPL) {
                message = rb.getString("LOGOUT_LOGIN_FROM_ANOTHER_PLACE");
            } else {
                message = "unknown reason";
            }

            listeners.eventHappened(new IMEvent(this, IMEventName.protocolMessageReceived, message));  // TODO
            setStatus(Status.OFFLINE);  // TODO
        }
    }
}

/* */
