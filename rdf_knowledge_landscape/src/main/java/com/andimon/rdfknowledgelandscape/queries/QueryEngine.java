package com.andimon.rdfknowledgelandscape.queries;

import org.apache.jena.query.ResultSetRewindable;

/**
 * This interface defines base queries for retrieving information about a Knowledge Landscape.
 */
public interface QueryEngine {

    /**
     * Execute RDF graph against a query.
     * @param queryString SPARQL query
     * @return The result set of the query.
     */
    ResultSetRewindable queryExecutor(String queryString);
}