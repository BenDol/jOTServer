package org.jotserver.ot.model.creature;

import org.jotserver.ot.model.Cylinder;
import org.junit.Ignore;

@Ignore
public class TestableCreature extends Creature {

    public TestableCreature() {
    }

    public TestableCreature(long id, String name) {
        setInternal(new InternalCreature(this, id, name, null));
    }

    public TestableCreature(long id, String name, Cylinder parent) {
        setInternal(new InternalCreature(this, id, name, parent));
    }

    @Override
    public String getDescription() {
        return null;
    }

    public void setInternal(InternalCreature internal) {
        super.setInternal(internal);
    }

}
