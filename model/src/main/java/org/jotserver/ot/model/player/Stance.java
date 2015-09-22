package org.jotserver.ot.model.player;

public enum Stance {
    OFFENSIVE(1.0f, 1.0f), BALANCED(0.8f, 1.2f), DEFENSIVE(0.5f, 2.0f);

    private float defensive;
    private float offensive;

    private Stance(float offensive, float defensive) {
        this.offensive = offensive;
        this.defensive = defensive;
    }

    public float getOffensiveMultiplier() {
        return offensive;
    }

    public float getDefensiveMultiplier() {
        return defensive;
    }
}
