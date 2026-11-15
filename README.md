# Dijkstra and A* Visualization

Java implementations of Dijkstra and A*, with HTML visualizations that replay recordings of their actual execution.

- **Dijkstra** prioritizes the lowest accumulated distance.
- **A*** prioritizes **f = g + h**, where g is the accumulated distance and h is the Manhattan estimate to the goal.
- Both reconstruct the found route using the Java method’s `prev` map.

## How to visualize

Requires JDK 21 and a web browser.

| Algorithm | Run in IntelliJ | Open in your browser | Select recording |
|---|---|---|---|
| Dijkstra | `app.Dijkstra.main` | `dijkstra-visualisering.html` | `dijkstra-trace.json` |
| A* | `app.Astar.Astar.main` | `astar-visualisering.html` | `astar-trace.json` |

1. Run the algorithm’s `main` method to generate its JSON recording. The console prints the file’s location.
2. Open the corresponding HTML file and use its file picker to load the JSON.
3. Step through the execution or use automatic playback. A* also provides **Next choice** to jump to the next queue selection.

## Colors and costs

- **Dark grey:** visited nodes.
- **Yellow:** candidates in the priority queue.
- **Green:** the found shortest route.

Dijkstra displays edge distances and total kilometers. A* displays grid movement costs and each queue entry’s **g + h = f**, making the heuristic’s effect visible.

After changing the Java code, run `main` again and load the updated JSON. The HTML pages replay recorded Java state; they do not run a separate search algorithm.
