package cpen221.graphs.applications.textsimilarity;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

/* === TASK 5 === */
/* Implement this class completely */

public class Document {
    private final String documentURL;                 // the URL for the document
    private final String documentId;                     // an identifier for the document, e.g., book title

    // You may need more fields for a document.

    /**
     * Create a document given a URL to the document's text as well as an id
     *
     * @param id is not null and is not the empty
     * @param url is not null and is not the empty string
     * @throws IOException
     */
    public Document(String id, String url) throws IOException {
        assert (id != null);
        assert (!id.equals(""));
        assert (url != null);
        assert (!url.equals(""));

        this.documentURL = url;
        this.documentId = id;

        // TODO: Your constructor will have more work to do.

    }

    // a simpler constructor that sets the id to be the url
    public Document(String url) throws IOException {
        this(url, url);
    }


    // obtain an immutable map that represents the words in the document
    // and the number of occurrences of each work
    public Map<String, Integer> getCountMap() {
        return null;
    }

    // get document identifier
    public String getDocumentId() {
        return documentId;
    }

    // get document URL
    public String getDocumentURL() {
        return documentURL;
    }

    /**
     * Compute the Jensen-Shannon Divergence between the two textList
     * on the basis of the words used.
     *
     * @param otherDoc is not null
     * @return the Jensen-Shannon divergence multiplied by 100 and rounded to the nearest integer
     */
    public long computeJSDiv(Document otherDoc) {
        // TODO: Implement this method
        return -1;
    }

    /**
     * Produce a string representation of a Document
     */
    @Override
    public String toString() {
        // return the id associated with this document
        return this.documentId;
    }

}