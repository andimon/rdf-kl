package com.andimon.rdfknowledgelandscape.updater;

import com.andimon.rdfknowledgelandscape.constructionmethods.KnowledgeLandscapeConstructor;
import com.andimon.rdfknowledgelandscape.features.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class KnowledgeLandscapeUpdaterTest {
    KnowledgeLandscapeConstructor knowledgeLandscapeConstructor;
    Set<Feature> features;


    @Before
    public void setup() throws Exception {
        features = new HashSet<>();
        features.add(Visibility.EXPLICIT);
        features.add(Category.TECHNICAL);
        features.add(Operationality.DECLARATIVE);
        features.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor();
    }

    @Test
    public void baseUpdaterTeamKnowledgeProperty() throws Exception {
        knowledgeLandscapeConstructor.personIdentification("John");
        knowledgeLandscapeConstructor.personIdentification("Jane");
        knowledgeLandscapeConstructor.personIdentification("Jason");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K3", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K4", features);
        knowledgeLandscapeConstructor.personIdentification("Jason");
        knowledgeLandscapeConstructor.createTeam("AwesomeTeam");
        knowledgeLandscapeConstructor.addPersonToTeam("AwesomeTeam", "John");
        knowledgeLandscapeConstructor.addPersonToTeam("AwesomeTeam", "Jane");
        knowledgeLandscapeConstructor.knowledgeObservation("John", "K1", 1);
        knowledgeLandscapeConstructor.knowledgeObservation("Jane", "K3", 1);
        knowledgeLandscapeConstructor.knowledgeObservation("John", "K4", 1);
        knowledgeLandscapeConstructor.knowledgeObservation("Jason", "K2", 1);
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
        knowledgeLandscapeConstructor.personIdentification("John");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K3", features);
        knowledgeLandscapeConstructor.composedOf("K1", "K2");
        knowledgeLandscapeConstructor.composedOf("K1", "K3");
        knowledgeLandscapeConstructor.knowledgeObservation("John", "K2", 1);
        Model model = knowledgeLandscapeConstructor.generateGraph(new BaseUpdater());
        //Assert derived knowledge
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Resource subject = model.createResource(defaultNamespace + "JohnK1");
        Property predicate = model.createProperty(defaultNamespace + "hasMagnitude");
        Assert.assertTrue(model.containsLiteral(subject, predicate, 0.5));
    }


}
