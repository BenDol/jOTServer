package org.jotserver.ot.model;

public class Outfit {

    private final OutfitType type;
    private final int head;
    private final int body;
    private final int legs;
    private final int feet;
    private final int addons;

    public Outfit(OutfitType type) {
        this.type = type;
        head = 40;
        body = 40;
        legs = 40;
        feet = 40;
        addons = 0;
    }

    public Outfit(OutfitType type, int head, int body, int legs, int feet, int addons) {
        this.type = type;
        this.head = head;
        this.body = body;
        this.legs = legs;
        this.feet = feet;
        this.addons = addons;
    }

    public OutfitType getType() {
        return type;
    }

    public int getHead() {
        return head;
    }

    public int getBody() {
        return body;
    }

    public int getLegs() {
        return legs;
    }

    public int getFeet() {
        return feet;
    }

    public int getAddons() {
        return addons;
    }

}
