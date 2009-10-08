/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.event;


/**
 * �߂�l������ꍇ�Ɏg�p���� IM �̃C�x���g�ł��D
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class CallableIMEvent extends IMEvent {

    /** �C�x���g�̖߂�l */
    private Object result;

    /**
     * �߂�l������ IM �̃C�x���g���\�z���܂��B
     * 
     * @param source the event source
     * @param name the event name
     * @param arguments the event argument
     */
    public CallableIMEvent(Object source, Name name, Object ... arguments) {
        super(source, name, arguments);
    }

    /** �C�x���g�̖߂�l��ݒ肵�܂��B */
    public void setResult(Object result) {
        this.result = result;
    }

    /** �C�x���g�̖߂�l���擾���܂��B */
    public Object getResult() {
        return result;
    }
}

/* */
