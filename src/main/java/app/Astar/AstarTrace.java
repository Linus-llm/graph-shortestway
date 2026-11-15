package app.Astar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.ToIntFunction;

//ai-visualization
/** Records the actual Astar method; never modifies its collections or chooses a route. */
final class AstarTrace {
    private final List<CityNode> nodes = new ArrayList<>();
    private final List<String> frames = new ArrayList<>();
    private final CityNode source, target;
    private final int[][] grid;
    private final ToIntFunction<CityNode> heuristic;

    AstarTrace(CityNode source, CityNode target, int[][] grid, ToIntFunction<CityNode> heuristic) {
        this.source = source;
        this.target = target;
        this.grid = grid;
        this.heuristic = heuristic;
        nodes.add(source);
        if (target != source) nodes.add(target);
        for (int i = 0; i < nodes.size(); i++) {
            for (CityNode neighbor : nodes.get(i).getNeighbourghNodes().keySet()) {
                if (!nodes.contains(neighbor)) nodes.add(neighbor);
            }
        }
    }

    void record(String event, String message, Map<CityNode, Integer> dist,
                Map<CityNode, CityNode> prev, Set<CityNode> visited,
                PriorityQueue<CityNodeWithDistA> queue, CityNodeWithDistA current, CityNode neighbor) {
        snapshot(event, message, dist, prev, visited, queue, current, neighbor, false, List.of());
    }

    private String entry(CityNodeWithDistA value) {
        if (value == null) return "null";
        // Read g and f directly from the actual queue entry, including old duplicates.
        return "{\"node\":" + nodes.indexOf(value.node) + ",\"g\":" + value.dist
                + ",\"h\":" + (value.distAndHeuristic - value.dist) + ",\"f\":" + value.distAndHeuristic + "}";
    }

    private void snapshot(String event, String message, Map<CityNode, Integer> dist,
                          Map<CityNode, CityNode> prev, Set<CityNode> visited,
                          PriorityQueue<CityNodeWithDistA> queue, CityNodeWithDistA current,
                          CityNode neighbor, boolean done, List<CityNode> path) {
        List<Integer> distances = new ArrayList<>(), predecessors = new ArrayList<>(), seen = new ArrayList<>();
        for (CityNode node : nodes) {
            distances.add(dist.get(node));
            predecessors.add(prev.containsKey(node) ? nodes.indexOf(prev.get(node)) : null);
            if (visited.contains(node)) seen.add(nodes.indexOf(node));
        }
        // Poll a copy, since iterating PriorityQueue does not give priority order.
        PriorityQueue<CityNodeWithDistA> copy = new PriorityQueue<>(queue);
        List<String> pending = new ArrayList<>();
        while (!copy.isEmpty()) pending.add(entry(copy.poll()));
        frames.add("{\"event\":" + quote(event) + ",\"message\":" + quote(message)
                + ",\"dist\":" + distances + ",\"prev\":" + predecessors + ",\"visited\":" + seen
                + ",\"queue\":[" + String.join(",", pending) + "],\"selected\":" + entry(current)
                + ",\"neighbor\":" + (neighbor == null ? "null" : nodes.indexOf(neighbor))
                + ",\"done\":" + done + ",\"path\":" + path.stream().map(nodes::indexOf).toList() + "}");
    }

    void finish(boolean found, Map<CityNode, Integer> dist, Map<CityNode, CityNode> prev,
                Set<CityNode> visited, PriorityQueue<CityNodeWithDistA> queue) {
        List<CityNode> path = new ArrayList<>();
        if (found) {
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
                ? "Goal polled. Reconstruct the green route from the method's prev map."
                : "Queue exhausted. No route to the goal.", dist, prev, visited, queue, null, null, true, path);
        List<String> metadata = new ArrayList<>(), edges = new ArrayList<>();
        for (CityNode node : nodes) {
            metadata.add("{\"row\":" + node.getRow() + ",\"col\":" + node.getCol()
                    + ",\"h\":" + heuristic.applyAsInt(node) + "}");
            for (var edge : node.getNeighbourghNodes().entrySet()) {
                edges.add("[" + nodes.indexOf(node) + "," + nodes.indexOf(edge.getKey()) + "," + edge.getValue() + "]");
            }
        }
        String json = "{\"version\":1,\"algorithm\":\"astar\",\"source\":" + nodes.indexOf(source)
                + ",\"target\":" + nodes.indexOf(target) + ",\"grid\":" + Arrays.deepToString(grid)
                + ",\"nodes\":[" + String.join(",", metadata) + "],\"roads\":[" + String.join(",", edges)
                + "],\"frames\":[" + String.join(",\n", frames) + "]}";
        Path output = Path.of("astar-trace.json").toAbsolutePath();
        try {
            Files.writeString(output, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write trace to " + output, e);
        }
        System.out.println("A* trace: " + output);
        System.out.println("Open astar-visualisering.html and choose this JSON file.");
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
