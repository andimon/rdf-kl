/**
 * A class for executing various queries on a knowledge graph.
 */
package com.andimon.rdfknowledgelandscape.queries;

import com.andimon.rdfknowledgelandscape.factories.DefaultOntoKnowledgeLandscapeOwlClassFactory;
import com.andimon.rdfknowledgelandscape.factories.OntoKnowledgeLandscapeOwlClassFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;

public class KLQuery implements QueryEngine {
    Model knowledgeLandscape;
    String knowledgeLandscapeNameSpace;

    OntoKnowledgeLandscapeOwlClassFactory classFactory;

    /**
     * Constructs a KnowledgeGraphQueries object with the specified knowledge landscape namespace and model.
     *
     * @param knowledgeLandscapeNameSpace The namespace of the knowledge landscape.
     * @param knowledgeLandscape          The model representing the knowledge landscape.
     */
    public KLQuery(Model knowledgeLandscape) {
        this.knowledgeLandscape = knowledgeLandscape;
        this.knowledgeLandscapeNameSpace = DEFAULT_NAMESPACE.getValue(String.class);
        classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
    }




    @Override
    public ResultSetRewindable queryExecutor(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, knowledgeLandscape);
        return qExec.execSelect().rewindable();
    }


}
