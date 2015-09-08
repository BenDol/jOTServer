package org.jotserver.ot.model.util;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Thing;

public interface Location {
	Cylinder getCylinder();
	Thing get();
	int getIndex();
}
