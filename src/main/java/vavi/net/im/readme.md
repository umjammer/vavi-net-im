#

Instant Messenger 関連の基本クラス群です。

## Class References

```

    Group +-- Buddy ←------------- Session --+------------→ Buddy
          |                                  |
          +-- Buddy                          +------------→ Buddy

```

## Design

```java
    // ---- API ----

    Group {
        List&lt;Buddy&gt; buddies;
    }

    Session {
        isGroup
        Buddy self
        List&lt;Buddy&gt; buddies;
    }

    IM {
        List&lt;Session&gt; sessions;
        
        List&lt;Group&gt; groups;
        
        Group defaultGroup;

        Group ignored;
    }

    // ---- Application ----

    IM im = new IM();
    im.addIMListener(IMListener);
    List&lt;Group&gt; groups = im.getGroups();
    
    im.addBuddy(Buddy);
    im.ignoreBudy(Buddy);
    IMListener {
        invited(IMEvent ev);
    }
    
    Session session = im.openSession(Buddy);
    Session session = im.openSession(List&lt;Buddy&gt;);

    session.sendMessage(String);
    IMListener {
        messageReceived(IMEvent ev) {
            if (session.exists()) {
                session.
            } else {
                session = im.openSession(Buddy);
            }
        }
    }

```

## TODO

  * Protocol 別でなく Protocol を意識しない Session ？
