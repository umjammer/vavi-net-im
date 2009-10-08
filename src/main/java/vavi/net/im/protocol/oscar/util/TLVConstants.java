/*
 *
 * Hamsam - Instant Messaging API
 *
 * Copyright (C) 2003 Mike Miller <mikemil@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
package vavi.net.im.protocol.oscar.util;


/**
 * @author mike
 */
public class TLVConstants {
    public static final int TLV_TYPE_SCREEN_NAME = 0x0001;
    public static final int TLV_TYPE_ROASTED_PASSWORD = 0x0002;
    public static final int TLV_TYPE_CLIENT_ID_STRING = 0x0003;
    public static final int TLV_TYPE_ERR_URL = 0x0004;
    public static final int TLV_TYPE_SERVER = 0x0005;
    public static final int TLV_TYPE_COOKIE = 0x0006;
    public static final int TLV_TYPE_USER_STATUS_FLAGS = 0X0006;
    public static final int TLV_TYPE_ERROR_CODE = 0x0008;
    public static final int TLV_TYPE_DC_INFO = 0x000C;
    public static final int TLV_TYPE_CLIENT_COUNTRY = 0x000E;
    public static final int TLV_TYPE_CLIENT_LANGUAGE = 0x000F;
    public static final int TLV_TYPE_EMAIL_ID = 0x0011;
    public static final int TLV_TYPE_DISTRIBUTION_NUMBER = 0x0014;
    public static final int TLV_TYPE_CLIENT_ID = 0x0016;
    public static final int TLV_TYPE_CLIENT_MAJOR_VERSION = 0x0017;
    public static final int TLV_TYPE_CLIENT_MINOR_VERSION = 0x0018;
    public static final int TLV_TYPE_CLIENT_LESSOR_VERSION = 0x0019;
    public static final int TLV_TYPE_CLIENT_BUILD_NUMBER = 0x001A;
    public static final int TLV_TYPE_CHG_PSWD_URL = 0x0054;

    // AOL Away related TLVs
    public static final int TLV_TYPE_AWAY_FMT = 0x0003;
    public static final int TLV_TYPE_AWAY = 0x0004;
}
