package app;

import java.util.HashMap;
import java.util.Map;


public class CityNode {
    private String name;
    private Map<CityNode, Integer> neighbors;

    public CityNode(String name){
        this.name = name;
        this.neighbors = new HashMap<>();
    }

    public Map<CityNode, Integer> getNeighbors() {
        return neighbors;
    }

    public String getName() {
        return name;
    }

    public void addNeighbor(CityNode neighbor, int distanceWeight){
        neighbors.put(neighbor,distanceWeight);
    }
}
