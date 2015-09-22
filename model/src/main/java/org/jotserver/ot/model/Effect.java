package org.jotserver.ot.model;

public enum Effect {
    DRAW_BLOOD(1),
    LOSE_ENERGY(2),
    PUFF(3),
    BLOCKHIT(4),
    EXPLOSION_AREA(5),
    EXPLOSION_DAMAGE(6),
    FIRE_AREA(7),
    YELLOW_RINGS(8),
    POISON_RINGS(9),
    HIT_AREA(10),
    TELEPORT(11),
    ENERGY_DAMAGE(12),
    MAGIC_ENERGY(13),
    MAGIC_BLOOD(14),
    MAGIC_POISON(15),
    HITBY_FIRE(16),
    POISON(17),
    MORT_AREA(18),
    SOUND_GREEN(19),
    SOUND_RED(20),
    POISON_AREA(21),
    SOUND_YELLOW(22),
    SOUND_PURPLE(23),
    SOUND_BLUE(24),
    SOUND_WHITE(25),
    BUBBLES(26),
    CRAPS(27),
    GIFT_WRAPS(28),
    FIREWORK_YELLOW(29),
    FIREWORK_RED(30),
    FIREWORK_BLUE(31),
    STUN(32),
    SLEEP(33),
    WATERCREATURE(34),
    GROUNDSHAKER(35),
    HEARTS(36),
    FIREATTACK(37),
    ENERGY_AREA(38),
    SMALLCLOUDS(39),
    HOLYDAMAGE(40),
    BIGCLOUDS(41),
    ICEAREA(42),
    ICETORNADO(43),
    ICEATTACK(44),
    STONES(45),
    SMALLPLANTS(46),
    CARNIPHILA(47),
    PURPLEENERGY(48),
    YELLOWENERGY(49),
    HOLYAREA(50),
    BIGPLANTS(51),
    CAKE(52),
    GIANTICE(53),
    WATERSPLASH(54),
    PLANTATTACK(55);

    private int type;

    private Effect(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
