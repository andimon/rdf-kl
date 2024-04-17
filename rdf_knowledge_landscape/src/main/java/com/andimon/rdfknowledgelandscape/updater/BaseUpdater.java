package com.andimon.rdfknowledgelandscape.updater;

import com.andimon.rdfknowledgelandscape.constructionmethods.KnowledgeLandscapeConstructor;
import com.andimon.rdfknowledgelandscape.factories.*;
import com.github.owlcs.ontapi.Ontology;
import com.github.owlcs.ontapi.OntologyManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.rmi.UnexpectedException;
import java.util.*;
import java.util.stream.Collectors;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;


public class BaseUpdater implements KnowledgeLandscapeUpdater {
    OntoKnowledgeLandscapeOwlClassFactory classFactory;
    OntoKnowledgeLandscapeObjectPropertyFactory objectPropertyFactory;
    Random random = new Random();
    OntoKnowledgeLandscapeDataPropertyFactory dataPropertyFactory;
    OWLReasonerFactory reasonerFactory;
    OWLReasoner reasoner;
    OntologyManager manager;
    Ontology ontology;
    PrefixManager prefixManager;
    protected static final Logger logger = LogManager.getLogger(KnowledgeLandscapeConstructor.class);


    /**
     * Updates the knowledge graph as required by every knowledge graph updater.
     * In this particular case (BaseUpdater), the inferred knowledge graph
     * generated from the knowledge graph constructor along with the owl reasoner
     * is re-evaluated to model transitivity of knowledge
     * through knowledge composition and dependency.
     *
     * @param ontology The inferred ontology representing the knowledge graph to be updated.
     */
    public void updateKnowledgeGraph(Ontology ontology) {
        this.ontology = ontology;
        String namespace = DEFAULT_NAMESPACE.getValue(String.class);
        prefixManager = new DefaultPrefixManager(null, null, namespace);
        classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
        objectPropertyFactory = new DefaultOntoKnowledgeLandscapeObjectPropertyFactory();
        dataPropertyFactory = new DefaultOntoKnowledgeLandscapeDataPropertyFactory();
        reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(ontology);
        manager = ontology.getOWLOntologyManager();
        transitivityOfKnowledgeThroughComposition();
        transitivityThroughDependency();
    }

    private void transitivityOfKnowledgeThroughComposition() {
        logger.info("Creating transitivity through composition");
        for (OWLIndividual person : EntitySearcher.getInstances(classFactory.getPersonClass(), ontology).collect(Collectors.toSet())) {
            Set<OWLNamedIndividual> knowledgeAssets = getKnowledgeAssetsAssociatedToAPerson(person.asOWLNamedIndividual());
            knowledgeAssets
                    .forEach(ka -> {
                        try {
                            createTransitiveCompositionKnowledgeRelationships(person.asOWLNamedIndividual(), knowledgeAssets, ka);
                        } catch (UnexpectedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        }
    }

    private void transitivityThroughDependency() {
        logger.info("Creating transitivity through dependency composition");
        for (OWLIndividual person : EntitySearcher.getInstances(classFactory.getPersonClass(), ontology).collect(Collectors.toSet())) {
            Set<OWLNamedIndividual> knowledgeAssets = getKnowledgeAssetsAssociatedToAPerson(person.asOWLNamedIndividual());
            knowledgeAssets
                    .forEach(ka -> {
                        try {
                            createTransitiveDependencyKnowledgeRelationships(person.asOWLNamedIndividual(), knowledgeAssets, ka);
                        } catch (UnexpectedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        }


    }


    private void createTransitiveDependencyKnowledgeRelationships(OWLNamedIndividual person, Set<OWLNamedIndividual> knowledgeAssetsKnown, OWLNamedIndividual knowledgeAsset) throws UnexpectedException {
        System.out.println("knowledge asset:" + knowledgeAsset.getIRI().getFragment() + " depnds on " + getKnowledgeAssetsAGivenKnowledgeAssetDependsOn(knowledgeAsset));

        for (OWLNamedIndividual ka : getKnowledgeAssetsAGivenKnowledgeAssetDependsOn((knowledgeAsset))) {
            if (!knowledgeAssetsKnown.contains(ka)) {
                OWLNamedIndividual knowledgeObservation = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class)+UUID.randomUUID());
                OWLClass knowledgeObservationClass = classFactory.getKnowledgeObservationClass();
                OWLDeclarationAxiom declarationAxiom = manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeObservation);
                OWLClassAssertionAxiom classAssertion = manager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeObservationClass, knowledgeObservation);
                OWLObjectPropertyAssertionAxiom hasPersonAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectPropertyFactory.getHasPerson(), knowledgeObservation, person);
                OWLObjectPropertyAssertionAxiom hasKnowledgeAssetAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectPropertyFactory.getHasKnowledgeAsset(), knowledgeObservation, ka);
                int magnitude = generateRandomNumber(getMagnitude(person, knowledgeAsset),getLargestMagnitude());
                System.out.println("magnitude "+magnitude);
                OWLDataPropertyAssertionAxiom hasMagnitudeAssertion = manager.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(dataPropertyFactory.getHasMagnitudeProperty(), knowledgeObservation, magnitude );
                ontology.addAxiom(declarationAxiom);
                ontology.addAxiom(classAssertion);
                ontology.addAxiom(hasPersonAssertion);
                ontology.addAxiom(hasKnowledgeAssetAssertion);
                ontology.addAxiom(hasMagnitudeAssertion);
                makeEntityDifferent(knowledgeObservation); // Non unique naming assumption
            }
        }
    }

