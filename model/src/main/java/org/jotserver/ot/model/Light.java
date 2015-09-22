package org.jotserver.ot.model;

public class Light {
    private int level;
    private int color;

    public Light(int level, int color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public int getColor() {
        return color;
    }
}