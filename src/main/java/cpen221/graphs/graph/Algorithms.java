package cpen221.graphs.graph;

import java.util.*;

/**
 * Shared graph algorithms used by Graph implementations.
 * Supports both SUM_EDGES (standard shortest-path) and MAX_EDGE (minimax)path cost metrics.
 * This class is a static class
 */
public final class Algorithms {


    private Algorithms() {}

    /**
     * Compute the cost of a concrete path in a given graph.
     * Requirements:
     * g!= null
     * path!= null
     * pathsize >= 0
     * for each i with 0 <= i < pathsize -1:
     * g.getEdge(path.get(i), path.get(i+1)) != null
     * Effects: none (does not mutate g or path).
     * Returns:
     * if pathsize <= 1: 0.0
     * otherwise:
     * if type == SUM_EDGES:
     * sum of lengths of all edges along the path
     * if type == MAX_EDGE:
     * maximum edge length along the path.
     * Throws:
     * NullPointerException if g, path, type, or any vertex in path is null.
     * IllegalArgumentException if:
     * any consecutive vertices are not adjacent,
     * an encountered edge has negative or NaN length.
     */
    public static <V extends Vertex, E extends Edge<V>>
    double pathCost(Graph<V,E> g, List<V> path, PathCostType type) {

        if (g == null || path == null || type == null) {
            throw new NullPointerException("Arguments is null.");
        }
        if (path.size() <= 1) return 0.0;

        double cost = (type == PathCostType.MAX_EDGE) ? 0.0 : 0.0;

        for (int i = 0; i < path.size()-1; i++) {
            V a = path.get(i);
            V b = path.get(i+1);

            if (a == null || b == null) throw new NullPointerException("Path contains null vertex.");

            E e = g.getEdge(a, b);
            if (e == null) {
                throw new IllegalArgumentException("Path contains non-adjacent vertices: " + a + "," + b);
            }

            double len = e.length();
            if (len < 0 || Double.isNaN(len)) {
                throw new IllegalArgumentException("Invalid edge length: " + len);
            }

            if (type == PathCostType.SUM_EDGES) {
                cost += len;
            } else {
                cost = Math.max(cost, len);
            }
        }

        return cost;
    }

    //minimum cost
    private static final class NodeCost<V> {
        final V v; final double cost;
        NodeCost(V v, double c){ this.v = v; this.cost = c; }
    }

    /**
     * Relax the tentative distance to {@code v} through {@code u} under
     * the SUM_EDGES metric.
     * Requires:
     * u, v, e, dist, prev, pq are non-null.
     * dist contains entries for both u and v.
     * e connects u and v.
     * e.length() >= 0 and not NaN.
     * Effects:
     * Computes alt = dist(u) + e.length().
     * If alt < dist(v), updates dist(v), sets prev(v)=u, and enqueues v.
     * Returns:Nothing.
     * Throws:
     * NullPointerException if any argument is null.
     * IllegalArgumentException if dist lacks u or v, or if edge length is invalid.
     */

    private static <V extends Vertex, E extends Edge<V>>
    void relaxSum(V u, V v, E e,
                  Map<V, Double> dist, Map<V, V> prev,
                  PriorityQueue<NodeCost<V>> pq) {

        if (u == null || v == null || e == null || dist == null || prev == null || pq == null) {
            throw new NullPointerException("Arguments is null");
        }
        if (!dist.containsKey(u) || !dist.containsKey(v)) {
            throw new IllegalArgumentException("dist map missing u or v.");
        }
        if (e.length() < 0 || Double.isNaN(e.length())) {
            throw new IllegalArgumentException("Edge length must be >= 0 and not NaN.");
        }

        double alt = dist.get(u) + e.length();
        if (alt < dist.get(v)) {
            dist.put(v, alt);
            prev.put(v, u);
            pq.add(new NodeCost<>(v, alt));
        }
    }
    /**
     * Relax the tentative distance to {@code v} through {@code u} under
     * the MAX_EDGE metric.
     * Requires:
     * u, v, e, dist, prev, pq are non-null.
     * dist contains entries for both u and v.
     * e connects u and v.
     * e.length() >= 0 and not NaN.
     * Effects:
     * Computes alt = max(dist(u), e.length()).
     * If alt < dist(v), updates dist(v), sets prev(v)=u, and enqueues v.
     * Returns:Nothing.
     * Throws:
     * NullPointerException if any argument is null.
     * IllegalArgumentException if dist lacks u or v, or if edge length is invalid.
     */
    private static <V extends Vertex, E extends Edge<V>>
    void relaxMax(V u, V v, E e,
                  Map<V, Double> dist, Map<V, V> prev,
                  PriorityQueue<NodeCost<V>> pq) {

        double alt = Math.max(dist.get(u), e.length());
        if (alt < dist.get(v)) {
            dist.put(v, alt);
            prev.put(v, u);
            pq.add(new NodeCost<>(v, alt));
        }
    }

