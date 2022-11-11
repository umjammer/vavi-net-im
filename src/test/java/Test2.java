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
        String username = args[0];
        String password = args[1];
        String server = args[2];
        String userId = args[3];

        XMPPConnection connection = new XMPPConnection(server);
        connection.connect();
        connection.login(username, password, "SomeResource");
        ChatManager chatmanager = connection.getChatManager();
        Chat chat = chatmanager.createChat(username + "@" + server,
                (chat1, message) -> System.out.println("Received message: " + message));
        chat.sendMessage("Hello!");
        connection.disconnect();
    }
}

/* */
