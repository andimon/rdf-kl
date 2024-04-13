package com.andimon.rdfknowledgelandscape.testscenario;

import com.andimon.rdfknowledgelandscape.constructionmethods.KnowledgeLandscapeConstructor;
import com.andimon.rdfknowledgelandscape.features.*;
import com.andimon.rdfknowledgelandscape.ontology.OntoKL;
import com.andimon.rdfknowledgelandscape.queries.KLQuery;
import com.andimon.rdfknowledgelandscape.queries.QueryEngine;
import com.andimon.rdfknowledgelandscape.updater.BaseUpdater;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;

public class ScenarioTest {
    private static Model knowledgeLandscapeGraphModel;
    private static QueryEngine queryEngine;

    @BeforeAll
    static void setup() throws Exception {
        OntoKL ontoKL = new OntoKL();


        ontoKL.addFeature("Age",Set.of("Old","Established","New"));
        KnowledgeLandscapeConstructor knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor();
        // Knowledge Asset and their features
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Java", Set.of(Visibility.EXPLICIT, Category.TECHNICAL, Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("OOP", Set.of(Category.TECHNICAL, Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("SQL", Set.of(Operationality.PROCEDURAL, Category.BUSINESS, Sociality.INDIVIDUAL));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("SmallTalk", Set.of(Category.TECHNICAL,Age.OLD));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Servlets", Set.of(Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("WebDevelopment", Set.of(Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("JDBC", Set.of(Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Haskell", Set.of(Category.TECHNICAL,Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Elixir", Set.of(Category.TECHNICAL,Age.NEW));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("FunctionalProgramming", Set.of(Category.TECHNICAL));
        // Knowledge Asset with no identified feature



        // Persons and teams
        knowledgeLandscapeConstructor.personIdentification("Jane");
        knowledgeLandscapeConstructor.personIdentification("Chris");
        knowledgeLandscapeConstructor.personIdentification("John");
        knowledgeLandscapeConstructor.personIdentification("Peter");
        knowledgeLandscapeConstructor.createTeam("FunctionalProgrammers");
        knowledgeLandscapeConstructor.createTeam("ObjectOrientedProgrammers");
        knowledgeLandscapeConstructor.createTeam("Managers");
        //Adding Persons to Teams
        knowledgeLandscapeConstructor.addPersonToTeam("ObjectOrientedProgrammers", "Jane");
        knowledgeLandscapeConstructor.addPersonToTeam("ObjectOrientedProgrammers", "Chris");
        knowledgeLandscapeConstructor.addPersonToTeam("FunctionalProgrammers", "John");
        knowledgeLandscapeConstructor.addPersonToTeam("FunctionalProgrammers", "Peter");
        knowledgeLandscapeConstructor.addPersonToTeam("Managers", "John");
        knowledgeLandscapeConstructor.addPersonToTeam("Managers", "Jane");

        // Structural properties of Knowledge Assets
        // k1 dependent on k2 relationships e.g. Java dependent on Object Oriented Programming
        knowledgeLandscapeConstructor.dependentOn("Java", "OOP");
        knowledgeLandscapeConstructor.dependentOn("SmallTalk", "OOP");
        knowledgeLandscapeConstructor.dependentOn("Haskell", "FunctionalProgramming");
        knowledgeLandscapeConstructor.dependentOn("Erlang", "FunctionalProgramming");
        // If k1 is a part of k2 then k2 is composed using k1
        knowledgeLandscapeConstructor.composedOf("Java", "JDBC");
        knowledgeLandscapeConstructor.composedOf("Java", "Servlets");
        knowledgeLandscapeConstructor.relatedTo("Servlets", "WebDevelopment");
        // Create Knowledge Asset Relationships
        knowledgeLandscapeConstructor.knowledgeObservation("Jane", "SmallTalk", 16);
        knowledgeLandscapeConstructor.knowledgeObservation("Chris", "Java", 32);
        knowledgeLandscapeConstructor.knowledgeObservation("John", "Erlang", 17);
        knowledgeLandscapeConstructor.knowledgeObservation("John", "Haskell", 20);
        knowledgeLandscapeConstructor.knowledgeObservation("Peter", "Erlang", 16);
        knowledgeLandscapeConstructor.knowledgeObservation("Peter", "Haskell", 30);
        // Generate graph inferring information from structural properties of OWL and deduces further information using an updater (in this case the BaseUpdater is used)
        knowledgeLandscapeGraphModel = knowledgeLandscapeConstructor.getGraph(new BaseUpdater());
        queryEngine = new KLQuery(knowledgeLandscapeGraphModel);
    }

    @Test
    public void cq1() {
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?KnowledgeAsset WHERE { ?KnowledgeAsset a kl:KnowledgeAsset}\n";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expectedKnowledgeAssets = Set.of("SmallTalk", "Java", "OOP", "WebDevelopment", "Haskell", "JDBC", "FunctionalProgramming", "Erlang", "Servlets", "SQL");
        Set<String> actualKnowledgeAssets = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            String knowledgeAssets = binding.get("KnowledgeAsset").asResource().getLocalName();
            actualKnowledgeAssets.add(knowledgeAssets);
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expectedKnowledgeAssets, actualKnowledgeAssets);
    }

    @Test
    public void cq2() {
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?Person WHERE { ?Person a kl:Person}\n";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expectedKnowledgeAssets = Set.of("Chris", "John", "Peter", "Jane");
        Set<String> actualKnowledgeAssets = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            String knowledgeAssets = binding.get("Person").asResource().getLocalName();
            actualKnowledgeAssets.add(knowledgeAssets);
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expectedKnowledgeAssets, actualKnowledgeAssets);
    }

    @Test
    public void cq3example1() {
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?KnowledgeAsset WHERE { \n" +
                "?KnowledgeAsset a kl:KnowledgeAsset .\n" +
                "?KnowledgeAsset kl:hasCategory ?CategoryValue .\n" +
                "?CategoryValue a kl:TechnicalCategoryValue }";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expectedKnowledgeAssets = Set.of("Java", "OOP", "SmallTalk", "Haskell", "FunctionalProgramming", "Erlang");
        Set<String> actualKnowledgeAssets = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            String knowledgeAssets = binding.get("KnowledgeAsset").asResource().getLocalName();
            actualKnowledgeAssets.add(knowledgeAssets);
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expectedKnowledgeAssets, actualKnowledgeAssets);
    }

    @Test
    public void cq3example2() {
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?KnowledgeAsset WHERE { \n" +
                "?KnowledgeAsset a kl:KnowledgeAsset .\n" +
                "?KnowledgeAsset kl:hasCategory ?CategoryValue .\n" +
                "?CategoryValue a kl:TechnicalCategoryValue .\n" +
                "?KnowledgeAsset kl:hasOperationality ?OperationalityValue .\n" +
                "?OperationalityValue a kl:ProceduralOperationalityValue}";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expectedKnowledgeAssets = Set.of("Java");
        Set<String> actualKnowledgeAssets = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            String knowledgeAssets = binding.get("KnowledgeAsset").asResource().getLocalName();
            actualKnowledgeAssets.add(knowledgeAssets);
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expectedKnowledgeAssets, actualKnowledgeAssets);
    }


    @Test
    public void cq4() {
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "PREFIX rdfs: <" + knowledgeLandscapeGraphModel.getNsPrefixURI("rdfs") + ">\n" +
                "SELECT ?Team WHERE { ?Team rdfs:subClassOf kl:Person }\n";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expectedTeams = Set.of("ObjectOrientedProgrammers", "Managers", "FunctionalProgrammers");
        Set<String> actualTeams = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            String team = binding.get("Team").asResource().getLocalName();
            actualTeams.add(team);
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expectedTeams, actualTeams);
    }


    @Test
    public void cq5() {
        String teamName = "FunctionalProgrammers";
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "PREFIX rdfs: <" + knowledgeLandscapeGraphModel.getNsPrefixURI("rdfs") + ">\n" +
                "SELECT ?Member WHERE { ?Member a kl:" + teamName + "}";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expectedMembers = Set.of("John", "Peter");
        Set<String> actualMembers = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            String team = binding.get("Member").asResource().getLocalName();
            actualMembers.add(team);
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expectedMembers, actualMembers);
    }


