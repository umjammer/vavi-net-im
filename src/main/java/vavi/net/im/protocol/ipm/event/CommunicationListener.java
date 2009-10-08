/*
 * 1997/10/16 (C) Copyright T.Kazawa (Digitune)
 */

package vavi.net.im.protocol.ipm.event;

import java.io.IOException;
import java.util.EventListener;


/**
 * CommunicationListener.
 */
public interface CommunicationListener extends EventListener {
    /** */
    void receive(CommunicationEvent event) throws IOException;
}

/* */
