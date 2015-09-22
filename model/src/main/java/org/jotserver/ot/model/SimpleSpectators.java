package org.jotserver.ot.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Spectators;

public class SimpleSpectators<T extends Creature> extends LinkedList<T> implements Spectators<T> {

    private static final long serialVersionUID = 1L;

    public SimpleSpectators() {
        super();
    }

    public SimpleSpectators(Collection<? extends T> spectators) {
        super(spectators);
    }

    public SimpleSpectators(T spectator) {
        super();
        add(spectator);
    }

    public SimpleSpectators(Class<T> type, Iterable<?> spectators) {
        super();
        for(Object object : spectators) {
            if(type.isInstance(object)) {
                add(type.cast(object));
            }
        }
    }

    public static <T extends Creature> Spectators<T> getEmpty(Class<T> type) {
        return new Spectators<T>() {

            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    public boolean hasNext() {
                        return false;
                    }
                    public T next() {
                        return null;
                    }
                    public void remove() {}
                };
            }
        };
    }

}