    private void createTransitiveCompositionKnowledgeRelationships(OWLNamedIndividual person, Set<OWLNamedIndividual> knowledgeAssetsKnown, OWLNamedIndividual knowledgeAsset) throws UnexpectedException {
        System.out.println("knowledge asset:" + knowledgeAsset.getIRI().getFragment() + " part of " + getKnowledgeAssetsAGivenKnowledgeAssetIsAPartOf(knowledgeAsset));
        for (OWLNamedIndividual ka : getKnowledgeAssetsAGivenKnowledgeAssetIsAPartOf((knowledgeAsset))) {
            if (!knowledgeAssetsKnown.contains(ka)) {
                String personName = person.getIRI().getFragment();
                String knowledgeAssetName = ka.getIRI().getFragment();
                OWLNamedIndividual knowledgeObservation = manager.getOWLDataFactory().getOWLNamedIndividual(":" + personName + knowledgeAssetName, prefixManager);
                OWLClass knowledgeObservationClass = classFactory.getKnowledgeObservationClass();
                OWLDeclarationAxiom declarationAxiom = manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeObservation);
                OWLClassAssertionAxiom classAssertion = manager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeObservationClass, knowledgeObservation);
                OWLObjectPropertyAssertionAxiom hasPersonAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectPropertyFactory.getHasPerson(), knowledgeObservation, person);
                OWLObjectPropertyAssertionAxiom hasKnowledgeAssetAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectPropertyFactory.getHasKnowledgeAsset(), knowledgeObservation, ka);
                int magnitude = generateRandomNumber(0,getMagnitude(person, knowledgeAsset));
                OWLDataPropertyAssertionAxiom hasMagnitudeAssertion = manager.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(dataPropertyFactory.getHasMagnitudeProperty(), knowledgeObservation, magnitude);
                ontology.addAxiom(declarationAxiom);
                ontology.addAxiom(classAssertion);
                ontology.addAxiom(hasPersonAssertion);
                ontology.addAxiom(hasKnowledgeAssetAssertion);
                ontology.addAxiom(hasMagnitudeAssertion);
                makeEntityDifferent(knowledgeObservation); // non unique naming assumption
            }
        }
    }


    private int getMagnitude(OWLNamedIndividual person, OWLNamedIndividual knowledgeAsset) {
        OWLObjectProperty hasKnowledgeAsset = objectPropertyFactory.getHasKnowledgeAsset();
        OWLObjectProperty hasPerson = objectPropertyFactory.getHasPerson();
        OWLDataProperty hasMagnitudeProperty = dataPropertyFactory.getHasMagnitudeProperty();
        for (OWLIndividual knowledgeObservation : EntitySearcher.getInstances(classFactory.getKnowledgeObservationClass(), ontology).collect(Collectors.toSet())) {
            if (EntitySearcher.getObjectPropertyValues(knowledgeObservation, hasKnowledgeAsset, ontology).anyMatch(x -> x.asOWLNamedIndividual().equals(knowledgeAsset)) && EntitySearcher.getObjectPropertyValues(knowledgeObservation, hasPerson, ontology).anyMatch(x -> x.asOWLNamedIndividual().equals(person))) {
                return EntitySearcher.getDataPropertyValues(knowledgeObservation, hasMagnitudeProperty, ontology).toList().getFirst().parseInteger();
            }
        }
        return 0;
    }


    private Set<OWLNamedIndividual> getKnowledgeAssetsAssociatedToAPerson(OWLNamedIndividual person) {
        Set<OWLNamedIndividual> knowledgeAssets = new HashSet<>();
        Set<OWLObjectPropertyAssertionAxiom> objectProperties = ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION);
        for (OWLObjectPropertyAssertionAxiom objectProperty : objectProperties) {
            if (objectProperty.getProperty().equals(objectPropertyFactory.getHasPerson()) && objectProperty.getObject().equals(person)) {
                OWLNamedIndividual knowledgeObservation = objectProperty.getSubject().asOWLNamedIndividual();
                knowledgeAssets.addAll(EntitySearcher.getObjectPropertyValues(knowledgeObservation, objectPropertyFactory.getHasKnowledgeAsset(), ontology).map(AsOWLNamedIndividual::asOWLNamedIndividual).collect(Collectors.toSet()));
            }
        }
        return knowledgeAssets;
    }

    private Set<OWLNamedIndividual> getKnowledgeAssetsAGivenKnowledgeAssetIsAPartOf(OWLNamedIndividual knowledgeAsset) {
        Set<OWLNamedIndividual> knowledgeAssets = new HashSet<>();
        Set<OWLObjectPropertyAssertionAxiom> objectProperties = ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION);
        for (OWLObjectPropertyAssertionAxiom objectProperty : objectProperties) {
            if (objectProperty.getProperty().equals(objectPropertyFactory.getComposedOfProperty()) && objectProperty.getObject().equals(knowledgeAsset)) {
                OWLNamedIndividual main = objectProperty.getSubject().asOWLNamedIndividual();
                knowledgeAssets.add(main);
            }
        }
        return knowledgeAssets;
    }

    private Set<OWLNamedIndividual> getKnowledgeAssetsAGivenKnowledgeAssetDependsOn(OWLNamedIndividual knowledgeAsset) {
        Set<OWLNamedIndividual> knowledgeAssets = new HashSet<>();
        Set<OWLObjectPropertyAssertionAxiom> objectProperties = ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION);
        for (OWLObjectPropertyAssertionAxiom objectProperty : objectProperties) {
            if (objectProperty.getProperty().equals(objectPropertyFactory.getDependsOnProperty()) && objectProperty.getSubject().equals(knowledgeAsset)) {
                OWLNamedIndividual main = objectProperty.getObject().asOWLNamedIndividual();
                knowledgeAssets.add(main);
            }
        }
        return knowledgeAssets;
    }

    private void makeEntityDifferent(OWLNamedIndividual individual) {
        for (OWLDifferentIndividualsAxiom axiom : ontology.getAxioms(AxiomType.DIFFERENT_INDIVIDUALS)) {
            ontology.removeAxioms(axiom);
        }
        ontology.addAxiom(ontology.getOWLOntologyManager().getOWLDataFactory().getOWLDifferentIndividualsAxiom(ontology.getIndividualsInSignature()));
    }

    private int getLargestMagnitude() {
        Set<OWLDataPropertyAssertionAxiom> dataProperties = ontology.getAxioms(AxiomType.DATA_PROPERTY_ASSERTION);
        int max = 0;
        for (OWLDataPropertyAssertionAxiom dataProperty : dataProperties) {
            if (dataProperty.getProperty().equals(dataPropertyFactory.getHasMagnitudeProperty())) {
                int mag = dataProperty.getObject().parseInteger();
                if (mag > max) {
                    max = mag;
                }
            }
        }
        System.out.println("max "+max);
        return max;

    }

    private int generateRandomNumber(int n, int m) {
        // return number between n and m (inclusive)
        return random.nextInt(m + 1 - n) + n;
    }

}