    @Test
    public void cq6() {
        String teamName = "Managers";
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?Member ?KnowledgeAsset ?Magnitude " +
                "WHERE { ?Member a kl:" + teamName + " ." +
                "?KnowledgeObservation kl:hasPerson ?Member ." +
                "?KnowledgeObservation kl:hasKnowledgeAsset ?KnowledgeAsset ." +
                "?KnowledgeObservation kl:hasMagnitude ?Magnitude .}" +
                "ORDER BY DESC(?Magnitude)\n";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<List<String>> expected = new HashSet<List<String>>();
        Set<List<String>> actual = new HashSet<List<String>>();
        expected.add(List.of("Jane", "SmallTalk", "16.0"));
        expected.add(List.of("John", "Erlang", "17.0"));
        expected.add(List.of("John", "Haskell", "20.0"));
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            String member = binding.get("Member").asResource().getLocalName();
            String knowledgeAsset = binding.get("KnowledgeAsset").asResource().getLocalName();
            String magnitude = binding.get("Magnitude").asLiteral().getString();
            actual.add(List.of(member, knowledgeAsset, magnitude));
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void cq7() {
        String knowledgeAsset = "Java";
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?KnowledgeAsset " +
                "WHERE { ?KnowledgeAsset a kl:KnowledgeAsset .\n" +
                "?KnowledgeAsset kl:relatedTo  kl:" + knowledgeAsset + " . }";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expected = new HashSet<>();
        expected.add("OOP");
        expected.add("JDBC");
        expected.add("Servlets");
        Set<String> actual = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            actual.add(binding.get("KnowledgeAsset").asResource().getLocalName());
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void cq8() {
        String knowledgeAsset = "Java";
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?KnowledgeAsset " +
                "WHERE { ?KnowledgeAsset a kl:KnowledgeAsset .\n" +
                "kl:" + knowledgeAsset + " kl:dependsOn  ?KnowledgeAsset. }";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expected = new HashSet<>();
        expected.add("OOP");
        expected.add("JDBC");
        expected.add("Servlets");
        Set<String> actual = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            actual.add(binding.get("KnowledgeAsset").asResource().getLocalName());
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void cq9() {
        String knowledgeAsset = "Java";
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?KnowledgeAsset " +
                "WHERE { ?KnowledgeAsset a kl:KnowledgeAsset .\n" +
                "kl:" + knowledgeAsset + " kl:composedOf  ?KnowledgeAsset. }";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expected = new HashSet<>();
        expected.add("JDBC");
        expected.add("Servlets");
        Set<String> actual = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            actual.add(binding.get("KnowledgeAsset").asResource().getLocalName());
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void cq13Embeddedness() {
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?KnowledgeAsset (COUNT(?Superior)AS ?Embeddedness) \n" +
                "WHERE { ?KnowledgeAsset a kl:KnowledgeAsset . \n" +
                "OPTIONAL {  ?Superior a kl:KnowledgeAsset . ?KnowledgeAsset kl:dependsOn ?Superior . } }\n" +
                "GROUP BY ?KnowledgeAsset\n" +
                "ORDER BY DESC(?Embeddedness)";


        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expected = Set.of("SmallTalk", "Java", "OOP", "WebDevelopment", "Haskell", "JDBC", "FunctionalProgramming", "Erlang", "Servlets", "SQL");
        Set<String> actual = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            actual.add(binding.get("KnowledgeAsset").asResource().getLocalName());
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void cq13KnowledgeDistance() {
        knowledgeLandscapeGraphModel.write(System.out,"TURTLE");
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?PerSrc ?PerRec ?KA ?KASup  ?MSrc ?KoSrc  ?KoKA\n" +
                "WHERE { " +
                "?PerSrc a kl:Person . \n" +
                "?PerRec a kl:Person . \n" +
                "?KA a kl:KnowledgeAsset .\n" +
                "?KASup a kl:KnowledgeAsset ." +
                "?KA kl:dependsOn ?KASup . \n" +
                "?KoSrc a kl:KnowledgeObservation . \n" +
                "?KoSrc kl:hasPerson ?PerSrc .\n" +
                "?KoSrc kl:hasKnowledgeAsset ?KASup.\n" +
                "FILTER (?PerSrc != ?PerRec)\n" +
                "}\n";

        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        Set<String> expected = Set.of("SmallTalk", "Java", "OOP", "WebDevelopment", "Haskell", "JDBC", "FunctionalProgramming", "Erlang", "Servlets", "SQL");
        Set<String> actual = new HashSet<>();
        QuerySolution binding;
        while (results.hasNext()) {
            binding = results.nextSolution();
            System.out.println(binding.get("KnowledgeAsset"));
        }
        results.reset();
        System.out.println(ResultSetFormatter.asText(results));
        Assertions.assertEquals(expected, actual);
    }


}
