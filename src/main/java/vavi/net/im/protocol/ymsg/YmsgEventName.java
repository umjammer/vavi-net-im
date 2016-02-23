/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg;

import vavi.net.im.Buddy;
import vavi.net.im.Group;
import vavi.net.im.event.IMEvent;
import vavi.net.im.event.IMEvent.IMEventName;
import vavi.net.im.event.IMEvent.Name;


/**
 * Ymsg のイベント Name です．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public enum YmsgEventName implements Name {

    /**
     * @arguments String group
     * @see IMEvent.IMEventName#groupAdded
     */
    groupAdded {
        public IMEvent toIMEvent(IMEvent event) {
            String groupName = (String) event.getArguments()[0];
            Group group = new Group(groupName);
            return new IMEvent(event.getSource(), IMEventName.groupAdded, group);
        }
    },
    /**
     * @arguments String oldGroup, String newGroup
     */
    groupNameChanged {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments String buddy, String group
     * @see IMEvent.IMEventName#buddyAdded
     */
    buddyAdded {
        public IMEvent toIMEvent(IMEvent event) {
            String buddyName = (String) event.getArguments()[0];
            String groupName = (String) event.getArguments()[1];
            Buddy buddy = new Buddy(buddyName);
            return new IMEvent(event.getSource(), IMEventName.buddyAdded, buddy, groupName);
        }
    },
    /**
     * @arguments String buddy, String group
     * @see IMEvent.IMEventName#buddyAdded
     */
    buddyDeleted {
        public IMEvent toIMEvent(IMEvent event) {
            String buddyName = (String) event.getArguments()[0];
            String groupName = (String) event.getArguments()[1];
            Buddy buddy = new Buddy(buddyName);
            return new IMEvent(event.getSource(), IMEventName.buddyDeleted, buddy, groupName);
        }
    },
    /**
     * @arguments String buddy 
     */
    confInvited {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments String buddy, String session
     */
    confLogon {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /** 
     * @arguments String buddy, String session
     */
    confLogoff {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments String buddy, String session, String message
     * @see IMEvent.IMEventName#conferenceMessageReceived
     * @see "YmsgConnection.HuckedIMSupport#eventHappened(IMEvent)"
     */
    confMessageReceived {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /** */
    contactAccepted {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /** */
    contactDenied {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments String buddy
     * @see IMEvent.IMEventName#typingStarted
     */
    typingStarted {
        public IMEvent toIMEvent(IMEvent event) {
            String buddyName = (String) event.getArguments()[0];
            Buddy buddy = new Buddy(buddyName);
            return new IMEvent(event.getSource(), IMEventName.typingStarted, buddy);
        }
    },
    /**
     * @arguments String buddy
     * @see IMEvent.IMEventName#typingStopped
     */
    typingStopped {
        public IMEvent toIMEvent(IMEvent event) {
            String buddyName = (String) event.getArguments()[0];
            Buddy buddy = new Buddy(buddyName);
            return new IMEvent(event.getSource(), IMEventName.typingStopped, buddy);
        }
    },
    /**
     * @arguments String message, String buddy, Date date
     * @see IMEvent.IMEventName#instantMessageReceived
     * @see "YmsgConnection.HuckedIMSupport#eventHappened(IMEvent)"
     */
    messageReceived {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event; // ここでは出来ない変換が要るよ
        }
    },
    /**
     * @arguments String message
     * @see IMEvent.IMEventName#protocolMessageReceived
     */
    systemMessageReceived {
        public IMEvent toIMEvent(IMEvent event) {
            String message = (String) event.getArguments()[0];
            return new IMEvent(event.getSource(), IMEventName.protocolMessageReceived, message);
        }
    },
    /**
     * @arguments String buddy, String message
     * @see IMEvent.IMEventName#buddyStatusChanged
     */
    customStatusChanged {
        public IMEvent toIMEvent(IMEvent event) {
            String buddyName = (String) event.getArguments()[0];
            String statusString = (String) event.getArguments()[1];
            Buddy buddy = new Buddy(buddyName);
            return new IMEvent(event.getSource(), IMEventName.buddyStatusChanged, buddy, statusString);
        }
    },
    /**
     * @arguments String buddy, Status staus
     * @see IMEvent.IMEventName#buddyStatusChanged
     * @see "YmsgConnection.HuckedIMSupport#eventHappened(IMEvent)"
     */
    statusChanged {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event; // ここでは出来ない変換が要るよ
        }
    },
    /**
     * @arguments String buddy
     * @see IMEvent.IMEventName#buddyIgnored
     */
    buddyIgnored {
        public IMEvent toIMEvent(IMEvent event) {
            String buddyName = (String) event.getArguments()[0];
            Buddy buddy = new Buddy(buddyName);
            return new IMEvent(event.getSource(), IMEventName.buddyIgnored, buddy);
        }
    },
    /**
     * @arguments String buddy
     * @see IMEvent.IMEventName#buddyUnignored
     */
    buddyUnignored {
        public IMEvent toIMEvent(IMEvent event) {
            String buddyName = (String) event.getArguments()[0];
            Buddy buddy = new Buddy(buddyName);
            return new IMEvent(event.getSource(), IMEventName.buddyUnignored, buddy);
        }
    },
    /**
     * @arguments int errorCode
     * @see "YmsgConnection#defaultIMListener"
     */
    processLoginResponse {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments String seed, String sn, int version
     * @see "YmsgConnection#defaultIMListener"
     */
    responseAuth {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments int connectionId, String[] buddies, String session, String message
     * @see "YmsgConnection#defaultIMListener"
     */
    acceptBuddiesToGroupSession {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments int connectionId, String buddies, String session
     * @see "YmsgConnection#defaultIMListener"
     */
    declineBuddiesToGroupSession {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @see "YmsgConnection#defaultIMListener"
     */
    rejectContact {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments void
     * @see "YmsgConnection#defaultIMListener"
     */
    incUnreadMailCount {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments int count
     * @see "YmsgConnection#defaultIMListener"
     */
    setUnreadMailCount {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments void
     * @see "YmsgConnection#defaultIMListener"
     */
    verify {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments void
     * @see "YmsgConnection#defaultIMListener"
     */
    requestAuthDirect {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    },
    /**
     * @arguments void
     * @see "YmsgConnection#defaultIMListener"
     */
    ping {
        /** do nothing */
        public IMEvent toIMEvent(IMEvent event) {
            return event;
        }
    };
    /**
     * ローカルな IMEvent を汎用の IMEvent に変換します。
     * TODO もしかしてこのやり方よくないかも？
     */
    public abstract IMEvent toIMEvent(IMEvent event);
}

/* */
