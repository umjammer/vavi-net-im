/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im;

import javax.swing.Icon;


/**
 * A component to represent smileys (emoticons) in instant messages.
 *
 * <p>
 * A smiley is a small picture, sometimes referred to as an emoticon, used to represent
 * a user's emotions. For each smiley, there will be one or more alternate text
 * representations, and a descriptive name.
 *
 * <p>
 * For example, for a smiley showing a "sad face", the textual representation may be
 * something like ":(" or ":-(", while the descriptive name may be "sad".
 *
 * <p>
 * Clients that don't want to display smileys (may be because the client is a text
 * based one), may use the text representation. Alternatively, they may use the descriptive
 * name to convey the exact mood represented by the smiley.
 *
 */
public class SmileyComponent implements MessageComponent {
    /**
     * The icon for this smiley.
     */
    private Icon icon;

    /**
     * Alternate textual representation for this smiley.
     */
    private String[] text;

    /**
     * A descriptive name for this smiley.
     */
    private String name;

    /**
     * Construct a new smiley with a given icon and alternate text.
     *
     * @param icon the icon for this smiley.
     * @param text the alternate text for this smiley.
     * @param name the descriptive name for this smiley.
     */
    public SmileyComponent(Icon icon, String[] text, String name) {
        this.icon = icon;
        this.text = text;
        this.name = name;
    }

    /**
     * Returns the icon of this smiley.
     *
     * @return icon representing this smiley.
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Returns the alternate text for this smiley.
     *
     * @return alternate text representing this smiley.
     */
    public String[] getText() {
        return text;
    }

    /**
     * Returns the descriptive name for this smiley.
     *
     * @return descriptive name representing this smiley.
     */
    public String getName() {
        return name;
    }

    /** */
    public String  toString() {
        return "<smiley>" + name;
    }
}

/* */
