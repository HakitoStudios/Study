package hakito.trycatch.Game.Logic;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hakito.trycatch.Game.Field;
import hakito.trycatch.Game.Game;
import hakito.trycatch.Game.Objects.BasicCell;
import hakito.trycatch.Game.Objects.Cell;
import hakito.trycatch.Game.Objects.Portal;

/**
 * Created by Oleg on 26-Dec-15.
 */
public class PathFinder {
    static class Node {
        enum Color {WHITE, GRAY, BLACK}

        Color color;
        Node parent;
        int x;
        int y;
        private boolean finish;

        public Node(int x, int y, int size) {
            this.x = x;
            this.y = y;
            color = Color.WHITE;
            finish = Helper.isFinish(x, y, size);
        }

        public boolean isFinish() {
            return finish;
        }
    }

    static void addNeighbour(int x, int y, List<Node> neigbours, Node[][] nodes) {
        if (Helper.isCorrectIndex(x, nodes.length) && Helper.isCorrectIndex(y, nodes.length)) {
            if (nodes[x][y] != null) {
                neigbours.add(nodes[x][y]);
            }
        }
    }

    static List<Node> getNeighbours(Node n, Node[][] nodes) {
        List<Node> neighbours = new ArrayList<>(4);
        BasicCell bc = Game.get().getField().getCells()[n.x][n.y];
        if (bc instanceof Portal) {
            Portal portal = (Portal) bc;
            if (((Portal) bc).getPortalType() == Portal.PortalType.IN) {
                Portal neig = portal.getNeighbour();
                addNeighbour(neig.getX(), neig.getY(), neighbours, nodes);
                return neighbours;
            }
        }
        addNeighbour(n.x + 1, n.y, neighbours, nodes);
        addNeighbour(n.x - 1, n.y, neighbours, nodes);
        addNeighbour(n.x, n.y + 1, neighbours, nodes);
        addNeighbour(n.x, n.y - 1, neighbours, nodes);

        return neighbours;
    }

    static Direction getDirection(Node s, Node e) {
        int dx = e.x - s.x, dy = e.y - s.y;
        if(dx==1)return Direction.RIGHT;
        if(dx==-1)return Direction.LEFT;
        if(dy==-1)return Direction.UP;
        if(dy==1)return Direction.DOWN;
        return null;
    }

    static Direction getDirection(Node n) {
        if (n == null || n.parent==null) return null;
        while (n.parent.parent != null) {
            n = n.parent;
        }
        return getDirection(n.parent, n);
    }

    public static Direction getPath() {
        Field f = Game.get().getField();
        BasicCell[][] cells = f.getCells();
        int size = Game.get().getLevel().getSize();
        Point cat = f.getToy().position();

        Node[][] nodes = new Node[size][size];
        //nodes  init
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                if (cells[x][y].isFree()) {
                    nodes[x][y] = new Node(x, y, size);
                }
            }


        LinkedList<Node> queue = new LinkedList<>();

        Node startNode = nodes[cat.x][cat.y];
        startNode.color = Node.Color.GRAY;
        queue.add(startNode);
        Node n = null;
        while (queue.size() > 0) {
            n = queue.remove();
            if (Helper.isFinish(n.x, n.y, size))
                break;

            for (Node q : getNeighbours(n, nodes)) {
                if (q.color == Node.Color.WHITE) {
                    q.parent = n;
                    queue.add(q);
                }
            }
            n.color = Node.Color.BLACK;
        }
        if(Helper.isFinish(n.x, n.y, size))
            return getDirection(n);
        return Direction.BLOCKED;
    }


}
