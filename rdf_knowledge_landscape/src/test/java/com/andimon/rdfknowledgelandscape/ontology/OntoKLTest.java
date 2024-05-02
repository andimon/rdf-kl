package com.andimon.rdfknowledgelandscape.ontology;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.slf4j.IMarkerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class OntoKLTest {
    OntoKL ontoKL;
    OWLDataFactory dataFactory;
    String klNamespace;

    @BeforeEach
    public void setup() throws Exception {
        ontoKL = new OntoKL();
        dataFactory = ontoKL.getOntology().getOWLOntologyManager().getOWLDataFactory();
        klNamespace = DEFAULT_NAMESPACE.getValue(String.class);
    }

    @Test
    public void expectedAxioms() {
        Assertions.assertEquals(baseAxioms(), ontoKL.getOntology().getAxioms());
        ontoKL.getOntology().addAxioms(ontoKL.getOntology().getOWLOntologyManager().getOWLDataFactory().getOWLDeclarationAxiom(ontoKL.getOntology().getOWLOntologyManager().getOWLDataFactory().getOWLClass("k:WQ")));
        ontoKL.getOntology().addAxioms(ontoKL.getOntology().getOWLOntologyManager().getOWLDataFactory().getOWLClassAssertionAxiom(ontoKL.getOntology().getOWLOntologyManager().getOWLDataFactory().getOWLClass("k:WQ"),ontoKL.getOntology().getOWLOntologyManager().getOWLDataFactory().getOWLNamedIndividual("k:WSDQ")));
        for (OWLAxiom axiom : ontoKL.getOntology().getTBoxAxioms(Imports.INCLUDED)) {
            System.out.println(axiom);
        }

    }

    @Test
    public void addFeature() throws OntoKLException {
        boolean val = ontoKL.addFeature("Age", Set.of("New", "Established", "Old"));
        // Expected axioms
        Set<OWLAxiom> expectedAxioms = new HashSet<>();
        OWLClass knowledgeAssetFeatures = dataFactory.getOWLClass(klNamespace + "KnowledgeAssetFeature");
        OWLClass feature = dataFactory.getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + "Age");
        OWLObjectProperty featureProperty = dataFactory.getOWLObjectProperty(DEFAULT_NAMESPACE.getValue(String.class) + "hasAge");
        OWLClass oldValue = dataFactory.getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + "OldAgeValue");
        OWLClass establishedValue = dataFactory.getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + "EstablishedAgeValue");
        OWLClass newValue = dataFactory.getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + "NewAgeValue");
        Set<OWLClass> featureValues = Set.of(oldValue, establishedValue, newValue);
        // declaration for feature
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(feature));
        // declaration for values
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(oldValue));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(establishedValue));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(newValue));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(newValue));
        // declaration for object property
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(featureProperty));
        // object property set to functional
        expectedAxioms.add(dataFactory.getOWLFunctionalObjectPropertyAxiom(featureProperty));
        // feature clas set to disjoint union of classes
        expectedAxioms.add(dataFactory.getOWLDisjointUnionAxiom(feature, featureValues));
        // feature class subset of knowledge asset feature
        expectedAxioms.add(dataFactory.getOWLSubClassOfAxiom(feature, knowledgeAssetFeatures));
        Assertions.assertAll(
                () -> Assertions.assertTrue(ontoKL.getOntology().getAxioms().containsAll(expectedAxioms)),
                () -> Assertions.assertTrue(ontoKL.getOntology().getAxioms().containsAll(baseAxioms())),
                () -> Assertions.assertTrue(val)
        );
    }


    @Test
    public void addFeatureAlreadyExists() throws OntoKLException {
        Exception exception = assertThrows(OntoKLException.class, () -> {
            ontoKL.addFeature("Visibility", Set.of("New", "Established", "Old"));
        });
        //feature Visibility is default, already exists
        String actualMessage = exception.getMessage();
        String expectedMessage = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Visibility";
        System.out.println(actualMessage);
        Assertions.assertAll(
                () -> Assertions.assertEquals(baseAxioms(), ontoKL.getOntology().getAxioms()),
                () -> Assertions.assertTrue(actualMessage.contains(expectedMessage))
        );
    }

    @Test
    public void addValueToFeature() throws OntoKLException {
        ontoKL.addValue("Category", "Education");
        OWLClass category = dataFactory.getOWLClass(klNamespace + "Category");
        OWLClass generalCategory = dataFactory.getOWLClass(klNamespace + "GeneralCategoryValue");
        OWLClass businessCategory = dataFactory.getOWLClass(klNamespace + "BusinessCategoryValue");
        OWLClass technicalCategory = dataFactory.getOWLClass(klNamespace + "TechnicalCategoryValue");
        OWLClass newCategory = dataFactory.getOWLClass(klNamespace + "EducationCategoryValue");
        Set<OWLAxiom> expectedAxioms = baseAxioms();
        expectedAxioms.remove(dataFactory.getOWLDisjointUnionAxiom(category, Set.of(generalCategory, businessCategory, technicalCategory)));
        expectedAxioms.add(dataFactory.getOWLDisjointUnionAxiom(category, Set.of(generalCategory, businessCategory, technicalCategory, newCategory)));
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(baseAxioms(), ontoKL.getOntology().getAxioms()),
                () -> Assertions.assertEquals(expectedAxioms, ontoKL.getOntology().getAxioms())
        );
        boolean val = ontoKL.addFeature("Age", Set.of("New", "Established", "Old"));
    }


    @Test
    public void addValueToFeatureDoesNotExist() throws OntoKLException {
        Exception exception = assertThrows(OntoKLException.class, () -> {
            ontoKL.addValue("NonExistentFeature", "Education");
        });
        String actualMessage = exception.getMessage();
        String expectedMessage = "Feature https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#NonExistentFeature does not exists.";
        Assertions.assertAll(
                () -> Assertions.assertEquals(baseAxioms(), ontoKL.getOntology().getAxioms()),
                () -> Assertions.assertTrue(actualMessage.contains(expectedMessage))
        );
    }

    public Set<OWLAxiom> baseAxioms() {
        Set<OWLAxiom> expectedAxioms = new HashSet<>();
        OWLDataFactory dataFactory = ontoKL.getOntology().getOWLOntologyManager().getOWLDataFactory(); // get a datafactory
        OWLClass persons = dataFactory.getOWLClass(klNamespace + "Person");
        OWLClass knowledgeAssets = dataFactory.getOWLClass(klNamespace + "KnowledgeAsset");
        OWLClass knowledgeAssetFeatures = dataFactory.getOWLClass(klNamespace + "KnowledgeAssetFeature");
        OWLClass knowledgeObservation = dataFactory.getOWLClass(klNamespace + "KnowledgeObservation");
        OWLClass category = dataFactory.getOWLClass(klNamespace + "Category");
        OWLClass generalCategory = dataFactory.getOWLClass(klNamespace + "GeneralCategoryValue");
        OWLClass businessCategory = dataFactory.getOWLClass(klNamespace + "BusinessCategoryValue");
        OWLClass technicalCategory = dataFactory.getOWLClass(klNamespace + "TechnicalCategoryValue");
        OWLClass visbility = dataFactory.getOWLClass(klNamespace + "Visibility");
        OWLClass tacitVisibility = dataFactory.getOWLClass(klNamespace + "TacitVisibilityValue");
        OWLClass explicitVisibility = dataFactory.getOWLClass(klNamespace + "ExplicitVisibilityValue");
        OWLClass sociality = dataFactory.getOWLClass(klNamespace + "Sociality");
        OWLClass individualSociality = dataFactory.getOWLClass(klNamespace + "IndividualSocialityValue");
        OWLClass socialSociality = dataFactory.getOWLClass(klNamespace + "SocialSocialityValue");
        OWLClass operationality = dataFactory.getOWLClass(klNamespace + "Operationality");
        OWLClass proceduralOperationality = dataFactory.getOWLClass(klNamespace + "ProceduralOperationalityValue");
        OWLClass causalOperationality = dataFactory.getOWLClass(klNamespace + "CausalOperationalityValue");
        OWLClass declarativeOperationality = dataFactory.getOWLClass(klNamespace + "DeclarativeOperationalityValue");
        OWLClass conditionalOperationality = dataFactory.getOWLClass(klNamespace + "ConditionalOperationalityValue");
        OWLClass relationalOperationality = dataFactory.getOWLClass(klNamespace + "RelationalOperationalityValue");
        OWLObjectProperty relatedTo = dataFactory.getOWLObjectProperty(klNamespace + "relatedTo");
        OWLObjectProperty dependsOn = dataFactory.getOWLObjectProperty(klNamespace + "dependsOn");
        OWLObjectProperty composedOf = dataFactory.getOWLObjectProperty(klNamespace + "composedOf");
        OWLObjectProperty hasCategory = dataFactory.getOWLObjectProperty(klNamespace + "hasCategory");
        OWLObjectProperty hasVisibility = dataFactory.getOWLObjectProperty(klNamespace + "hasVisibility");
        OWLObjectProperty hasOperationality = dataFactory.getOWLObjectProperty(klNamespace + "hasOperationality");
        OWLObjectProperty hasSociality = dataFactory.getOWLObjectProperty(klNamespace + "hasSociality");
        OWLObjectProperty hasPerson = dataFactory.getOWLObjectProperty(klNamespace + "hasPerson");
        OWLObjectProperty hasKnowledgeAsset = dataFactory.getOWLObjectProperty(klNamespace + "hasKnowledgeAsset");
        OWLDataProperty hasMagnitude = dataFactory.getOWLDataProperty(klNamespace + "hasMagnitude");
        // declared classes
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(persons));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(knowledgeAssetFeatures));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(knowledgeObservation));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(category));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(generalCategory));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(businessCategory));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(technicalCategory));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(visbility));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(tacitVisibility));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(explicitVisibility));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(sociality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(individualSociality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(socialSociality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(operationality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(proceduralOperationality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(declarativeOperationality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(causalOperationality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(conditionalOperationality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(relationalOperationality));
        // declared object properties
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(relatedTo));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(dependsOn));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(composedOf));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(hasCategory));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(hasVisibility));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(hasOperationality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(hasSociality));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(hasPerson));
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(hasKnowledgeAsset));
        // declared data property
        expectedAxioms.add(dataFactory.getOWLDeclarationAxiom(hasMagnitude));
        // domain axioms
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(dependsOn, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(composedOf, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(relatedTo, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(hasCategory, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(hasVisibility, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(hasSociality, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(hasOperationality, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(hasKnowledgeAsset, knowledgeObservation));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyDomainAxiom(hasPerson, knowledgeObservation));
        expectedAxioms.add(dataFactory.getOWLDataPropertyDomainAxiom(hasMagnitude, knowledgeObservation));
        // range axioms
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(dependsOn, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(composedOf, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(relatedTo, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(hasCategory, category));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(hasVisibility, visbility));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(hasSociality, sociality));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(hasOperationality, operationality));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(hasKnowledgeAsset, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLObjectPropertyRangeAxiom(hasPerson, persons));
        expectedAxioms.add(dataFactory.getOWLDataPropertyRangeAxiom(hasMagnitude, OWL2Datatype.XSD_NON_NEGATIVE_INTEGER));
        // structural properties
        expectedAxioms.add(dataFactory.getOWLSymmetricObjectPropertyAxiom(relatedTo));
        expectedAxioms.add(dataFactory.getOWLAsymmetricObjectPropertyAxiom(dependsOn));
        expectedAxioms.add(dataFactory.getOWLAsymmetricObjectPropertyAxiom(composedOf));
        expectedAxioms.add(dataFactory.getOWLSubObjectPropertyOfAxiom(composedOf, dependsOn));
        expectedAxioms.add(dataFactory.getOWLSubObjectPropertyOfAxiom(dependsOn, relatedTo));
        expectedAxioms.add(dataFactory.getOWLSubObjectPropertyOfAxiom(composedOf, relatedTo));
        expectedAxioms.add(dataFactory.getOWLFunctionalObjectPropertyAxiom(hasCategory));
        expectedAxioms.add(dataFactory.getOWLFunctionalObjectPropertyAxiom(hasVisibility));
        expectedAxioms.add(dataFactory.getOWLFunctionalObjectPropertyAxiom(hasSociality));
        expectedAxioms.add(dataFactory.getOWLFunctionalObjectPropertyAxiom(hasOperationality));

        expectedAxioms.add(dataFactory.getOWLSubClassOfAxiom(visbility, knowledgeAssetFeatures));
        expectedAxioms.add(dataFactory.getOWLSubClassOfAxiom(category, knowledgeAssetFeatures));
        expectedAxioms.add(dataFactory.getOWLSubClassOfAxiom(sociality, knowledgeAssetFeatures));
        expectedAxioms.add(dataFactory.getOWLSubClassOfAxiom(operationality
                , knowledgeAssetFeatures));

        expectedAxioms.add(dataFactory.getOWLDisjointClassesAxiom(persons, knowledgeAssets));
        expectedAxioms.add(dataFactory.getOWLDisjointUnionAxiom(visbility, Set.of(explicitVisibility, tacitVisibility)));
        expectedAxioms.add(dataFactory.getOWLDisjointUnionAxiom(category, Set.of(businessCategory, technicalCategory, generalCategory)));
        expectedAxioms.add(dataFactory.getOWLDisjointUnionAxiom(sociality, Set.of(socialSociality, individualSociality)));
        expectedAxioms.add(dataFactory.getOWLDisjointUnionAxiom(operationality, Set.of(causalOperationality, conditionalOperationality, proceduralOperationality, relationalOperationality, declarativeOperationality)));


        OWLObjectIntersectionOf intersection = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLDataExactCardinality(1, hasMagnitude), dataFactory.getOWLObjectExactCardinality(1, hasKnowledgeAsset), dataFactory.getOWLObjectExactCardinality(1, hasPerson));
        expectedAxioms.add(dataFactory.getOWLSubClassOfAxiom(knowledgeObservation, intersection));
        expectedAxioms.add(dataFactory.getOWLHasKeyAxiom(knowledgeObservation, Set.of(hasPerson, hasKnowledgeAsset)));


        expectedAxioms.add(getOWLObjectPropertyTransitiveAxiom(composedOf));
        expectedAxioms.add(getOWLObjectPropertyTransitiveAxiom(dependsOn));
        return expectedAxioms;
    }


    private SWRLRule getOWLObjectPropertyTransitiveAxiom(OWLObjectProperty owlObjectProperty) {
        /* We create a SWRL rule for transitive property. */
        SWRLVariable x = dataFactory.getSWRLVariable(DEFAULT_NAMESPACE.getValue(String.class) + "x");
        SWRLVariable y = dataFactory.getSWRLVariable(DEFAULT_NAMESPACE.getValue(String.class) + "y");
        SWRLVariable z = dataFactory.getSWRLVariable(DEFAULT_NAMESPACE.getValue(String.class) + "z");
        SWRLObjectPropertyAtom r1 = dataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, x, y);
        SWRLObjectPropertyAtom r2 = dataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, y, z);
        SWRLObjectPropertyAtom r3 = dataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, x, z);
        Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
        antecedent.add(r1);
        antecedent.add(r2);
        return dataFactory.getSWRLRule(antecedent, Collections.singleton(r3));
    }

}
