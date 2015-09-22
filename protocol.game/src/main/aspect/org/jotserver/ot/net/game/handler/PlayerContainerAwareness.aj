package org.jotserver.ot.net.game.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.ContainerAware;
import org.jotserver.ot.model.item.InternalContainer;
import org.jotserver.ot.model.player.Player;

public aspect PlayerContainerAwareness {

    public pointcut registerContainer(ContainerAware spectator, InternalContainer container) :
        target(container) &&
        args(spectator) &&
        execution(public void InternalContainer.open(Creature));

    public pointcut unregisterContainer(ContainerAware spectator, InternalContainer container) :
        target(container) &&
        args(spectator) &&
        execution(public void InternalContainer.close(Creature));

    public pointcut changeParent(Thing thing) :
        target(thing) &&
        execution(public void Thing.onChangeParent());

    before(ContainerAware spectator, InternalContainer container) : registerContainer(spectator, container) {
        spectator.registerContainer(container.getContainer());
    }

    after(ContainerAware spectator, InternalContainer container) : unregisterContainer(spectator, container) {
        spectator.unregisterContainer(container.getContainer());
    }

    after(Thing thing) : changeParent(thing) {
        if(thing instanceof Container) {
            Container container = (Container)thing;
            for(Creature creature : container.getContentsSpectators(Creature.class)) {
                if(creature.isPlaced() && container.isPlaced() &&
                        !container.canBeViewedBy(creature)) {
                    container.close(creature);
                }
            }
        } else if(thing instanceof ContainerAware) {
            ContainerAware containerAware = (ContainerAware)thing;
            if(containerAware instanceof Creature) {
                Creature creature = (Creature)containerAware;
                for(Container container : containerAware.getContainers()) {
                    if(creature.isPlaced() && container.isPlaced() &&
                            !container.canBeViewedBy(creature)) {
                        container.close(creature);
                    }
                }
            }
        }
    }

    declare parents: Player implements ContainerAware;

    private Map<Integer, Container> Player.containers = new TreeMap<Integer, Container>();
    private int Player.nextContainerId = 0;

    public void Player.registerContainer(Container container) {
        containers.put(getNextContainerId(), container);
        nextContainerId = -1;
    }

    public void Player.unregisterContainer(Container container) {
        containers.values().remove(container);
    }

    public boolean Player.isContainerRegistred(Container container) {
        return containers.containsValue(container);
    }

    public int Player.getNextContainerId() {
        if(nextContainerId == -1) {
            nextContainerId = findNextContainerId();
        }
        return nextContainerId;
    }

    public void Player.suggestNextContainerId(int id) {
        if(!containers.containsKey(id)) {
            nextContainerId = id;
        }
    }

    public int Player.getContainerId(Container container) {
        for(Entry<Integer, Container> entry : containers.entrySet()) {
            if(entry.getValue().equals(container)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public Container Player.getContainer(int id) {
        return containers.get(id);
    }

    public Collection<Container> Player.getContainers() {
        return new ArrayList<Container>(containers.values());
    }

    private int Player.findNextContainerId() {
        for(int i = 0; i < 255; i++) {
            if(!containers.containsKey(i)) {
                return i;
            }
        }
        return -1;
    }

}
