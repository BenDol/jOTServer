package org.jotserver.ot.model.map.pathfind;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import org.jotserver.ot.model.action.AddCreatureAction;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.DirectionPath;
import org.jotserver.ot.model.creature.Path;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Interval2D;
import org.jotserver.ot.model.util.Position;

public class AStarPathFinder {

    private static final int COST_DIAGONAL = 25;
    private static final int COST_NORMAL = 10;
    private static final int MAXSEARCHDEPTH = 30;

    private HashSet<AStarNode> closed;
    private PriorityQueue<AStarNode> open;
    private Map<AStarNode, AStarNode> nodes;

    private AStarHeuristic estimator = new AStarHeuristic() {

            public int estimateDistance(int fromX, int fromY, int toX, int toY) {
                int deltaY = Math.abs(fromY - toY);
                int deltaX = Math.abs(fromX - toX);
                int h_diagonal = Math.min(deltaX, deltaY);
                int h_straight = (deltaX + deltaY);
                return COST_DIAGONAL * h_diagonal + COST_NORMAL * (h_straight - 2 * h_diagonal);
            }
        };
    private org.jotserver.ot.model.map.Map map;

    public AStarPathFinder(org.jotserver.ot.model.map.Map map) {
        this.map = map;
        closed = new HashSet<AStarNode>();
        open = new PriorityQueue<AStarNode>();
        nodes = new HashMap<AStarNode, AStarNode>();
    }

    public Path findPath(Creature creature, Position from, Interval2D to) {

        closed.clear();
        open.clear();
        nodes.clear();

        AStarNode current = getNode(from.getX(), from.getY());
        current.cost = 0;
        current.estimated = estimator.estimateDistance(from.getX(), from.getY(),
                to.getClosestX(from.getX()),
                to.getClosestY(from.getY()));

        current.total = current.estimated;

        AStarNode end = null;

        open.add(current);

        int maxDepth = 0;

        while(!open.isEmpty() && maxDepth < MAXSEARCHDEPTH) {
            current = open.peek();
            if(to.contains(current.x, current.y)) {
                end = current;
                break;
            }

            open.poll();
            closed.add(current);

            int[][] surrounding = {
                {-1, 0},
                {0, 1},
                {1, 0},
                {0, -1},

                //diagonal
                {-1, -1},
                {1, -1},
                {1, 1},
                {-1, 1},
            };

            for(int[] pair : surrounding) {
                int dx = pair[0];
                int dy = pair[1];

                int x = current.x+dx;
                int y = current.y+dy;


                AStarNode next = getNode(x, y);
                if(closed.contains(next)) {
                    continue;
                }

                Tile tile = map.getTile(new Position(x, y, from.getZ()));
                if(tile == null || !tile.test(new AddCreatureAction(creature, true))) {
                    continue;
                }

                int newCost = current.cost + getCost(dx, dy);
                boolean contains = open.contains(next);
                if(!contains || newCost < next.cost) {
                    if(contains) {
                        open.remove(next);
                    }
                    maxDepth = Math.max(maxDepth, next.setParent(current));
                    next.cost = newCost;
                    next.estimated = estimator.estimateDistance(x, y, to.getClosestX(x), to.getClosestY(y));
                    next.total = next.cost + next.estimated;
                    open.add(next);
                }
            }
        }

        if(end == null || end.parent == null) {
            return null;
        } else {
            DirectionPath path = new DirectionPath(from);
            current = end;
            while(current.parent != null) {
                path.addStepFirst(getDirection(current));
                current = current.parent;
            }
            return path;
        }
    }

    private int getCost(int dx, int dy) {
        if(dx != 0 && dy != 0) {
            return COST_DIAGONAL;
        } else {
            return COST_NORMAL;
        }
    }

    private Direction getDirection(AStarNode node) {
        int x1 = node.parent.x;
        int y1 = node.parent.y;
        int x2 = node.x;
        int y2 = node.y;

        Direction ret = Direction.NONE;
        if(x2 > x1) {
            ret = ret.combine(Direction.EAST);
        } else if(x2 < x1) {
            ret = ret.combine(Direction.WEST);
        }

        if(y2 > y1) {
            ret = ret.combine(Direction.SOUTH);
        } else if(y2 < y1) {
            ret = ret.combine(Direction.NORTH);
        }
        return ret;
    }

    private AStarNode getNode(int x, int y) {
        AStarNode ret = new AStarNode(x, y);
        AStarNode temp = nodes.get(ret);
        if(temp == null) {
            nodes.put(ret, ret);
            return ret;
        } else {
            return temp;
        }

    }

}