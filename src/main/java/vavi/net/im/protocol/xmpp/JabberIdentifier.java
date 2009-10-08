/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.xmpp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vavi.util.Debug;


/**
 * JabberIdentifier.
 *
 * @author	<a href=mailto:vavivavi@yahoo.co.jp>Naohide Sano</a> (nsano)
 * @version	0.00	040308	nsano	initial version <br>
 */
public class JabberIdentifier {

    /**
     * ノード識別子
     * ・optional
     * ・クライアントなどのサービスを受けるエンティティ
     * ・256バイトまで
     *   (0x20以上のユニコード文字使用可。"&':<>@ および 0xfffe, 0xffff は除く)
     * ・大文字小文字区別有
     */
    private String node;

    /**
     * ドメイン識別子
     * ・必須
　   * ・ネットワークゲートウェイもしくは"プライマリ"ホスト。サービスも含む。
     * ・255文字以下のcase-insensitive 7-bit ASCII (RRC 952, REF 1123)
     */
    private String domain;

    /**
     * リソース識別子
     * ・optional
     * ・特定のセッション、接続(device, location)、
     *   あるノードに属するオブジェクトなど。
     * ・ひとつのノードに対して、複数のリソースがあってよい。
     * ・256バイトまで(0x20 以上のユニコード文字使用可。0xfffe, 0xffff は除く)
     * ・大文字小文字区別有
     */
    private String resource;

    /** */
    public JabberIdentifier(String node, String domain, String resource) {
        this(node + "@" + domain + "/" + resource);
    }

    /** */
    private static final Pattern pattern = Pattern.compile("(.*@)?([^\\/]+)(\\/.*)?");

    /** */
    public JabberIdentifier(String jid) {
        Matcher matcher = pattern.matcher(jid);
//  Debug.println("---- " + jid + " (" + matcher.matches() + ") ----");
//  if (matcher.matches()) {
//   Debug.println("groups: " + matcher.groupCount());
//   Debug.println("0: " +  matcher.group(0));
//   for (int i = 1; i <= matcher.groupCount(); i++) {
//    Debug.println("groups:" + i + ": " + matcher.group(i));
//   }
//  }
        if (!matcher.matches() || matcher.groupCount() != 3) {
            throw new IllegalArgumentException(jid);
        }

        this.node = matcher.group(1);
        node = node == null ? null : node.substring(0, node.length() - 1);

        this.domain = matcher.group(2);
        if (domain == null || domain.length() == 0) {
            throw new IllegalArgumentException("domain must be set");
        }

        this.resource = matcher.group(3);
        resource = resource == null ? null : resource.substring(1);
Debug.println(this);
    }

    /** */
    public void setNode(String node) {
        this.node = node;
    }

    /** */
    public String getNode() {
        return node;
    }

    /** */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /** */
    public String getDomain() {
        return domain;
    }

    /** */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /** */
    public String getResource() {
        return resource;
    }

    /** TODO */
    public boolean isValid() {
        return ! (domain == null || domain.length() == 0);
    }

    /** */
    public String toStringWithoutResource() {
        return (node == null ? "" : node + "@") + domain;
    }

    /** */
    public String toString() {
        return (node == null ? "" : node + "@") + domain + (resource == null ? "" :  "/" + resource);
    }

    //----

    public static void main(String[] args) {
        new JabberIdentifier("nsano@jabber.jp/rymbox");
        new JabberIdentifier("jabber.jp/rymbox");
        new JabberIdentifier("@jabber.jp/rymbox");
        new JabberIdentifier("nsano@jabber.jp");
        new JabberIdentifier("jabber.jp/");
        new JabberIdentifier("sano@/JabberWeb");	// TODO 通る
    }
}

/* */
