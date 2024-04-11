/**
 * A class for executing various queries on a knowledge graph.
 */
package com.andimon.rdfknowledgelandscape.queries;

import com.andimon.rdfknowledgelandscape.factories.DefaultOntoKnowledgeLandscapeOwlClassFactory;
import com.andimon.rdfknowledgelandscape.factories.OntoKnowledgeLandscapeOwlClassFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

import static com.andimon.rdfknowledgelandscape.parameters.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;

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
    public ResultSetRewindable getKnowledgeAssets() {
        String queryString = "PREFIX kl: <" + knowledgeLandscapeNameSpace + ">\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT ?KnowledgeAsset WHERE { ?KnowledgeAsset rdf:type kl:KnowledgeAsset}\n";
        return queryExecutor(queryString).rewindable();
    }

    @Override
    public ResultSetRewindable getPersons() {
        String queryString = "PREFIX kl: <" + knowledgeLandscapeNameSpace + ">\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT ?Person WHERE { ?Person rdf:type kl:Person}\n";
        return queryExecutor(queryString).rewindable();
    }

    @Override
    public ResultSetRewindable getKnowledgeAssetDefinedFeatures() {
        String queryString = "PREFIX kl: <" + knowledgeLandscapeNameSpace + ">\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?KnowledgeAssetFeatures WHERE { ?KnowledgeAssetFeatures rdfs:subClassOf kl:KnowledgeAssetFeature}\n";
        return queryExecutor(queryString).rewindable();
    }

    @Override
    public ResultSetRewindable getComposedOf(String knowledgeAssetName) {
        String varName = knowledgeAssetName + "ComposedOf";
        String queryString = "PREFIX kl: <" + knowledgeLandscapeNameSpace + ">\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?" + varName + " WHERE {kl:" + knowledgeAssetName + " kl:composedOf ?" + varName + "}\n";
        return queryExecutor(queryString).rewindable();
    }

    @Override
    public ResultSetRewindable getDependentKnowledgeAsset(String knowledgeAssetName) {
        String varName = knowledgeAssetName + "DependentOn";
        String queryString = "PREFIX kl: <" + knowledgeLandscapeNameSpace + ">\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?" + varName + " WHERE {kl:" + knowledgeAssetName + " kl:dependsOn ?" + varName + "}\n";
        return queryExecutor(queryString).rewindable();
    }

    @Override
    public ResultSetRewindable getRelatedKnowledgeAssets(String knowledgeAssetName) {
        String varName = knowledgeAssetName + "RelatedTo";
        String queryString = "PREFIX kl: <" + knowledgeLandscapeNameSpace + ">\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?" + varName + " WHERE {kl:" + knowledgeAssetName + " kl:relatedTo ?" + varName + "}\n";
        return queryExecutor(queryString).rewindable();
    }

    @Override
    public void knownBy(String knowledgeAssetName) {
        // Implementation pending
    }

    @Override
    public ResultSetRewindable queryExecutor(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, knowledgeLandscape);
        return qExec.execSelect().rewindable();
    }


}
