package org.jotserver.ot.model;

public enum AnimatedTextColor {
    BLUE(5),
    LIGHTBLUE(35),
    LIGHTGREEN(30),
    PURPLE(83),
    LIGHTGREY(129),
    DARKRED(144),
    RED(180),
    ORANGE(198),
    YELLOW(210),
    WHITE(215);

    private int id;

    private AnimatedTextColor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
