package com.andimon.rdfknowledgelandscape.constructionmethods;

import com.andimon.rdfknowledgelandscape.features.*;
import com.andimon.rdfknowledgelandscape.ontology.OntoKL;
import com.andimon.rdfknowledgelandscape.testscenario.Age;
import com.andimon.rdfknowledgelandscape.updater.NullUpdater;
import com.github.owlcs.ontapi.jena.vocabulary.RDF;
import org.apache.http.util.Asserts;
import org.apache.jena.assembler.Mode;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.StmtIteratorImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;

public class KnowledgeLandscapeConstructorTest {
    String klNamespace = DEFAULT_NAMESPACE.getValue(String.class);
    KnowledgeLandscapeConstructor knowledgeLandscapeConstructor;

    @BeforeEach
    public void setup() throws Exception {
        knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor();
    }

    @Test
    public void personIdentificationTest() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        knowledgeLandscapeConstructor.personIdentification("Andre");
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource peronName = model.createResource(klNamespace + "Andre");
        Property a = RDF.type;
        Resource className = model.createResource(klNamespace + "Person");
        Assertions.assertTrue(model.contains(peronName, a, className));
    }


    @Test
    public void removePersonTest() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        knowledgeLandscapeConstructor.personIdentification("Andre");
        knowledgeLandscapeConstructor.removePerson("Andre");
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource peronName = model.createResource(klNamespace + "Andre");
        Property a = RDF.type;
        Resource className = model.createResource(klNamespace + "Person");
        //removing person
        Assertions.assertFalse(model.contains(peronName, a, className));
    }

    @Test
    public void removePersonPersonDoesNotExistTest() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        boolean personCreated = knowledgeLandscapeConstructor.personIdentification("Andre");
        boolean personDeleted = knowledgeLandscapeConstructor.removePerson("Andre");
        boolean personDoesNotExist = knowledgeLandscapeConstructor.removePerson("Andre");
        Assertions.assertAll(
                () -> Assertions.assertTrue(personCreated),
                () -> Assertions.assertTrue(personDeleted),
                () -> Assertions.assertFalse(personDoesNotExist) // returns false indicating that no change occurs
        );
    }


    @Test
    public void k1IdentificationTest() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException, KnowledgeGraphConstructorException {
        boolean expected = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT, Category.TECHNICAL));
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource k1 = model.createResource(klNamespace + "k1");
        Property a = RDF.type;
        Resource knowledgeAsset = model.createResource(klNamespace + "KnowledgeAsset");
        Property hasCategory = model.createProperty(klNamespace + "hasCategory");
        Property hasVisibility = model.createProperty(klNamespace + "hasVisibility");
        Resource categoryValue = model.createResource(klNamespace + "k1CategoryAssignment");
        Resource visibilityValue = model.createResource(klNamespace + "k1VisibilityAssignment");
        Resource tacitValue = model.createResource(klNamespace + "TacitVisibilityValue");
        Resource technicalValue = model.createResource(klNamespace + "TechnicalCategoryValue");
        Assertions.assertAll(
                () -> Assertions.assertTrue(model.contains(k1, a, knowledgeAsset)),
                () -> Assertions.assertTrue(model.contains(k1, hasVisibility, visibilityValue)),
                () -> Assertions.assertTrue(model.contains(k1, hasCategory, categoryValue)),
                () -> Assertions.assertTrue(model.contains(k1, hasVisibility, visibilityValue)),
                () -> Assertions.assertTrue(model.contains(k1, hasCategory, categoryValue)),
                () -> Assertions.assertTrue(model.contains(visibilityValue, a, tacitValue)),
                () -> Assertions.assertTrue(model.contains(categoryValue, a, technicalValue)),
                () -> Assertions.assertTrue(expected)
        );
    }

    @Test
    public void k1IdentificationFeatureAlreadyExists() throws Exception {
        boolean k1Created = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean k1AlreadyExists = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Category.UNDEFINED, Visibility.TACIT));
        Assertions.assertAll(
                () -> Assertions.assertTrue(k1Created),
                () -> Assertions.assertFalse(k1AlreadyExists)

        );
    }


    @Test
    public void k1IdentificationWithAFeatureAndANonDefinedValue() throws Exception {
        OntoKL ontoKL = new OntoKL();
        ontoKL.addFeature("Age", Set.of("Old"));
        knowledgeLandscapeConstructor = new KnowledgeLandscapeConstructor(ontoKL); //create knowledge asset with
        boolean expected = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT, Age.ESTABLISHED));
        Assertions.assertFalse(expected); //false indicating that no knowledge asset has been added
    }


    @Test
    public void k1IdentificationWithANonDeclaredFeatureTest() throws KnowledgeGraphConstructorException {
        boolean expected = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT, Age.ESTABLISHED));
        Assertions.assertFalse(expected); //false indicating that no knowledge asset has been added
    }

    @Test
    public void removeKnowledgeAssetTest() throws KnowledgeGraphConstructorException, OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        boolean knowledgeAssetCreated = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean person = knowledgeLandscapeConstructor.knowledgeAssetIdentification("person", Set.of(Visibility.TACIT));
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource knowledgeAssetName = model.createResource(klNamespace + "k1");
        Property a = RDF.type;
        Resource className = model.createResource(klNamespace + "KnowledgeAsset");


        boolean expectedTriplesInGraph = model.contains(knowledgeAssetName, a, className);
        boolean knowledgeAssetRemoved = knowledgeLandscapeConstructor.removeKnowledgeAsset("k1");
        model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        boolean triplesRemoved = !model.contains(knowledgeAssetName, a, className);
        Assertions.assertAll(
                //check successful knowledge asset removal
                () -> Assertions.assertTrue(knowledgeAssetCreated),
                //check expected triples are in graph
                () -> Assertions.assertTrue(expectedTriplesInGraph),
                //check successful removal
                () -> Assertions.assertTrue(knowledgeAssetRemoved),
                //check the expected triples are removed from graph
                () -> Assertions.assertTrue(triplesRemoved)
        );
    }


    @Test
    public void removeKnowledgeAssetKnowledgeAssetDoesNotExistTest() throws KnowledgeGraphConstructorException, OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        boolean knowledgeAssetCreated = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean removeKnowledgeAsset = knowledgeLandscapeConstructor.removeKnowledgeAsset("k1");
        boolean knowledgeAssetAlreadyRemoved = knowledgeLandscapeConstructor.removeKnowledgeAsset("k1");
        Assertions.assertAll(
                () -> Assertions.assertTrue(knowledgeAssetCreated),
                () -> Assertions.assertTrue(removeKnowledgeAsset),
                () -> Assertions.assertFalse(knowledgeAssetAlreadyRemoved)

        );
    }


    @Test
    public void removeKnowledgeAssetRemovesCorrespondingFeaturesTest() throws KnowledgeGraphConstructorException, OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        boolean knowledgeAssetCreated = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource knowledgeAssetName = model.createResource(klNamespace + "k1");
        Property a = RDF.type;
        Property hasVisibility = model.createProperty(klNamespace + "hasVisibility");
        Property value = model.createProperty(klNamespace + "k1VisibilityAssignment");
        Resource tacitValue = model.createResource(klNamespace + "TacitVisibilityValue");
        boolean expectedTriplesInGraph = model.contains(knowledgeAssetName, hasVisibility, value) && model.contains(value, a, tacitValue);
        boolean knowledgeAssetRemoved = knowledgeLandscapeConstructor.removeKnowledgeAsset("k1");
        model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        boolean triplesRemoved = !(model.contains(knowledgeAssetName, hasVisibility, value) && model.contains(value, a, tacitValue));
        Assertions.assertAll(
                //check successful knowledge asset removal
                () -> Assertions.assertTrue(knowledgeAssetCreated),
                //check expected triples are in graph
                () -> Assertions.assertTrue(expectedTriplesInGraph),
                //check successful removal
                () -> Assertions.assertTrue(knowledgeAssetRemoved),
                //check the expected triples are removed from graph
                () -> Assertions.assertTrue(triplesRemoved)
        );
    }

    @Test
    public void removeKnowledgeAssetRemovesRespectiveKnowledgeObservationsTest() throws Exception {
        boolean knowledgeAssetCreated = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean personCreated = knowledgeLandscapeConstructor.personIdentification("Andre");
        boolean knowledgeObservation = knowledgeLandscapeConstructor.knowledgeObservation("Andre", "k1", 1);
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource observation = model.createResource(klNamespace + "Andrek1Observation");
        Resource k1 = model.createResource(klNamespace + "k1");
        Property hasPerson = model.createProperty(klNamespace + "hasPerson");
        Property hasMagnitude = model.createProperty(klNamespace + "hasMagnitude");
        Property hasKnowledgeAsset = model.createProperty(klNamespace + "hasKnowledgeAsset");
        Property person = model.createProperty(klNamespace + "Andre");
        Literal magnitude = ResourceFactory.createTypedLiteral("1", XSDDatatype.XSDnonNegativeInteger);
        boolean expectedTriplesInGraph = model.contains(observation, hasPerson, person) && model.contains(observation, hasKnowledgeAsset, k1) && model.contains(observation, hasMagnitude, magnitude);
        boolean knowledgeAssetRemoved = knowledgeLandscapeConstructor.removeKnowledgeAsset("k1");
        model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        boolean triplesRemoved = !(model.contains(observation, hasPerson, person) && model.contains(observation, hasKnowledgeAsset, k1) && model.contains(observation, hasMagnitude, magnitude));
        Assertions.assertAll(
                //check successful knowledge asset removal
                () -> Assertions.assertTrue(knowledgeAssetCreated),
                //check expected triples are in graph
                () -> Assertions.assertTrue(personCreated),
                //check successful removal
                () -> Assertions.assertTrue(knowledgeObservation),
                () -> Assertions.assertTrue(expectedTriplesInGraph),
                () -> Assertions.assertTrue(knowledgeAssetRemoved),
                () -> Assertions.assertTrue(triplesRemoved)

        );
    }

    @Test
    public void knowledgeObservationTest() throws Exception {
        boolean knowledgeAssetCreated = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean personCreated = knowledgeLandscapeConstructor.personIdentification("Andre");
        boolean knowledgeObservation = knowledgeLandscapeConstructor.knowledgeObservation("Andre", "k1", 1);
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource observation = model.createResource(klNamespace + "Andrek1Observation");
        Resource k1 = model.createResource(klNamespace + "k1");
        Property hasPerson = model.createProperty(klNamespace + "hasPerson");
        Property hasMagnitude = model.createProperty(klNamespace + "hasMagnitude");
        Property hasKnowledgeAsset = model.createProperty(klNamespace + "hasKnowledgeAsset");
        Property person = model.createProperty(klNamespace + "Andre");
        Literal magnitude = ResourceFactory.createTypedLiteral("1", XSDDatatype.XSDnonNegativeInteger);
        boolean expectedTriplesInGraph = model.contains(observation, hasPerson, person) && model.contains(observation, hasKnowledgeAsset, k1) && model.contains(observation, hasMagnitude, magnitude);
        Assertions.assertAll(
                () -> Assertions.assertTrue(knowledgeAssetCreated),
                () -> Assertions.assertTrue(personCreated),
                () -> Assertions.assertTrue(knowledgeObservation),
                () -> Assertions.assertTrue(expectedTriplesInGraph)
        );
    }

    @Test
    public void knowledgeObservationPersonDoesNotExistTest() throws Exception {
        boolean knowledgeAssetCreated = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean personCreated = knowledgeLandscapeConstructor.personIdentification("Andre");
        boolean personRemoved = knowledgeLandscapeConstructor.removePerson("Andre");
        boolean knowledgeObservation = knowledgeLandscapeConstructor.knowledgeObservation("Andre", "k1", 1);
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource observation = model.createResource(klNamespace + "Andrek1Observation");
        Resource k1 = model.createResource(klNamespace + "k1");
        Property hasPerson = model.createProperty(klNamespace + "hasPerson");
        Property hasMagnitude = model.createProperty(klNamespace + "hasMagnitude");
        Property hasKnowledgeAsset = model.createProperty(klNamespace + "hasKnowledgeAsset");
        Property person = model.createProperty(klNamespace + "Andre");
        Literal magnitude = ResourceFactory.createTypedLiteral("1", XSDDatatype.XSDnonNegativeInteger);
        boolean expectedTriplesInGraph = model.contains(observation, hasPerson, person) && model.contains(observation, hasKnowledgeAsset, k1) && model.contains(observation, hasMagnitude, magnitude);
        Assertions.assertAll(
                () -> Assertions.assertTrue(knowledgeAssetCreated),
                () -> Assertions.assertTrue(personCreated),
                () -> Assertions.assertTrue(personRemoved),
                () -> Assertions.assertFalse(knowledgeObservation),
                () -> Assertions.assertFalse(expectedTriplesInGraph)
        );
    }

    @Test
    public void knowledgeObservationKnowledgeAssetDoesNotExistTest() throws Exception {
        boolean knowledgeAssetCreated = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean personCreated = knowledgeLandscapeConstructor.personIdentification("Andre");
        boolean knowledgeAssetRemoved = knowledgeLandscapeConstructor.removeKnowledgeAsset("k1");
        boolean knowledgeObservation = knowledgeLandscapeConstructor.knowledgeObservation("Andre", "k1", 1);
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource observation = model.createResource(klNamespace + "Andrek1Observation");
        Resource k1 = model.createResource(klNamespace + "k1");
        Property hasPerson = model.createProperty(klNamespace + "hasPerson");
        Property hasMagnitude = model.createProperty(klNamespace + "hasMagnitude");
        Property hasKnowledgeAsset = model.createProperty(klNamespace + "hasKnowledgeAsset");
        Property person = model.createProperty(klNamespace + "Andre");
        Literal magnitude = ResourceFactory.createTypedLiteral("1", XSDDatatype.XSDnonNegativeInteger);
        boolean expectedTriplesInGraph = model.contains(observation, hasPerson, person) && model.contains(observation, hasKnowledgeAsset, k1) && model.contains(observation, hasMagnitude, magnitude);
        Assertions.assertAll(
                () -> Assertions.assertTrue(knowledgeAssetCreated),
                () -> Assertions.assertTrue(personCreated),
                () -> Assertions.assertTrue(knowledgeAssetRemoved),
                () -> Assertions.assertFalse(knowledgeObservation),
                () -> Assertions.assertFalse(expectedTriplesInGraph)
        );
    }


    @Test
    public void knowledgeObservationMultipleObservationsWithSamePersonLeadsToAContradiction() throws Exception {
        boolean knowledgeAssetCreated = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean personCreated = knowledgeLandscapeConstructor.personIdentification("Andre");
        boolean knowledgeObservation = knowledgeLandscapeConstructor.knowledgeObservation("Andre", "k1", 1);
        boolean knowledgeObservation2 = knowledgeLandscapeConstructor.knowledgeObservation("Andre", "k1", 2);
        Model model = knowledgeLandscapeConstructor.getGraph(new NullUpdater());
        Resource observation = model.createResource(klNamespace + "Andrek1Observation");
        Property hasMagnitude = model.createProperty(klNamespace + "hasMagnitude");
        Literal magnitude = ResourceFactory.createTypedLiteral("2", XSDDatatype.XSDnonNegativeInteger);
        boolean tripleNotExpected = !model.contains(observation, hasMagnitude, magnitude);
        Assertions.assertAll(
                () -> Assertions.assertTrue(knowledgeAssetCreated),
                () -> Assertions.assertTrue(personCreated),
                () -> Assertions.assertTrue(knowledgeObservation),
                () -> Assertions.assertTrue(tripleNotExpected),
                () -> Assertions.assertFalse(knowledgeObservation2)
        );
    }


    @Test
    public void dependentAsymmetryContradiction() throws Exception {
        boolean ka1Creation = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT));
        boolean ka2Creation = knowledgeLandscapeConstructor.knowledgeAssetIdentification("k2", Set.of(Category.TECHNICAL));
        boolean goodRelation = knowledgeLandscapeConstructor.dependentOn("k1","k2");
        boolean badRelation = knowledgeLandscapeConstructor.dependentOn("k2","k1");
        Assertions.assertAll(
                () -> Assertions.assertTrue(ka1Creation),
                () -> Assertions.assertTrue(ka2Creation),
                () -> Assertions.assertTrue(goodRelation),
                () -> Assertions.assertFalse(badRelation)
        );
    }


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
        knowledgeLandscapeConstructor.knowledgeAssetIdentification("k1", Set.of(Visibility.TACIT, Category.TECHNICAL));
        knowledgeLandscapeConstructor.personIdentification("Andre");
        knowledgeLandscapeConstructor.knowledgeObservation("Andre", "k1", 8);
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


}
