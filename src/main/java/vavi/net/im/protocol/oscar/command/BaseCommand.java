/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.net.im.protocol.oscar.command;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import vavi.net.im.protocol.oscar.flap.FlapConstants;
import vavi.net.im.protocol.oscar.flap.FlapHeader;
import vavi.net.im.protocol.oscar.snac.SNACPacket;
import vavi.net.im.protocol.oscar.util.TLV;


/**
 *  Concrete Command class.  Used as a concrete Command class
 *  for incoming Commands and also to create 'empty' SNAC Commands.
 */
public class BaseCommand extends Command {
    /**
     * Constructor
     * @param hdr flap header
     * @param cmdbytes flap command bytes
     * @param tlvs flap tlvs
     */
    public BaseCommand(FlapHeader hdr, byte[] cmdbytes, List<TLV> tlvs) {
        flapHdr = hdr;
        data = cmdbytes;
        tlvList = tlvs;
    }

    /**
     * Constructor
     * @param hdr flap header
     * @param packet SNAC Packet
     * @param tlvs command TLVs
     * @param dataBytes extra command data
     */
    public BaseCommand(FlapHeader hdr, SNACPacket packet, List<TLV> tlvs,
                   byte[] dataBytes) {
        super(hdr, packet, tlvs, dataBytes);
    }

    /**
     * Constructor - used for creating empty SNAC Commands
     * @param seqNum flap header sequence number
     * @param snacFamily SNAC Packet family id
     * @param snacSubtype SNAC Packet subtype id
     */
    public BaseCommand(int seqNum, int snacFamily, int snacSubtype) {
        flapHdr = new FlapHeader(FlapConstants.FLAP_CHANNEL_SNAC, seqNum);
        snacPacket = new SNACPacket(snacFamily, snacSubtype);
    }

    /** */
    public void writeCommandData(OutputStream os) throws IOException {
        // nothing to write - empty command bytes
    }
}
