package com.andimon.rdfknowledgelandscape.updater;

import com.andimon.rdfknowledgelandscape.constructor.KnowledgeLandscapeConstructor;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeLandscapeUpdaterTest {
    KnowledgeLandscapeConstructor knowledgeLandscapeConstructor;
    Map<String, String> features;


    @Before
    public void setup() throws Exception {
        features = new HashMap<>();
        features.put("Visibility", "Tacit");
        features.put("Category", "Technical");
        features.put("Operationality", "Declarative");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor();
    }

    @Test
    public void baseUpdaterTeamKnowledgeProperty() throws Exception {
        knowledgeLandscapeConstructor.personJoinsOrganisation("John");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Jane");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Jason");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K3", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K4", features);
        knowledgeLandscapeConstructor.personJoinsOrganisation("Jason");
        knowledgeLandscapeConstructor.createTeam("AwesomeTeam");
        knowledgeLandscapeConstructor.addPersonToTeam("AwesomeTeam", "John");
        knowledgeLandscapeConstructor.addPersonToTeam("AwesomeTeam", "Jane");
        knowledgeLandscapeConstructor.knowledgeObservation("K1", "John", 1);
        knowledgeLandscapeConstructor.knowledgeObservation("K3", "Jane", 1);
        knowledgeLandscapeConstructor.knowledgeObservation("K4", "John", 1);
        knowledgeLandscapeConstructor.knowledgeObservation("K2", "Jason", 1);
        Model model = knowledgeLandscapeConstructor.generateGraph(new BaseUpdater());
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Resource team = model.createResource(defaultNamespace + "AwesomeTeam");
        Resource knowledgeAsset1 = model.createResource(defaultNamespace + "K1");
        Resource knowledgeAsset2 = model.createResource(defaultNamespace + "K2");
        Resource knowledgeAsset3 = model.createResource(defaultNamespace + "K3");
        Resource knowledgeAsset4 = model.createResource(defaultNamespace + "K4");
        Property property = model.createProperty(defaultNamespace + "knows");
        Assert.assertTrue(model.contains(team, property, knowledgeAsset1));
        Assert.assertFalse(model.contains(team, property, knowledgeAsset2));
        Assert.assertTrue(model.contains(team, property, knowledgeAsset3));
        Assert.assertTrue(model.contains(team, property, knowledgeAsset4));
    }

    @Test
    public void transitivityOfKnowledgeThroughComposition() throws Exception {
        knowledgeLandscapeConstructor.personJoinsOrganisation("John");
        features = new HashMap<>();
        features.put("Visibility", "Undefined");
        features.put("Category", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K3", features);
        knowledgeLandscapeConstructor.composedOf("K1", "K2");
        knowledgeLandscapeConstructor.composedOf("K1", "K3");
        knowledgeLandscapeConstructor.knowledgeObservation("K2", "John", 1);
        Model model = knowledgeLandscapeConstructor.generateGraph(new BaseUpdater());
        //Assert derived knowledge
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Resource subject = model.createResource(defaultNamespace + "JohnK1");
        Property predicate = model.createProperty(defaultNamespace + "hasMagnitude");
        Assert.assertTrue(model.containsLiteral(subject, predicate, 0.5));
    }




}
