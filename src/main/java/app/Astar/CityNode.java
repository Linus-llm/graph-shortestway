package app.Astar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityNode {
   private int row;
   private int col;
   private String name;
   private Map<CityNode, Integer> neighbourghNodes;

    public CityNode(int row, int col, String name) {
        this.row = row;
        this.col = col;
        this.name = name;
        neighbourghNodes = new HashMap<>();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Map<CityNode, Integer> getNeighbourghNodes() {
        return neighbourghNodes;
    }

    public void addNeighbourgh(CityNode neighbourgh, int distanceWeight){
        neighbourghNodes.put(neighbourgh, distanceWeight);
    }
}
