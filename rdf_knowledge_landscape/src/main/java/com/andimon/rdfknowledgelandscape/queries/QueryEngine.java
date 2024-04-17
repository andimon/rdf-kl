package com.andimon.rdfknowledgelandscape.queries;

import org.apache.jena.query.ResultSetRewindable;

/**
 * This interface defines base queries for retrieving information about a Knowledge Landscape.
 */
public interface QueryEngine {


    ResultSetRewindable queryExecutor(String queryString);
}