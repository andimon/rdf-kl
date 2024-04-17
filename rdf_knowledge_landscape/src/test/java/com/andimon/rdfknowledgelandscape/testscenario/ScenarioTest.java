package com.andimon.rdfknowledgelandscape.testscenario;

import com.andimon.rdfknowledgelandscape.constructionmethods.KnowledgeLandscapeConstructor;
import com.andimon.rdfknowledgelandscape.features.*;
import com.andimon.rdfknowledgelandscape.ontology.OntoKL;
import com.andimon.rdfknowledgelandscape.queries.KLQuery;
import com.andimon.rdfknowledgelandscape.queries.QueryEngine;
import com.andimon.rdfknowledgelandscape.updater.BaseUpdater;
import org.apache.jena.base.Sys;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.core.Prologue;
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
    static Prologue prologue;
    @BeforeAll
    static void setup() throws Exception {
        OntoKL ontoKL = new OntoKL();
        prologue = new Prologue();
        prologue.setPrefix("kl",DEFAULT_NAMESPACE.getValue(String.class));

        ontoKL.addFeature("Age", Set.of("Old", "Established", "New"));
        KnowledgeLandscapeConstructor knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor(ontoKL);

        // Knowledge Asset and their features
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Java", Set.of(Visibility.EXPLICIT, Category.TECHNICAL, Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("OOP", Set.of(Category.TECHNICAL, Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("SQL", Set.of(Operationality.PROCEDURAL, Category.BUSINESS, Sociality.INDIVIDUAL));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("SmallTalk", Set.of(Category.TECHNICAL, Age.OLD));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Servlets", Set.of(Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("WebDevelopment", Set.of(Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("JDBC", Set.of(Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Haskell", Set.of(Category.TECHNICAL, Age.ESTABLISHED));
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("Elixir", Set.of(Category.TECHNICAL, Age.NEW));
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
        knowledgeLandscapeConstructor.dependentOn("Elixir", "FunctionalProgramming");
        // If k1 is a part of k2 then k2 is composed using k1
        knowledgeLandscapeConstructor.composedOf("Java", "JDBC");
        knowledgeLandscapeConstructor.composedOf("Java", "Servlets");
        knowledgeLandscapeConstructor.relatedTo("Servlets", "WebDevelopment");
        // Create Knowledge Asset Relationships
        knowledgeLandscapeConstructor.knowledgeObservation("Jane", "SmallTalk", 16);
        knowledgeLandscapeConstructor.knowledgeObservation("Chris", "Java", 32);
        knowledgeLandscapeConstructor.knowledgeObservation("John", "Elixir", 17);
        knowledgeLandscapeConstructor.knowledgeObservation("John", "Haskell", 20);
        knowledgeLandscapeConstructor.knowledgeObservation("Peter", "Elixir", 16);
        knowledgeLandscapeConstructor.knowledgeObservation("Peter", "Haskell", 30);
        // Generate graph inferring information from structural properties of OWL and deduces further information using an updater (in this case the BaseUpdater is used)
        knowledgeLandscapeGraphModel = knowledgeLandscapeConstructor.getGraph(new BaseUpdater());
        queryEngine = new KLQuery(knowledgeLandscapeGraphModel);
    }


    @Test
    public void personKnowledgeAssetExtentQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                SELECT ?extent\s
                WHERE {
                    ?ko a kl:KnowledgeObservation .
                    ?ko kl:hasKnowledgeAsset kl:Java .
                    ?ko kl:hasPerson kl:Chris .
                    ?ko kl:hasMagnitude ?extent
                }
                """;
                ResultSetRewindable results = queryEngine.queryExecutor(queryString);
                System.out.println(ResultSetFormatter.asText(results));
    }

    @Test
    public void whoKnowsAKnowledgeAssetQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                SELECT ?person ?extent\s
                WHERE {
                    ?ko a kl:KnowledgeObservation .
                    ?ko kl:hasKnowledgeAsset kl:Elixir .
                    ?ko kl:hasPerson ?person .
                    ?ko kl:hasMagnitude ?extent
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);

        System.out.println(ResultSetFormatter.asText(results,prologue));
    }



    @Test
    public void knownKnowledgeAssetsQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                SELECT ?ka ?extent\s
                WHERE {
                    ?ko a kl:KnowledgeObservation .
                    ?ko kl:hasKnowledgeAsset ?ka .
                    ?ko kl:hasPerson kl:Peter .
                    ?ko kl:hasMagnitude ?extent
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);

        System.out.println(ResultSetFormatter.asText(results,prologue));
    }


    @Test
    public void getKnowledgeAssetFeaturesQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                SELECT ?feature
                WHERE {
                    ?feature rdfs:subClassOf kl:KnowledgeAssetFeature .
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }


    @Test
    public void getValuesForKnowledgeAssetQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                SELECT ?value
                WHERE {
                    ?value rdfs:subClassOf kl:Age .
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }


    @Test
    public void getKnowledgeAssetFeatureQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                SELECT ?ka
                WHERE {
                    ?ka a kl:KnowledgeAsset .
                    ?ka kl:hasCategory ?technicalValue .
                    ?ka kl:hasAge ?ageValue .
                    ?technicalValue a kl:TechnicalCategoryValue .
                    ?ageValue a kl:OldAgeValue .
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }

    @Test
    public void getExhaustiveKnowledgeAssetFeatureQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                PREFIX owl: <http://www.w3.org/2002/07/owl#>
                SELECT ?ka ?feature ?value
                WHERE {
                    ?ka a kl:KnowledgeAsset .
                    ?feature rdfs:subClassOf kl:KnowledgeAssetFeature .
                    OPTIONAL {
                        ?featureProperty rdfs:domain kl:KnowledgeAsset .
                        ?featureProperty rdfs:range ?feature .
                        ?ka ?featureProperty ?featureValAssignment .
                        ?featureVal rdfs:subClassOf ?feature .
                        ?featureValAssignment a ?featureVal .
                    }
                    BIND(COALESCE(?featureVal,"Undefined") AS ?value)
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }


    @Test
    public void teamMembersExtentQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                PREFIX owl: <http://www.w3.org/2002/07/owl#>
                SELECT ?team ?member ?ka ?extent
                WHERE {
                    ?team rdfs:subClassOf kl:Person .
                    ?member a ?team .
                    ?ko kl:hasPerson ?member .
                    ?ko kl:hasKnowledgeAsset ?ka .
                    ?ko kl:hasMagnitude ?extent .
                    FILTER(?team=kl:Managers)
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }


    @Test
    public void teamMembersExtentExampleFormulaQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                PREFIX owl: <http://www.w3.org/2002/07/owl#>
                SELECT ?team ?member ?ka ?extent
                WHERE {
                    ?team rdfs:subClassOf kl:Person .
                    ?ka a kl:KnowledgeAsset
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }


    @Test
    public void requirementKLQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                SELECT ?ka
                WHERE {
                    ?ka a kl:KnowledgeAsset .
                    ?ka1 kl:dependsOn ?ka .s
                    FILTER (?ka1 = kl:Java)
                }
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }


    @Test
    public void embeddednessQuery(){
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?ka (COUNT(?ka2) AS ?embeddedness)\n" +
                "WHERE {\n" +
                "    ?ka a kl:KnowledgeAsset .\n" +
                "    OPTIONAL { ?ka kl:dependsOn ?ka2 . }\n" +
                "}\n" +
                "GROUP BY ?ka\n" +
                "ORDER BY DESC(?embeddedness)\n";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }

    @Test
    public void knowledgeDistance() {
        String queryString = "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "SELECT ?perSrc ?perRec ?ka (SUM(IF(COALESCE(?perSrcMag, 0) - COALESCE(?perRecMag, 0) < 0, 0, COALESCE(?perSrcMag, 0) - COALESCE(?perRecMag, 0))) AS ?knowledgeDistance)\n" +
                "WHERE {\n" +
                "    ?perSrc a kl:Person .\n" +
                "    ?perRec a kl:Person .\n" +
                "    ?ka a kl:KnowledgeAsset .\n" +
                "    OPTIONAL {\n" +
                "        ?ko kl:hasKnowledgeAsset ?ka .\n" +
                "        ?ko kl:hasPerson ?perSrc .\n" +
                "        ?ko kl:hasMagnitude ?perSrcMag .\n" +
                "    }\n" +
                "    OPTIONAL {\n" +
                "        ?ko kl:hasKnowledgeAsset ?ka .\n" +
                "        ?ko kl:hasPerson ?perRec .\n" +
                "        ?ko kl:hasMagnitude ?perRecMag .\n" +
                "    }\n" +
                "    FILTER (?perSrc != ?perRec)\n" +
                "}\n" +
                "GROUP BY ?perSrc ?perRec ?ka\n" +
                "ORDER BY DESC(?knowledgeDistance)\n";
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results,prologue));
    }

    @Test
    public void knowledgeDistanceMatrix() {
        String queryString =
                "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                        "SELECT ?person ?ka (IF(COALESCE(?mag,0)=0,MAX(?genericMag)+1,MAX(?genericMag)/?mag) AS ?knowledgeMatrix)\n" +
                        "WHERE {\n" +
                        "    ?person a kl:Person .\n" +
                        "    ?ka a kl:KnowledgeAsset .\n" +
                        "    OPTIONAL {\n" +
                        "        ?ko kl:hasKnowledgeAsset ?ka .\n" + // Ensure this matches your intended variable linking
                        "        ?ko kl:hasPerson ?person .\n" + // Ensure this matches your intended variable linking
                        "        ?ko kl:hasMagnitude ?mag .\n" +
                        "    }\n" +
                        "    OPTIONAL {\n" +
                        "        ?genericKo kl:hasKnowledgeAsset ?genericKA .\n" + // Ensure this matches your intended variable linking
                        "        ?genericKo kl:hasPerson ?genericPerson .\n" + // Ensure this matches your intended variable linking
                        "        ?genericKo kl:hasMagnitude ?genericMag .\n" +
                        "    }\n" +
                        "}\n" +
                        "GROUP BY ?person ?ka ?mag \n"; // Proper placement outside the WHERE block

        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results));
    }


    @Test
    public void knowledgeDistanceMatrixKnowledgeDistance() {
        String matrixQuery =
                        "SELECT ?person ?ka (IF(COALESCE(?mag,0)=0,MAX(?genericMag)+1,MAX(?genericMag)/?mag) AS ?knowledgeMatrix)\n" +
                        "WHERE {\n" +
                        "    ?person a kl:Person .\n" +
                        "    ?ka a kl:KnowledgeAsset .\n" +
                        "    OPTIONAL {\n" +
                        "        ?ko kl:hasKnowledgeAsset ?ka .\n" + // Ensure this matches your intended variable linking
                        "        ?ko kl:hasPerson ?person .\n" + // Ensure this matches your intended variable linking
                        "        ?ko kl:hasMagnitude ?mag .\n" +
                        "    }\n" +
                        "    OPTIONAL {\n" +
                        "        ?genericKo kl:hasKnowledgeAsset ?genericKA .\n" + // Ensure this matches your intended variable linking
                        "        ?genericKo kl:hasPerson ?genericPerson .\n" + // Ensure this matches your intended variable linking
                        "        ?genericKo kl:hasMagnitude ?genericMag .\n" +
                        "    }\n" +
                        "}\n" +
                        "GROUP BY ?person ?ka ?mag \n"; // Proper placement outside the WHERE block
        String queryString =
                "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                        "SELECT (SUM(?knowledgeMatrix) AS ?knowledgeDistance) WHERE \n" +
                        "{\n" +
                        matrixQuery+
                        "\n}"; // Proper placement outside the WHERE block

        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results));
    }


    @Test
    public void knowledgeInDistance() {
        String queryString =
                "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                        "SELECT ?ka (SUM(?knowledgeMatrix) AS ?knowledgeInDistance)\n" +
                        "WHERE { SELECT ?person ?ka (IF(COALESCE(?mag,0)=0,MAX(?genericMag)+1,MAX(?genericMag)/?mag) AS ?knowledgeMatrix)\n" +
                        "WHERE {\n" +
                        "    ?person a kl:Person .\n" +
                        "    ?ka a kl:KnowledgeAsset .\n" +
                        "    OPTIONAL {\n" +
                        "        ?ko kl:hasKnowledgeAsset ?ka .\n" +
                        "        ?ko kl:hasPerson ?person .\n" +
                        "        ?ko kl:hasMagnitude ?mag .\n" +
                        "    }\n" +
                        "    OPTIONAL {\n" +
                        "        ?genericKo kl:hasKnowledgeAsset ?genericKA .\n" +
                        "        ?genericKo kl:hasPerson ?genericPerson .\n" +
                        "        ?genericKo kl:hasMagnitude ?genericMag .\n" +
                        "    }\n" +
                        "}\n" +
                        "GROUP BY ?person ?ka ?mag \n}" +
                        "GROUP BY ?ka"; // Proper placement outside the WHERE block

        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results));
    }


    @Test
    public void knowledgeMobilityRisk() {
        String queryString =

                "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                """
                SELECT ?ka ?kmr
                WHERE {
                  {
                    SELECT ?ka (SUM(?knowledgeMatrix) AS ?knowledgeInDistance)
                    WHERE {
                        # Calculate Knowledge In Distance Here
                        SELECT ?person ?ka (IF(COALESCE(?mag,0)=0,MAX(?genericMag)+1,MAX(?genericMag)/?mag) AS ?knowledgeMatrix)
                        WHERE {
                            ?person a kl:Person .
                            ?ka a kl:KnowledgeAsset .
                            OPTIONAL {
                                ?ko kl:hasKnowledgeAsset ?ka .
                                ?ko kl:hasPerson ?person .
                                ?ko kl:hasMagnitude ?mag .
                            }
                            OPTIONAL {
                                ?genericKo kl:hasKnowledgeAsset ?genericKA .
                                ?genericKo kl:hasPerson ?genericPerson .
                                ?genericKo kl:hasMagnitude ?genericMag .
                            }
                        }
                        GROUP BY ?person ?ka ?mag\s
                    }
                    GROUP BY ?ka
                  }
                  {
                    # Calculate Knowledge Distance Here
                    SELECT (SUM(?knowledgeMatrix) AS ?knowledgeDistance)
                    WHERE {
                        {
                            SELECT ?person ?ka (IF(COALESCE(?mag, 0) = 0, MAX(?genericMag) + 1, MAX(?genericMag) / ?mag) AS ?knowledgeMatrix)
                            WHERE {
                                ?person a kl:Person .
                                ?ka a kl:KnowledgeAsset .
                                 OPTIONAL {
                                    ?ko kl:hasKnowledgeAsset ?ka .
                                    ?ko kl:hasPerson ?person .
                                    ?ko kl:hasMagnitude ?mag .
                                }
                                OPTIONAL {
                                    ?genericKo kl:hasKnowledgeAsset ?genericKA .
                                    ?genericKo kl:hasPerson ?genericPerson .
                                    ?genericKo kl:hasMagnitude ?genericMag .
                                }
                                }
                            GROUP BY ?person ?ka ?mag
                            }    \s
                    }
                  }
                  BIND( ?knowledgeDistance/?knowledgeInDistance AS ?kmr)
                }
                GROUP BY ?ka ?kmr
                ORDER BY DESC(?kmr)
                """;
        ResultSetRewindable results = queryEngine.queryExecutor(queryString);
        System.out.println(ResultSetFormatter.asText(results));
    }

    @Test
    public void lowHighMediumknowledgeMobilityRisk() {
        String kmrValueQuery =
                """
                         SELECT ?ka ?kmr
                        WHERE {
                          {
                            SELECT ?ka (SUM(?knowledgeMatrix) AS ?knowledgeInDistance)
                            WHERE {
                                # Calculate Knowledge In Distance Here
                                SELECT ?person ?ka (IF(COALESCE(?mag,0)=0,MAX(?genericMag)+1,MAX(?genericMag)/?mag) AS ?knowledgeMatrix)
                                WHERE {
                                    ?person a kl:Person .
                                    ?ka a kl:KnowledgeAsset .
                                    OPTIONAL {
                                        ?ko kl:hasKnowledgeAsset ?ka .
                                        ?ko kl:hasPerson ?person .
                                        ?ko kl:hasMagnitude ?mag .
                                    }
                                    OPTIONAL {
                                        ?genericKo kl:hasKnowledgeAsset ?genericKA .
                                        ?genericKo kl:hasPerson ?genericPerson .
                                        ?genericKo kl:hasMagnitude ?genericMag .
                                    }
                                }
                                GROUP BY ?person ?ka ?mag\s
                            }
                            GROUP BY ?ka
                          }
                          {
                            # Calculate Knowledge Distance Here
                            SELECT (SUM(?knowledgeMatrix) AS ?knowledgeDistance)
                            WHERE {
                                {
                                    SELECT ?person ?ka (IF(COALESCE(?mag, 0) = 0, MAX(?genericMag) + 1, MAX(?genericMag) / ?mag) AS ?knowledgeMatrix)
                                    WHERE {
                                        ?person a kl:Person .
                                        ?ka a kl:KnowledgeAsset .
                                         OPTIONAL {
                                            ?ko kl:hasKnowledgeAsset ?ka .
                                            ?ko kl:hasPerson ?person .
                                            ?ko kl:hasMagnitude ?mag .
                                        }
                                        OPTIONAL {
                                            ?genericKo kl:hasKnowledgeAsset ?genericKA .
                                            ?genericKo kl:hasPerson ?genericPerson .
                                            ?genericKo kl:hasMagnitude ?genericMag .
                                        }
                                        }
                                    GROUP BY ?person ?ka ?mag
                                    }    \s
                            }
                          }
                          BIND( ?knowledgeDistance/?knowledgeInDistance AS ?kmr)
                        }
                        GROUP BY ?ka ?kmr
                        """;

        String queryString =
                "PREFIX kl: <" + DEFAULT_NAMESPACE.getValue(String.class) + ">\n" +
                "PREFIX afn: <http://jena.apache.org/ARQ/function#>\n" +
                        "SELECT ?ka ?kmr ?sdev ?mean ?kmrCat WHERE {\n" +
                        "{\n" +
                        kmrValueQuery +
                        "}\n" +
                        "{\n" +
                        "SELECT ((SUM((?kmr - ?mean)*(?kmr - ?mean)))/(COUNT(?kmr)) AS ?variance) ?mean WHERE {\n" +
                            "{\n" +
                                kmrValueQuery +
                            "}\n" +
                            "{\n" +
                                "SELECT (AVG(?kmr) AS ?mean) WHERE {\n" +
                                    kmrValueQuery+
                                "}\n" +
                            "}\n" +
                        "}\n" +
                        "GROUP BY ?mean \n" +
                        "}\n" +
                        "BIND(afn:sqrt(?variance) AS ?sdev)\n"+
                        "BIND (\n" +
                        "  COALESCE(\n" +
                        "    IF( ?kmr < ?mean && ?kmr <= (?mean+?sdev), \"MEDIUM\", 1/0),\n" +
                        "    IF(?kmr <= ?mean, \"LOW\", 1/0),\n" +
                        "    IF(?kmr > (?mean+?sdev), \"HIGH\", 1/0),\n" +
                        "    \"Undefined\"\n" +
                        "  ) AS ?kmrCat\n" +
                        ")\n"+
                        "}\n" ;

                        // 

                    ResultSetRewindable results = queryEngine.queryExecutor(queryString);
                    System.out.println(ResultSetFormatter.asText(results));
    }


}
