/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.event;


/**
 * 戻り値がある場合に使用する IM のイベントです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class CallableIMEvent extends IMEvent {

    /** イベントの戻り値 */
    private Object result;

    /**
     * 戻り値がある IM のイベントを構築します。
     * 
     * @param source the event source
     * @param name the event name
     * @param arguments the event argument
     */
    public CallableIMEvent(Object source, Name name, Object ... arguments) {
        super(source, name, arguments);
    }

    /** イベントの戻り値を設定します。 */
    public void setResult(Object result) {
        this.result = result;
    }

    /** イベントの戻り値を取得します。 */
    public Object getResult() {
        return result;
    }
}

/* */
