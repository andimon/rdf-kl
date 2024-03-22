package com.andimon.rdf_knowledge_landscape;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class KnowledgeGraphConstructorTest {
    @Before
    public void setup() throws Exception {
        knowledgeGraphConstructor = new KnowledgeGraphConstructor(new OntoKnowledgeLandscape());
    }

    KnowledgeGraphConstructor knowledgeGraphConstructor;


    @Test
    public void firstPersonToJoinOrganisation() throws Exception {
        knowledgeGraphConstructor.personJoinsOrganisation("Andre");
        knowledgeGraphConstructor.personJoinsOrganisation("Andre");
    }

    @Test
    public void knowledgeAssetIdentified() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Visibility", "Tacit");
        features.put("Category", "Technical");
        features.put("Operationality", "Tacit");
        features.put("Sociality", "Undefined");
        knowledgeGraphConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void emptyGraph() throws Exception {
        System.out.println(knowledgeGraphConstructor.generateGraph());
    }

    @Test
    public void sampleKnowledgeLandscapeGraph() throws Exception {
        Map<String, String> k1features = new HashMap<>();
        k1features.put("Visibility", "Tacit");
        k1features.put("Category", "Technical");
        k1features.put("Operationality", "Undefined");
        k1features.put("Sociality", "Undefined");
        knowledgeGraphConstructor.knowledgeAssetIdentification("K1", k1features);
        Map<String, String> k2features = new HashMap<>();
        k2features.put("Visibility", "Tacit");
        k2features.put("Category", "Technical");
        k2features.put("Operationality", "Declarative");
        k2features.put("Sociality", "Undefined");
        knowledgeGraphConstructor.knowledgeAssetIdentification("K2", k2features);
        knowledgeGraphConstructor.knowledgeAssetIdentification("K3", k1features);
        knowledgeGraphConstructor.dependentOn("K1", "K2");
        knowledgeGraphConstructor.dependentOn("K2", "K3");
        System.out.println(knowledgeGraphConstructor.generateGraph());
    }
}
