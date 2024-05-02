package com.andimon.rdfknowledgelandscape.updater;

import com.andimon.rdfknowledgelandscape.constructionmethods.KnowledgeGraphConstructorException;
import com.andimon.rdfknowledgelandscape.constructionmethods.KnowledgeLandscapeConstructor;
import com.andimon.rdfknowledgelandscape.features.Feature;
import com.andimon.rdfknowledgelandscape.features.Visibility;
import com.andimon.rdfknowledgelandscape.queries.KLQuery;
import com.andimon.rdfknowledgelandscape.queries.QueryEngine;
import com.sun.source.tree.AssertTree;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.KL_NAMESPACE;

public class BaseUpdaterTest {
    static KnowledgeLandscapeConstructor knowledgeLandscapeConstructor;
    private static QueryEngine queryEngine;


    @BeforeEach
    public void setup() throws Exception {
        knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor();
    }

    @Test
    public void transitivityThroughDependency() throws Exception {
        //create new knowledge assets
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("k2", Set.of(Visibility.TACIT));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("k3", Set.of(Visibility.TACIT));
        //create persons
        knowledgeLandscapeConstructor.personIdentification("p1");
        knowledgeLandscapeConstructor.personIdentification("p2");
        //create knowledge observation
        knowledgeLandscapeConstructor.knowledgeObservation("p1", "k1", 10);
        knowledgeLandscapeConstructor.knowledgeObservation("p2", "k2", 20);
        //create dependent relationship
        knowledgeLandscapeConstructor.dependentOn("k1", "k3");
        //create graph with base updater
        Model graph = knowledgeLandscapeConstructor.getGraph(new BaseUpdater());
        //person p1 should know k3 with magnitude more than 10 and less than 20
        String queryString = "PREFIX kl: <" + KL_NAMESPACE.getValue(String.class) + ">\n" +
                """
                        SELECT ?mag
                        WHERE {
                            ?ko a kl:KnowledgeObservation .
                            ?ko kl:hasKnowledgeAsset kl:k3 .
                            ?ko kl:hasPerson kl:p1 .
                            ?ko kl:hasMagnitude ?mag .
                        }
                        """;
        queryEngine = new KLQuery(graph);
        List<Integer> magnitudes = new ArrayList<Integer>();
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        while (results.hasNext()) {
            QuerySolution sol = results.nextSolution();
            Literal l = sol.getLiteral("mag");
            magnitudes.add(l.getInt());
        }
        Assertions.assertEquals(1, magnitudes.size());
        Assertions.assertTrue(10 <= magnitudes.getFirst());
        Assertions.assertTrue(magnitudes.getFirst() <= 20);

    }

    @Test
    public void transitivityThroughComposition() throws Exception {
        //create new knowledge assets
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("k2", Set.of(Visibility.TACIT));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("k3", Set.of(Visibility.TACIT));
        //create persons
        knowledgeLandscapeConstructor.personIdentification("p1");
        knowledgeLandscapeConstructor.personIdentification("p2");
        //create knowledge observation
        knowledgeLandscapeConstructor.knowledgeObservation("p1", "k1", 10);
        knowledgeLandscapeConstructor.knowledgeObservation("p2", "k2", 20);
        //create dependent relationship
        knowledgeLandscapeConstructor.composedOf("k3", "k1");
        //create graph with base updater
        Model graph = knowledgeLandscapeConstructor.getGraph(new BaseUpdater());
        //person p1 should know k3 with magnitude more than 10 and less than 20
        String queryString = "PREFIX kl: <" + KL_NAMESPACE.getValue(String.class) + ">\n" +
                """
                        SELECT ?mag
                        WHERE {
                            ?ko a kl:KnowledgeObservation .
                            ?ko kl:hasKnowledgeAsset kl:k3 .
                            ?ko kl:hasPerson kl:p1 .
                            ?ko kl:hasMagnitude ?mag .
                        }
                        """;
        queryEngine = new KLQuery(graph);
        List<Integer> magnitudes = new ArrayList<Integer>();
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        while (results.hasNext()) {
            QuerySolution sol = results.nextSolution();
            Literal l = sol.getLiteral("mag");
            magnitudes.add(l.getInt());
        }
        Assertions.assertEquals(1, magnitudes.size());
        Assertions.assertTrue(0 <= magnitudes.getFirst());
        Assertions.assertTrue(magnitudes.getFirst() <= 10);

    }
}
