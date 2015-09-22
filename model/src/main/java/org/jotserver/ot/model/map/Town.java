package org.jotserver.ot.model.map;

import org.jotserver.ot.model.util.Position;

public class Town {
    private int id;
    private String name;
    private Position position;

    public Town() {
        id = 0;
        name = "Unnamed town";
        position = new Position();
    }

    public Town(int id) {
        this();
        this.id = id;
    }

    public Town(int id, String name) {
        this(id);
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
}
