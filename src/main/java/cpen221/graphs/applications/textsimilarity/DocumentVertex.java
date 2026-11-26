package cpen221.graphs.applications.textsimilarity;

import cpen221.graphs.graph.Vertex;

public class DocumentVertex extends Vertex {

    private final Document document;

    public DocumentVertex(int vertexId, Document document) {
        super(vertexId);
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

}
