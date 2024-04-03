package com.andimon.rdfknowledgelandscape.constructor;

import com.andimon.rdfknowledgelandscape.baseontology.OntoKnowledgeLandscape;
import com.andimon.rdfknowledgelandscape.exceptions.KnowledgeGraphConstructorException;
import com.andimon.rdfknowledgelandscape.factories.DefaultOntoKnowledgeLandscapeOwlClassFactory;
import com.andimon.rdfknowledgelandscape.factories.OntoKnowledgeLandscapeOwlClassFactory;
import com.andimon.rdfknowledgelandscape.features.Feature;
import com.andimon.rdfknowledgelandscape.updater.KnowledgeLandscapeUpdater;
import com.github.owlcs.ontapi.OntManagers;
import com.github.owlcs.ontapi.Ontology;
import com.github.owlcs.ontapi.OntologyManager;
import org.apache.jena.rdf.model.Model;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.andimon.rdfknowledgelandscape.parameters.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;

public class KnowledgeLandscapeConstructor implements KnowledgeEvents {
    private final Ontology populatedOntology;
    private final PrefixManager prefixManager;
    private final OntologyManager manager;
    private final OWLReasoner reasoner;
    private final OntoKnowledgeLandscapeOwlClassFactory ontoKnowledgeLandscapeOwlClassFactory;
    protected static final Logger logger = LogManager.getLogger(KnowledgeLandscapeConstructor.class);

    /**
     * Create an instance of a Knowledge Graph Constructor
     * with OntoKnowledgeLandscape as schema layer
     */
    public KnowledgeLandscapeConstructor() throws Exception {
        ontoKnowledgeLandscapeOwlClassFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
        OntoKnowledgeLandscape ontoKnowledgeLandscape = new OntoKnowledgeLandscape();
        populatedOntology = ontoKnowledgeLandscape.getOntology();
        prefixManager = ontoKnowledgeLandscape.getPrefixManager();
        manager = ontoKnowledgeLandscape.getOntology().getOWLOntologyManager();
        OWLReasonerFactory reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(populatedOntology);
    }


    /**
     * Create an instance of a Knowledge Graph Constructor
     * with custom ontology, conforming with the OntoKnowledgeLandscape in RDF Knowledge Landscape Framework, as schema layer.
     *
     * @param ontology                              Ontology conforming to OntoKnowledgeLandscape
     * @param ontoKnowledgeLandscapeOwlClassFactory Defines the IRI used for the classes
     * @param prefixManager                         A prefix manager than can provide prefixes for prefix names.
     */
    public KnowledgeLandscapeConstructor(Ontology ontology, OntoKnowledgeLandscapeOwlClassFactory ontoKnowledgeLandscapeOwlClassFactory, PrefixManager prefixManager, OntoKnowledgeLandscapeOwlClassFactory ontoKnowledgeLandscapeOwlClassFactory1) throws Exception {
        populatedOntology = ontology;
        this.prefixManager = prefixManager;
        manager = ontology.getOWLOntologyManager();
        this.ontoKnowledgeLandscapeOwlClassFactory = ontoKnowledgeLandscapeOwlClassFactory1;
        OWLReasonerFactory reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(populatedOntology);
    }

    /**
     * Create an instance of a person class with IRI
     *
     * @param personName The fragment identifier of the IRI used to represent a person.
     * @return An instance of the Person class.
     */
    @Override
    public boolean personJoinsOrganisation(String personName) {
        OWLClass personClass = manager.getOWLDataFactory().getOWLClass(":Person", prefixManager);
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(":" + personName, prefixManager);
        if (entityInClass(personClass, person)) {
            logger.warn(person.getIRI() + " is already an instance of class " + personClass.getIRI());
            return false;
        } else {
            logger.info("Creating person with IRI " + person.getIRI());
            OWLDeclarationAxiom declarationAxiom = manager.getOWLDataFactory().getOWLDeclarationAxiom(person);
            OWLClassAssertionAxiom classAssertion = manager.getOWLDataFactory().getOWLClassAssertionAxiom(personClass, person);
            manager.addAxiom(populatedOntology, declarationAxiom);
            manager.addAxiom(populatedOntology, classAssertion);
            makeIndividualDifferentFromOtherIndividualsInAClass(person, personClass);
            return true;
        }
    }

