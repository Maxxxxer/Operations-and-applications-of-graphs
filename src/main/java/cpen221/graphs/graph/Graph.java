package cpen221.graphs.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Graph<V extends Vertex, E extends Edge<V>> {

    //task 1

    /** Attempt to add {@code v} */
    boolean addVertex(V v);

    /** Return {@code true} if and only if the graph already contains {@code v} */
    boolean hasVertex(V v);

    /**
     * Attempt to add {@code e}. Return {@code false} if an edge between
     * the same two endpoints already exists or if either endpoint is not
     * in the graph.
     */
    boolean addEdge(E e);

    /** Return {@code true} if and the only if {@code e} is present in the graph. */
    boolean hasEdge(E e);

    /** Return {@code true} iif an edge between v1 and v2 exists. */
    boolean hasEdge(V v1, V v2);

    /**
     * Return the length of the edge between v1 and v2
     * @throws java.util.NoSuchElementException if such an edge does not exist
     */
    double edgeLength(V v1, V v2);

    // task 2

    /** Sum of the lengths*/
    double edgeLengthSum();

    /** remove edges */
    boolean removeEdge(E e);

    /** Remove the edge between v1 and v2 if present */
    boolean removeEdge(V v1, V v2);

    /**
     * Remove v and all incident edges if present
     * Return true if and only if the vertex was removed
     */
    boolean removeVertex(V v);

    /** Return the edge object between v1 and v2, or null if none. */
    E getEdge(V v1, V v2);

    /** Return a snapshot set of all vertices. */
    Set<V> getVertices();

    /** Return a snapshot set of all edges. */
    Set<E> getEdges();

    /** Return a snapshot set of all edges incident on v */
    Set<E> getEdges(V v);

    /**
     * Return a mapping from each neighbour of v to the edge connecting
     * v to that neighbour.
     */
    Map<V, E> getNeighbours(V v);

   // task 3
    double pathCost(List<V> path, PathCostType costType);
    List<V> minimumCostPath(V source, V sink, PathCostType costType);

    // task 4
    double getDiameter(PathCostType costType);
    V getCenter(PathCostType costType);

   // task 5
    Set<Set<V>> getComponents();
}


