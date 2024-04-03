package com.andimon.rdfknowledgelandscape.constructor;

import com.andimon.rdfknowledgelandscape.updater.NullUpdater;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class KnowledgeLandscapeConstructorTest {
    @Before
    public void setup() throws Exception {
        knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor();
    }

    KnowledgeLandscapeConstructor knowledgeLandscapeConstructor;

    @Test
    public void creatingAKnowledgeAssetWithTechnicalCategory() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Technical");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);

    }

    @Test
    public void creatingAKnowledgeAssetWithBusinessCategory() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Business");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);

    }

    @Test
    public void creatingAKnowledgeAssetWithGeneralCategory() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "General");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);

    }

    @Test
    public void creatingAKnowledgeAssetWithUndefinedCategory() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);

    }

    @Test
    public void creatingAKnowledgeAssetWithTacitVisibility() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Tacit");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);

    }

    @Test
    public void creatingAKnowledgeAssetWithExplicitVisibility() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Explicit");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);

    }

    @Test
    public void creatingAKnowledgeAssetWithUndefinedVisibility() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);

    }


    @Test
    public void creatingAKnowledgeAssetWithIndividualSocialClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Individual");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);

    }

    @Test
    public void creatingAKnowledgeAssetWithSocialSocialClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Social");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void creatingAKnowledgeAssetWithUndefinedSocialClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void creatingAKnowledgeAssetWithDeclarativeOperationalClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Declarative");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void creatingAKnowledgeAssetWithProceduralOperationalClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Procedural");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void creatingAKnowledgeAssetWithCausalOperationalClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Causal");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void creatingAKnowledgeAssetWithConditionalOperationalClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Conditional");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void creatingAKnowledgeAssetWithRelationalOperationalClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Relational");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void creatingAKnowledgeAssetWithUndefinedOperationalClassification() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }


    @Test
    public void personJoinsOrganisation() throws Exception {
        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
    }

    @Test
    public void knowledgeAssetIdentified() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Visibility", "Tacit");
        features.put("Category", "Technical");
        features.put("Operationality", "Declarative");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
    }

    @Test
    public void emptyGraph() throws Exception {
    }

    @Test
    public void exampleScenario() throws Exception {
        Map<String, String> javaFeatures = new HashMap<>();
        javaFeatures.put("Visibility", "Explicit");
        javaFeatures.put("Category", "Technical");
        javaFeatures.put("Operationality", "Undefined");
        javaFeatures.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Java", javaFeatures);
        Map<String, String> jdbcFeatures = new HashMap<>();
        jdbcFeatures.put("Visibility", "Tacit");
        jdbcFeatures.put("Category", "Technical");
        jdbcFeatures.put("Operationality", "Declarative");
        jdbcFeatures.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("JDBC", jdbcFeatures);
        Map<String, String> servletsFeatures = new HashMap<>();
        servletsFeatures.put("Visibility", "Tacit");
        servletsFeatures.put("Category", "Technical");
        servletsFeatures.put("Operationality", "Declarative");
        servletsFeatures.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Servlets", servletsFeatures);
        Map<String, String> sqlFeatures = new HashMap<>();
        sqlFeatures.put("Visibility", "Tacit");
        sqlFeatures.put("Category", "Technical");
        sqlFeatures.put("Operationality", "Declarative");
        sqlFeatures.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("SQL", sqlFeatures);
        Map<String, String> webDevelopmentFeatures = new HashMap<>();
        webDevelopmentFeatures.put("Visibility", "Tacit");
        webDevelopmentFeatures.put("Category", "Technical");
        webDevelopmentFeatures.put("Operationality", "Declarative");
        webDevelopmentFeatures.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("WebDevelopment", webDevelopmentFeatures);
        Map<String, String> oopFeatures = new HashMap<>();
        oopFeatures.put("Visibility", "Tacit");
        oopFeatures.put("Category", "Technical");
        oopFeatures.put("Operationality", "Declarative");
        oopFeatures.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("OOP", oopFeatures);
        Map<String, String> smallTalkFeatures = new HashMap<>();
        smallTalkFeatures.put("Visibility", "Tacit");
        smallTalkFeatures.put("Category", "Technical");
        smallTalkFeatures.put("Operationality", "Declarative");
        smallTalkFeatures.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("SmallTalk", smallTalkFeatures);
        knowledgeLandscapeConstructor.personJoinsOrganisation("Jane");
        knowledgeLandscapeConstructor.personJoinsOrganisation("John");
        knowledgeLandscapeConstructor.dependentOn("Java", "OOP");
        knowledgeLandscapeConstructor.composedOf("Java", "JDBC");
        knowledgeLandscapeConstructor.composedOf("Java", "Servlets");
        knowledgeLandscapeConstructor.composedOf("JDBC", "Servlets");
        knowledgeLandscapeConstructor.relatedTo("Servlets", "WebDevelopment");
        knowledgeLandscapeConstructor.dependentOn("SmallTalk", "OOP");
        knowledgeLandscapeConstructor.dependentOn("JDBC", "SQL");
        knowledgeLandscapeConstructor.knowledgeObservation("Java", "Jane", 32);
        knowledgeLandscapeConstructor.knowledgeObservation("Java", "John", 16);
        Model j = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
        j.setNsPrefix("kl", "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#");
        j.write(System.out, "TURTLE");
    }


    @Test
    public void teamCreationSuccessfulTest() {
        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
        Assert.assertTrue(knowledgeLandscapeConstructor.createTeam("Testers"));

    }

    @Test
    public void teamCreationTeamAlreadyExists() {
        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
        Assert.assertTrue(knowledgeLandscapeConstructor.createTeam("Testers"));
        Assert.assertFalse(knowledgeLandscapeConstructor.createTeam("Testers"));
    }

    @Test
    public void addMemberToTeamSuccessful() throws Exception {
        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
        Assert.assertTrue(knowledgeLandscapeConstructor.createTeam("Testers"));
        Assert.assertTrue(knowledgeLandscapeConstructor.addPersonToTeam("Testers", "Andre"));
    }

    @Test
    public void addMemberToTeamPersonAlreadyAMember() {
        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
        Assert.assertTrue(knowledgeLandscapeConstructor.createTeam("Testers"));
        Assert.assertTrue(knowledgeLandscapeConstructor.addPersonToTeam("Testers", "Andre"));
        Assert.assertFalse(knowledgeLandscapeConstructor.addPersonToTeam("Testers", "Andre"));
    }

    @Test
    public void addMemberToTeamTeamDoesNotExist() {
        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
        Assert.assertFalse(knowledgeLandscapeConstructor.addPersonToTeam("Testers", "Andre"));
    }

    @Test
    public void addMemberToTeamPersonNotIdentified() {
        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
        Assert.assertFalse(knowledgeLandscapeConstructor.addPersonToTeam("Testers", ",Mary"));
    }

    @Test
    public void transitivityOfDependencies() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1",features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2",features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K3",features);
        knowledgeLandscapeConstructor.dependentOn("K1","K2");
        knowledgeLandscapeConstructor.dependentOn("K2","K3");
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
        Resource k1 = m.createResource(defaultNamespace+"K1");
        Resource k3 = m.createResource(defaultNamespace+"K3");
        Property p = m.createProperty(defaultNamespace+"dependsOn");
        Assert.assertTrue(m.contains(k1,p,k3));
    }

    @Test
    public void transitivityOfComposition() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1",features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2",features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K3",features);
        knowledgeLandscapeConstructor.composedOf("K1","K2");
        knowledgeLandscapeConstructor.composedOf("K2","K3");
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
        Resource k1 = m.createResource(defaultNamespace+"K1");
        Resource k3 = m.createResource(defaultNamespace+"K3");
        Property p = m.createProperty(defaultNamespace+"composedOf");
        Assert.assertTrue(m.contains(k1,p,k3));
    }

    @Test
    public void compositionAsDependency() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1",features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2",features);
        knowledgeLandscapeConstructor.composedOf("K1","K2");
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
        Resource k1 = m.createResource(defaultNamespace+"K1");
        Resource k2 = m.createResource(defaultNamespace+"K2");
        Property p1 = m.createProperty(defaultNamespace+"composedOf");
        Property p2 = m.createProperty(defaultNamespace+"dependsOn");
        Assert.assertTrue(m.contains(k1,p1,k2));
        Assert.assertTrue(m.contains(k1,p2,k2));
    }

    @Test
    public void dependencyDoesNotImplyComposition() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1",features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2",features);
        knowledgeLandscapeConstructor.dependentOn("K1","K2");
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
        Resource k1 = m.createResource(defaultNamespace+"K1");
        Resource k2 = m.createResource(defaultNamespace+"K2");
        Property p1 = m.createProperty(defaultNamespace+"composedOf");
        Property p2 = m.createProperty(defaultNamespace+"dependsOn");
        Assert.assertFalse(m.contains(k1,p1,k2));
        Assert.assertTrue(m.contains(k1,p2,k2));
    }

    @Test
    public void nonSymmetryOfDependency() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1",features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2",features);
        knowledgeLandscapeConstructor.dependentOn("K1","K2");
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
        Resource k1 = m.createResource(defaultNamespace+"K1");
        Resource k2 = m.createResource(defaultNamespace+"K2");
        Property p = m.createProperty(defaultNamespace+"dependsOn");
        Assert.assertTrue(m.contains(k1,p,k2));
        Assert.assertFalse(m.contains(k2,p,k1));
    }

    @Test
    public void nonSymmetryOfComposedOf() throws Exception {
        Map<String, String> features = new HashMap<>();
        features.put("Category", "Undefined");
        features.put("Visibility", "Undefined");
        features.put("Operationality", "Undefined");
        features.put("Sociality", "Undefined");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1",features);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2",features);
        knowledgeLandscapeConstructor.composedOf("K1","K2");
        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
        Resource k1 = m.createResource(defaultNamespace+"K1");
        Resource k2 = m.createResource(defaultNamespace+"K2");
        Property p = m.createProperty(defaultNamespace+"composedOf");
        Assert.assertTrue(m.contains(k1,p,k2));
        Assert.assertFalse(m.contains(k2,p,k1));
    }
}
