package cpen221.graphs.applications.textsimilarity;

import java.util.Set;

public class Similarity {
    /* === TASK 5 === */
    /* Implement this class completely */

    /* compute the Jensen-Shannon divergence between two documents
       remember to return (double) Math.round(1000 * ...) */
    public static double getJensenShannonDivergence(Document document1, Document document2) {
        // TODO: Implement this method
        return -1; // TODO: change this
    }

    // partition the documents based on the Jensen-Shannon divergence
    public static Set<Set<Document>> groupSimilarDocuments(Set<Document> documentSet, int numberOfGroups) {
        return null; // TODO: Implement this method
    }
}
