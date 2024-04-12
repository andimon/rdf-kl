package com.andimon.rdfknowledgelandscape.updater;

import com.andimon.rdfknowledgelandscape.factories.*;
import com.github.owlcs.ontapi.Ontology;
import com.github.owlcs.ontapi.OntologyManager;
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

    OntoKnowledgeLandscapeDataPropertyFactory dataPropertyFactory;
    OWLReasonerFactory reasonerFactory;
    OWLReasoner reasoner;
    OntologyManager manager;
    Ontology ontology;
    PrefixManager prefixManager;

    /**
     * Updates the knowledge graph as required by every knowledge graph updater.
     * In this particular case (BaseUpdater), the inferred knowledge graph
     * generated from the knowledge graph constructor along with the owl reasoner
     * is re-evaluated to model team knowledge and transitivity of knowledge
     * through knowledge composition.
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
        teamKnowledge();
        transitivityOfKnowledgeThroughComposition();
        dependentRelationships();
    }

    private void teamKnowledge() {
        OWLClass person = classFactory.getPersonClass();
        Set<OWLClass> teams = reasoner.getSubClasses(person, true).getFlattened(); //Get teams.
        for (OWLClass team : teams) {
            Set<OWLNamedIndividual> membersOfTeam = reasoner.getInstances(team, true).getFlattened();
            if (!membersOfTeam.isEmpty())
                createKnowledgeRelationship(team, membersOfTeam);
        }
    }

    private void transitivityOfKnowledgeThroughComposition() {
        for (OWLNamedIndividual person : reasoner.getInstances(classFactory.getPersonClass()).getFlattened()) {
            Set<OWLNamedIndividual> knowledgeAssets = getKnowledgeAssetsAssociatedToAPerson(person);
            knowledgeAssets
                    .forEach(ka -> {
                        try {
                            createTransitiveKnowledgeRelationships(person, knowledgeAssets, ka);
                        } catch (UnexpectedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        }
    }


    private void dependentRelationships() {
        for (OWLObjectPropertyAssertionAxiom axiom : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
            if (axiom.getProperty().equals(objectPropertyFactory.getDependsOnProperty())) {
                System.out.println("Dependent: " + axiom.getSubject());
                createNewRelationshipBasedOnDependentOn(axiom.getSubject(), axiom.getObject());
            }
        }
    }

    private void createNewRelationshipBasedOnDependentOn(OWLIndividual dependentKA, OWLIndividual superiorKA) {
        //dependentKA depends on superiorKA

        Set<OWLIndividual> knowledgeObservations = EntitySearcher.getInstances(classFactory.getKnowledgeObservationClass(), ontology).filter(ko -> EntitySearcher.getObjectPropertyValues(ko, objectPropertyFactory.getHasKnowledgeAsset(), ontology).anyMatch(ka -> ka.equals(dependentKA))).collect(Collectors.toSet());
        Set<OWLIndividual> knowledgeAssets = EntitySearcher.getInstances(classFactory.getKnowledgeObservationClass(), ontology).map(ko -> EntitySearcher.getObjectPropertyValues(ko, objectPropertyFactory.getHasKnowledgeAsset(), ontology).findAny().orElse(null)).collect(Collectors.toSet());
        Set<OWLIndividual> persons = EntitySearcher.getInstances(classFactory.getKnowledgeObservationClass(), ontology).map(ko -> EntitySearcher.getObjectPropertyValues(ko, objectPropertyFactory.getHasKnowledgeAsset(), ontology).findAny().orElse(null)).collect(Collectors.toSet());

        for (OWLIndividual ko : knowledgeObservations) {
            OWLIndividual person = EntitySearcher.getObjectPropertyValues(ko, objectPropertyFactory.getHasPerson(), ontology).findAny().orElse(null);
            if (!koWithPersonAndKnowledgeAssetExists(person,superiorKA) ) {
                //check if there exists  a knowledge observation with person
                double magnitude = Objects.requireNonNull(EntitySearcher.getDataPropertyValues(ko, dataPropertyFactory.getHasMagnitudeProperty(), ontology).findAny().orElse(null)).parseDouble();
                String kaName = superiorKA.asOWLNamedIndividual().getIRI().getFragment();
                String personName = Objects.requireNonNull(person).asOWLNamedIndividual().getIRI().getFragment();
                OWLNamedIndividual knowledgeObservation = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class)+personName+kaName);
                ontology.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeObservation));
                ontology.addAxiom(manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectPropertyFactory.getHasPerson(),knowledgeObservation,person));
                ontology.addAxiom(manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectPropertyFactory.getHasKnowledgeAsset(),knowledgeObservation,person));
                ontology.addAxiom(manager.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(dataPropertyFactory.getHasMagnitudeProperty(),knowledgeObservation,manager.getOWLDataFactory().getOWLLiteral(magnitude)));
            }
        }


    }

    private boolean koWithPersonAndKnowledgeAssetExists(OWLIndividual person, OWLIndividual knowledgeAsset){
        Set<OWLIndividual> knowledgeObservations = EntitySearcher.getInstances(classFactory.getKnowledgeObservationClass(), ontology).collect(Collectors.toSet());
        for(OWLIndividual knowledgeObservation : knowledgeObservations){
            boolean knowledgeAssetCheck = EntitySearcher.getObjectPropertyValues(knowledgeObservation,objectPropertyFactory.getHasKnowledgeAsset(),ontology).collect(Collectors.toSet()).contains(knowledgeAsset);
            boolean personCheck = EntitySearcher.getObjectPropertyValues(knowledgeObservation,objectPropertyFactory.getHasPerson(),ontology).collect(Collectors.toSet()).contains(person);
            if(knowledgeAssetCheck && personCheck){
                return true;
            }
        }
        return false;
    }

    private void createKnowledgeRelationship(OWLClass team, Set<OWLNamedIndividual> membersOfTeam) {
        OWLObjectProperty knows = manager.getOWLDataFactory().getOWLObjectProperty(DEFAULT_NAMESPACE.getValue(String.class) + "knows");
        OWLNamedIndividual teamAsIndividual = manager.getOWLDataFactory().getOWLNamedIndividual(team.getIRI());//Punning class to individual.
        ontology.add(manager.getOWLDataFactory().getOWLDeclarationAxiom(knows));
        for (OWLNamedIndividual member : membersOfTeam) {
            getKnowledgeAssetsAssociatedToAPerson(member)
                    .forEach(ka -> ontology.addAxiom(manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(knows, teamAsIndividual, ka)));
        }
    }

    private void createTransitiveKnowledgeRelationships(OWLNamedIndividual person, Set<OWLNamedIndividual> knowledgeAssetsKnown, OWLNamedIndividual knowledgeAsset) throws UnexpectedException {
        for (OWLNamedIndividual ka : getKnowledgeAssetsAGivenKnowledgeAssetIsAPartOf((knowledgeAsset))) {
            if (!knowledgeAssetsKnown.contains(ka)) {
                String personName = person.getIRI().getFragment();
                String knowledgeAssetName = ka.getIRI().getFragment();
                OWLNamedIndividual knowledgeObservation = manager.getOWLDataFactory().getOWLNamedIndividual(":" + personName + knowledgeAssetName, prefixManager);
                OWLObjectProperty hasPerson = objectPropertyFactory.getHasPerson();
                OWLClass knowledgeObservationClass = classFactory.getKnowledgeObservationClass();
                OWLObjectProperty hasKnowledgeAsset = objectPropertyFactory.getHasKnowledgeAsset();
                OWLDataProperty hasMagnitude = dataPropertyFactory.getHasMagnitudeProperty();
                makeIndividualDifferentFromOtherIndividualsInAClass(knowledgeObservation, knowledgeObservationClass);
                OWLClassAssertionAxiom classAssertion = manager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeObservationClass, knowledgeObservation);
                OWLObjectPropertyAssertionAxiom hasPersonAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(hasPerson, knowledgeObservation, person);
                OWLObjectPropertyAssertionAxiom hasKnowledgeAssetAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(hasKnowledgeAsset, knowledgeObservation, ka);
                OWLDataPropertyAssertionAxiom hasMagnitudeAssertion = manager.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(hasMagnitude, knowledgeObservation, manager.getOWLDataFactory().getOWLLiteral(meanMagnitude(person, ka, knowledgeAssetsKnown)));
                ontology.addAxiom(classAssertion);
                ontology.addAxiom(hasPersonAssertion);
                ontology.addAxiom(hasKnowledgeAssetAssertion);
                ontology.addAxiom(hasMagnitudeAssertion);
            }
        }
    }

    private double meanMagnitude(OWLNamedIndividual person, OWLNamedIndividual knowledgeAsset, Set<OWLNamedIndividual> knowledgeAssetsKnown) {
        List<Double> magnitudes = new ArrayList<>();
        EntitySearcher.getObjectPropertyValues(knowledgeAsset, objectPropertyFactory.getComposedOfProperty(), ontology)
                .forEach(x -> magnitudes.add(getMagnitude(person, x.asOWLNamedIndividual())));
        return magnitudes.stream().mapToDouble(val -> val).average().orElse(0.0);
    }

    private double getMagnitude(OWLNamedIndividual person, OWLNamedIndividual knowledgeAsset) {
        OWLObjectProperty hasKnowledgeAsset = objectPropertyFactory.getHasKnowledgeAsset();
        OWLObjectProperty hasPerson = objectPropertyFactory.getHasPerson();
        OWLDataProperty hasMagnitudeProperty = dataPropertyFactory.getHasMagnitudeProperty();
        for (OWLNamedIndividual knowledgeObservation : reasoner.getInstances(classFactory.getKnowledgeObservationClass()).getFlattened()) {
            if (reasoner.getObjectPropertyValues(knowledgeObservation, hasKnowledgeAsset).getFlattened().contains(knowledgeAsset) && reasoner.getObjectPropertyValues(knowledgeObservation, hasPerson).getFlattened().contains(person)) {
                return EntitySearcher.getDataPropertyValues(knowledgeObservation, hasMagnitudeProperty, ontology).toList().getFirst().parseDouble();
            }
        }
        return 0.0;
    }


    private Set<OWLNamedIndividual> getKnowledgeAssetsAssociatedToAPerson(OWLNamedIndividual person) {
        Set<OWLNamedIndividual> knowledgeAssets = new HashSet<>();
        Set<OWLObjectPropertyAssertionAxiom> objectProperties = ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION);
        for (OWLObjectPropertyAssertionAxiom objectProperty : objectProperties) {
            if (objectProperty.getProperty().equals(objectPropertyFactory.getHasPerson()) && objectProperty.getObject().equals(person)) {
                OWLNamedIndividual knowledgeObservation = objectProperty.getSubject().asOWLNamedIndividual();
                knowledgeAssets.addAll(reasoner.getObjectPropertyValues(knowledgeObservation, objectPropertyFactory.getHasKnowledgeAsset()).getFlattened());
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

    private void makeIndividualDifferentFromOtherIndividualsInAClass(OWLNamedIndividual owlNamedIndividual, OWLClass owlClass) {
        OWLDataFactory owlDataFactory = manager.getOWLDataFactory();
        for (OWLNamedIndividual individual : reasoner.getInstances(owlClass, true).getFlattened()) {
            manager.addAxiom(ontology, owlDataFactory.getOWLDifferentIndividualsAxiom(owlNamedIndividual, individual));
        }
    }
}
