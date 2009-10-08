/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im;

import java.net.URL;


/**
 * A component to represent URLs in instant messages.
 *
 * A URL is special in that clients may want to show it as a "web link" rather than
 * plain text. The URL component allows this by providing a link text and link URL.
 * The link text is plain text that needs to be displayed to the user, while
 * the link URL is a URL to which the user may connect to.
 */
public class URLComponent implements MessageComponent {
    /**
     * The text to be displayed to the user.
     */
    private String linkText;

    /**
     * The URL of this component.
     */
    private URL linkURL;

    /**
     * Construct a url component with a link text and link url.
     *
     * @param linkText the link text for this component.
     * @param linkURL  the link URL for this component.
     */
    public URLComponent(String linkText, URL linkURL) {
        this.linkText = linkText;
        this.linkURL = linkURL;
    }

    /**
     * Returns the link text associated with this URL component.
     *
     * @return the link text of this URL component.
     */
    public String getLinkText() {
        return linkText;
    }

    /**
     * Returns the link URL associated with this URL component.
     *
     * @return the link URL of this URL component.
     */
    public URL getLinkURL() {
        return linkURL;
    }

    /** */
    public String  toString() {
        return "<url>" + linkText;
    }
}

/* */
