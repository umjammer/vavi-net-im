/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im;

import java.awt.Color;
import java.awt.Font;


/**
 * A text component of a message.
 *
 * <p>
 * A text component contains a sequence of characters that represent a message.
 * This sequence, together with the font and color information, determines what
 * to display to the user.
 *
 * <p>
 * This class defines various methods to set and retrieve the sequence of characters,
 * as well as the font and color information.
 *
 * @author  Raghu
 */
public class TextComponent implements MessageComponent {
    /**
     * Buffer holding the sequence of characters in this
     * text component.
     */
    protected StringBuilder buffer = new StringBuilder();

    /**
     * The font to be used for this component.
     */
    protected Font font;

    /**
     * The color to be used for this component.
     */
    protected Color color;

    /**
     * Construct a new text component that represents an empty character
     * sequence. This constructor is equivalent to
     * <code>TextComponent(null, null, null)</code>.
     */
    public TextComponent() {
    }

    /**
     * Construct a new text component and initialize it with the given
     * character sequence. This constructor is equivalent to
     * <code>TextComponent(str, null, null)</code>.
     *
     * @param text sequence of characters representing this text component.
     */
    public TextComponent(String text) {
        this(text, null, null);
    }

    /**
     * Construct a new text component and initialize it with a given
     * character sequence and color information. This constructor is
     * equivalent to <code>TextComponent(str, null, color)</code>.
     *
     * @param text sequence of characters representing this text component.
     * @param color the color of the text represented by this component.
     */
    public TextComponent(String text, Color color) {
        this(text, null, color);
    }

    /**
     * Construct a new text component and initialize it with a given
     * character sequence and font information. This constructor is
     * equivalent to <code>TextComponent(str, font, null)</code>.
     *
     * @param text sequence of characters representing this text component.
     * @param font the font of the text represented by this component.
     */
    public TextComponent(String text, Font font) {
        this(text, font, null);
    }

    /**
     * Construct a new text component and initialize it with a given
     * character sequence, font, and color information.
     *
     * @param text sequence of characters representing this text component.
     * @param font the font of the text represented by this component.
     * @param color the color of the text represented by this component.
     */
    public TextComponent(String text, Font font, Color color) {

        if (text != null) {
            append(text);
        }

        if (font != null) {
            setFont(font);
        }

        if (color != null) {
            setColor(color);
        }
    }

    /**
     * Appends the string to this text component.
     *
     * @param text the string to be appended.
     */
    public void append(String text) {
        buffer.append(text);
    }

    /**
     * Appends the sequence of characters to this text component.
     *
     * @param text the characters to be appended.
     */
    public void append(char[] text) {
        buffer.append(text);
    }

    /**
     * Returns the sequence of characters corresponding to this
     * text component.
     *
     * @return the sequence of characters of this component.
     */
    public char[] getSequence() {
        return buffer.toString().toCharArray();
    }

    /**
     * Specify the font to be used with this text component.
     *
     * @param font the font to be used.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Returns the font that is used with this text component.
     *
     * @return the font used.
     */
    public Font getFont() {
        return font;
    }

    /**
     * Specify the color to be used with this text component.
     *
     * @param color the color to be used.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the color that is used with this text component.
     *
     * @return the color used.
     */
    public Color getColor() {
        return color;
    }

    /** */
    public String toString() {
        return new String(getSequence());
    }
}

/* */
