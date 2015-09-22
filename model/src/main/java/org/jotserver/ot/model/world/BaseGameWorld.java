package org.jotserver.ot.model.world;

import java.net.InetSocketAddress;

public class BaseGameWorld implements GameWorld {

    private String name;
    private InetSocketAddress address;
    private boolean local;
    private String identifier;

    private BaseGameWorld() {
        name = null;
        address = null;
        identifier = null;
        local = false;
    }

    public BaseGameWorld(String identifier, String name) {
        this();
        this.identifier = identifier;
        this.name = name;
    }

    public BaseGameWorld(String identifier, String name, InetSocketAddress address, boolean local) {
        this(identifier, name);
        this.address = address;
        this.local = local;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

}
