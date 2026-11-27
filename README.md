# Graph Operations and Applications

A Java coursework project exploring weighted graphs and their applications. It contains adjacency-list and adjacency-matrix graph implementations, shared graph algorithms, and starter code for Boggle, sea-level analysis, and document similarity.

## Current implementation

The graph package provides:

- Vertices identified by integer IDs and undirected weighted edges.
- A mutable adjacency-list graph and an adjacency-matrix graph constructed from sets of vertices and edges. The matrix graph rejects mutation operations.
- Operations for querying vertices, edges, neighbours, and connected components.
- Path costs, minimum-cost paths, graph diameter, and graph center using `SUM_EDGES` or `MAX_EDGE` cost types.

The application packages include Swing interfaces and supporting data, but their core algorithms are **not complete**:

- `BogglePlayer.getAllValidWords` currently returns an empty set.
- `SeaLevels.isSubmerged` returns an all-false grid, and `dangerLevel` returns `null`.
- `Similarity.getJensenShannonDivergence` returns `-1`, and `groupSimilarDocuments` returns `null`.

The interfaces can be explored as starter code, but these application results should not be treated as completed calculations.

## Requirements

- A JDK supporting Java 16 or later; the source uses pattern matching for `instanceof`.
- A compatible Gradle installation. This repository does not include a Gradle wrapper.
- Network access to Maven Central when dependencies are first downloaded.
- A graphical desktop for the Swing applications.

## Build and tests

Run the following from the project root:

```sh
gradle build
```

To run the JUnit 5 tests separately:

```sh
gradle test
```

Gradle writes its HTML test report to `build/reports/tests/test/index.html`. The application methods listed above remain placeholders regardless of the test result.

## Using the graph library

```java
import cpen221.graphs.graph.AdjacencyListGraph;
import cpen221.graphs.graph.Edge;
import cpen221.graphs.graph.PathCostType;
import cpen221.graphs.graph.Vertex;

Vertex a = new Vertex(1);
Vertex b = new Vertex(2);
AdjacencyListGraph<Vertex, Edge<Vertex>> graph = new AdjacencyListGraph<>();
graph.addVertex(a);
graph.addVertex(b);
graph.addEdge(new Edge<>(a, b, 2.5));

var path = graph.minimumCostPath(a, b, PathCostType.SUM_EDGES);
double cost = graph.pathCost(path, PathCostType.SUM_EDGES);
```

`SUM_EDGES` uses the sum of edge lengths along a path; `MAX_EDGE` uses its largest edge length. See `Graph.java` and the implementation classes for method contracts and behavior on disconnected graphs.

## Exploring the applications

Open the project as a Gradle project in a Java IDE. Run either main class from the IDE with the **project root as the working directory**, so relative paths to the supplied data can be resolved:

- `cpen221.graphs.applications.boggle.BoggleGUI` — Boggle interface. It accepts zero arguments for a 4 × 4 board, one integer for a square board, or two integers for rows and columns.
- `cpen221.graphs.applications.sealevels.SeaLevelsGUI` — terrain and flooding visualizer, using files in `data/terrains/`.

The Gradle build applies the Java plugin but does not define an application run task or a default main class. The Boggle solver and sea-level calculations are placeholders as described above. Document similarity is exposed through Java classes rather than a supplied GUI.

## Repository layout

```text
src/main/java/cpen221/graphs/
├── graph/                      Graph types and algorithms
├── applications/
│   ├── boggle/                 Boggle board, player, and GUI
│   ├── sealevels/              Terrain loading, analysis, and visualizer
│   └── textsimilarity/         Documents and similarity operations
└── utils/                      Input and utility classes
src/test/java/cpen221/graphs/   JUnit tests
data/boggle/                    Dictionaries and boards
data/terrains/                  Terrain datasets and attribution
data/marvel/                    Graph dataset
data/tests/                     Additional test data
build.gradle                    Build and dependencies
```

See the [project description](https://ubc-ece.craft.me/rATkzlhgvx5elK), [terrain attribution](data/terrains/ATTRIBUTION.txt) and [reflection](reflection.pdf) for supporting material.
