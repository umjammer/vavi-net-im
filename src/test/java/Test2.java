/*
 * Copyright (c) 2009 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;


/**
 * Test2. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/06/25 nsano initial version <br>
 */
public class Test2 {

    /** */
    public static void main(String[] args) throws Exception {
        String username = "nsano"; // args[0];
        String password = "12345963"; // args[1];
        String server = "jabber.jp"; // args[2];

        XMPPConnection connection = new XMPPConnection(server);
        connection.connect();
        connection.login(username, password, "SomeResource");
        ChatManager chatmanager = connection.getChatManager();
        Chat chat = chatmanager.createChat("sano-n@jabber.jp", new MessageListener() {
            public void processMessage(Chat chat, Message message) {
                System.out.println("Received message: " + message);
            }
        });
        chat.sendMessage("Hello!");
        connection.disconnect();
    }
}

/* */
