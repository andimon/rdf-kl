package com.andimon.rdfknowledgelandscape.constructionmethods;

import com.andimon.rdfknowledgelandscape.features.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit test for simple App.
 */
import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;

public class KnowledgeLandscapeConstructorTest {
    @BeforeEach
    public void setup() throws Exception {
        knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor();
    }

    KnowledgeLandscapeConstructor knowledgeLandscapeConstructor;

    @Test
    public void creatingAKnowledgeAssetWithTechnicalCategory() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.TECHNICAL);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);

    }

    @Test
    public void creatingAKnowledgeAssetWithBusinessCategory() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.BUSINESS);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void creatingAKnowledgeAssetWithGeneralCategory() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.GENERAL);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);

    }

    @Test
    public void creatingAKnowledgeAssetWithUndefinedCategory() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);


    }

    @Test
    public void creatingAKnowledgeAssetWithTacitVisibility() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.TACIT);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);

    }

    @Test
    public void creatingAKnowledgeAssetWithExplicitVisibility() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.EXPLICIT);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);

    }

    @Test
    public void creatingAKnowledgeAssetWithUndefinedVisibility() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }


    @Test
    public void creatingAKnowledgeAssetWithIndividualSocialClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.INDIVIDUAL);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);

    }

    @Test
    public void creatingAKnowledgeAssetWithSocialSocialClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.SOCIAL);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void creatingAKnowledgeAssetWithUndefinedSocialClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void creatingAKnowledgeAssetWithDeclarativeOperationalClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.DECLARATIVE);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void creatingAKnowledgeAssetWithProceduralOperationalClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.PROCEDURAL);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void creatingAKnowledgeAssetWithCausalOperationalClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.CAUSAL);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void creatingAKnowledgeAssetWithConditionalOperationalClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.CONDITIONAL);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void creatingAKnowledgeAssetWithRelationalOperationalClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.RELATIONAL);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void creatingAKnowledgeAssetWithUndefinedOperationalClassification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.UNDEFINED);
        featureSet.add(Visibility.UNDEFINED);
        featureSet.add(Operationality.UNDEFINED);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }


    @Test
    public void personJoinsOrganisation() throws Exception {
        knowledgeLandscapeConstructor.personIdentification("Andre");
        Assertions.assertTrue(knowledgeLandscapeConstructor.containsEntityInSignature(IRI.create(DEFAULT_NAMESPACE.getValue(String.class) + "Andre")));
    }

    @Test
    public void removePerson() throws Exception {
        knowledgeLandscapeConstructor.personIdentification("Andre");
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", Set.of(Visibility.TACIT));
        Assertions.assertTrue(knowledgeLandscapeConstructor.containsEntityInSignature(IRI.create(DEFAULT_NAMESPACE.getValue(String.class) + "Andre")));
        knowledgeLandscapeConstructor.knowledgeObservation("Andre", "K1", 4);

        knowledgeLandscapeConstructor.removePerson("Andre");

        Assertions.assertFalse(knowledgeLandscapeConstructor.containsEntityInSignature(IRI.create(DEFAULT_NAMESPACE.getValue(String.class) + "Andre")));
    }


    @Test
    public void removeKnowledgeAsset() throws Exception {
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT,Category.TECHNICAL));
        knowledgeLandscapeConstructor.personIdentification("Andre");
        knowledgeLandscapeConstructor.knowledgeObservation("Andre","k1",8);
        knowledgeLandscapeConstructor.removeKnowledgeAsset("k1");
    }






    @Test
    public void knowledgeAssetIdentification() throws Exception {
        Set<Feature> featureSet = new HashSet<>();
        featureSet.add(Category.TECHNICAL);
        featureSet.add(Visibility.TACIT);
        featureSet.add(Operationality.DECLARATIVE);
        featureSet.add(Sociality.UNDEFINED);
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", featureSet);
    }

    @Test
    public void emptyGraph() throws Exception {
    }

