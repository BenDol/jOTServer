package org.jotserver.ot.model.util;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Thing;

public interface Location {
	public Cylinder getCylinder();
	public Thing get();
	public int getIndex();
}
