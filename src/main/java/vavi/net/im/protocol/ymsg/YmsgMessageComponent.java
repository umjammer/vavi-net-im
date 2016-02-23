/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.ymsg;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import vavi.net.im.TextComponent;
import vavi.util.StringUtil;


/**
 * YmsgMessageComponent.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040221 nsano initial version <br>
 */
public class YmsgMessageComponent extends TextComponent {

    /** */
    private static final String defaultFontName = "MS UI Gothic";
    /** */
    private static final int defaultFontSize = 10; // 10pt
    /** */
    private static final int defaultColor = 0; // black

    /** */
    private int charSet = 128;
    /** */
    private int bgColor = -1;
    
    /**
     * YMSG -> Generic
     * @param message the message
     */
    public YmsgMessageComponent(String message) {
        String tmp = null;

        // 余分な改行の削除
        String mes = message;
        // カラーの取得
        this.color = new Color(parseColor(message, tmp));
        mes = tmp;
        // スタイルの取得 (未対応)
        tmp = removeColorAndStyle(mes);
        mes = tmp;
        // フォント名の取得
        this.font = new Font(parseFontName(mes, tmp), Font.PLAIN, parseFontSize(mes, tmp));
        mes = tmp;
        // 余分なタグの削除
        mes = removeTags(mes);

        // メッセージの保存
        buffer.append(color);
        buffer.append("<font face=\"");
        buffer.append(font.getName());
        buffer.append("%s\" size=\"");
        buffer.append(font.getSize());
        buffer.append("\">");
        buffer.append(mes);
    }
    
    /**
     * Generic -> YMSG
     * @param text
     * @param font
     * @param color
     */
    public YmsgMessageComponent(String text, Font font, Color color) {
        super(text, font, color);
    }

