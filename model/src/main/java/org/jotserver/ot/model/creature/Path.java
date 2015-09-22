package org.jotserver.ot.model.creature;

import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;

public interface Path {
    Path EMPTY = new EmptyPath();

    Direction getNextStep();
    Position getCurrentPosition();
    boolean isEmpty();
}
