/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Group.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040824 nsano initial version <br>
 */
public class Group implements Iterable<Buddy> {

    /** */
    public Group() {
    }

    /** */
    public Group(String name) {
        this.name = name;
    }

    /** */
    private String name;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** */
    private Map<String, Buddy> buddies = new HashMap<>();
    
    /**
     * @param buddy
     */
    public void addBuddy(Buddy buddy) {
        buddies.put(buddy.getUsername(), buddy);
    }

    /**
     * @param buddy
     */
    public void removeBuddy(Buddy buddy) {
        buddies.remove(buddy.getUsername());
    }

    /**
     * @param buddyName
     */
    public void removeBuddy(String buddyName) {
        buddies.remove(buddyName);
    }

    /**
     * @param buddy
     * @return is the buddy contained
     */
    public boolean contains(Buddy buddy) {
        return buddies.containsValue(buddy);
    }

    /**
     * @param buddyName
     * @return is the name contained
     */
    public boolean contains(String buddyName) {
        return buddies.containsKey(buddyName);
    }

    /** */
    public Iterator<Buddy> iterator() {
        return buddies.values().iterator();
    }
}

/* */
