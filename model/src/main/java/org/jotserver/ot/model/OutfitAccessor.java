package org.jotserver.ot.model;

import java.util.Collection;

public interface OutfitAccessor {
	Collection<OutfitType> getOutfits();

	OutfitType getOutfit(int look);
}
