package app.Astar;

import app.CityNodeWithDist;

import java.util.*;

public class Astar {

    static int[][] demoGrid = {
            {0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0},
            {0,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1,0,1,0,1,0,0,0,1,0},
            {0,1,0,0,0,1,0,1,0,1,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0},
            {0,1,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0},
            {0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,0,0},
            {0,0,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0},
            {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0},
    };
    static final int ROWS = 13, COLS = 17;

    static List<String> names = List.of(
            "København", "Aarhus", "Odense", "Aalborg", "Esbjerg",
            "Randers", "Kolding", "Horsens", "Vejle", "Roskilde",
            "Herning", "Silkeborg", "Hørsholm", "Helsingør", "Næstved",
            "Viborg", "Fredericia", "Køge", "Holstebro", "Taastrup",
            "Slagelse", "Hillerød", "Sønderborg", "Svendborg", "Holbæk",
            "Hjørring", "Frederikshavn", "Nørresundby", "Ringsted", "Haderslev",
            "Skive", "Birkerød", "Farum", "Skanderborg", "Nyborg",
            "Kalundborg", "Frederikssund", "Ikast", "Aabenraa", "Middelfart",
            "Grenaa", "Nakskov", "Rønne", "Varde", "Thisted",
            "Vordingborg", "Brønderslev", "Espergærde", "Struer", "Hobro",
            "Solrød Strand", "Frederiksværk", "Lillerød", "Vejen", "Tønder",
            "Jyllinge", "Haslev", "Humlebæk", "Helsinge", "Støvring",
            "Sæby", "Maribo", "Skjern", "Ribe", "Odder",
            "Rødekro", "Sorø", "Nykøbing Falster", "Nykøbing Mors", "Nykøbing Sjælland",
            "Lemvig", "Ringkøbing", "Hedensted", "Brande", "Billund",
            "Grindsted", "Give", "Jelling", "Børkop", "Egtved",
            "Tarm", "Videbæk", "Aulum", "Vildbjerg", "Ulfborg",
            "Harboøre", "Thyborøn", "Hurup", "Hanstholm", "Fjerritslev",
            "Brovst", "Aars", "Farsø", "Løgstør", "Nibe",
            "Hadsund", "Arden", "Mariager", "Aalestrup", "Hammel",
            "Hadsten", "Hinnerup", "Hornslet", "Rønde", "Ebeltoft",
            "Kolind", "Ryomgård", "Auning", "Allingåbro", "Langå",
            "Bjerringbro", "Kjellerup", "Them", "Ry", "Galten",
            "Hørning", "Stilling", "Låsby", "Brædstrup", "Tørring",
            "Juelsminde", "Heden", "Glamsbjerg", "Assens", "Tommerup",
            "Aarup", "Bogense", "Otterup", "Søndersø", "Kerteminde",
            "Munkebo", "Langeskov", "Ringe", "Faaborg", "Rudkøbing",
            "Ærøskøbing", "Marstal", "Sakskøbing", "Nysted", "Rødby",
            "Holeby", "Bandholm", "Stubbekøbing", "Gedser", "Nørre Alslev",
            "Præstø", "Stege", "Faxe", "Faxe Ladeplads", "Rønnede",
            "Karise", "Store Heddinge", "Hårlev", "Borup", "Ejby",
            "Hvalsø", "Lejre", "Jyderup", "Mørkøv", "Tølløse",
            "Svinninge", "Asnæs", "Vig", "Højby", "Rørvig",
            "Nørre Asmindrup", "Gørlev", "Høng", "Svebølle", "Dianalund",
            "Stenlille", "Munke Bjergby", "Glumsø", "Fuglebjerg", "Herlufmagle",
            "Tappernøje", "Lundby", "Mern", "Kalvehave", "Nyråd",
            "Ørslev", "Næstelsø", "Karrebæksminde", "Fensmark", "Holme-Olstrup",
            "Mogenstrup", "Toksværd", "Lynge", "Slangerup", "Ølstykke",
            "Stenløse", "Ganløse", "Veksø", "Jægerspris", "Skibby",
            "Kulhuse", "Hundested", "Liseleje", "Tisvildeleje", "Vejby",
            "Græsted", "Gilleleje", "Dronningmølle", "Hornbæk", "Ålsgårde",
            "Snekkersten", "Kvistgård", "Fredensborg", "Nivå", "Kokkedal",
            "Vedbæk", "Holte", "Virum", "Søborg", "Brøndby",
            "Glostrup", "Albertslund", "Ballerup", "Måløv", "Smørumnedre",
            "Dragør"
    );