    /** */
    private int parseColor(String src, String dst) {
        StringBuilder dstBody = new StringBuilder();

        boolean colorFound = false;
        int color = 0;

        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) == 0x1b) {
                int c = toColorInt(src.substring(i));

                if (0 <= c) {
                    String yc = toEscapeSequenceString(c);
                    i += yc.length() - 1;

                    if (!colorFound) {
                        color = c;
                        colorFound = true;
                    }

                    continue;
                }
            }

            dstBody.append(src.charAt(i));
        }

        dst = dstBody.toString();

        return color;
    }

    /** */
    private String removeColorAndStyle(String src) {
        StringBuilder dstBody = new StringBuilder();

        for (int i = 0; i < src.length(); i++) {
            if ((src.charAt(i) == 0x1b) && (src.charAt(i + 1) == '[')) {
                while (src.charAt(i) != 'm') {
                    i++;
                }

                continue;
            }

            dstBody.append(src.charAt(i));
        }

        return dstBody.toString();
    }

    /** */
    private String parseFontName(String src, String dst) {
        final String fontTag = "<font";
        final String fontNameTag = "face=\"";

        boolean foundFontTag = false;

        StringBuilder dstBody = new StringBuilder();
        String fontName = null;

        for (int i = 0; i < src.length(); i++) {
            if (fontName == null) {
                if (!foundFontTag) {
                    if (src.substring(i).toLowerCase().startsWith(fontTag)) {
                        foundFontTag = true;
                    }
                } else {
                    if (src.substring(i).toLowerCase().startsWith(fontNameTag)) {
                        i += fontNameTag.length();

                        for (int k = i; k < src.length(); k++) {
                            if (src.charAt(k) == '\"') {
                                fontName = src.substring(i, k - 1);
                                i += k;

                                break;
                            }
                        }

                        i--;

                        continue;
                    } else if (src.charAt(i) == '>') {
                        foundFontTag = false;
                    }
                }
            }

            dstBody.append(src.charAt(i));
        }

        dst = dstBody.toString();

        String fn = null;

        if (fontName == null) {
            fn = defaultFontName;
        } else {
            fn = fontName;
        }

        return fn;
    }

    /** */
    private int parseFontSize(String src, String dst) {
        final String fontTag = "<font";
        final String fontSizeTag = "size=\"";

        boolean foundFontTag = false;

        StringBuilder dstBody = new StringBuilder();
        String fontSize = null;

        for (int i = 0; i < src.length(); i++) {
            if (fontSize == null) {
                if (!foundFontTag) {
                    if (src.substring(i).toLowerCase().startsWith(fontTag)) {
                        foundFontTag = true;
                    }
                } else {
                    if (src.substring(i).toLowerCase().startsWith(fontSizeTag)) {
                        i += fontSizeTag.length();

                        for (int k = i; k < src.length(); k++) {
                            if (src.charAt(k) == '\"') {
                                fontSize = src.substring(i, k - 1);
                                i += k;

                                break;
                            }
                        }

                        i--;

                        continue;
                    } else if (src.charAt(i) == '>') {
                        foundFontTag = false;
                    }
                }
            }

            dstBody.append(src.charAt(i));
        }

        dst = dstBody.toString();

        int fs = -1;

        if (fontSize != null) {
            fs = Integer.parseInt(fontSize);
        }

        return fs;
    }

    /** */
    private static final String[] tags = {
        "<font", "<fade", "</fade", "<alt", "</alt"
    };

    /**
     * @return tag removed string 
     */
    private String removeTags(String text) {
        boolean insideTag = false;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (insideTag) {
                if (text.charAt(i) == '>') {
                    insideTag = false;

                    continue;
                }
            } else {
                for (int k = 0; k < tags.length; k++) {
                    if (text.substring(i).toLowerCase().startsWith(tags[k])) {
                        i += tags[k].length() - 1;
                        insideTag = true;

                        break;
                    }
                }

                if (!insideTag) {
                    result.append(text.charAt(i));
                }
            }
        }

        return result.toString();
    }

    //----
    
    /** */
    private static final Map<Integer, String> colorLookupTable = new HashMap<Integer, String>();
    
    /** <#rrggbb, escapeSequence> */
    static {
        colorLookupTable.put(0x000000, (char) 0x1b + "[30m"); // BLACK
        colorLookupTable.put(0xff0000, (char) 0x1b + "[31m"); // BLUE
        colorLookupTable.put(0xffff00, (char) 0x1b + "[32m"); // LIGHTBLUE
        colorLookupTable.put(0x808080, (char) 0x1b + "[33m"); // GRAY
        colorLookupTable.put(0x008000, (char) 0x1b + "[34m"); // GREEN
        colorLookupTable.put(0xff00ff, (char) 0x1b + "[35m"); // PINK
        colorLookupTable.put(0x800080, (char) 0x1b + "[36m"); // PURPLE
        colorLookupTable.put(0x0080ff, (char) 0x1b + "[37m"); // ORANGE
        colorLookupTable.put(0x0000ff, (char) 0x1b + "[38m"); // RED
        colorLookupTable.put(0x008080, (char) 0x1b + "[39m"); // OLIVE
        colorLookupTable.put(0x00ffff, (char) 0x1b + "[37m"); // YELLOW
    };

    /**
     * @param escapeSequence ESC [ ansiColor m, ESC [ # bb gg rr m
     * @return 0xrrggbb
     */
    private static int toColorInt(String escapeSequence) {
        if (colorLookupTable.containsValue(escapeSequence)) {
            for (Map.Entry<Integer, String> tableEntry : colorLookupTable.entrySet()) {
                if (tableEntry.getValue().equals(escapeSequence)) {
                    return tableEntry.getKey();
                }
            }
        }

        if (escapeSequence.charAt(0) == 0x1b &&
            escapeSequence.charAt(1) == '[' &&
            escapeSequence.charAt(2) == '#') {

            int ymagColor = Integer.parseInt(escapeSequence.substring(3));

            int b = (ymagColor & 0xff0000) >> 16;
            int g = (ymagColor & 0x00ff00) >> 8;
            int r =  ymagColor & 0x0000ff;

            int color = (r << 16) | (g << 8) | b;

            return color;
        }

        return -1;
    }

    /**
     * @param color 0xrrggbb
     * @return ESC [ bb rr gg m
     */
    private static String toEscapeSequenceString(int color) {
        if (colorLookupTable.containsKey(color)) {
            return colorLookupTable.get(color);
        }

        int r = (color & 0xff0000) >> 16;
        int g = (color & 0x00ff00) >> 8;
        int b =  color & 0x0000ff;

        return (char) 0x1b + "[#" +
            StringUtil.toHex2(b) +
            StringUtil.toHex2(g) +
            StringUtil.toHex2(r) +
            "m";
    }
}

/* */
