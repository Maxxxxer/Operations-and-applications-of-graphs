package cpen221.graphs.graph;

/**
 * Immutable vertex identified by a non-negative integer id
 * Two Vertex objects are equal iff they have the same id
 */
public class Vertex {
    private final int id;



    /**
     * Create a vertex with the given identifier.
     * Requires:
     * id >= 0.
     * Effects:
     * constructs a new Vertex object.
     */
    public Vertex(int id) {
        this.id = id;
    }
   // public int getId() {
     //   return id;
  //  }

    /**
     * Compare this vertex to another object for equality.
     * Effects: none.
     * Returns:
     * true iff o is also a Vertex and Vertex o.id == this.id.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Vertex other) {
            return (this.id == other.id);
        }
        return false;
    }

    /**
     * Return the hash code of this vertex.
     * Effects: none.
     * Returns: an int consistent with equals, only based on id
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Get the identifier of this vertex.
     * Effects: none.
     * Returns:
     * the non-negative id of this vertex
     */
    public int id() {
        return id;
    }

}
