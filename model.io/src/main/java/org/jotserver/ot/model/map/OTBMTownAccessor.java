package org.jotserver.ot.model.map;

import org.jotserver.io.BinaryNode;
import org.jotserver.net.CDataInputStream;
import org.jotserver.ot.model.util.Position;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class OTBMTownAccessor implements TownAccessor {
	
	private HashMap<Integer, Town> townMap;
	
	public OTBMTownAccessor() {
		townMap = new HashMap<Integer, Town>();
	}
	
	protected void load(BinaryNode topTownNode) throws IOException {
		for(BinaryNode townNode : topTownNode.getChildren()) {
			OTBMNodeType nodeType = OTBMNodeType.get(townNode.getType());
			if(nodeType == OTBMNodeType.TOWN) {
				CDataInputStream in = new CDataInputStream(townNode.getDataStream());
				int id = (int)in.readU32();
				Town town = getTown(id);
				if(town == null) {
					town = new Town(id);
					townMap.put(id, town);
				}
				String name = in.readString();
				town.setName(name);
				
				int x = in.readU16();
				int y = in.readU16();
				int z = in.readByte();
				town.setPosition(new Position(x, y, z));
			} else {
				throw new IOException("Unknown node type " + townNode.getType() + ".");
			}
		}
	}
	
	public Town getTown(int id) {
		return townMap.get(id);
	}

	public Town getTown(String name) {
		for(Town t : getTowns()) {
			if(t.getName().equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}

	public Collection<Town> getTowns() {
		return Collections.unmodifiableCollection(townMap.values());
	}
}
