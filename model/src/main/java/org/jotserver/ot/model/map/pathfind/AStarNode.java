package org.jotserver.ot.model.map.pathfind;

public class AStarNode implements Comparable<AStarNode> {
	protected int x, y;
	protected AStarNode parent;
	protected int cost, estimated;
	protected int depth;
	public int total;
	
	public AStarNode(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int hashCode() {
		return Integer.valueOf(x).hashCode() + Integer.valueOf(y).hashCode();
	}
	
	public boolean equals(Object object) {
		if(object instanceof AStarNode) {
			AStarNode node = (AStarNode)object;
			return x == node.x && y == node.y;
		} else {
			return false;
		}
	}
	
	public int setParent(AStarNode node) {
		depth = node.depth+1;
		parent = node;
		return depth;
	}
	
	
	public int compareTo(AStarNode node) {
		return Integer.valueOf(total).compareTo(node.total);
	}
	
}
