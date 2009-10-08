/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package vavi.net.im.protocol.oscar.snac;


/**
 * @author mikem
 */
public class SNACConstants {
    public static final String VERSION = "$Id: SNACConstants.java,v 1.5 2003/09/20 03:24:05 mikemil Exp $";

    // SNAC Family Ids
    public static final int SNAC_FAMILY_GENERIC_SERVICE_CONTROLS = 0x0001;
    public static final int SNAC_FAMILY_LOCATION_SERVICES = 0x0002;
    public static final int SNAC_FAMILY_BUDDY_LIST_MANAGEMENT = 0x0003;
    public static final int SNAC_FAMILY_MESSAGING = 0x0004;
    public static final int SNAC_FAMILY_ADVERTISEMENTS = 0x0005;
    public static final int SNAC_FAMILY_INVITATION = 0x0006;
    public static final int SNAC_FAMILY_ADMINISTRATIVE = 0x0007;
    public static final int SNAC_FAMILY_POPUP_NOTICES = 0x0008;
    public static final int SNAC_FAMILY_PRIVACY_MANAGEMENT = 0x0009;
    public static final int SNAC_FAMILY_USER_LOOKUP = 0x000a;
    public static final int SNAC_FAMILY_STATS = 0x000b;
    public static final int SNAC_FAMILY_TRANSLATE = 0x000c;
    public static final int SNAC_FAMILY_CHAT_NAVIGATION = 0x000d;
    public static final int SNAC_FAMILY_CHAT = 0x000e;
    public static final int SNAC_FAMILY_SEARCH = 0x000f;
    public static final int SNAC_FAMILY_SERVER_STORED_ICONS = 0x0010;
    public static final int SNAC_FAMILY_SERVER_STORED_INFO = 0x0013;
    public static final int SNAC_FAMILY_AUTHENTICATION = 0x0017;
    public static final int SNAC_FAMILY_EMAIL = 0x0018;
    public static final int SNAC_FAMILY_UNKNOWN = 0x0045;

    // SNAC Subtypes for the Generic Service Controls (0x0001)

    /** C/S Error */
    public static final int SNAC_SUBTYPE_01_01 = 0x0001;
    public static final int CLIENT_READY = 0x0002;
    public static final int FAMILY_LIST = 0x0003;
    public static final int REQ_SERVER_RATE_INFO = 0x0006;
    public static final int SERVER_RATE_INFO = 0x0007;
    public static final int ACK_RATE_INFO = 0x0008;
    public static final int SRV_ONLINE_INFO = 0x000f;
    public static final int MSG_OF_THE_DAY = 0x0013;
    public static final int REQ_SRV_SVC_VERSIONS = 0x0017;
    public static final int SRV_SVC_VERSIONS = 0x0018;
    public static final int CLIENT_STATUS = 0x001e;

    // SNAC Subtypes for Location Services (0x0002)
    public static final int CLIENT_SET_LOCATION_INFO = 0x0004;

    // Authentication subtypes (0x0017)
    public static final int SNAC_SUBTYPE_ERROR = 0x0001;
    public static final int SNAC_SUBTYPE_SEND_LOGIN_DATA = 0x0002;
    public static final int SNAC_SUBTYPE_AUTH_RESPONSE = 0x0003;
    public static final int SNAC_SUBTYPE_KEY_REQUEST = 0x0006;
    public static final int SNAC_SUBTYPE_KEY_REQUEST_RESPONSE = 0x0007;
    public static final int SNAC_SUBTYPE_SECURID_REQUEST = 0x000A;
    public static final int SNAC_SUBTYPE_SEND_SECURID_REQUEST = 0x000B;
    public static final int SNAC_SUBTYPE_POSSIBLE_TLV = 0xFFFF;

    // Buddy List Management subtypes (0x0003)
    public static final int SNAC_SUBTYPE_ADD_BUDDY = 0x0004;
    public static final int SNAC_SUBTYPE_DEL_BUDDY = 0x0005;
    public static final int SNAC_SUBTYPE_USER_ONLINE = 0x000B;
    public static final int SNAC_SUBTYPE_USER_OFFLINE = 0x000C;

    // Messaging Family subtypes (0x0004)
    public static final int SRV_ICBM_ERROR = 0x0001;
    public static final int SET_ICBM_PARMS = 0x0002;
    public static final int REQUEST_ICBM_PARMIFO = 0x0004;
    public static final int RESPONSE_ICBM_PARMINFO = 0x0005;
    public static final int CLIENT_SEND_ICBM = 0x0006;
    public static final int CLIENT_RECV_ICBM = 0x0007;
    public static final int SNAC_SUBTYPE_SRV_ACK_MSG = 0x000c;
    public static final int SNAC_SUBTYPE_MINI_TYPING_NOTIFICATION = 0x0014;

    // Privacy Management subtypes (0x0009)
    public static final int SNAC_SUBTYPE_ADD_VISIBLE = 0x0005;
    public static final int SNAC_SUBTYPE_DEL_VISIBLE = 0x0006;
    public static final int SNAC_SUBTYPE_ADD_INVISIBLE = 0x0007;
    public static final int SNAC_SUBTYPE_DEL_INVISIBLE = 0x0008;

    // Mini Typing Notification values
    public static final int MTN_TYPING_FINISHED = 0x00;
    public static final int MTN_TEXT_TYPED = 0x01;
    public static final int MTN_TYPING_STARTED = 0x02;

    // Chat Navigation (0x000d)
    // SSI subtype values (0x0010)
    public static final int REQUEST_CONTACT_LIST = 0x0004;
    public static final int CONTACT_LIST_RESPONSE = 0x0006;
    public static final int ACTIVATE_SERVER_SSI = 0x0007;

    // Roster (0x0013)

    /** C, Modify an item in the server-stored information */
    public static final int SNAC_SUBTYPE_13_09 = 0x0009;

    /** C, About to modify list */
    public static final int SNAC_SUBTYPE_13_11 = 0x0011;

    /** C, Finished modifying list */
    public static final int SNAC_SUBTYPE_13_12 = 0x0012;
}

/* */