    @Override
    public boolean personLeavesOrganisation(String personName) {
        OWLClass personClass = manager.getOWLDataFactory().getOWLClass(":Person", prefixManager);
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(":" + personName, prefixManager);
        if (!entityInClass(personClass, person)) {
            logger.warn(prefixManager.getDefaultPrefix() + personName + " is not an instance of class " + prefixManager.getDefaultPrefix() + "Person");
            return false;
        } else {
            logger.info("Remove all axioms containing person with IRI " + prefixManager.getDefaultPrefix() + personName);
            removeAllAxiomsContainingSomeEntityInAnOntology(populatedOntology, person);
            return true;
        }
    }

    @Override
    public boolean knowledgeAssetIdentification(String knowledgeAssetName, Set<Feature> features) throws Exception {
        OWLClass knowledgeAssetFeatures = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetFeatureClass();
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAssetName, prefixManager);
        OWLClass knowledgeAssetClass = manager.getOWLDataFactory().getOWLClass(":KnowledgeAsset", prefixManager);
        reasoner.flush();
        if (entityInClass(knowledgeAssetClass, knowledgeAsset)) {
            logger.warn("Knowledge Asset " + knowledgeAsset.getIRI() + " is already an instance of class " + knowledgeAssetClass.getIRI());
            return false;
        }
        System.out.println("Features " + features);
        for (OWLClass feature : reasoner.getSubClasses(knowledgeAssetFeatures, true).getFlattened()) {
            checkValueForKnowledgeAssetFeature(feature, features);
        }
        logger.info("Creating knowledge asset with IRI " + knowledgeAsset.getIRI());
        OWLDeclarationAxiom declarationAxiom = manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeAsset);
        OWLClassAssertionAxiom classAssertion = manager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeAssetClass, knowledgeAsset);
        manager.addAxiom(populatedOntology, declarationAxiom);
        manager.addAxiom(populatedOntology, classAssertion);
        addKnowledgeAssetFeatures(knowledgeAsset, features);
        return true;
    }

    @Override
    public boolean knowledgeAssetDiscarded(String knowledgeAssetName) {
        OWLClass knowledgeAssetClass = manager.getOWLDataFactory().getOWLClass(":KnowledgeAsset", prefixManager);
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAssetName, prefixManager);
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset)) {
            logger.warn(knowledgeAsset.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI());
            return false;
        } else {
            logger.info("Remove all axioms containing person with IRI " + knowledgeAsset.getIRI());
            removeAllAxiomsContainingSomeEntityInAnOntology(populatedOntology, knowledgeAsset);
            return true;
        }
    }

    @Override
    public boolean knowledgeObservation(String knowledgeAssetName, String personName, double n) throws Exception {
        OWLClass knowledgeObservationClass = manager.getOWLDataFactory().getOWLClass(":KnowledgeObservation", prefixManager);
        OWLClass knowledgeAssetClass = manager.getOWLDataFactory().getOWLClass(":KnowledgeAsset", prefixManager);
        OWLClass personClass = manager.getOWLDataFactory().getOWLClass(":Person", prefixManager);
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAssetName, prefixManager);
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(":" + personName, prefixManager);
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset)) {
            logger.warn("Knowledge Asset " + knowledgeAsset.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(personClass, person)) {
            logger.warn("Person " + knowledgeAsset.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The person is needs to be identified/created first.");
            return false;
        } else if (n < 0) {
            throw new KnowledgeGraphConstructorException(n + " is not a non-negative integer.");
        } else {
            /* Create observation */
            OWLNamedIndividual knowledgeObservation = manager.getOWLDataFactory().getOWLNamedIndividual(":" + personName + knowledgeAssetName, prefixManager);
            OWLObjectProperty hasPerson = OntManagers.getDataFactory().getOWLObjectProperty(":hasPerson", prefixManager);
            OWLObjectProperty hasKnowledgeAsset = OntManagers.getDataFactory().getOWLObjectProperty(":hasKnowledgeAsset", prefixManager);
            OWLDataProperty hasMagnitude = OntManagers.getDataFactory().getOWLDataProperty(":hasMagnitude", prefixManager);
            makeIndividualDifferentFromOtherIndividualsInAClass(knowledgeObservation, knowledgeObservationClass);
            OWLDeclarationAxiom declarationAxiom = manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeObservation);
            OWLClassAssertionAxiom classAssertion = manager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeObservationClass, knowledgeObservation);
            OWLObjectPropertyAssertionAxiom hasPersonAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(hasPerson, knowledgeObservation, person);
            OWLObjectPropertyAssertionAxiom hasKnowledgeAssetAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(hasKnowledgeAsset, knowledgeObservation, knowledgeAsset);
            OWLDataPropertyAssertionAxiom hasMagnitudeAssertion = manager.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(hasMagnitude, knowledgeObservation, manager.getOWLDataFactory().getOWLLiteral(n));
            populatedOntology.addAxiom(declarationAxiom);
            populatedOntology.addAxiom(classAssertion);
            populatedOntology.addAxiom(hasPersonAssertion);
            populatedOntology.addAxiom(hasKnowledgeAssetAssertion);
            populatedOntology.addAxiom(hasMagnitudeAssertion);
            return true;
        }
    }

    @Override
    public boolean relatedTo(String knowledgeAsset1Name, String knowledgeAsset2Name) {
        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset1Name, prefixManager);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset2Name, prefixManager);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " related to " + knowledgeAsset2.getIRI());
            OWLObjectProperty relatedTo = manager.getOWLDataFactory().getOWLObjectProperty(":relatedTo", prefixManager);
            OWLObjectPropertyAssertionAxiom relatedToAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(relatedTo, knowledgeAsset1, knowledgeAsset2);
            populatedOntology.addAxiom(relatedToAssertion);
            return true;
        }
    }

    @Override
    public boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name) {
        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset1Name, prefixManager);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset2Name, prefixManager);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " dependent on " + knowledgeAsset2.getIRI());
            OWLObjectProperty dependentOn = manager.getOWLDataFactory().getOWLObjectProperty(":dependsOn", prefixManager);
            OWLObjectPropertyAssertionAxiom dependentOnAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(dependentOn, knowledgeAsset1, knowledgeAsset2);
            populatedOntology.addAxiom(dependentOnAssertion);
            return true;
        }
    }

    @Override
    public boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name) {
        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset1Name, prefixManager);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset2Name, prefixManager);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " composed of " + knowledgeAsset2.getIRI());
            OWLObjectProperty composedOf = manager.getOWLDataFactory().getOWLObjectProperty(":composedOf", prefixManager);
            OWLObjectPropertyAssertionAxiom composedOfAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(composedOf, knowledgeAsset1, knowledgeAsset2);
            populatedOntology.addAxiom(composedOfAssertion);
            return true;
        }
    }

    @Override
    public boolean createTeam(String teamName) {
        // Check if team exits.
        OWLClass teamClass = manager.getOWLDataFactory().getOWLClass((DEFAULT_NAMESPACE.getValue(String.class) + teamName));
        reasoner.flush();
        Set<OWLClass> teams = reasoner.getSubClasses(ontoKnowledgeLandscapeOwlClassFactory.getPersonClass()).getFlattened();
        if (teams.contains(teamClass)) {
            logger.warn("Team " + teamClass.getIRI() + "already exists.");
            return false;
        }
        logger.info("Creating team " + teamClass.getIRI() + ".");
        populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(teamClass));
        populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLSubClassOfAxiom(teamClass, ontoKnowledgeLandscapeOwlClassFactory.getPersonClass()));
        return true;
    }

    @Override
    public boolean addPersonToTeam(String teamName, String personName) {
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(personName, prefixManager); //Person IRI
        OWLClass teamClass = manager.getOWLDataFactory().getOWLClass(teamName, prefixManager); //Team IRI
        reasoner.flush();
        if (!reasoner.getInstances(ontoKnowledgeLandscapeOwlClassFactory.getPersonClass(), false).getFlattened().contains(person)) {
            logger.warn("Person " + person.getIRI() + " not found. Person needs to be identified first.");
            return false;
        } else if (!reasoner.getSubClasses(ontoKnowledgeLandscapeOwlClassFactory.getPersonClass()).getFlattened().contains(teamClass)) {
            logger.warn("Team " + teamClass.getIRI() + " does not exists.");
            return false;
        } else if (reasoner.getInstances(teamClass, true).getFlattened().contains(person)) {
            logger.info("Person " + person.getIRI() + " already a member of team " + teamClass.getIRI() + ".");
            return false;
        } else {
            logger.info("Person " + person.getIRI() + " added to team " + teamClass.getIRI() + " .");
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLClassAssertionAxiom(teamClass, person));
            return true;
        }
    }

    public String getPopulatedOntology() throws OWLOntologyStorageException, IOException, OWLOntologyCreationException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        manager.saveOntology(populatedOntology, new TurtleDocumentFormat(), new StreamDocumentTarget(out));
        String data = out.toString();
        out.close();
        return data;
    }

    public Model generateGraph(KnowledgeLandscapeUpdater updater) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
        // Step 1: Deep copy of the current ontology
        Ontology infOnt = manager.createOntology();
        manager.addAxioms(infOnt, populatedOntology.getAxioms());

        // Step 2: Fill the ontology with inferred axioms using the reasoner
        reasoner.flush();
        InferredOntologyGenerator inference = new InferredOntologyGenerator(reasoner);
        OWLDataFactory df = manager.getOWLDataFactory();
        inference.fillOntology(df, infOnt);

        // Step 3: Update ontology with specific requirements
        updater.updateKnowledgeGraph(infOnt);

        // Step 4: Transform the populated ontology with inferences into an RDF graph
        return infOnt.asGraphModel();
    }

    private boolean entityInClass(OWLClass owlClass, OWLNamedIndividual person) {
        reasoner.flush();
        return reasoner.getInstances(owlClass, false).containsEntity(person);
    }

    private void makeIndividualDifferentFromOtherIndividualsInAClass(OWLNamedIndividual owlNamedIndividual, OWLClass owlClass) {
        OWLReasonerFactory reasonerFactory = new ReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(populatedOntology);
        OWLDataFactory owlDataFactory = manager.getOWLDataFactory();
        for (OWLNamedIndividual individual : reasoner.getInstances(owlClass, true).getFlattened()) {
            manager.addAxiom(populatedOntology, owlDataFactory.getOWLDifferentIndividualsAxiom(owlNamedIndividual, individual));
        }
    }

    private void removeAllAxiomsContainingSomeEntityInAnOntology(OWLOntology ontology, OWLEntity entity) {
        for (OWLAxiom axiom : ontology.getAxioms()) {
            if (axiom.containsEntityInSignature(entity)) {
                populatedOntology.removeAxiom(axiom);
            }
        }
    }

    private String suffixRemover(String target, String suffix) {
        int index = target.lastIndexOf(suffix);
        if (index > 0) {
            return target.substring(0, index);
        }
        return target;
    }

    private void checkValueForKnowledgeAssetFeature(OWLClass knowledgeAssetFeature, Set<Feature> features) throws Exception {
        Set<String> values = new HashSet<>();
        String featureNameIRI = knowledgeAssetFeature.getIRI().getIRIString();
        for (Feature feature : features) {
            if (feature.getFeatureIRI().equals(featureNameIRI)) {
                values.add(feature.getValueIRI());
            }
        }
        if (values.isEmpty()) {
            throw new KnowledgeGraphConstructorException("No values defined for feature: " + featureNameIRI + ".");

        } else if (values.size() > 1) {
            throw new KnowledgeGraphConstructorException("Multiple values " + values + " defined for feature " + featureNameIRI + ".");

        } else {
            reasoner.flush();
            Set<String> featureValues = reasoner.getSubClasses(knowledgeAssetFeature, true).entities().map(x -> x.getIRI().toString()).collect(Collectors.toSet());
            String value = values.stream().findAny().get();
            if (!featureValues.contains(value)) {
                throw new KnowledgeGraphConstructorException("Value " + value + " undefined for feature " + featureNameIRI + ".");
            }
        }
    }

    private void addKnowledgeAssetFeatures(OWLNamedIndividual knowledgeAsset, Set<Feature> features) {
        String knowledgeAssetName = knowledgeAsset.getIRI().getFragment();
        for (Feature feature : features) {
            String featureName = IRI.create(feature.getFeatureIRI()).getFragment();
            OWLClass valueClass = manager.getOWLDataFactory().getOWLClass(feature.getValueIRI());
            OWLObjectProperty objectProperty = manager.getOWLDataFactory().getOWLObjectProperty(DEFAULT_NAMESPACE.getValue(String.class) + "has" + featureName);
            OWLNamedIndividual knowledgeAssetFeatureAssignment = manager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAssetName + featureName + "Assignment", prefixManager);
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeAssetFeatureAssignment));
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLClassAssertionAxiom(valueClass, knowledgeAssetFeatureAssignment));
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectProperty, knowledgeAsset, knowledgeAssetFeatureAssignment));

        }
    }

}
