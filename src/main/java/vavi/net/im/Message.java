/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vavi.net.im.event.IMListener;


/**
 * An instant message.
 *
 * Instant messages contain not only plain text messages, but various
 * other things such as URLs, font and color variations, smileys etc.
 * Unfortunately, different protocols use different encoding schemes for
 * sending instant messages. This class provides an implementation
 * independent way of representing instant messages, so that users
 * of Hamsam library do not have to worry about proprietary message
 * formats.
 *
 * <p>
 * Each message object contains a sequence of message components. Each
 * message component will be one of the following.
 *
 * <p>
 * <h3>1. TextComponent</h3>
 * A text component contains a sequence of characters that represent a message.
 * This sequence, together with the font and color information, determines what
 * to display to the user.
 *
 * <p>
 * <h3>2. SmileyComponent</h3>
 * This is a small picture, sometimes referred to as an emoticon, used to represent
 * a user's emotions. For each smiley, there will be an alternate text representation.
 * For clients that don't want to display smileys, may use this text representation.
 *
 * <p>
 * <h3>3. URLComponent</h3>
 * A URL is special in that clients may want to show it as a "web link" rather than
 * plain text. The URL component allows this by providing a link text and link URL.
 * The link text is a text component that needs to be displayed to the user, while
 * the link URL is a URL to which the user may connect to.
 *
 * <p>
 * All the message components mentioned above are inherited from the
 * {@link MessageComponent MessageComponent} interface. You can use these components
 * in an {@link IMListener IMListener} as shown below.
 *
 * <p>
 * <code>
 * public void instantMessageReceived(Buddy buddy, Message message) {
 *     Iterator i = message.getComponents();
 *     while (i.hasNext()) {
 *         MessageComponent comp = (MessageComponent) i.next();
 *         if(comp instanceof TextComponent) {
 *             // handle text component
 *         } else if (comp instanceof SmileyComponent) {
 *             // handle smiley component
 *         } else if (comp instanceof URLComponent) {
 *             // handle URL component
 *         }
 *     }
 * }
 * </code>
 *
 * @author Raghu
 */
public class Message implements Serializable {
    /**
     * This vector holds all the mesage components.
     */
    private List<MessageComponent> components = new ArrayList<>();
    
    /** */
    private Date date = new Date();

    /**
     * Default constructor.
     */
    public Message() {
    }

    /**
     * Utility constructor.
     */
    public Message(String text) {
        components.add(new TextComponent(text));
    }

    /**
     * Add a message component to this message.
     *
     * @param comp the component to be added.
     */
    public void addComponent(MessageComponent comp) {
        components.add(comp);
    }

    /**
     * Returns list of the components of this message. The returned
     * Enumeration object will generate all message components in this message.
     *
     * @return an enumeration of the message components of this message.
     * @see MessageComponent MessageComponent
     */
    public List<MessageComponent> getComponents() {
        return components;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return Returns the date.
     */
    public Date getDate() {
        return date;
    }

    /** */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MessageComponent component : components) {
            sb.append(component.toString());
        }
        return sb.toString();
    }
}
