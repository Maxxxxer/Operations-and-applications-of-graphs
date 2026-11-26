package cpen221.graphs.graph;

import java.util.NoSuchElementException;

public class Edge<V extends Vertex> {

    private V v1;
    private V v2;
    private double length;

    /**
     * Create an edge between a and  b with the given length.
     * Endpoints are stored in canonical order (by id).
     */

    public Edge(V v1, V v2) {
        this(v1, v2, 1);
    }

    public Edge(V v1, V v2, double length) {
        if (v1 == null || v2 == null) {
            throw new IllegalArgumentException("Vertices is null");
        }
        if (v1.equals(v2)) {
            throw new IllegalArgumentException("The same vertex can't be at both ends of an edge");
        }

        if (v1.id() < v2.id()) {
            this.v1 = v1;
            this.v2 = v2;
        }
        else {
            this.v1 = v2;
            this.v2 = v1;
        }

        this.length = length;
    }
    /** @return the first endpoint; by construction {@code v1.id() <= v2.id()} */
    public V v1() {
        return v1;
    }
    /** @return the second endpoint; by construction {@code v1.id() <= v2.id()} */

    public V v2() {
        return v2;
    }
    /** @return the (non-negative) length/weight of this edge */
    public double length() {
        return length;
    }

    public boolean equals(Object o) {
        if (o instanceof Edge<?> other) {
            if (other.v1.equals(this.v1) && other.v2.equals(this.v2)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return v1.hashCode() + v2.hashCode();
    }
    /**
     * @param v a vertex
     * @return true iif   v ==(v1 || v2)
     */

    public boolean incident(V v) {
        if (v == null) {
            return false;
        }
        return(v.equals(v1) || v.equals(v2));
    }

    public boolean intersects(Edge<V> e) {
        if (e == null) {
            return false;
        }
        return this.incident(e.v1) || this.incident(e.v2);
    }

    /**
     * @param e another edge that intersects this edge
     * @return the common vertex
     * @throws NoSuchElementException if the edges do not share an endpoint
     */

    public V intersection(Edge<V> e) throws NoSuchElementException {
        if (e == null) {
            throw new NoSuchElementException("No common vertex");
        }
        if (this.v1.equals(e.v1) || this.v1.equals(e.v2)) {
            return this.v1;
        }
        if (this.v2.equals(e.v1) || this.v2.equals(e.v2)) {
            return this.v2;
        }
        throw new NoSuchElementException("No common vertex");
    }

    public V distinctVertex(V v) {
        if (this.v1.equals(v)) {
            return this.v2;
        } else {
            return this.v1;
        }
    }

    public V distinctVertex(Edge<V> e) {
        if (this.equals(e)) {
            throw new NoSuchElementException("No distinct vertex");
        }
        V sv1;
        try {
            sv1 = this.intersection(e);
        }
        catch (NoSuchElementException nse) {

            return v1;
        }
        if (v1.equals(sv1)) {
            return v2;
        } else {
            return v1;
        }
    }
}
