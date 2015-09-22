package org.jotserver.ot.model.creature;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.InternalThing;
import org.jotserver.ot.model.Light;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.OutfitType;
import org.jotserver.ot.model.util.Direction;

public class InternalCreature extends InternalThing {

    private final Creature creature;

    private final long id;
    private final String name;

    private Direction direction;
    private Outfit outfit;

    private int health;
    private int maxHealth;

    private int speed;
    private Light light;

    public InternalCreature(Creature creature, long id, String name, Cylinder parent) {
        super(creature, parent);
        this.creature = creature;

        this.id = id;
        this.name = name;

        direction = Direction.NORTH;
        outfit = new Outfit(new OutfitType("", OutfitType.Type.FEMALE, 140, true));
        light = new Light(0, 0);

        health = 150;
        maxHealth = 150;
        speed = 900;
    }

    /*
     * Public action methods
     */

    public void turn(Direction direction) {
        switch(direction) {
        case NORTH:
        case EAST:
        case SOUTH:
        case WEST:
            this.direction = direction;
            break;
        default:
            throw new IllegalArgumentException("Creature can not face to the " + direction + ".");
        }
    }

    public void setOutfit(Outfit outfit) {
        this.outfit = outfit;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /*
     * Public retrieval methods.
     */

    public Creature getCreature() {
        return creature;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

    public Outfit getOutfit() {
        return outfit;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getSpeed() {
        return speed;
    }

    public Light getLight() {
        return light;
    }
}
