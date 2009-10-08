/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.event;

import java.io.IOException;
import java.util.Date;

import vavi.net.im.Buddy;
import vavi.net.im.Group;
import vavi.net.im.Message;
import vavi.net.im.Session;
import vavi.net.im.event.IMEvent.IMEventName;
import vavi.net.im.event.IMEvent.Name;


/**
 * IMAdapter. 
 * 泥臭いイベント機構を隠蔽します。
 * ユーザ作成のリスナはこのクラスを継承し各メソッドを実装してください。
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051106 nsano initial version <br>
 */
public class IMAdapter implements IMListener {

    /** */
    public void eventHappened(IMEvent event) throws IOException {
        Name name = event.getName();

        if (name instanceof IMEventName) {
            IMEventName eventName = (IMEventName) name; 

            switch (eventName) {
            case doConfirmation: {
                String message = (String) event.getArguments()[0];
                boolean result = doConfirmation(message);
                ((CallableIMEvent) event).setResult(result);
            }
                break;
            case connecting:
                connecting();
                break;
            case connected:
                connected();
                break;
            case disconnected:
                disconnected();
                break;
            case buddyListReceived: {
                Buddy[] buddies = (Buddy[]) event.getArguments()[0];
                buddyListReceived(buddies);
            }
                break;
            case ignoreListReceived: {
                Buddy[] buddies = (Buddy[]) event.getArguments()[0];
                ignoreListReceived(buddies);
            }
                break;
            case protocolMessageReceived: {
                Message message = (Message) event.getArguments()[0];
                protocolMessageReceived(message);
            }
                break;
            case instantMessageReceived: {
                Session session = (Session) event.getArguments()[0];
                Message message = (Message) event.getArguments()[1];
                instantMessageReceived(session, message);
            }
                break;
            case offlineMessageReceived: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                Date time = (Date) event.getArguments()[1];
                Message message = (Message) event.getArguments()[2];
                offlineMessageReceived(buddy, time, message);
            }
                break;
            case mailNotificationReceived: {
                int count = (Integer) event.getArguments()[0];
                String[] from = (String[]) event.getArguments()[1];
                String[] subject = (String[]) event.getArguments()[2];
                mailNotificationReceived(count, from, subject);
            }
                break;
            case conferenceInvitationAccepted: {
                Session session = (Session) event.getArguments()[0];
                Buddy buddy = (Buddy) event.getArguments()[1];
                conferenceInvitationAccepted(session, buddy);
            }
                break;
            case conferenceInvitationDeclined: {
                Session session = (Session) event.getArguments()[0];
                Buddy buddy = (Buddy) event.getArguments()[1];
                String message = (String) event.getArguments()[2];
                conferenceInvitationDeclined(session, buddy, message);
            }
                break;
            case conferenceInvitationReceived: {
                Session session = (Session) event.getArguments()[0];
                String message = (String) event.getArguments()[1];
                conferenceInvitationReceived(session, message);
            }
                break;
            case conferenceMessageReceived: {
                Session session = (Session) event.getArguments()[0];
                Buddy buddy = (Buddy) event.getArguments()[1];
                Message message = (Message) event.getArguments()[2];
                conferenceMessageReceived(session, buddy, message);
            }
                break;
            case buddyStatusChanged: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                buddyStatusChanged(buddy);
            }
                break;
            case conferenceParticipantLeft: {
                Session session = (Session) event.getArguments()[0];
                Buddy buddy = (Buddy) event.getArguments()[1];
                conferenceParticipantLeft(session, buddy);
            }
                break;
            case conferenceParticipantJoined: {
                Session session = (Session) event.getArguments()[0];
                Buddy buddy = (Buddy) event.getArguments()[1];
                conferenceParticipantJoined(session, buddy);
            }
                break;
            case typingStarted: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                typingStarted(buddy);
            }
                break;
            case typingStopped: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                typingStopped(buddy);
            }
                break;
            case buddyAdded: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                buddyAdded(buddy);
            }
                break;
            case buddyAddRejected: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                String message = (String) event.getArguments()[1];
                buddyAddRejected(buddy, message);
            }
                break;
            case buddyDeleted: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                buddyDeleted(buddy);
            }
                break;
            case buddyIgnored: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                buddyIgnored(buddy);
            }
                break;
            case buddyUnignored: {
                Buddy buddy = (Buddy) event.getArguments()[0];
                buddyUnignored(buddy);
            }
                break;
            case conferenceClosed: {
                Session session = (Session) event.getArguments()[0];
                conferenceClosed(session);
            }
                break;
            case groupAdded: {
                Group group = (Group) event.getArguments()[0];
                groupAdded(group);
            }
                break;
            }
        }
    }

    /**
     * 
     */
    public void connecting() throws IOException {
    }

    /**
     * 
     */
    public void connected() throws IOException {
    }

    /**
     * 
     */
    public void disconnected() throws IOException {
    }

    /**
     * @param buddy
     */
    public void buddyStatusChanged(Buddy buddy) throws IOException {
    }

    /**
     * 
     * @param buddy
     */
    public void buddyAdded(Buddy buddy) throws IOException {
    }

    /**
     * @param buddy
     * @param message
     */
    public void buddyAddRejected(Buddy buddy, String message) throws IOException {
    }

    /**
     * 
     * @param buddy
     */
    public void buddyDeleted(Buddy buddy) throws IOException {
        
    }

    /**
     * 
     * @param buddy
     */
    public void buddyIgnored(Buddy buddy) throws IOException {
        
    }

    /**
     * 
     * @param buddy
     */
    public void buddyUnignored(Buddy buddy) throws IOException {
        
    }

    /**
     * @param buddies
     */
    public void ignoreListReceived(Buddy[] buddies) throws IOException {
    }

    /**
     * @param buddies
     */
    public void buddyListReceived(Buddy[] buddies) throws IOException {
    }

    /**
     * 
     * @param group
     */
    public void groupAdded(Group group) throws IOException {
        
    }

    /**
     * @param session
     * @param message
     */
    public void instantMessageReceived(Session session, Message message) throws IOException {
    }

    /**
     * @param buddy
     * @param time
     * @param message
     */
    public void offlineMessageReceived(Buddy buddy, Date time, Message message) throws IOException {
    }

    /**
     * @param message
     */
    public void protocolMessageReceived(Message message) throws IOException {
    }

    /**
     * @param session
     * @param buddy
     * @param message
     */
    public void conferenceMessageReceived(Session session, Buddy buddy, Message message) throws IOException {
    }

    /**
     * @param session
     * @param message
     */
    public void conferenceInvitationReceived(Session session, String message) throws IOException {
    }

    /**
     * @param session
     * @param buddy
     */
    public void conferenceInvitationAccepted(Session session, Buddy buddy) throws IOException {
    }

    /**
     * @param session
     * @param buddy
     * @param message
     */
    public void conferenceInvitationDeclined(Session session, Buddy buddy, String message) throws IOException {
    }

    /**
     * 
     * @param session
     * @param buddy
     */
    public void conferenceParticipantJoined(Session session, Buddy buddy) throws IOException {
    }

    /**
     * 
     * @param session
     * @param buddy
     */
    public void conferenceParticipantLeft(Session session, Buddy buddy) throws IOException {
    }

    /**
     * 
     * @param session
     */
    public void conferenceClosed(Session session) throws IOException {
        
    }

    /**
     * 
     * @param buddy
     */
    public void typingStarted(Buddy buddy) throws IOException {
    }

    /**
     * 
     * @param buddy
     */
    public void typingStopped(Buddy buddy) throws IOException {
    }

    /**
     * @param count
     * @param from
     * @param subject
     */
    public void mailNotificationReceived(int count, String[] from, String[] subject) throws IOException {
    }

    /**
     * 
     */
    public boolean doConfirmation(String message) throws IOException {
        return false;
    }
}

/* */
