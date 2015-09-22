package org.jotserver.ot.model.player;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.creature.InternalCreature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class InternalPlayer extends InternalCreature {

    private final Player player;
    private Skills skills;

    private int level;
    private long experience;

    private int capacity;
    private int mana;
    private int maxMana;
    private int magicLevel;
    private int soul;

    private List<Player> vipList;

    public InternalPlayer(Player player, long id, String name, Cylinder parent) {
        super(player, id, name, parent);

        this.player = player;

        vipList = new ArrayList<Player>();

        skills = new Skills();
        level = 1;
        experience = 0;
        mana = 0;
        maxMana = 0;
        magicLevel = 0;
        soul = 0;
    }

    /*
     * Public action methods.
     */

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public void setMagicLevel(int magicLevel) {
        this.magicLevel = magicLevel;
    }

    public void setSoul(int soul) {
        this.soul = soul;
    }

    public void removeVip(Player player) {
        Iterator<Player> it = vipList.iterator();
        while(it.hasNext()) {
            if(it.next().getGlobalId() == player.getGlobalId()) {
                it.remove();
            }
        }
    }

    public void addVip(Player player) {
        vipList.add(player);
    }

    /*
     * Public retrieval methods.
     */
    public List<Player> getVipList() {
        return Collections.unmodifiableList(vipList);
    }

    public boolean isVip(String name) {
        for(Player player : vipList) {
            if(player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVip(int globalId) {
        for(Player player : vipList) {
            if(player.getGlobalId() == globalId) {
                return true;
            }
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public Skills getSkills() {
        return skills;
    }


    public int getLevel() {
        return level;
    }


    public long getExperience() {
        return experience;
    }


    public int getCapacity() {
        return capacity;
    }


    public int getMana() {
        return mana;
    }


    public int getMaxMana() {
        return maxMana;
    }


    public int getMagicLevel() {
        return magicLevel;
    }


    public int getSoul() {
        return soul;
    }

}
