package org.jotserver.ot.model.item;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.util.ItemLocation;

import java.lang.ref.WeakReference;
import java.util.*;

public class InternalContainer extends InternalItem {

    private List<Creature> spectators;
    private Container container;
    private LinkedList<Item> items;
    private LinkedList<WeakReference<ContainerLocation>> slotCache;

    public InternalContainer(Container container, Cylinder parent) {
        super(container, parent);
        this.container = container;
        spectators = new LinkedList<Creature>();
        items = new LinkedList<Item>();

        slotCache = new LinkedList<WeakReference<ContainerLocation>>();
    }

    public Container getContainer() {
        return container;
    }

    public void open(Creature spectator) {
        if(spectator == null) {
            throw new IllegalArgumentException("Null can not open container.");
        } else if(spectators.contains(spectator)) {
            throw new IllegalArgumentException("Creature is already a spectator.");
        } else {
            spectators.add(spectator);
        }
    }

    public void close(Creature spectator) {
        if(spectator == null) {
            throw new IllegalArgumentException("Null cannot close a container.");
        } else {
            boolean found = spectators.remove(spectator);
            if(!found) {
                throw new IllegalArgumentException("This creature has not opened the container.");
            }
        }
    }

    public List<Creature> getSpectators() {
        return new ArrayList<Creature>(spectators);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isSpectator(Creature spectator) {
        return spectators.contains(spectator);
    }

    public void addItem(Item item) {
        if(item == null) {
            throw new IllegalArgumentException("Can not add null items to container.");
        }
        items.addFirst(item);
        item.setParent(getContainer());

        ListIterator<WeakReference<ContainerLocation>> it = slotCache.listIterator();
        while(it.hasNext()) {
            WeakReference<ContainerLocation> ref = it.next();
            if(ref.get() == null) {
                it.remove();
            } else {
                ref.get().increment();
            }
        }
    }

    public void removeItem(int slot) {
        if(slot >= items.size()) {
            throw new IllegalArgumentException("Cannot remove item from that slot.");
        }
        Item item = items.remove(slot);
        item.setParent(null);

        ListIterator<WeakReference<ContainerLocation>> it = slotCache.listIterator();
        while(it.hasNext()) {
            WeakReference<ContainerLocation> ref = it.next();
            if(ref.get() == null) {
                it.remove();
            } else {
                ContainerLocation loc = ref.get();
                if(loc.getIndex() >= slot) {
                    ref.get().decrement();
                }
            }
        }
    }

    public int getSlot(Item item) {
        return items.indexOf(item);
    }

    public Item getItem(int slot) {
        if(slot >= items.size()) {
            return null;
        } else {
            return items.get(slot);
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public ItemLocation getLocationOf(Item item) {
        int index = getSlot(item);
        if(index != -1) {
            return new ContainerLocation(index);
        } else {
            return null;
        }
    }

    public ItemLocation getSlotLocation(int slot) {
        return new ContainerLocation(slot);
    }

    private class ContainerLocation implements ItemLocation/*, Comparable<ContainerLocation>*/ {
        private int index;

        public ContainerLocation(int index) {
            this.index = index;
            slotCache.add(new WeakReference<ContainerLocation>(this));
        }

        public Item get() {
            if(index < 0) {
                return null;
            } else {
                return getItem(index);
            }
        }

        public int getIndex() {
            if(index < 0) {
                return -1;
            } else {
                return index;
            }
        }

        public void increment() {
            index++;
        }

        public void decrement() {
            index--;
        }

        public Container getCylinder() {
            return getContainer();
        }
    }
}