    /**
     * Relax the tentative distance to {@code v} through {@code u} under
     * the MAX_EDGE (bottleneck/minimax) metric.
     *
     * Requires:
     * u, v, e, dist, prev, pq are non-null.
     * dist contains entries for both u and v.
     * e connects u and v.
     * e.length() >= 0 and not NaN.
     * Effects:
     * Computes alt = max(dist(u), e.length()).
     * If alt < dist(v), updates dist(v), sets prev(v)=u, and enqueues v.
     * Returns:
     * Nothing.
     * Throws:
     * NullPointerException if any argument is null.
     * IllegalArgumentException if dist lacks u or v, or if edge length is invalid.
     */
    private static <V extends Vertex, E extends Edge<V>>
    void dijkstraLike(Graph<V,E> g, V source, PathCostType type,
                      Set<V> domain,
                      Map<V, Double> dist, Map<V, V> prev) {


        if (g == null || source == null || type == null ||
                domain == null || dist == null || prev == null) {
            throw new NullPointerException("Arguments must not be null.");
        }
        if (!domain.contains(source)) {
            throw new IllegalArgumentException("source must be in domain.");
        }
        final double INF = Double.POSITIVE_INFINITY;

        dist.clear();
        prev.clear();

        for (V v : domain) {
            if (v == null) throw new NullPointerException("domain contains null vertex.");
            if (!g.hasVertex(v)) {
                throw new IllegalArgumentException("domain contains vertex not in graph: " + v);
            }
            dist.put(v, INF);
        }
        dist.put(source, 0.0);

        PriorityQueue<NodeCost<V>> pq =
                new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));
        pq.add(new NodeCost<>(source, 0.0));

        Set<V> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            NodeCost<V> nc = pq.poll();
            V u = nc.v;

            if (!domain.contains(u)) continue; // extra safety
            if (!g.hasVertex(u)) {
                throw new IllegalStateException("Graph changed unexpectedly during search.");
            }

            if (visited.contains(u)) continue;
            visited.add(u);

            Map<V,E> nbrs = g.getNeighbours(u);
            if (nbrs == null) {
                throw new IllegalStateException("getNeighbours(u) returned null.");
            }

            for (Map.Entry<V,E> en : nbrs.entrySet()) {
                V v = en.getKey();
                E e = en.getValue();


                if (!domain.contains(v)) continue; // skip safely
                if (!g.hasEdge(u, v)) {
                    throw new IllegalStateException("Neighbour returned but edge missing: " + u + "," + v);
                }

                double len = e.length();


                if (!dist.containsKey(v)) {
                    throw new IllegalStateException("dist missing vertex " + v);
                }

                if (type == PathCostType.SUM_EDGES) {
                    relaxSum(u, v, e, dist, prev, pq);
                } else {
                    relaxMax(u, v, e, dist, prev, pq);
                }
            }
        }
    }

    /**
     * Compute a minimum-cost path from a given metric.
     * Uses a Dijkstra-like algorithm that supports both SUM_EDGES and MAX_EDGE metrics.
     * Requires:
     * g != null
     * source != null, sink != null
     * g.hasVertex(source) && g.hasVertex(sink)
     * Effects: none (does not mutate g).
     * Returns:
     * a list of vertices representing a minimum-cost path
     * under the given type, including both endpoints;
     * if no path exists, returns an empty list.
     * Throws:
     * NullPointerException if g, source, sink, or type is null.
     * IllegalArgumentException if source or sink is not a vertex of g.
     * IllegalStateException if:
     * dist(sink) is missing,
     * a path is reported as reachable but cannot be reconstructed.
     */
    public static <V extends Vertex, E extends Edge<V>>
    List<V> minimumCostPath(Graph<V,E> g, V source, V sink, PathCostType type) {
        if (source.equals(sink)) return java.util.List.of(source);
        Set<V> dom = g.getVertices();
        Map<V, Double> dis = new HashMap<>();
        Map<V, V> prev = new HashMap<>();
        dijkstraLike(g, source, type, dom, dis, prev);
        if (!dis.containsKey(sink) || dis.get(sink) == Double.POSITIVE_INFINITY)
            return java.util.List.of();
        LinkedList<V> path = new LinkedList<>();
        V cure = sink;
        while (cure != null && !cure.equals(source)) {
            path.addFirst(cure);
            cure = prev.get(cure);
        }
        if (cure == null) return java.util.List.of();
        path.addFirst(source);
        return path;
    }

    private static <V extends Vertex, E extends Edge<V>>
    Set<Set<V>> computeComponents(Graph<V,E> g) {
        Set<V> all = g.getVertices();
        Set<V> vis = new HashSet<>();
        Set<Set<V>> comps = new HashSet<>();
        for (V start : all) {
            if (vis.contains(start)) continue;
            Set<V> comp = new HashSet<>();
            Deque<V> dq = new ArrayDeque<>();
            dq.add(start); vis.add(start);
            while (!dq.isEmpty()) {
                V u = dq.removeFirst();
                comp.add(u);
                for (V nb : g.getNeighbours(u).keySet()) {
                    if (!vis.contains(nb)) {
                        vis.add(nb);
                        dq.addLast(nb);
                    }
                }
            }
            comps.add(comp);
        }
        return comps;
    }

    private static <V extends Vertex> Set<V> largestComponent(Set<Set<V>> comps) {
        Set<V> best = null; int bestSize = -1;
        for (Set<V> c : comps) if (c.size() > bestSize) { bestSize = c.size(); best = c; }
        return best == null ? java.util.Set.of() : best;
    }
    /**
     * Compute the diameter of a connected graph
     * Requires:
     * g != null
     * g has at least one vertex
     * each connected component is considered; if the graph is disconnected,
     * the definition may be restricted to the largest component
     * Effects: none.
     * Returns:
     * the diameter value under PathCostType type.
     */
    public static <V extends Vertex, E extends Edge<V>>
    double diameter(Graph<V,E> g, PathCostType type) {
        Set<Set<V>> com = computeComponents(g);
        Set<V> dom = largestComponent(com);
        if (dom.isEmpty()) return 0.0;
        double dia = 0.0;
        Map<V, Double> dist = new HashMap<>();
        Map<V, V> prev = new HashMap<>();
        for (V s : dom) {
            dijkstraLike(g, s, type, dom, dist, prev);
            double ecc = 0.0;
            for (V t : dom) {
                double d = dist.get(t);
                if (d != Double.POSITIVE_INFINITY && d > ecc) ecc = d;
            }
            if (ecc > dia) dia = ecc;
        }
        return dia;
    }
    /**
     * Compute a center of graph g under a given cost metric.
     * Requires:
     * g != null
     * g has at least one vertex.
     * Effects: none.
     * Returns:
     * a vertex c in g
     * If multiple centers exist, any one of them may be returned.
     */
    public static <V extends Vertex, E extends Edge<V>>
    V center(Graph<V,E> g, PathCostType type) {
        Set<Set<V>> com = computeComponents(g);
        Set<V> dom = largestComponent(com);
        if (dom.isEmpty()) return null;
        V best = null; double bestEcc = Double.POSITIVE_INFINITY;
        Map<V, Double> dist = new HashMap<>();
        Map<V, V> prev = new HashMap<>();
        for (V s : dom) {
            dijkstraLike(g, s, type, dom, dist, prev);
            double ecc = 0.0;
            for (V t : dom) {
                double d = dist.get(t);
                if (d != Double.POSITIVE_INFINITY && d > ecc) ecc = d;
            }
            if (ecc < bestEcc) { bestEcc = ecc; best = s; }
        }
        return best;
    }
}

