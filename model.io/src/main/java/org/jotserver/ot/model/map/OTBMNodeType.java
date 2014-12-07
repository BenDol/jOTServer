/**
 * 
 */
package org.jotserver.ot.model.map;

enum OTBMNodeType {
	NONE,
	ROOTV1, MAP_DATA, ITEM_DEF, TILE_AREA, TILE, ITEM, TILE_SQUARE, 
	TILE_REF, SPAWNS, SPAWN_AREA, MONSTER, TOWNS, TOWN, HOUSETILE;
	
	public static OTBMNodeType get(int i) {
		return values()[i];
	}
}