package org.jotserver.ot.model.map;

import java.util.Iterator;

import org.jotserver.ot.model.creature.Creature;

public interface Spectators<T extends Creature> extends Iterable<T> {
	Iterator<T> iterator();
}