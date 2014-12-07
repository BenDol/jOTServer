package org.jotserver.ot.model.item;

import java.util.HashMap;

public enum FluidType {
	NONE(0, Color.EMPTY, "nothing"),
	WATER(1, Color.BLUE, "water"),
	BLOOD(2, Color.RED, "blood"),
	BEER(3, Color.BROWN, "beer"),
	SLIME(4, Color.GREEN, "slime"),
	LEMONADE(5, Color.YELLOW, "lemonade"),
	MILK(6, Color.WHITE, "milk"),
	MANAFLUID(7, Color.PURPLE, "mana fluid"),
	WATER2(9, Color.BLUE, "water"),
	LIFEFLUID(10, Color.RED, "life fluid"),
	OIL(11, Color.BROWN, "oil"),
	SLIME2(12, Color.GREEN, "slime"),
	URINE(13, Color.YELLOW, "urine"),
	COCONUT_MILK(14, Color.WHITE, "coconut milk"),
	WINE(15, Color.PURPLE, "wine"),
	MUD(19, Color.BROWN, "mud"),
	FRUIT_JUICE(21, Color.YELLOW, "fruit juice"),
	LAVA(26, Color.RED, "lava"),
	RUM(27, Color.BROWN, "rum"),
	SWAMP(28, Color.GREEN, "swamp");
	
	public static enum Color {
		EMPTY(0, 0),
		BLUE(1, 1),
		RED(2, 5),
		BROWN(3, 3),
		GREEN(4, 6),
		YELLOW(5, 8),
		WHITE(6, 9),
		PURPLE(7, 2);
		
		private int id;
		private int clientId;

		private Color(int id, int clientId) {
			this.id = id;
			this.clientId = clientId;
		}
		
		public int getId() {
			return id;
		}
		
		public int getClientId() {
			return clientId;
		}
	}
	
	private static HashMap<Integer, FluidType> lookup = new HashMap<Integer, FluidType>();
	static {
		for(FluidType type : values()) {
			lookup.put(type.getId(), type);
		}
	}
	
	public static FluidType get(int id) {
		return lookup.get(id);
	}

	private int id;
	private Color color;
	private String description;

	private FluidType(int id, Color color, String description) {
		this.id = id;
		this.color = color;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public Color getColor() {
		return color;
	}

	public String getDescription() {
		return description;
	}
}
