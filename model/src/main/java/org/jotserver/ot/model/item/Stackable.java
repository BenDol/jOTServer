package org.jotserver.ot.model.item;

import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.action.Tester;
import org.jotserver.ot.model.action.Visitor;

public class Stackable extends Item {

    protected Stackable(ItemType type, int count) {
        super(type);
        setInternal(new InternalStackable(this, count, null));
    }

    protected InternalStackable getInternal() {
        return (InternalStackable)super.getInternal();
    }

    public boolean test(Tester action) {
        return action.test(this);
    }

    public void execute(Visitor visitor) {
        visitor.execute(this);
    }

    public int getCount() {
        return getInternal().getCount();
    }

    /*
     * Action methods.
     */

    public ErrorType queryAddCount(int count) {
        if(count < 0) {
            return ErrorType.NOTPOSSIBLE;
        } else if(count+getCount() > 100) {
            return ErrorType.NOTENOUGHROOM;
        } else {
            return ErrorType.NONE;
        }
    }

    public void executeAddCount(int count) {
        getInternal().setCount(getCount()+count);
    }

    public ErrorType queryAdd(Stackable stackable) {
        if(getId() != stackable.getId()) {
            return ErrorType.NOTPOSSIBLE;
        } else {
            return queryAddCount(stackable.getCount());
        }
    }

    public void executeAdd(Stackable stackable) {
        executeAddCount(stackable.getCount());
    }

    public ErrorType queryRemoveCount(int count) {
        if(count < 0 || getCount()-count < 1) {
            return ErrorType.NOTPOSSIBLE;
        } else {
            return ErrorType.NONE;
        }
    }

    public void executeRemoveCount(int count) {
        getInternal().setCount(getCount()-count);
    }

    public Stackable clone(int count) {
        return new Stackable(type, count);
    }


    public String getDescription() {
        if(getCount() <= 1) {
            return super.getDescription();
        } else {
            return getCount() + " " + type.getPluralName();
        }
    }

}
