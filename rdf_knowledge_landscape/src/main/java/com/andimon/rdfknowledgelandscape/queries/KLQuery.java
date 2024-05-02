/**
 * A class for executing various queries on a knowledge graph.
 */
package com.andimon.rdfknowledgelandscape.queries;

import com.andimon.rdfknowledgelandscape.factories.DefaultOntoKnowledgeLandscapeOwlClassFactory;
import com.andimon.rdfknowledgelandscape.factories.OntoKnowledgeLandscapeOwlClassFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.KL_NAMESPACE;

public class KLQuery implements QueryEngine {
    Model knowledgeLandscape;
    String knowledgeLandscapeNameSpace;

    OntoKnowledgeLandscapeOwlClassFactory classFactory;

    /**
     * Create a query engine for an RDF graph
     * @param knowledgeLandscape The graph to run query on.
     */
    public KLQuery(Model knowledgeLandscape) {
        this.knowledgeLandscape = knowledgeLandscape;
        this.knowledgeLandscapeNameSpace = KL_NAMESPACE.getValue(String.class);
        classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
    }

    @Override
    public ResultSetRewindable queryExecutor(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, knowledgeLandscape);
        return qExec.execSelect().rewindable();
    }


}