    static int nameIndex = 0;


    public static void main(String[] args) {
        CityNode[][] cityNodes = new CityNode[ROWS][COLS];

        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                if(demoGrid[r][c]==0){
                    cityNodes[r][c] = new CityNode(r, c, names.get(nameIndex++));
                }
            }
        }

        //directions
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        //loop through rows and columns then check for null. If not null then we take the directions and try it on the neighbourghs and if valid then we add it to our cityNodes grid
        for (int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                if(cityNodes[r][c] == null){
                    continue;
                }
                for(int[] d: directions){
                    int nr = r + d[0];
                    int nc = c + d[1];
                    if(nr>=0 && nr < ROWS && nc>=0 && nc < COLS && cityNodes[nr][nc] != null){
                        cityNodes[r][c].addNeighbourgh(cityNodes[nr][nc], 1);
                    }
                }
            }
        }
        //start node
        CityNode source = cityNodes[0][0];
        //destination node
        CityNode destination = cityNodes[12][16];
        //activate method
        findShortestPathAstar(source,destination);


    }

    private static void findShortestPathAstar(CityNode startNode, CityNode destinationNode){
        Map<CityNode, Integer> dist = new HashMap<>();
        Map<CityNode, CityNode> prev = new HashMap<>();
        Set<CityNode> visited = new HashSet<>();
        PriorityQueue<CityNodeWithDistA> queue = new PriorityQueue<>();

        CityNodeWithDistA start = new CityNodeWithDistA(startNode,0, heuristic(startNode, destinationNode));
        queue.add(start);
        dist.put(startNode,0);

        //ai-visualization
        AstarTrace trace = new AstarTrace(startNode, destinationNode, demoGrid, node -> heuristic(node, destinationNode));
        //ai-visualization
        trace.record("start", "Add the start node to the priority queue.", dist, prev, visited, queue, null, null);
        //ai-visualization
        boolean found = false;

        while(!queue.isEmpty()){
            CityNodeWithDistA currentNode = queue.poll();
            //ai-visualization
            trace.record("poll", "Poll the queue: choose the lowest f = g + h, including the heuristic.", dist, prev, visited, queue, currentNode, null);

            if(currentNode.node.equals(destinationNode)){
                //ai-visualization
                found = true;
                break;
            }
            if(visited.contains(currentNode.node)){
                //ai-visualization
                trace.record("skip", "Already visited: skip this old queue entry.", dist, prev, visited, queue, currentNode, null);
                continue;
            }
            visited.add(currentNode.node);
            //ai-visualization
            trace.record("visited", "Add the current node to visited (dark grey).", dist, prev, visited, queue, currentNode, null);

            for (Map.Entry<CityNode,Integer> entry:currentNode.node.getNeighbourghNodes().entrySet()){

                CityNode nextCityNode = entry.getKey();
                int nextCityWeight = entry.getValue();
                //ai-visualization
                trace.record("neighbor", "Inspect an outgoing neighbor with edge weight " + nextCityWeight + ".", dist, prev, visited, queue, currentNode, nextCityNode);



                if(visited.contains(nextCityNode)){
                    //ai-visualization
                    trace.record("skip", "The neighbor is already visited. Skip it.", dist, prev, visited, queue, currentNode, nextCityNode);
                    continue;
                }

                int newDist = currentNode.dist + nextCityWeight; //is +1 neeeded here?

                if(newDist < dist.getOrDefault(nextCityNode, Integer.MAX_VALUE)){

                    dist.put(nextCityNode, newDist);
                    prev.put(nextCityNode, currentNode.node);
                    queue.add(new CityNodeWithDistA(nextCityNode,newDist,heuristic(nextCityNode,destinationNode)));
                    //ai-visualization
                    trace.record("enqueue", "A lower g: update dist and prev, then enqueue with f = g + h.", dist, prev, visited, queue, currentNode, nextCityNode);
                }
                //ai-visualization
                else { trace.record("keep", "No lower g: keep the existing dist and prev.", dist, prev, visited, queue, currentNode, nextCityNode); }

            }
        }
        //ai-visualization
        trace.finish(found, dist, prev, visited, queue);
    }

    private static int heuristic(CityNode node, CityNode destination) {
        return Math.abs(destination.getRow() - node.getRow())
                + Math.abs(destination.getCol() - node.getCol());

    }
}
