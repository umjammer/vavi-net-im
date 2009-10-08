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
     * �m�[�h���ʎq
     * �Eoptional
     * �E�N���C�A���g�Ȃǂ̃T�[�r�X���󂯂�G���e�B�e�B
     * �E256�o�C�g�܂�
     *   (0x20�ȏ�̃��j�R�[�h�����g�p�B"&':<>@ ����� 0xfffe, 0xffff �͏���)
     * �E�啶����������ʗL
     */
    private String node;

    /**
     * �h���C�����ʎq
     * �E�K�{
�@   * �E�l�b�g���[�N�Q�[�g�E�F�C��������"�v���C�}��"�z�X�g�B�T�[�r�X���܂ށB
     * �E255�����ȉ���case-insensitive 7-bit ASCII (RRC 952, REF 1123)
     */
    private String domain;

    /**
     * ���\�[�X���ʎq
     * �Eoptional
     * �E����̃Z�b�V�����A�ڑ�(device, location)�A
     *   ����m�[�h�ɑ�����I�u�W�F�N�g�ȂǁB
     * �E�ЂƂ̃m�[�h�ɑ΂��āA�����̃��\�[�X�������Ă悢�B
     * �E256�o�C�g�܂�(0x20 �ȏ�̃��j�R�[�h�����g�p�B0xfffe, 0xffff �͏���)
     * �E�啶����������ʗL
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
        new JabberIdentifier("sano@/JabberWeb");	// TODO �ʂ�
    }
}

/* */
