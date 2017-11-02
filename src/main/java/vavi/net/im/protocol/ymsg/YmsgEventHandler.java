/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import vavi.net.im.event.IMSupport;
import vavi.net.im.protocol.Connection;
import vavi.net.im.protocol.ymsg.YmsgPacketHeader.Event;
import vavi.net.im.protocol.ymsg.command.Command;
import vavi.util.Debug;
import vavi.util.StringUtil;


/**
 * YmsgEventHandler.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040221 nsano initial version <br>
 */
class YmsgEventHandler {

    /** */
    private Connection connection;

    /** */
    private Map<Event, Command> commands = new HashMap<>();

    /** */
    public YmsgEventHandler(Connection connection) {
        this.connection = connection;

        try {
            final String path = "command.properties";
            Properties props = new Properties();
            props.load(YmsgEventHandler.class.getResourceAsStream(path));
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                YmsgPacketHeader.Event eventNumber = YmsgPacketHeader.Event.valueOf(Integer.parseInt(key));
                String commandClassName = props.getProperty(key);
                @SuppressWarnings("unchecked")
                Class<Command> commandClass = (Class<Command>) Class.forName(commandClassName);
                Command command = commandClass.newInstance();
                commands.put(eventNumber, command);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /** Connects event handlers */
    public void setIMListeners(IMSupport listeners) {
        for (Command command : commands.values()) {
            command.setIMListeners(listeners);
        }
    }

    // ----

    /** */
    private ExecutorService handringService = Executors.newSingleThreadExecutor();

    /** */
    private Future<?> handring;

    /** */
    private Runnable handler = new Runnable() {
        public void run() {

Debug.println("\n------------------------ start loop ------------------------");
            InputStream is = connection.getInputStream();

            while (true) {
                YmsgPacket yp = null;
                try {
                    if (is.available() > 0) {
//byte[] b = new byte[2048];
//ByteArrayOutputStream baos = new ByteArrayOutputStream();
//while (is.available() > 0) {
// int r = is.read(b, 0, b.length);
// if (r < 0) {
//  break;
// }
// baos.write(b, 0, r);
//}
//Debug.println("received:\n" + StringUtil.getDump(baos.toByteArray()));
//InputStream isOrig = is;
//is = new ByteArrayInputStream(baos.toByteArray());
                        yp = YmsgPacket.readFrom(is);
Debug.println("received:\n" + StringUtil.getDump(yp.toByteArray()));
//is = isOrig;
                        YmsgPacketHeader.Event event = yp.getHeader().getEvent();
                        Command command = commands.get(event);
if (command == null) {
  throw new IllegalStateException("command is not implemented: " + event);
}
Debug.println("command: " + command + ", " + event);
                        command.exec(yp);
Debug.println("\n------------------------ done a process ------------------------");
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                } catch (Exception t) {
Debug.printStackTrace(t);
if (yp != null) {
 Debug.println("error packet:\n" + StringUtil.getDump(yp.toByteArray()));
}
// TODO コネクションが切れた場合ループする
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
Debug.println("\n------------------------ exit loop ------------------------");
        }
    };

    /** */
    public void start() {
        handring = handringService.submit(handler);
    }

    /** */
    public void stop() {
Debug.println("cancelled: " + handring.isCancelled());
        if (!handring.isCancelled()) {
            handring.cancel(true);
        }
    }
}

/* */
