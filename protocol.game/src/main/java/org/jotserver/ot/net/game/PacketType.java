package org.jotserver.ot.net.game;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.jotserver.ot.net.game.parsers.*;

public enum PacketType {
    LOGOUT(0x14, LogoutParser.class),

    AUTOWALK(0x64, AutoWalkParser.class),
    MOVENORTH(0x65, MoveParser.class),             MOVEEAST(0x66, MoveParser.class),
    MOVESOUTH(0x67, MoveParser.class),             MOVEWEST(0x68, MoveParser.class),
    STOPWALK(0x69, StopWalkParser.class),
    MOVENORTHEAST(0x6A, MoveParser.class),         MOVESOUTHEAST(0x6B, MoveParser.class),
    MOVESOUTHWEST(0x6C, MoveParser.class),         MOVENORTHWEST(0x6D, MoveParser.class),

    TURNNORTH(0x6F, TurnParser.class),             TURNEAST(0x70, TurnParser.class),
    TURNSOUTH(0x71, TurnParser.class),             TURNWEST(0x72, TurnParser.class),

    MOVETHING(0x78, MoveThingParser.class),
    USE(0x82, UseParser.class),
    CLOSECONTAINER(0x87, CloseContainerParser.class),
    LOOKAT(0x8C, LookParser.class),

    SAY(0x96, SayParser.class),
    REQUESTCHANNELS(0x97, ChannelParser.class),    OPENCHANNEL(0x98, ChannelParser.class),
    CLOSECHANNEL(0x99, ChannelParser.class),     OPENPRIVATECHANNEL(0x9A, ChannelParser.class),

    CHANGESTANCE(0xA0, StanceParser.class),     ATTACK(0xA1, AttackParser.class),

    REQUESTOUTFITS(0xD2, OutfitParser.class),     CHANGEOUTFIT(0xD3, OutfitParser.class),

    ADDVIP(0xDC, VipParser.class),                 REMOVEVIP(0xDD, VipParser.class);

    private static final Map<Integer,PacketType> lookup = new HashMap<Integer,PacketType>();
    static {
        for(PacketType type : EnumSet.allOf(PacketType.class)) {
             lookup.put(type.opbyte, type);
        }
    }

    public static PacketType getType(int opbyte) {
        return lookup.get(opbyte);
    }

    private int opbyte;
    private Class<? extends MessageParser> parserClass;

    private PacketType(int opbyte, Class<? extends MessageParser> parserClass) {
        this.opbyte = opbyte;
        this.parserClass = parserClass;
    }

    public MessageParser getParser() {
        try {
            return parserClass.newInstance();
        } catch(Exception e) {
            throw new RuntimeException("Failed to instantiate message parser class.", e);
        }
    }

}
