package app.Astar;

public class CityNodeWithDistA implements Comparable<CityNodeWithDistA> {
    int dist;
    int distAndHeuristic;
    CityNode node;



    CityNodeWithDistA(CityNode node, int dist, int heuristic){
        this.node = node;
        this.dist = dist;
        this.distAndHeuristic = dist+heuristic;
    }

    @Override
    public int compareTo(CityNodeWithDistA other) {
        return Integer.compare(this.distAndHeuristic, other.distAndHeuristic);
    }

}