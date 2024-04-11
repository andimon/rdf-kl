package com.andimon.rdfknowledgelandscape.constructionmethods;

import com.andimon.rdfknowledgelandscape.exceptions.KnowledgeGraphConstructorException;
import com.andimon.rdfknowledgelandscape.factories.*;
import com.andimon.rdfknowledgelandscape.features.Feature;
import com.andimon.rdfknowledgelandscape.ontology.OntoKL;
import com.andimon.rdfknowledgelandscape.updater.KnowledgeLandscapeUpdater;
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

public class KnowledgeLandscapeConstructor implements ConstructionMethods {
    private final Ontology populatedOntology;
    private final OntologyManager manager;
    private final OWLReasoner reasoner;
    private final OntoKnowledgeLandscapeOwlClassFactory ontoKnowledgeLandscapeOwlClassFactory;
    private final OntoKnowledgeLandscapeObjectPropertyFactory ontoKnowledgeLandscapeObjectPropertyFactory;
    private final OntoKnowledgeLandscapeDataPropertyFactory ontoKnowledgeLandscapeDataPropertyFactory;

    protected static final Logger logger = LogManager.getLogger(KnowledgeLandscapeConstructor.class);

    /**
     * Create an instance of a Knowledge Graph Constructor
     * with OntoKnowledgeLandscape as schema layer
     */
    public KnowledgeLandscapeConstructor() throws Exception {
        ontoKnowledgeLandscapeOwlClassFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
        ontoKnowledgeLandscapeObjectPropertyFactory = new DefaultOntoKnowledgeLandscapeObjectPropertyFactory();
        ontoKnowledgeLandscapeDataPropertyFactory = new DefaultOntoKnowledgeLandscapeDataPropertyFactory();
        OntoKL ontoKnowledgeLandscape = new OntoKL();
        populatedOntology = ontoKnowledgeLandscape.getOntology();
        manager = ontoKnowledgeLandscape.getOntology().getOWLOntologyManager();
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
    public boolean personIdentification(String personName) {
        OWLClass personClass = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class)+personName);
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
        OWLClass personClass = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        IRI personIRI = IRI.create(DEFAULT_NAMESPACE.getValue(String.class) + "personName");
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(personName);
        if (!entityInClass(personClass, person)) {
            logger.warn(personIRI.getIRIString() + " is not an instance of class " + personClass.getIRI());
            return false;
        } else {
            logger.info("Remove all axioms containing person with IRI " + person.getIRI());
            removeAllAxiomsContainingSomeEntityInAnOntology(populatedOntology, person);
            return true;
        }
    }

    @Override
    public boolean knowledgeAssetIdentification(String knowledgeAssetName, Set<Feature> features) throws Exception {
        OWLClass knowledgeAssetFeatures = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetFeatureClass();
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAssetName);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        reasoner.flush();
        if (entityInClass(knowledgeAssetClass, knowledgeAsset)) {
            logger.warn("Knowledge Asset " + knowledgeAsset.getIRI() + " is already an instance of class " + knowledgeAssetClass.getIRI());
            return false;
        }
        for (OWLClass feature : reasoner.getSubClasses(knowledgeAssetFeatures, true).getFlattened()) {
            checkValueForKnowledgeAssetFeature(knowledgeAsset.getIRI(),feature, features);
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
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAssetName);
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
    public boolean knowledgeObservation(String personName, String knowledgeAssetName, double n) throws Exception {
        OWLClass knowledgeObservationClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeObservationClass();
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        OWLClass personClass = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAssetName);
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + personName);
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
            OWLNamedIndividual knowledgeObservation = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + personName + knowledgeAssetName);
            OWLObjectProperty hasPerson = ontoKnowledgeLandscapeObjectPropertyFactory.getHasPerson();
            OWLObjectProperty hasKnowledgeAsset = ontoKnowledgeLandscapeObjectPropertyFactory.getHasKnowledgeAsset();
            OWLDataProperty hasMagnitude = ontoKnowledgeLandscapeDataPropertyFactory.getHasMagnitudeProperty();
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
        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAsset1Name);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAsset2Name);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " related to " + knowledgeAsset2.getIRI());
            OWLObjectProperty relatedTo = ontoKnowledgeLandscapeObjectPropertyFactory.getRelatedToProperty();
            OWLObjectPropertyAssertionAxiom relatedToAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(relatedTo, knowledgeAsset1, knowledgeAsset2);
            populatedOntology.addAxiom(relatedToAssertion);
            return true;
        }
    }

    @Override
    public boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name) {

        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class)+ knowledgeAsset1Name);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class)+ knowledgeAsset2Name);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " dependent on " + knowledgeAsset2.getIRI());
            OWLObjectProperty dependentOn = ontoKnowledgeLandscapeObjectPropertyFactory.getDependsOnProperty();
            OWLObjectPropertyAssertionAxiom dependentOnAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(dependentOn, knowledgeAsset1, knowledgeAsset2);
            populatedOntology.addAxiom(dependentOnAssertion);
            return true;
        }
    }

    @Override
    public boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name) {
        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class)+ knowledgeAsset1Name);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class)+ knowledgeAsset2Name);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " composed of " + knowledgeAsset2.getIRI());
            OWLObjectProperty composedOf = ontoKnowledgeLandscapeObjectPropertyFactory.getComposedOfProperty();
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
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + personName); //Person IRI
        OWLClass teamClass = manager.getOWLDataFactory().getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + teamName); //Team IRI
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
        infOnt.asGraphModel().setNsPrefix("kl",DEFAULT_NAMESPACE.getValue(String.class));
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

    private void checkValueForKnowledgeAssetFeature(IRI knowledgeAssetIRI, OWLClass knowledgeAssetFeature, Set<Feature> features) throws Exception {
        Set<String> values = new HashSet<>();
        String featureNameIRI = knowledgeAssetFeature.getIRI().getIRIString();
        for (Feature feature : features) {
            if (feature.getFeatureIRI().equals(featureNameIRI)) {
                values.add(feature.getValueIRI());
            }
        }
        if (values.isEmpty()) {
            logger.info("No values defined for feature " + featureNameIRI + " for knowledge asset "+ knowledgeAssetIRI+".");
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
            OWLNamedIndividual knowledgeAssetFeatureAssignment = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAssetName + featureName + "Assignment");
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeAssetFeatureAssignment));
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLClassAssertionAxiom(valueClass, knowledgeAssetFeatureAssignment));
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectProperty, knowledgeAsset, knowledgeAssetFeatureAssignment));

        }
    }

}
