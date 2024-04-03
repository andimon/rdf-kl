//package com.andimon.rdfknowledgelandscape.queries;
//
//import com.andimon.rdfknowledgelandscape.constructor.KnowledgeLandscapeConstructor;
//import com.andimon.rdfknowledgelandscape.factories.DefaultOntoKnowledgeLandscapeOwlClassFactory;
//import com.andimon.rdfknowledgelandscape.factories.OntoKnowledgeLandscapeOwlClassFactory;
//import com.andimon.rdfknowledgelandscape.updater.NullUpdater;
//import org.apache.jena.query.QuerySolution;
//import org.apache.jena.query.ResultSetFormatter;
//import org.apache.jena.query.ResultSetRewindable;
//import org.apache.jena.rdf.model.Model;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.*;
//
//public class KnowledgeLandscapeQueriesTest {
//    Model exampleKnowledgeLandscape;
//
//    @Before
//    public void expectedMethods() {
//
//    }
//
//    @Before
//    public void setup() throws Exception {
//        KnowledgeLandscapeConstructor knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor();
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
//        exampleKnowledgeLandscape = knowledgeLandscapeConstructor.generateGraph(new NullUpdater());
//    }
//
//    @Test
//    public void testGetKnowledgeAssetQuery() {
//        OntoKnowledgeLandscapeOwlClassFactory classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
//        KnowledgeGraphQueries queries = new KnowledgeGraphQueries("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#", exampleKnowledgeLandscape);
//        ResultSetRewindable results = queries.getKnowledgeAssets();
//        Set<String> expectedKnowledgeAssets = new HashSet<>(Arrays.asList("Java", "JDBC", "WebDevelopment", "SQL", "OOP", "SmallTalk", "Servlets"));
//        Set<String> actualKnowledgeAssets = new HashSet<>();
//        results.reset(); // Back to first result.
//        while (results.hasNext()) {
//            QuerySolution binding = results.nextSolution();
//            String knowledgeAssets = binding.get("KnowledgeAsset").asResource().getLocalName();
//            actualKnowledgeAssets.add(knowledgeAssets);
//        }
//        Assert.assertEquals(expectedKnowledgeAssets, actualKnowledgeAssets);
//    }
//
//    @Test
//    public void testGetPersonsQuery() {
//        OntoKnowledgeLandscapeOwlClassFactory classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
//        KnowledgeGraphQueries queries = new KnowledgeGraphQueries("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#", exampleKnowledgeLandscape);
//        ResultSetRewindable results = queries.getPersons();
//        Set<String> expectedPersons = new HashSet<>(Arrays.asList("Jane", "John"));
//        Set<String> actualPersons = new HashSet<>();
//        System.out.println(ResultSetFormatter.asText(results)); // Print results.
//        results.reset(); // Back to first result.
//        while (results.hasNext()) {
//            QuerySolution binding = results.nextSolution();
//            String person = binding.get("Person").asResource().getLocalName();
//            actualPersons.add(person);
//        }
//        Assert.assertEquals(expectedPersons, actualPersons);
//    }
//
//    @Test
//    public void getPossibleFeaturesQuery() {
//        OntoKnowledgeLandscapeOwlClassFactory classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
//        KnowledgeGraphQueries queries = new KnowledgeGraphQueries("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#", exampleKnowledgeLandscape);
//        ResultSetRewindable results = queries.getKnowledgeAssetDefinedFeatures();
//        Set<String> expectedFeatures = new HashSet<>(Arrays.asList("Category", "Sociality", "Operationality", "Visibility"));
//        Set<String> actualFeatures = new HashSet<>();
//        System.out.println(ResultSetFormatter.asText(results)); // Print results.
//        results.reset(); // Back to first result.
//        while (results.hasNext()) {
//            QuerySolution binding = results.nextSolution();
//            String knowledgeAssets = binding.get("KnowledgeAssetFeatures").asResource().getLocalName();
//            actualFeatures.add(knowledgeAssets);
//        }
//        Assert.assertEquals(expectedFeatures, actualFeatures);
//    }
//
//    @Test
//    public void getComposedOfFeaturesTest() {
//        String knowledgeAssetName = "Java";
//        OntoKnowledgeLandscapeOwlClassFactory classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
//        KnowledgeGraphQueries queries = new KnowledgeGraphQueries("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#", exampleKnowledgeLandscape);
//        ResultSetRewindable results = queries.getComposedOf(knowledgeAssetName);
//        Set<String> expectedFeatures = new HashSet<>(Arrays.asList("JDBC", "Servlets"));
//        Set<String> actualFeatures = new HashSet<>();
//        System.out.println(ResultSetFormatter.asText(results)); // Print results.
//        results.reset(); // Back to first result.
//        while (results.hasNext()) {
//            QuerySolution binding = results.nextSolution();
//            String knowledgeAssets = binding.get(knowledgeAssetName+"ComposedOf").asResource().getLocalName();
//            actualFeatures.add(knowledgeAssets);
//        }
//        Assert.assertEquals(expectedFeatures, actualFeatures);
//    }
//
//    @Test
//    public void getRelatedToFeaturesTest() {
//        String knowledgeAssetName = "Servlets";
//        OntoKnowledgeLandscapeOwlClassFactory classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
//        KnowledgeGraphQueries queries = new KnowledgeGraphQueries("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#", exampleKnowledgeLandscape);
//        ResultSetRewindable results = queries.getRelatedKnowledgeAssets(knowledgeAssetName);
//        Set<String> expectedFeatures = new HashSet<>(Arrays.asList("WebDevelopment"));
//        Set<String> actualFeatures = new HashSet<>();
//        System.out.println(ResultSetFormatter.asText(results)); // Print results.
//        results.reset(); // Back to first result.
//        while (results.hasNext()) {
//            QuerySolution binding = results.nextSolution();
//            String knowledgeAssets = binding.get(knowledgeAssetName+"RelatedTo").asResource().getLocalName();
//            actualFeatures.add(knowledgeAssets);
//        }
//        Assert.assertEquals(expectedFeatures, actualFeatures);
//    }
//
//    @Test
//    public void getDependentOnFeaturesTest() {
//        String knowledgeAssetName = "Java";
//        OntoKnowledgeLandscapeOwlClassFactory classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
//        KnowledgeGraphQueries queries = new KnowledgeGraphQueries("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#", exampleKnowledgeLandscape);
//        ResultSetRewindable results = queries.getDependentKnowledgeAsset(knowledgeAssetName);
//        Set<String> expectedFeatures = new HashSet<>(Arrays.asList("JDBC", "Servlets","SQL","OOP"));
//        Set<String> actualFeatures = new HashSet<>();
//        System.out.println(ResultSetFormatter.asText(results)); // Print results.
//        results.reset(); // Back to first result.
//        while (results.hasNext()) {
//            QuerySolution binding = results.nextSolution();
//            String knowledgeAssets = binding.get(knowledgeAssetName+"DependentOn").asResource().getLocalName();
//            actualFeatures.add(knowledgeAssets);
//        }
//        Assert.assertEquals(expectedFeatures, actualFeatures);
//    }
//
//
//
//}
