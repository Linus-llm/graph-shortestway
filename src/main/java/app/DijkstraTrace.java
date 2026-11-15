package app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Takes immutable JSON snapshots without changing the algorithm's collections. */
final class DijkstraTrace {
    private final List<CityNode> cities = new ArrayList<>();
    private final List<String> frames = new ArrayList<>();
    private final CityNode source;
    private final CityNode target;

    DijkstraTrace(CityNode source, CityNode target) {
        this.source = source;
        this.target = target;
        // Discover the actual directed graph, including a possibly unreachable goal.
        cities.add(source);
        if (target != source) cities.add(target);
        for (int i = 0; i < cities.size(); i++) {
            for (CityNode neighbor : cities.get(i).getNeighbors().keySet()) {
                if (!cities.contains(neighbor)) cities.add(neighbor);
            }
        }
    }

    void record(String event, String message, Map<CityNode, Integer> dist,
                Map<CityNode, CityNode> prev, Set<CityNode> visited,
                PriorityQueue<CityNodeWithDist> queue, CityNode current, CityNode neighbor) {
        snapshot(event, message, dist, prev, visited, queue, current, neighbor, false, List.of());
    }

    private void snapshot(String event, String message, Map<CityNode, Integer> dist,
                          Map<CityNode, CityNode> prev, Set<CityNode> visited,
                          PriorityQueue<CityNodeWithDist> queue, CityNode current,
                          CityNode neighbor, boolean done, List<CityNode> path) {
        List<Integer> distances = new ArrayList<>();
        List<Integer> predecessors = new ArrayList<>();
        List<Integer> seen = new ArrayList<>();
        for (CityNode city : cities) {
            distances.add(dist.get(city));
            predecessors.add(prev.containsKey(city) ? cities.indexOf(prev.get(city)) : null);
            if (visited.contains(city)) seen.add(cities.indexOf(city));
        }
        // Copy the heap and poll the COPY: PriorityQueue iteration is not priority order.
        PriorityQueue<CityNodeWithDist> copy = new PriorityQueue<>(queue);
        List<String> pending = new ArrayList<>();
        while (!copy.isEmpty()) {
            CityNodeWithDist entry = copy.poll();
            pending.add("{\"node\":" + cities.indexOf(entry.node) + ",\"dist\":" + entry.dist + "}");
        }
        frames.add("{\"event\":" + quote(event) + ",\"message\":" + quote(message)
                + ",\"dist\":" + distances + ",\"prev\":" + predecessors + ",\"visited\":" + seen
                + ",\"queue\":[" + String.join(",", pending) + "],\"current\":" + id(current)
                + ",\"neighbor\":" + id(neighbor) + ",\"done\":" + done
                + ",\"path\":" + path.stream().map(cities::indexOf).toList() + "}");
    }

    void finish(boolean found, Map<CityNode, Integer> dist, Map<CityNode, CityNode> prev,
                Set<CityNode> visited, PriorityQueue<CityNodeWithDist> queue) {
        List<CityNode> path = new ArrayList<>();
        if (found) {
            // The green route comes directly from the method's prev map.
            Set<CityNode> guard = new HashSet<>();
            for (CityNode node = target; node != null; node = prev.get(node)) {
                if (!guard.add(node)) throw new IllegalStateException("Cycle in prev");
                path.add(node);
                if (node == source) break;
            }
            Collections.reverse(path);
            if (path.get(0) != source) throw new IllegalStateException("prev does not reach start");
        }
        snapshot(found ? "found" : "unreachable", found
                ? "Målet er taget ud af køen. Den grønne rute følger metodens prev tilbage til start."
                : "Køen er tom. Målet kan ikke nås.", dist, prev, visited, queue,
                found ? target : null, null, true, path);
        List<String> edges = new ArrayList<>();
        for (CityNode city : cities) {
            for (var edge : city.getNeighbors().entrySet()) {
                edges.add("[" + cities.indexOf(city) + "," + cities.indexOf(edge.getKey()) + "," + edge.getValue() + "]");
            }
        }
        String json = "{\"version\":1,\"source\":" + id(source) + ",\"target\":" + id(target)
                + ",\"cities\":[" + String.join(",", cities.stream().map(c -> quote(c.getName())).toList())
                + "],\"roads\":[" + String.join(",", edges) + "],\"frames\":[" + String.join(",\n", frames) + "]}";
        Path output = Path.of("dijkstra-trace.json").toAbsolutePath();
        try {
            Files.writeString(output, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write trace to " + output, e);
        }
        System.out.println("Dijkstra trace: " + output);
        System.out.println("Open dijkstra-visualisering.html and load this JSON file.");
    }

    private String id(CityNode city) {
        return city == null ? "null" : Integer.toString(cities.indexOf(city));
    }

    private static String quote(String value) {
        StringBuilder out = new StringBuilder("\"");
        for (char c : value.toCharArray()) {
            if (c == '"' || c == '\\') out.append('\\').append(c);
            else if (c < 32) out.append(String.format("\\u%04x", (int) c));
            else out.append(c);
        }
        return out.append('"').toString();
    }
}
