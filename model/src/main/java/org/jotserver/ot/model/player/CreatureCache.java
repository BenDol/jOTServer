package org.jotserver.ot.model.player;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.jotserver.ot.model.creature.Creature;

/**
 * The CreatureCache class represents the client's cache of known creatures, and is 
 * used to determine which creatures needs their full client description sent and which
 * ones does not. 
 * @author jiddo
 */
public class CreatureCache {

    private int maxSize;
    private LinkedList<Creature> cache;
    private Creature owner;

    /**
     * Constructs a new creature cache with the specified maximum size. If
     * the cache reaches the specified size then cached creatures will be
     * removed before adding new ones.
     * @param owner
     *                 The intended owner of this cache.
     * @param maxSize
     *                 The maximum size of this cache.
     */
    public CreatureCache(Creature owner, int maxSize) {
        this.owner = owner;
        this.maxSize = maxSize;
        cache = new LinkedList<Creature>();
    }

    /**
     * Checks if the given creature is cached in this CreatureCache.
     * @param creature
     *             The creature to be looked up in the cache.
     * @return
     *             True if the creature is in the cache, false otherwise.
     */
    public boolean isCached(Creature creature) {
        return cache.contains(creature);
    }

    /**
     * Adds the specified creature to this creature cache unless it is already
     * cached. An old cached creature will be removed from the cache before adding
     * the new one if it is full.
     *
     *  When automatically removing creatures from this cache, only creatures which
     *  have been removed from the game, or which can no longer be seen by the owner
     *  will be removed.
     * @param creature
     *             The creature to add to the cache.
     * @return
     *             - Null if the creature was already in the cache.
     *             - The added creature if there was a free space in the cache.
     *             - The removed creature if the cache was full and some creature had to
     *                 be removed.
     * @throws IllegalStateException
     *             If the cache has reached its maximum size and no existing creature is
     *             viable for removal.
     */
    public Creature addCreature(Creature creature) {
        if(cache.contains(creature)) {
            return null;
        } else if(cache.size() < maxSize) {
            cache.add(creature);
            return creature;
        } else {
            Creature removed = remove();
            if(removed == null) {
                throw new IllegalStateException("Creature cache overflow!");
            } else {
                cache.add(creature);
                return removed;
            }
        }
    }

    /**
     * Attempts to remove a cached creature which does not need to be cached anymore.
     * Creatures which fall under this condition are those removed from the game, or
     * those that are outside the viewing range of the owner.
     * @return
     *             The removed creature, if one was found, null otherwise.
     */
    private Creature remove() {
        Iterator<Creature> it = cache.iterator();
        Creature removed = null;
        while(it.hasNext()) {
            Creature c = it.next();
            if(!c.isPlaced() || !owner.canSee(c.getPosition())) {
                it.remove();
                removed = c;
                break;
            }
        }
        return removed;
    }

    /**
     * Provides a collection of all the creatures cached in this cache.
     * @return
     *             A collection of cached creatures.
     */
    public Collection<Creature> getCreatures() {
        return Collections.unmodifiableCollection(cache);
    }

    /**
     * Returns the owner registered to this cache.
     * @return
     *         The owner of this cache.
     */
    public Creature getOwner() {
        return owner;
    }

}
