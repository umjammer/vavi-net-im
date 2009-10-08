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

    /** key 66 の値 */
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

    /** 設定ファイル */
    private static Preferences systemPrefs = Preferences.systemNodeForPackage(YmsgConnection.class);

    /** */
    private static String defaultHost;
    /** */
    private static int defaultPort;

    /** ログインホスト */
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

    /** 未読メール数 */
    private int unreadMailCount;

    /** */
    private IMSupport listeners;

    /**
     * @param listeners 
     */
    public YmsgConnection(String username, String password, IMSupport listeners)
        throws IOException {

        this.connection = new DirectConnection(defaultHost, defaultPort);

        // ログインアカウント
        int p = username.indexOf('@');
        if (p == -1) {
            this.username = username;
        } else {
            this.username = username.substring(0, p - 1);
        }
//Debug.println("username: " + this.username);
        // パスワード
        this.password = password;
//Debug.println("password: " + this.password);

        // 未読メール数
        this.unreadMailCount = 0;
        
        // TODO
    	if (Locale.JAPAN.getDisplayLanguage().equalsIgnoreCase(Locale.getDefault().getDisplayLanguage())) {
    	    this.version = YmsgPacketHeader.VERSION_JP;
    	} else {
    	    this.version = YmsgPacketHeader.VERSION_WEB_EN;
    	}

        // 受信スレッドの起動
        this.listeners = listeners;
        listeners.addIMListener(defaultIMListener);

        this.eventHandler = new YmsgEventHandler(connection);
        eventHandler.setIMListeners(listeners);
        eventHandler.start();
    }

    //----

    /**
     * 認証要請を行います。
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

    /** ダイレクト接続の認証を行います。 */
    private void requestAuthDirect() throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.AUTH, 0);
        yp.addData(new YmsgData(1, username));
Debug.println("sending:\n" + StringUtil.getDump(yp.toByteArray()));
        connection.write(yp.toByteArray());
    }
    
    /** WEB での認証ロジック */
    private static final ChallengeResponse challengeResponseWeb = new ChallengeResponseWeb();

    /** WEB での認証を行います。 */
    private void requestAuthWeb() throws IOException {
        String[] responses = challengeResponseWeb.getResponses(username, password, null);

        YmsgPacket yp = new YmsgPacket(YmsgPacketHeader.VERSION_WEB_EN, 0, Event.WEBLOGIN, Status.WEBLOGIN.value);
        yp.addData(new YmsgData(0, username));
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(6, responses[0]));
        connection.write(yp.toByteArray());
    }

    /** 認証ロジックのバージョン */
    private static final ChallengeResponse[] challengeResponses = {
        new ChallengeResponseV7(),
        new ChallengeResponseV11()
    };

    /**
     * 認証要請の返答に対する応答を行います。
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
     * バディリストに追加します。
     * @param buddy 追加するバディ
     * @param group 追加するグループ
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
     * グループにバディを追加します。
     * @param buddy 追加するバディ
     * @param group 追加するバディが所属するグループ
     */
    public void addBuddyIntoGroup(String buddy, String group) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.ADDBUDDY, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(65, group));
        yp.addData(new YmsgData(14, " ")); // TODO ignore -> の場合は無し
        connection.write(yp.toByteArray());
    }

    /**
     * バディリストから削除します。
     * @param buddy 対象バディ
     * @param group 対象バディが属するグループ
     */
    public void removeBuddyFromList(String buddy, String group) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.REMBUDDY, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(65, group));
        connection.write(yp.toByteArray());
    }

    /**
     * バディリストに追加要請を却下します。
     * @param buddy 却下するバディ
     * @param message 却下メッセージ
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
     * 禁止リストに追加します。
     * @param buddy 対象バディ
     */
    public void ignoreBuddy(String buddy) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.IGNORECONTACT, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(13, "1"));
        connection.write(yp.toByteArray());
    }

    /**
     * 禁止リストから削除します。
     * @param buddy 対象バディ
     */
    public void unignoreBuddy(String buddy) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.IGNORECONTACT, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(7, buddy));
        yp.addData(new YmsgData(13, "2"));
        connection.write(yp.toByteArray());
    }

    /**
     * ステータスを変更します。
     * @param status ステータス
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
     * グループセッションに追加招待します。
     * @param session セッションの名前
     * @param buddies 招待するバディ
     * @param message 招待メッセージ
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
     * グループセッションに招待します。
     * @param session セッションの名前
     * @param buddies 招待するバディ
     * @param message 招待メッセージ
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
     * グループセッションを終了します。
     * @param session セッションの名前
     * @param buddies セッションに参加中のバディ
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
     * グループ名を変更します。 
     */
    public void changeGroupName(String oldGroupName, String newGroupName) throws IOException {
        YmsgPacket yp = new YmsgPacket(version, 0, Event.GROUPRENAME, 0);
        yp.addData(new YmsgData(1, username));
        yp.addData(new YmsgData(65, oldGroupName));
        yp.addData(new YmsgData(67, newGroupName));
        connection.write(yp.toByteArray());
    }

    /**
     * グループセッションにメッセージを送ります。
     * @param message メッセージ
     * @param session セッションの名前
     * @param buddies セッションに参加中のバディ
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
     * セッションにメッセージを送ります。 
     * @param message メッセージ
     * @param buddy 送り先
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
     * タイピングを始めた事を知らせます。
     * @param buddy 知らせるバディ
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
     * タイピングを終えた事を知らせます。
     * @param buddy 知らせるバディ
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
     * グループセッションへの参加要請を受諾します。
     * @param session セッションの名前
     * @param message 許可メッセージ
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
     * グループセッションへの参加要請を拒否します。
     * @param session セッションの名前
     * @param message 却下メッセージ
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

    /** 文字列リソース */
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
