package org.jotserver.ot.model.map.pathfind;

public interface AStarHeuristic {
    public int estimateDistance(int fromX, int fromY, int toX, int toY);
}
