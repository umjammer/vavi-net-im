/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.event;

import java.io.IOException;
import java.util.EventListener;


/**
 * IMListener. 
 * リスナを作成する場合は、わかりやすいプログラムを書くために
 * {@link vavi.net.im.event.IMAdapter} を使用してください。
 * @see vavi.net.im.event.IMAdapter
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 051102 nsano initial version <br>
 */
public interface IMListener extends EventListener {
    /** */
    void eventHappened(IMEvent event) throws IOException;
}

/* */
