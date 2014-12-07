package org.jotserver.ot.model.map;

import org.apache.log4j.Logger;
import org.jotserver.io.BinaryNode;
import org.jotserver.net.CData;
import org.jotserver.ot.model.item.*;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.LocalGameWorld;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.jotserver.ot.model.item.ItemAttribute.STACKABLE;

public class OTBMMap extends Map {
	private static Logger logger = Logger.getLogger(OTBMMap.class);
	
	private static enum Attribute {
		FIRST,
		DESCRIPTION, EXT_FILE, TILE_FLAGS, ACTION_ID, UNIQUE_ID,
		TEXT, DESC, TELE_DEST, ITEM, DEPOT_ID, EXT_SPAWN_FILE,
		RUNE_CHARGES, EXT_HOUSE_FILE, HOUSEDOORID
	};
	
	private HashMap<Position, Tile> tiles;
	
	private int width;
	private int height;
	
	private OTBMTownAccessor townAccessor;
	
	public OTBMMap(LocalGameWorld world) {
		super(world);
		tiles = new HashMap<Position, Tile>();
		townAccessor = new OTBMTownAccessor();
	}

	public Tile getTile(Position position) {
		return tiles.get(position);
	}
	
	private void setTile(Position position, Tile tile) {
		tiles.put(position, tile);
	}
	
	public void loadFromOTBM(String file, ItemTypeAccessor itemTypes) throws IOException {
		parse(BinaryNode.load(file), itemTypes);
	}
	
	public void loadFromStream(InputStream stream, ItemTypeAccessor itemTypes) throws IOException {
		parse(BinaryNode.load(stream), itemTypes);
	}
	
	public void parse(BinaryNode root, ItemTypeAccessor itemTypes) throws IOException {
		InputStream in = root.getDataStream();
		
		long version = CData.readU32(in);
		width = CData.readU16(in);
		height = CData.readU16(in);
		/*long majorVersionItems = */CData.readU32(in);
		/*long minorVersionItems = */CData.readU32(in);
		
		if(version != 0) {
			throw new IOException("Invalid header version.");
		}
		
		logger.info("Map size: " + width + "x" + height);
		
		BinaryNode map = root.getFirstChild();
		
		if(map.getType() != OTBMNodeType.MAP_DATA.ordinal()) {
			throw new IOException("Unexcpected node type.");
		}

		in = map.getDataStream();
		
		while(in.available() > 0) {
			int attribute = CData.readByte(in);
			if(attribute == Attribute.DESCRIPTION.ordinal()) {
				String mapDescription = CData.readString(in);
				logger.info("Map description: " + mapDescription);
			} else if(attribute == Attribute.EXT_SPAWN_FILE.ordinal()) {
				/*String spawnFile = */CData.readString(in);
			} else if(attribute == Attribute.EXT_HOUSE_FILE.ordinal()) {
				/*String houseFile = */CData.readString(in);
			} else {
				throw new IOException("Unknown node type.");
			}
		}
		
		Tile tile = null;
		
		for(BinaryNode mapData : map.getChildren()) {
			in = mapData.getDataStream();
			
			if(mapData.getType() == OTBMNodeType.TILE_AREA.ordinal()) {
				
				int baseX =  CData.readU16(in);
				int baseY =  CData.readU16(in);
				int baseZ =  CData.readByte(in);
				
				for(BinaryNode tileNode : mapData.getChildren()) {
					in = tileNode.getDataStream();
					if(tileNode.getType() == OTBMNodeType.TILE.ordinal() || tileNode.getType() == OTBMNodeType.HOUSETILE.ordinal()) {

						int px = baseX + CData.readByte(in);
						int py = baseY + CData.readByte(in);
						int pz = baseZ;

						//boolean isHouseTile = false;
						
						Position pos = new Position(px, py, pz);
						if(tileNode.getType() == OTBMNodeType.TILE.ordinal()) {
							tile = new Tile(this, pos);
							setTile(pos, tile);
						} else if(tileNode.getType() == OTBMNodeType.HOUSETILE.ordinal()) {
							/*long houseId = */CData.readU32(in);
							tile = new Tile(this, pos);
							//isHouseTile = true;
						}
						
						//read tile attributes
						while(in.available() > 0) {
							int attribute = CData.readByte(in);
							if(attribute == Attribute.TILE_FLAGS.ordinal()) {
								/*long flags = */CData.readU32(in);
								/*if((flags & TILESTATE_PROTECTIONZONE) == TILESTATE_PROTECTIONZONE) {
									tile->setFlag(TILESTATE_PROTECTIONZONE);
								} else if((flags & TILESTATE_NOPVPZONE) == TILESTATE_NOPVPZONE){
									tile->setFlag(TILESTATE_NOPVPZONE);
								} else if((flags & TILESTATE_PVPZONE) == TILESTATE_PVPZONE){
									tile->setFlag(TILESTATE_PVPZONE);
								}
								
								if((flags & TILESTATE_NOLOGOUT) == TILESTATE_NOLOGOUT){
									tile->setFlag(TILESTATE_NOLOGOUT);
								}*/
							} else if(attribute == Attribute.ITEM.ordinal()) {
								Item item = parseItem(in, itemTypes);
								if(item == null){
									throw new IOException("Failed to load item.");
								}
								
								/*if(isHouseTile && !item.isNotMoveable()) {
									logger.warn("Moveable item in house. Item type = " + item.getId());
									item = null;
								} else */{
									tile.executeAddItem(item);
								}
							} else {
								throw new IOException("Unknown attribute type!");
							}
						}
						
						for(BinaryNode itemNode : tileNode.getChildren()) {
							InputStream iin = itemNode.getDataStream();
							if(itemNode.getType() == OTBMNodeType.ITEM.ordinal()) {
								
								Item item = parseItem(iin, itemTypes);
								if(item == null){
									throw new IOException("Failed to create item.");
								}
								
								unserializeItem(item, itemNode, iin);
								/*if(isHouseTile && !item.isNotMoveable()) {
									logger.warn("Moveable item in house. Item type = " + item.getId());
								} else */{
									tile.executeAddItem(item);
								}
							} else {
								throw new IOException("Unknown node type.");
							}
							
						}

						setTile(new Position(px, py, pz), tile);
					} else {
						throw new IOException("Unknown node type.");
					}
				}
			} else if(mapData.getType() == OTBMNodeType.TOWNS.ordinal()) {
				townAccessor.load(mapData);
			} else{
				throw new IOException("Unknown node type.");
			}
		}
	}

	private void unserializeItem(Item item, BinaryNode itemNode, InputStream iin) {
		// TODO: Implement item unserialization
	}

	private Item parseItem(InputStream in, ItemTypeAccessor itemTypes) throws IOException {
		int id = CData.readU16(in);
		
		ItemType type = itemTypes.getItemType(id);
		if(type != null) {
			Item item = itemTypes.createItem(type);
			if(type.hasAttribute(STACKABLE)) {
				int count = CData.readByte(in);
				((Stackable)item).executeAddCount(count-1);
			} else if(type.isSplash() || type.isFluidContainer()) {
				int fluidType = CData.readByte(in);
				item.setFluidType(FluidType.get(fluidType));
			}
			return item;
		} else {
			return null;
		}
	}

	public TownAccessor getTownAccessor() {
		return townAccessor;
	}
}
