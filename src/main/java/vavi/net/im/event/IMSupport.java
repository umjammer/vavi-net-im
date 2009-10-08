/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.event;

import java.io.IOException;

import javax.swing.event.EventListenerList;


/**
 * IMSupport.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040912 nsano initial version <br>
 */
public class IMSupport {

    /** The generic listeners */
    private EventListenerList listenerList = new EventListenerList();

    /** IMListener ‚ð’Ç‰Á‚µ‚Ü‚·D */
    public void addIMListener(IMListener l) {
        listenerList.add(IMListener.class, l);
    }

    /** IMListener ‚ðíœ‚µ‚Ü‚·D */
    public void removeIMListener(IMListener l) {
        listenerList.remove(IMListener.class, l);
    }

    /**
     * @param event the event
     */
    public void eventHappened(IMEvent event) throws IOException {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IMListener.class) {
                ((IMListener) listeners[i + 1]).eventHappened(event);
            }
        }
    }
}

/* */
