/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import vavi.net.im.Buddy;
import vavi.net.im.Group;
import vavi.net.im.Message;
import vavi.net.im.MessageComponent;
import vavi.net.im.Session;
import vavi.net.im.TextComponent;
import vavi.net.im.event.IMAdapter;
import vavi.net.im.event.IMListener;
import vavi.net.im.protocol.Protocol;
import vavi.util.Debug;
import vavi.util.PasswordField;


/**
 * A sample client for, written mostly for debugging.
 *
 * @author Raghu
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public class TestImClient {
    /** */
    private Protocol protocol;
    /** */
    private Session session;

    /** */
    public TestImClient(String username, String password) throws IOException {

        String className = System.getProperty("vavi.net.im.protocol");
Debug.println("className: " + className);
        try {
            this.protocol = (Protocol) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        if (username == null || password == null) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Username: ");
            username = rd.readLine();

            password = new String(PasswordField.getPassword(System.in, "Password: "));
        }

        protocol.addIMListener(imListener);

        Properties props = new Properties();
Debug.println("username: " + username);
        props.setProperty("username", username); 
        props.setProperty("password", password); 
        props.setProperty("connection", "DIRECT"); 
        protocol.connect(props);
    }

    /** */
    public static void main(String[] args) throws Exception {
if (args.length > 2) {
 System.setProperty("vavi.net.im.protocol", args[2]);
}
        TestImClient client = new TestImClient(args[0], args[1]);
        boolean done = false;

        while (!done) {
//          System.out.print(">");
            String[] arguments = client.getCommandLine();
            done = client.processCommand(arguments);
        }
    }

    /** */
    private String[] getCommandLine() throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String line = rd.readLine();

        StringTokenizer st = new StringTokenizer(line);
        String[] args = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            args[i] = st.nextToken();
        }

        return args;
    }

    /** */
    private boolean processCommand(String[] args) throws IOException {
        if (args.length < 1) {
            return false;
        }

        switch (args[0]) {
        case "msg":
            if (args.length >= 3) {
                StringBuilder text = new StringBuilder(new String());
                for (int i = 2; i < args.length; i++) {
                    text.append(args[i]).append(" ");
                }

                Buddy buddy = new Buddy(args[1]);
                Message message = new Message(text.toString());

                Session session = protocol.startSession(buddy);
                session.sendMessage(message);
            }
            break;
        case "add":
            if (args.length == 2) {
                Buddy buddy = new Buddy(args[1]); // , "Friends"
                protocol.addToBuddyList(buddy);
            }
            break;
        case "del":
            if (args.length == 2) {
                Buddy buddy = new Buddy(args[1]); // , "~"
                protocol.deleteFromBuddyList(buddy);
            }
            break;
        case "ign":
            if (args.length == 2) {
                Buddy buddy = new Buddy(args[1]);
                protocol.ignoreBuddy(buddy);
            }
            break;
        case "uign":
            if (args.length == 2) {
                Buddy buddy = new Buddy(args[1]);
                protocol.unignoreBuddy(buddy);
            }
            break;
        case "cnf":
            if (args.length > 2) {
                Buddy myself = new Buddy(args[1]);
                Buddy[] buddies = new Buddy[args.length - 1];
                for (int i = 0; i < buddies.length; i++) {
                    buddies[i] = new Buddy(args[i + 1]);
                }

                String message = "Friends, Romans, and Countrymen, lend me your IMs";
                session = protocol.startSession(myself.getUsername(), buddies, message);
            }
            break;
        case "cmsg":
            if (args.length == 2) {
                Message msg = new Message();
                msg.addComponent(new TextComponent(args[1]));

                session.sendMessage(msg);
            }
            break;
        case "cnfq":
            session.quit();
            break;
        case "off":
            protocol.disconnect();
            return true;
        }

        return false;
    }

    /** */
    private IMListener imListener = new IMAdapter() {

        @Override
        public void connecting() throws IOException {
            Debug.println("Connecting...");
        }

        @Override
        public void connected() throws IOException {
            Debug.println("Connected");
        }

        @Override
        public void disconnected() throws IOException {
            Debug.println("Disconnected: " + Thread.activeCount());
        }

        @Override
        public void buddyStatusChanged(Buddy buddy) throws IOException {
            Debug.println("Status of " + buddy.getUsername() + " is now " + buddy.getStatus());
        }

        @Override
        public void buddyAdded(Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " is added to the buddy list");
        }

        @Override
        public void buddyAddRejected(Buddy buddy, String message) {
Debug.println(buddy.getUsername() + " rejected to be a buddy: " + message);
        }

        @Override
        public void buddyDeleted(Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " is deleted from the buddy list");
        }

        @Override
        public void buddyIgnored(Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " is ignored now");
        }

        @Override
        public void buddyUnignored(Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " is unignored now");
        }

        @Override
        public void groupAdded(Group group) throws IOException {
Debug.println(group.getName() + " is added to groups");
        }

        @Override
        public void ignoreListReceived(Buddy[] buddies) throws IOException {
Debug.println("Ignore list received: ");
            for (Buddy buddy : buddies) {
                Debug.println(buddy.getUsername());
            }
        }

        @Override
        public void buddyListReceived(Buddy[] buddies) throws IOException {
Debug.println("Buddy list received: ");
            for (Buddy buddy : buddies) {
Debug.println(buddy.getUsername());
            }
        }

        @Override
        public void instantMessageReceived(Session session, Message message) throws IOException {
            printMessage(session.getParticipants()[0].getUsername() + ": ", message);
        }

        @Override
        public void offlineMessageReceived(Buddy buddy, Date time, Message message) throws IOException {
            printMessage(buddy.getUsername() + " [offline @ " + time + "] : ", message);
        }

        @Override
        public void protocolMessageReceived(Message message) throws IOException {
            printMessage("Protocol Message: ", message);
        }

        @Override
        public void conferenceMessageReceived(Session session, Buddy buddy, Message message) throws IOException {
            Debug.print(session);
            printMessage(" conference: " + buddy.getUsername() + ": ", message);
        }

        @Override
        public void conferenceInvitationReceived(Session session, String message) throws IOException {
            Buddy[] parts = session.getParticipants();
Debug.print("Conference invitation: ");

            for (Buddy part : parts) {
                System.err.print(part.getUsername() + " ");
            }

            System.err.println();
        }

        @Override
        public void conferenceInvitationAccepted(Session session, Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " joined the conference: " + session);
        }

        @Override
        public void conferenceInvitationDeclined(Session session, Buddy buddy, String message) throws IOException {
Debug.println(buddy.getUsername() + " declined to join the conference: " + session + " with the message: " + message);
        }

        @Override
        public void conferenceParticipantJoined(Session session, Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " joined the conference: " + session);
        }

        @Override
        public void conferenceParticipantLeft(Session session, Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " left the conference: " + session);
        }

        @Override
        public void conferenceClosed(Session session) throws IOException {
Debug.println("Conference closed: " + session);
        }

        @Override
        public void typingStarted(Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " is typing");
        }

        @Override
        public void typingStopped(Buddy buddy) throws IOException {
Debug.println(buddy.getUsername() + " stopped typing");
        }

        @Override
        public void mailNotificationReceived(int count, String[] from, String[] subject) throws IOException {
            if (count == -1) {
Debug.println("You have new mail");
            } else {
Debug.println("You have " + count + " new mail(s).");
            }

            if ((from != null) && (subject != null)) {
                for (int i = 0; (i < from.length) || (i < subject.length); i++) {
Debug.println("\t(" + (i + 1) + ") from: " + from[i] + " subject: " + subject[i]);
                }
            }
        }

        @Override
        public boolean doConfirmation(String message) {
            int yn = JOptionPane.showConfirmDialog(null, message, "YMSG", JOptionPane.YES_NO_OPTION);
            return yn == JOptionPane.YES_NO_OPTION;
        }

        private void printMessage(String header, Message msg) {
Debug.print(header);

            for (MessageComponent comp : msg.getComponents()) {
                System.err.println(comp);
            }

            System.err.println();
        }
    };
}

/* */
