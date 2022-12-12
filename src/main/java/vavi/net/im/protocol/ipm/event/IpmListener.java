/*
 * 1997/10/16 (C) Copyright T.Kazawa (Digitune)
 */
package vavi.net.im.protocol.ipm.event;

import java.util.EventListener;


/**
 * IP Messenger Event Listener Interface
 */
public interface IpmListener extends EventListener {
    void eventOccured(IpmEvent ev);
}
