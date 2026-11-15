package app;

public class CityNodeWithDist implements Comparable<CityNodeWithDist> {
    int dist;
    CityNode node;

    CityNodeWithDist(CityNode node, int dist){
        this.node = node;
        this.dist = dist;
    }

    @Override
    public int compareTo(CityNodeWithDist other) {
        return Integer.compare(this.dist, other.dist);
    }

}
