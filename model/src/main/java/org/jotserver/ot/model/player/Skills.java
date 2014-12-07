package org.jotserver.ot.model.player;

import java.util.EnumMap;

public class Skills {
	
	private EnumMap<Type, Skill> skills;
	
	public Skills() {
		skills = new EnumMap<Type, Skill>(Type.class);
		for(Type type : Type.values()) {
			skills.put(type, new Skill());
		}
	}
	
	public Skill getSkill(Type type) {
		return skills.get(type);
	}
	
	public void setSkill(Type type, Skill skill) {
		skills.put(type, skill);
	}
	
	public void addTries(Type type, int tries) {
		getSkill(type).addTries(tries);
	}
	
	
	public static enum Type {
		FIST, CLUB, SWORD, AXE, DISTANCE, SHIELD, FISH
	}
	
	public static class Skill {
		private int level;
		private int tries;
		private int percent;
		
		public Skill() {
			level = 10;
			tries = 0;
			percent = 0;
		}
		
		public Skill(int level) {
			this();
			this.level = level;
		}
		
		public Skill(int level, int tries) {
			this(level);
			this.tries = tries;
		}
		
		protected void addTries(int tries) {
			this.tries += tries;
		}
		
		public int getLevel() {
			return level;
		}
		protected void setLevel(int level) {
			this.level = level;
		}
		public int getTries() {
			return tries;
		}
		protected void setTries(int tries) {
			this.tries = tries;
		}
		public int getPercent() {
			return percent;
		}
		protected void setPercent(int percent) {
			this.percent = percent;
		}
	}
}
