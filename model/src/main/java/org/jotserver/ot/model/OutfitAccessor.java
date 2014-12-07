package org.jotserver.ot.model;

import java.util.Collection;

public interface OutfitAccessor {
	public Collection<OutfitType> getOutfits();

	public OutfitType getOutfit(int look);
}