//    @Test
//    public void exampleScenario() throws Exception {
//        Set<Feature> javaFeatures = new HashSet<>();
//        javaFeatures.add(Category.TECHNICAL);
//        javaFeatures.add(Visibility.EXPLICIT);
//        javaFeatures.add(Operationality.UNDEFINED);
//        javaFeatures.add(Sociality.UNDEFINED);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1",javaFeatures);
//        Map<String, String> javaFeatures = new HashMap<>();
//        javaFeatures.put("Visibility", "Explicit");
//        javaFeatures.put("Category", "Technical");
//        javaFeatures.put("Operationality", "Undefined");
//        javaFeatures.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Java", javaFeatures);
//        Map<String, String> jdbcFeatures = new HashMap<>();
//        jdbcFeatures.put("Visibility", "Tacit");
//        jdbcFeatures.put("Category", "Technical");
//        jdbcFeatures.put("Operationality", "Declarative");
//        jdbcFeatures.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("JDBC", jdbcFeatures);
//        Map<String, String> servletsFeatures = new HashMap<>();
//        servletsFeatures.put("Visibility", "Tacit");
//        servletsFeatures.put("Category", "Technical");
//        servletsFeatures.put("Operationality", "Declarative");
//        servletsFeatures.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Servlets", servletsFeatures);
//        Map<String, String> sqlFeatures = new HashMap<>();
//        sqlFeatures.put("Visibility", "Tacit");
//        sqlFeatures.put("Category", "Technical");
//        sqlFeatures.put("Operationality", "Declarative");
//        sqlFeatures.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("SQL", sqlFeatures);
//        Map<String, String> webDevelopmentFeatures = new HashMap<>();
//        webDevelopmentFeatures.put("Visibility", "Tacit");
//        webDevelopmentFeatures.put("Category", "Technical");
//        webDevelopmentFeatures.put("Operationality", "Declarative");
//        webDevelopmentFeatures.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("WebDevelopment", webDevelopmentFeatures);
//        Map<String, String> oopFeatures = new HashMap<>();
//        oopFeatures.put("Visibility", "Tacit");
//        oopFeatures.put("Category", "Technical");
//        oopFeatures.put("Operationality", "Declarative");
//        oopFeatures.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("OOP", oopFeatures);
//        Map<String, String> smallTalkFeatures = new HashMap<>();
//        smallTalkFeatures.put("Visibility", "Tacit");
//        smallTalkFeatures.put("Category", "Technical");
//        smallTalkFeatures.put("Operationality", "Declarative");
//        smallTalkFeatures.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("SmallTalk", smallTalkFeatures);
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Jane");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("John");
//        knowledgeLandscapeConstructor.dependentOn("Java", "OOP");
//        knowledgeLandscapeConstructor.composedOf("Java", "JDBC");
//        knowledgeLandscapeConstructor.composedOf("Java", "Servlets");
//        knowledgeLandscapeConstructor.composedOf("JDBC", "Servlets");
//        knowledgeLandscapeConstructor.relatedTo("Servlets", "WebDevelopment");
//        knowledgeLandscapeConstructor.dependentOn("SmallTalk", "OOP");
//        knowledgeLandscapeConstructor.dependentOn("JDBC", "SQL");
//        knowledgeLandscapeConstructor.knowledgeObservation("Java", "Jane", 32);
//        knowledgeLandscapeConstructor.knowledgeObservation("Java", "John", 16);
//        Model j = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
//        j.setNsPrefix("kl", "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#");
//        j.write(System.out, "TURTLE");
//    }
//
//
//    @Test
//    public void teamCreationSuccessfulTest() {
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
//        Assert.assertTrue(knowledgeLandscapeConstructor.createTeam("Testers"));
//
//    }
//
//    @Test
//    public void teamCreationTeamAlreadyExists() {
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
//        Assert.assertTrue(knowledgeLandscapeConstructor.createTeam("Testers"));
//        Assert.assertFalse(knowledgeLandscapeConstructor.createTeam("Testers"));
//    }
//
//    @Test
//    public void addMemberToTeamSuccessful() throws Exception {
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
//        Assert.assertTrue(knowledgeLandscapeConstructor.createTeam("Testers"));
//        Assert.assertTrue(knowledgeLandscapeConstructor.addPersonToTeam("Testers", "Andre"));
//    }
//
//    @Test
//    public void addMemberToTeamPersonAlreadyAMember() {
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
//        Assert.assertTrue(knowledgeLandscapeConstructor.createTeam("Testers"));
//        Assert.assertTrue(knowledgeLandscapeConstructor.addPersonToTeam("Testers", "Andre"));
//        Assert.assertFalse(knowledgeLandscapeConstructor.addPersonToTeam("Testers", "Andre"));
//    }
//
//    @Test
//    public void addMemberToTeamTeamDoesNotExist() {
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
//        Assert.assertFalse(knowledgeLandscapeConstructor.addPersonToTeam("Testers", "Andre"));
//    }
//
//    @Test
//    public void addMemberToTeamPersonNotIdentified() {
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Andre");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Mark");
//        knowledgeLandscapeConstructor.personJoinsOrganisation("Chris");
//        Assert.assertFalse(knowledgeLandscapeConstructor.addPersonToTeam("Testers", ",Mary"));
//    }
//
//    @Test
//    public void transitivityOfDependencies() throws Exception {
//        Map<String, String> features = new HashMap<>();
//        features.put("Category", "Undefined");
//        features.put("Visibility", "Undefined");
//        features.put("Operationality", "Undefined");
//        features.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K3", features);
//        knowledgeLandscapeConstructor.dependentOn("K1", "K2");
//        knowledgeLandscapeConstructor.dependentOn("K2", "K3");
//        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
//        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
//        Resource k1 = m.createResource(defaultNamespace + "K1");
//        Resource k3 = m.createResource(defaultNamespace + "K3");
//        Property p = m.createProperty(defaultNamespace + "dependsOn");
//        Assert.assertTrue(m.contains(k1, p, k3));
//    }
//
//    @Test
//    public void transitivityOfComposition() throws Exception {
//        Map<String, String> features = new HashMap<>();
//        features.put("Category", "Undefined");
//        features.put("Visibility", "Undefined");
//        features.put("Operationality", "Undefined");
//        features.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K3", features);
//        knowledgeLandscapeConstructor.composedOf("K1", "K2");
//        knowledgeLandscapeConstructor.composedOf("K2", "K3");
//        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
//        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
//        Resource k1 = m.createResource(defaultNamespace + "K1");
//        Resource k3 = m.createResource(defaultNamespace + "K3");
//        Property p = m.createProperty(defaultNamespace + "composedOf");
//        Assert.assertTrue(m.contains(k1, p, k3));
//    }
//
//    @Test
//    public void compositionAsDependency() throws Exception {
//        Map<String, String> features = new HashMap<>();
//        features.put("Category", "Undefined");
//        features.put("Visibility", "Undefined");
//        features.put("Operationality", "Undefined");
//        features.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
//        knowledgeLandscapeConstructor.composedOf("K1", "K2");
//        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
//        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
//        Resource k1 = m.createResource(defaultNamespace + "K1");
//        Resource k2 = m.createResource(defaultNamespace + "K2");
//        Property p1 = m.createProperty(defaultNamespace + "composedOf");
//        Property p2 = m.createProperty(defaultNamespace + "dependsOn");
//        Assert.assertTrue(m.contains(k1, p1, k2));
//        Assert.assertTrue(m.contains(k1, p2, k2));
//    }
//
//    @Test
//    public void dependencyDoesNotImplyComposition() throws Exception {
//        Map<String, String> features = new HashMap<>();
//        features.put("Category", "Undefined");
//        features.put("Visibility", "Undefined");
//        features.put("Operationality", "Undefined");
//        features.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
//        knowledgeLandscapeConstructor.dependentOn("K1", "K2");
//        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
//        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
//        Resource k1 = m.createResource(defaultNamespace + "K1");
//        Resource k2 = m.createResource(defaultNamespace + "K2");
//        Property p1 = m.createProperty(defaultNamespace + "composedOf");
//        Property p2 = m.createProperty(defaultNamespace + "dependsOn");
//        Assert.assertFalse(m.contains(k1, p1, k2));
//        Assert.assertTrue(m.contains(k1, p2, k2));
//    }
//
//    @Test
//    public void nonSymmetryOfDependency() throws Exception {
//        Map<String, String> features = new HashMap<>();
//        features.put("Category", "Undefined");
//        features.put("Visibility", "Undefined");
//        features.put("Operationality", "Undefined");
//        features.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
//        knowledgeLandscapeConstructor.dependentOn("K1", "K2");
//        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
//        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
//        Resource k1 = m.createResource(defaultNamespace + "K1");
//        Resource k2 = m.createResource(defaultNamespace + "K2");
//        Property p = m.createProperty(defaultNamespace + "dependsOn");
//        Assert.assertTrue(m.contains(k1, p, k2));
//        Assert.assertFalse(m.contains(k2, p, k1));
//    }
//
//    @Test
//    public void nonSymmetryOfComposedOf() throws Exception {
//        Map<String, String> features = new HashMap<>();
//        features.put("Category", "Undefined");
//        features.put("Visibility", "Undefined");
//        features.put("Operationality", "Undefined");
//        features.put("Sociality", "Undefined");
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K1", features);
//        knowledgeLandscapeConstructor.knowledgeAssetIdentification("K2", features);
//        knowledgeLandscapeConstructor.composedOf("K1", "K2");
//        String defaultNamespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
//        Model m = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
//        Resource k1 = m.createResource(defaultNamespace + "K1");
//        Resource k2 = m.createResource(defaultNamespace + "K2");
//        Property p = m.createProperty(defaultNamespace + "composedOf");
//        Assert.assertTrue(m.contains(k1, p, k2));
//        Assert.assertFalse(m.contains(k2, p, k1));
//    }
}
