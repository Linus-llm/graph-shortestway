package app;
/*
Lav en implementering af Dijkstras algoritme, der finder den korteste vej mellem byer i et vejnet.

    Design selv dit vejnet med mindst 8 byer og vægtede rettede veje
    Find den korteste vej fra én by til en anden
    Lav en visualisering der giver mening. Hvad er nyttigt at se når man skal forstå algoritmen?

Du kan finde inspiration til koden i graphs.dijkstra.
 */

import java.util.*;

/*
1. Byg nyder
2. Sæt naboer samt vægte
3. Kald findShortestPath metode med start og mål noder
4. Ny class der holder på den samlede dist og ikke kun en enkelt kant
5. I den nye klasse lav @Override på compareTo og sørg for at den kun gælder på dist
6. findShortestPath metoden tager source og destination
7. Map dist, map prev, set visited, PrioQueue kø, Sæt while loop op
8. Visualiser
 */
public class Dijkstra {
    public static void main(String[] args) {
        CityNode Hillerød = new CityNode("Hillerød");
        CityNode Helsinge = new CityNode("Helsinge");
        CityNode Helsingør = new CityNode("Helsingør");
        CityNode København = new CityNode("København");
        CityNode Lyngby = new CityNode("Lyngby");
        CityNode Søborg = new CityNode("Søborg");
        CityNode Brøndby = new CityNode("Brøndby");
        CityNode Allerød = new CityNode("Allerød");
        CityNode Birkerød = new CityNode("Birkerød");

        Hillerød.addNeighbor(Helsinge,10);
        Hillerød.addNeighbor(Allerød,12);
        Hillerød.addNeighbor(Helsingør, 9);
        Helsinge.addNeighbor(Helsingør,10);
        Helsinge.addNeighbor(Søborg, 10);
        Allerød.addNeighbor(Birkerød, 7);
        Allerød.addNeighbor(Lyngby, 20);
        Birkerød.addNeighbor(Lyngby,10);
        Birkerød.addNeighbor(Søborg,15);
        Lyngby.addNeighbor(København,15);
        Lyngby.addNeighbor(Søborg,10);
        Søborg.addNeighbor(København,4);
        Søborg.addNeighbor(Brøndby, 13);

        findShortestPath(Hillerød, København);
    }

    public static void findShortestPath(CityNode startNode, CityNode goalNode){
    Map<CityNode, Integer> dist = new HashMap<>();
    Map<CityNode, CityNode> prev = new HashMap<>();
    Set<CityNode> visited = new HashSet<>();
    PriorityQueue<CityNodeWithDist> queue = new PriorityQueue<>();

    CityNodeWithDist start = new CityNodeWithDist(startNode,0);
    queue.add(start);
    dist.put(startNode,0);

    // //ai for visualize
    DijkstraTrace trace = new DijkstraTrace(startNode, goalNode);
    //ai for visualize
    trace.record("start", "Startbyen lægges i kø med 0 km.", dist, prev, visited, queue, null, null);
    boolean found = false;

        while(!queue.isEmpty()){

            CityNodeWithDist currentNode = queue.poll();
            //ai for visualize
            trace.record("poll", currentNode.node.getName() + " tages ud af køen med " + currentNode.dist + " km.", dist, prev, visited, queue, currentNode.node, null);

            if(currentNode.node.equals(goalNode)){
                found = true;
                break;
            }
            if(visited.contains(currentNode.node)){
                //ai for visualize
                trace.record("skip", "Byen er allerede visited. Køposten springes over.", dist, prev, visited, queue, currentNode.node, null);
                continue;
            }
            visited.add(currentNode.node);
            //ai for visualize
            trace.record("visited", currentNode.node.getName() + " lægges i visited og markeres mørkegrå.", dist, prev, visited, queue, currentNode.node, null);
            for (Map.Entry<CityNode,Integer> entry:currentNode.node.getNeighbors().entrySet()){

                CityNode nextCityNode = entry.getKey();
                int nextCityWeight = entry.getValue();

                //ai for visualize
                trace.record("neighbor", "Undersøg vejen til " + nextCityNode.getName() + " på " + nextCityWeight + " km.", dist, prev, visited, queue, currentNode.node, nextCityNode);

                if(visited.contains(nextCityNode)){
                    //ai for visualize
                    trace.record("skip", "Naboen er allerede visited. Vejen springes over.", dist, prev, visited, queue, currentNode.node, nextCityNode);
                    continue;
                }

                int newDist = currentNode.dist + nextCityWeight;


                if(newDist < dist.getOrDefault(nextCityNode, Integer.MAX_VALUE)){

                    dist.put(nextCityNode, newDist);
                    prev.put(nextCityNode, currentNode.node);
                    queue.add(new CityNodeWithDist(nextCityNode,newDist));
                    //ai for visualize
                    trace.record("enqueue", "Kortere afstand: " + currentNode.dist + " + " + nextCityWeight + " = " + newDist + " km. Opdatér dist og prev, og læg en ny post i køen.", dist, prev, visited, queue, currentNode.node, nextCityNode);

                } else {
                    //ai for visualize
                    trace.record("keep", "Kandidaten på " + newDist + " km forbedrer ikke " + dist.get(nextCityNode) + " km. Behold dist og prev.", dist, prev, visited, queue, currentNode.node, nextCityNode);
                }

            }
        }
        //ai for visualize
        trace.finish(found, dist, prev, visited, queue);
    }
}
