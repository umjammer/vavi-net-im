/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import vavi.net.im.event.IMSupport;
import vavi.net.im.protocol.oscar.command.Command;
import vavi.net.im.protocol.oscar.handler.Handler;



/**
 * OscarEventHandler. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051117 nsano initial version <br>
 */
public class OscarEventHandler {

    /** */
    private static Logger log = Logger.getLogger(OscarEventHandler.class.getName());

    /** */
    private OscarConnection connection;

    /** */
    private Map<Integer, Handler> handlers = new HashMap<Integer, Handler>();

    /** */
    public OscarEventHandler(OscarConnection connection) {
        this.connection = connection;

        try {
            final String path = "handler.properties";
            Properties props = new Properties();
            props.load(OscarEventHandler.class.getResourceAsStream(path));
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                int eventNumber = Integer.parseInt(key);
                String handlerClassName = props.getProperty(key);
                @SuppressWarnings("unchecked")
                Class<Handler> handlerClass = (Class<Handler>) Class.forName(handlerClassName);
                Handler handler = handlerClass.newInstance();
                handlers.put(eventNumber, handler);
            }
        } catch (Exception e) {
            throw (RuntimeException) new IllegalStateException().initCause(e);
        }
    }


    /** Connects event handlers */
    public void setIMListeners(IMSupport listeners) {
        for (Handler handler : handlers.values()) {
            handler.setIMListeners(listeners);
        }
    }

    // ----

    /** */
    private ExecutorService handringService = Executors.newSingleThreadExecutor();

    /** */
    private Future<?> handring;

    /**
     * Processes the incoming events from the reader thread
     */
    private Runnable handler = new Runnable() {
        /** */
        public void run() {
log.info("+++ OscarEventHandler started");
            while (connection.isRunning()) {
                try {
                    Command command = Command.getCommand(connection.getConnection());
                    if (command == null) {
log.warning("command: " + command);
                        continue;
                    }
log.info("status: " + connection.getStatus());
log.info("incoming event <--- " + command.toString());
                    Handler handler = handlers.get(connection.getStatus().ordinal());
                    handler.exec(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
log.info("--- OscarEventHandler stopped");
        }
    };

    /** */
    public void start() {
        handring = handringService.submit(handler);
    }

    /** */
    public void stop() {
        if (!handring.isCancelled()) {
            handring.cancel(true);
        }
    }
}

/* */
