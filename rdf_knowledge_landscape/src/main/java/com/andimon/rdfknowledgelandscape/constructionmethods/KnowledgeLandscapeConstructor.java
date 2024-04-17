package com.andimon.rdfknowledgelandscape.constructionmethods;

import com.andimon.rdfknowledgelandscape.factories.*;
import com.andimon.rdfknowledgelandscape.features.Feature;
import com.andimon.rdfknowledgelandscape.ontology.OntoKL;
import com.andimon.rdfknowledgelandscape.updater.KnowledgeLandscapeUpdater;
import com.clarkparsia.owlapi.explanation.BlackBoxExplanation;
import com.clarkparsia.owlapi.explanation.HSTExplanationGenerator;
import com.clarkparsia.owlapi.explanation.TransactionAwareSingleExpGen;
import com.github.owlcs.ontapi.Ontology;
import com.github.owlcs.ontapi.OntologyManager;
import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.ReasonerFactory;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;

public class KnowledgeLandscapeConstructor implements ConstructionMethods {
    private final Ontology populatedOntology;
    private final OntologyManager manager;
    private final OWLReasoner reasoner;
    private final OntoKnowledgeLandscapeOwlClassFactory ontoKnowledgeLandscapeOwlClassFactory;
    private final OntoKnowledgeLandscapeObjectPropertyFactory ontoKnowledgeLandscapeObjectPropertyFactory;
    private final OntoKnowledgeLandscapeDataPropertyFactory ontoKnowledgeLandscapeDataPropertyFactory;
    private OWLEntityRemover remover;
    protected static final Logger logger = LogManager.getLogger(KnowledgeLandscapeConstructor.class);

    public boolean containsEntityInSignature(IRI entity) {
        return populatedOntology.containsEntityInSignature(entity);
    }

    /**
     * Create an instance of a Knowledge Graph Constructor
     * with OntoKnowledgeLandscape as schema layer
     */
    public KnowledgeLandscapeConstructor(){
        ontoKnowledgeLandscapeOwlClassFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
        ontoKnowledgeLandscapeObjectPropertyFactory = new DefaultOntoKnowledgeLandscapeObjectPropertyFactory();
        ontoKnowledgeLandscapeDataPropertyFactory = new DefaultOntoKnowledgeLandscapeDataPropertyFactory();
        OntoKL ontoKnowledgeLandscape = new OntoKL();
        populatedOntology = ontoKnowledgeLandscape.getOntology();
        manager = ontoKnowledgeLandscape.getOntology().getOWLOntologyManager();
        OWLReasonerFactory reasonerFactory = new ReasonerFactory();
        Configuration configuration = new Configuration();
        configuration.throwInconsistentOntologyException = false;
        reasoner = reasonerFactory.createReasoner(populatedOntology, configuration);
        remover = new OWLEntityRemover(populatedOntology);
    }


    public KnowledgeLandscapeConstructor(OntoKL ontoKL) {
        ontoKnowledgeLandscapeOwlClassFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
        ontoKnowledgeLandscapeObjectPropertyFactory = new DefaultOntoKnowledgeLandscapeObjectPropertyFactory();
        ontoKnowledgeLandscapeDataPropertyFactory = new DefaultOntoKnowledgeLandscapeDataPropertyFactory();
        populatedOntology = ontoKL.getOntology();
        manager = ontoKL.getOntology().getOWLOntologyManager();
        OWLReasonerFactory reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(populatedOntology);
        remover = new OWLEntityRemover(populatedOntology);
    }


    /**
     * First, it verifies that kl:personName is not already declared.
     * Then, it declares a fresh individual named kl:personName.
     * The individual is set to be an instance of kl:Person using the class assertion axiom.
     *
     * @param personName The fragment identifier of the IRI used to represent a person.
     * @return An instance of the Person class.
     */
    @Override
    public boolean personIdentification(String personName) throws KnowledgeGraphConstructorException {
        OWLNamedIndividual personIndividual = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + personName);
        OWLClass persons = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();

        if (populatedOntology.containsEntityInSignature(personIndividual)) {
            String message = personIndividual.getIRI() + " is already declared.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Creating person with name " + personIndividual.getIRI());
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(personIndividual));
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLClassAssertionAxiom(persons, personIndividual));
        }

        // check if ontology remains consistent
        reasoner.flush(); //update reasoner with latest changes
        if (!reasoner.isConsistent()) { // if not consistent
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.removeAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(personIndividual));
            populatedOntology.removeAxiom(manager.getOWLDataFactory().getOWLClassAssertionAxiom(persons, personIndividual));
            throw new KnowledgeGraphConstructorException("Creation of person " + personIndividual.getIRI() + " lead to an inconsistency");
        }
        return true; //success;
    }

    /**
     * Verifies if kl:personName is an instance of kl:Person.
     * If true, removes all axioms associated with kl:personName,
     * as well as those related to instances of kl:KnowledgeObservation
     * linked to kl:personName via the property kl:hasPerson.
     *
     * @param personName The name of the person to remove from the organization.
     * @return True if the operation succeeds, false otherwise.
     */
    @Override
    public boolean removePerson(String personName) throws KnowledgeGraphConstructorException {
        IRI personIRI = IRI.create(DEFAULT_NAMESPACE.getValue(String.class) + personName);
        OWLNamedIndividual personIndividual = manager.getOWLDataFactory().getOWLNamedIndividual(personIRI);
        OWLClass persons = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        Set<OWLAxiom> knowledgeObservations = removeRespectiveKnowledgeAssetObservations(personIndividual); // all person knowledge observation axioms
        Set<OWLAxiom> axioms = new HashSet<>(); // all axioms directly related to person
        boolean personExists = EntitySearcher.getInstances(persons, populatedOntology).anyMatch(instance -> instance.equals(personIndividual));
        if (!personExists) {
            String message = "Person with IRI " + personIRI + " does not exist";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Removing all axioms containing person with IRI " + personIRI);
            remover.visit(personIndividual); // remove all axioms related to person
            remover.getChanges().forEach(x -> axioms.add(x.getAxiom()));
            remover.reset();
            populatedOntology.removeAxioms(knowledgeObservations);
            populatedOntology.removeAxioms(axioms);
        }
        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.addAxioms(knowledgeObservations);
            populatedOntology.removeAxioms(axioms);
            throw new KnowledgeGraphConstructorException("Removal of person  " + personIndividual.getIRI() + " led to an inconsistency");
        }
        return true; //person removed
    }

    /**
     * features is a set whose elements are pairs of the type form (featureName, valueName).
     * <p>
     * First, it verifies that 'kl:knowledgeAssetName' is not already declared as an individual.
     * Then, it verifies that for each element in the feature set, 'kl:featureName' is a subset of 'kl:KnowledgeAssetFeature',
     * and 'kl:valueNamefeatureNameValue' is one of the sets forming the disjoint union of 'kl:featureName'.
     * If both verifications hold, an individual named 'kl:knowledgeAssetName' is declared.
     * The newly created individual is set to be an instance of 'kl:KnowledgeAsset' using the class assertion axiom.
     * Then, a fresh individual is declared and set to be an instance of class 'kl:valueNamefeatureNameValue'.
     * Utilizing an object property assertion axiom, 'kl:knowledgeAssetName' is related to this instance using the property 'kl:hasfeatureName'.
     *
     * @param knowledgeAssetName The name of the knowledge asset individual to be created.
     * @param features           The set of features associated with the knowledge asset.
     * @return True if the operation succeeds, false otherwise.
     */
    @Override
    public boolean knowledgeAssetIdentification(String knowledgeAssetName, Set<Feature> features) throws KnowledgeGraphConstructorException {
        IRI knoweldgeAssetIRI = IRI.create(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAssetName);
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(knoweldgeAssetIRI);
        OWLClass knowledgeAssets = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        OWLClass knowledgeAssetFeatures = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetFeatureClass();
        boolean knowledgeAssetExists = EntitySearcher.getInstances(knowledgeAssets, populatedOntology).anyMatch(instance -> instance.equals(knowledgeAsset));
        boolean knowledgeAssetFeaturesAndValuesDefinedInOntology = true;
        Feature unexpected = null;
        for (Feature feature : features) {
            OWLClass featureClass = manager.getOWLDataFactory().getOWLClass(feature.getFeatureIRI());
            OWLClass valueClass = manager.getOWLDataFactory().getOWLClass(feature.getValueIRI());
            boolean featureExists = EntitySearcher.getSubClasses(knowledgeAssetFeatures, populatedOntology).anyMatch(subclass -> subclass.equals(featureClass));
            boolean valueExists = checkFeatureValue(featureClass, valueClass);
            if (!(featureExists && valueExists)) {
                knowledgeAssetFeaturesAndValuesDefinedInOntology = false;
                unexpected = feature;
                break;
            }
        }
        if (knowledgeAssetExists) {
            String message = knoweldgeAssetIRI + " is already declared.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else if (!knowledgeAssetFeaturesAndValuesDefinedInOntology) {
            String message = "Unexpected feature " + unexpected.getFeatureIRI() + " or value " + unexpected.getValueIRI();
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Creating knowledge asset: " + knoweldgeAssetIRI);
            // declare individual
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeAsset));
            // set instance
            populatedOntology.addAxiom(manager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeAssets, knowledgeAsset));
            // add features
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

        reasoner.flush();

        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            // remove individual
            populatedOntology.removeAxioms(manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeAsset));
            // set instance
            populatedOntology.removeAxiom(manager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeAssets, knowledgeAsset));
            // add features
            for (Feature feature : features) {
                String featureName = IRI.create(feature.getFeatureIRI()).getFragment();
                OWLClass valueClass = manager.getOWLDataFactory().getOWLClass(feature.getValueIRI());
                OWLObjectProperty objectProperty = manager.getOWLDataFactory().getOWLObjectProperty(DEFAULT_NAMESPACE.getValue(String.class) + "has" + featureName);
                OWLNamedIndividual knowledgeAssetFeatureAssignment = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAssetName + featureName + "Assignment");
                populatedOntology.removeAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeAssetFeatureAssignment));
                populatedOntology.removeAxiom(manager.getOWLDataFactory().getOWLClassAssertionAxiom(valueClass, knowledgeAssetFeatureAssignment));
                populatedOntology.removeAxiom(manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectProperty, knowledgeAsset, knowledgeAssetFeatureAssignment));
            }
            throw new KnowledgeGraphConstructorException("Creation of knowledge asset " + knowledgeAsset.getIRI() + " led to an inconsistency");
        }
        return true; // success
    }

    /**
     * First, it verifies if kl:knowledgeAssetName is an instance of kl:KnowledgeAsset.
     * If so, the method removes all axioms associated with kl:knowledgeAssetName,
     * as well as all axioms related to instances of kl:KnowledgeObservation that are linked to kl:knowledgeAssetName via the property kl:hasKnowledgeAsset.
     * Furthermore, all the associated feature values are removed,
     * that is, the individuals that are instances of value classes of KAs features that are related to kl:hasKnowledgeAssetName via an object property kl:has{featureName} are removed.
     *
     * @param knowledgeAssetName The name of the knowledge asset to be removed.
     * @return True if the operation succeeds, false otherwise.
     */
    @Override
    public boolean removeKnowledgeAsset(String knowledgeAssetName) throws KnowledgeGraphConstructorException {
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAssetName);
        Set<OWLAxiom> axioms;
        if (EntitySearcher.getInstances(knowledgeAssetClass, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(knowledgeAsset))) {
            String message = knowledgeAsset.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI();
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Remove all axioms containing knowledge asset with IRI " + knowledgeAsset.getIRI());
            //axioms related to features instances related to the knowledge asset to be removed
            axioms = new HashSet<>(removeAssignedFeatures(knowledgeAsset));
            //axiom related to knowledge magnitude
            axioms.addAll(removeRespectiveKnowledgeAssetObservations(knowledgeAsset));
            // axioms related directly to knowledge assets
            remover.visit(knowledgeAsset);
            remover.getChanges().forEach(removeAxiom -> axioms.add(removeAxiom.getAxiom()));
            removeAssignedFeatures(knowledgeAsset);
            remover.reset();
            populatedOntology.removeAxioms(axioms);
        }
        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.addAxioms(axioms);
            throw new KnowledgeGraphConstructorException("Removing knowledge asset " + knowledgeAsset.getIRI() + " led to an inconsistency");
        }
        return true;
    }


    /**
     * First, verifies that kl:knowledgeAsset and kl:personName are instances of classes kl:KnowledgeAsset and kl:Person
     * respectively. If both verifications hold, then the method reifies a ternary relationship by declaring a fresh
     * individual. Subsequently, this individual is instantiated as an instance of kl:KnowledgeObservation by applying
     * the class assertion axiom. Using the object property assertion axiom, the individual is correlated with
     * kl:personName and kl:knowledgeAssetName using the object properties kl:hasPerson and kl:hasKnowledgeAsset,
     * respectively. Lastly, using the data property assertion axiom, the individual is linked to the value n via the
     * property kl:hasMagnitude
     *
     * @param personName         the name of the person
     * @param knowledgeAssetName the name of the knowledge asset
     * @param n                  the value of the magnitude
     * @return true if the knowledge observation is successful, false otherwise
     * @throws Exception if an error occurs during the observation process
     */
    @Override
    public boolean knowledgeObservation(String personName, String knowledgeAssetName, int n) throws Exception {
        OWLClass knowledgeObservations = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeObservationClass();
        OWLClass knowledgeAssets = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        OWLClass persons = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        OWLNamedIndividual knowledgeAsset = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAssetName);
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + personName);
        OWLNamedIndividual knowledgeObservation = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + UUID.randomUUID());
        Set<OWLAxiom> axioms = new HashSet<>();
        if (EntitySearcher.getInstances(knowledgeAssets, populatedOntology).noneMatch(x -> x.equals(knowledgeAsset))) {
            String message = "Knowledge Asset " + knowledgeAsset.getIRI() + " is not an instance of class " + knowledgeAssets.getIRI() + " The knowledge asset needs to be identified/created first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else if (EntitySearcher.getInstances(persons, populatedOntology).noneMatch(x -> x.equals(person))) {
            String message = "Person " + knowledgeAsset.getIRI() + " is not an instance of class " + knowledgeAssets.getIRI() + " The person is needs to be identified/created first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.warn("Creating knowledge observation with person " + person.getIRI() + ", knowledge asset " + knowledgeAsset.getIRI() + ", and magnitude " + n);
            OWLObjectProperty hasPerson = ontoKnowledgeLandscapeObjectPropertyFactory.getHasPerson();
            OWLObjectProperty hasKnowledgeAsset = ontoKnowledgeLandscapeObjectPropertyFactory.getHasKnowledgeAsset();
            OWLDataProperty hasMagnitude = ontoKnowledgeLandscapeDataPropertyFactory.getHasMagnitudeProperty();
            axioms.add(manager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeObservation));
            axioms.add(manager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeObservations, knowledgeObservation));
            axioms.add(manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(hasPerson, knowledgeObservation, person));
            axioms.add(manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(hasKnowledgeAsset, knowledgeObservation, knowledgeAsset));
            axioms.add(manager.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(hasMagnitude, knowledgeObservation, manager.getOWLDataFactory().getOWLLiteral(n)));
            populatedOntology.addAxioms(axioms);
        }

        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.removeAxioms(axioms);
            throw new KnowledgeGraphConstructorException("Failed to create knowledge observation, led to an inconsistency");
        }
        return true;
    }

    /**
     * Establishes a generic relation between kl:knowledgeAsset1Name and kl:knowledgeAsset2Name, provided that both
     * entities are instances of kl:KnowledgeAsset. It utilizes the object property assertion axiom to state that
     * kl:knowledgeAsset1Name is related to kl:knowledgeAsset2Name through the property kl:relatedTo.
     *
     * @param knowledgeAsset1Name the name of the first knowledge asset
     * @param knowledgeAsset2Name the name of the second knowledge asset
     * @return true if the relation is successfully established, false otherwise
     */
    @Override
    public boolean relatedTo(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException {
        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAsset1Name);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAsset2Name);
        OWLClass knowledgeAssets = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        OWLObjectProperty relatedTo = ontoKnowledgeLandscapeObjectPropertyFactory.getRelatedToProperty();
        OWLObjectPropertyAssertionAxiom relatedToAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(relatedTo, knowledgeAsset1, knowledgeAsset2);
        if (EntitySearcher.getInstances(knowledgeAssets, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(knowledgeAsset1))) {
            String message = "Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssets.getIRI() + " The knowledge is needs to be identified/created first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);

        } else if (EntitySearcher.getInstances(knowledgeAssets, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(knowledgeAsset2))) {
            String message = "Knowledge Asset " + knowledgeAsset2.getIRI() + " is not an instance of class " + knowledgeAssets.getIRI() + " The knowledge is needs to be identified/created first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " related to " + knowledgeAsset2.getIRI());
            populatedOntology.addAxiom(relatedToAssertion);
        }

        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.removeAxiom(relatedToAssertion);
            throw new KnowledgeGraphConstructorException("Related to relationship creation between " + knowledgeAsset1.getIRI() + " and " + knowledgeAsset2.getIRI() + " led to an inconsistency");
        }
        return true;
    }

    /**
     * Establishes a dependence relationship between kl:knowledgeAsset1 and kl:knowledgeAsset2,
     * provided that both entities are instances of kl:KnowledgeAsset. It utilises the object property
     * assertion axiom to state that kl:knowledgeAsset1 is dependent on kl:knowledgeAsset2
     * through the property kl:dependsOn.
     *
     * @param knowledgeAsset1Name The name of the first knowledge asset.
     * @param knowledgeAsset2Name The name of the second knowledge asset.
     * @return True if the dependence relationship is successfully established, false otherwise.
     */
    @Override
    public boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException {
        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAsset1Name);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAsset2Name);
        OWLClass knowledgeAssets = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        OWLObjectProperty dependsOn = ontoKnowledgeLandscapeObjectPropertyFactory.getDependsOnProperty();
        OWLObjectPropertyAssertionAxiom dependsOnAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(dependsOn, knowledgeAsset1, knowledgeAsset2);
        if (EntitySearcher.getInstances(knowledgeAssets, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(knowledgeAsset1))) {
            String message = "Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssets.getIRI() + " The knowledge is needs to be identified/created first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else if (EntitySearcher.getInstances(knowledgeAssets, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(knowledgeAsset2))) {
            String message = "Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssets.getIRI() + " The knowledge is needs to be identified/created first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " related to " + knowledgeAsset2.getIRI());
            populatedOntology.addAxiom(dependsOnAssertion);
        }

        reasoner.flush();
        if (!reasoner.isConsistent()) {
            reasoner.flush();
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.removeAxiom(dependsOnAssertion);
            throw new KnowledgeGraphConstructorException("Dependent on relationship creation between " + knowledgeAsset1.getIRI() + " and " + knowledgeAsset2.getIRI() + " led to an inconsistency");

        }
        return true;
    }


    /**
     * Establishes a composition relationship between kl:knowledgeAsset1Name and kl:knowledgeAsset2Name,
     * provided that both entities are instances of kl:KnowledgeAsset. It utilizes the object property
     * assertion axiom to state that kl:knowledgeAsset1Name is composed of kl:knowledgeAsset2Name through
     * the property kl:composedOf.
     *
     * @param knowledgeAsset1Name The name of the first knowledge asset.
     * @param knowledgeAsset2Name The name of the second knowledge asset.
     * @return True if the composition relationship is successfully established, false otherwise.
     */
    @Override
    public boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException {
        OWLNamedIndividual knowledgeAsset1 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAsset1Name);
        OWLNamedIndividual knowledgeAsset2 = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + knowledgeAsset2Name);
        OWLClass knowledgeAssets = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        OWLObjectProperty composedOf = ontoKnowledgeLandscapeObjectPropertyFactory.getComposedOfProperty();
        OWLObjectPropertyAssertionAxiom composedOfAssertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(composedOf, knowledgeAsset1, knowledgeAsset2);
        if (EntitySearcher.getInstances(knowledgeAssets, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(knowledgeAsset1))) {
            String message = "Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssets.getIRI() + " The knowledge is needs to be identified/created first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else if (EntitySearcher.getInstances(knowledgeAssets, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(knowledgeAsset2))) {
            String message = "Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssets.getIRI() + " The knowledge is needs to be identified/created first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " composed of " + knowledgeAsset2.getIRI());
            populatedOntology.addAxiom(composedOfAssertion);
        }

        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.removeAxiom(composedOfAssertion);
            throw new KnowledgeGraphConstructorException("Composed to relationship creation between " + knowledgeAsset1.getIRI() + " and " + knowledgeAsset2.getIRI() + " led to an inconsistency");
        }
        return true;
    }

    /**
     * Verifies whether kl:teamName is already declared. If not, it proceeds to declare a new class kl:teamName,
     * subsequently utilizing the subclass of axiom to specify this class as a subclass of kl:Person.
     *
     * @param teamName The name of the team to be created.
     * @return True if the operation succeeds, false otherwise.
     */
    @Override
    public boolean createTeam(String teamName) throws KnowledgeGraphConstructorException {
        // Check if team exits.
        OWLClass teamClass = manager.getOWLDataFactory().getOWLClass((DEFAULT_NAMESPACE.getValue(String.class) + teamName));
        OWLClass persons = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        Set<OWLAxiom> axioms = new HashSet<>();
        if (EntitySearcher.getSubClasses(persons, populatedOntology).anyMatch(x -> x.asOWLClass().equals(teamClass))) {
            String message = "Team " + teamClass.getIRI() + "already exists.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        }
        logger.info("Creating team " + teamClass.getIRI() + ".");
        axioms.add(manager.getOWLDataFactory().getOWLDeclarationAxiom(teamClass));
        axioms.add(manager.getOWLDataFactory().getOWLSubClassOfAxiom(teamClass, ontoKnowledgeLandscapeOwlClassFactory.getPersonClass()));
        populatedOntology.addAxioms(axioms);
        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.removeAxioms(axioms);
            throw new KnowledgeGraphConstructorException("Inconsistency. Failed to create team " + teamClass.getIRI());
        }
        return true;
    }

    /**
     * The process begins by confirming that kl:personName is genuinely an instance of kl:Person and that kl:teamName is a subclass of kl:Person.
     * If both conditions are met, the class assertion axiom is used to set kl:personName as an instance of kl:teamName.
     *
     * @param teamName   The name of the team.
     * @param personName The name of the person.
     * @return True if the operation succeeds, false otherwise.
     */
    @Override
    public boolean addPersonToTeam(String teamName, String personName) throws KnowledgeGraphConstructorException {
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + personName); //Person IRI
        OWLClass team = manager.getOWLDataFactory().getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + teamName); //Team IRI
        OWLClass persons = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        Set<OWLAxiom> axioms = new HashSet<>();
        reasoner.flush();
        if (EntitySearcher.getInstances(persons, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(person))) {
            String message = "Person " + person.getIRI() + " not found. Person needs to be identified first.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else if (EntitySearcher.getSubClasses(persons, populatedOntology).noneMatch(x -> x.asOWLClass().equals(team))) {
            String message = "Team " + team.getIRI() + " does not exists.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else if (EntitySearcher.getInstances(team, populatedOntology).anyMatch(x -> x.asOWLNamedIndividual().equals(person))) {
            String message = "Person " + person.getIRI() + " already a member of team " + team.getIRI() + ".";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Person " + person.getIRI() + " added to team " + team.getIRI() + " .");
            axioms.add(manager.getOWLDataFactory().getOWLClassAssertionAxiom(team, person));
            populatedOntology.addAxioms(axioms);
        }
        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.removeAxioms(axioms);
            throw new KnowledgeGraphConstructorException("Adding person  " + person.getIRI() + " to team " + team.getIRI() + " led to an inconsistency");
        }
        return true;
    }

    /**
     * Deletes the team identified by the specified team name.
     *
     * @param teamName the name of the team to be deleted
     * @return True if the team is successfully deleted, false  otherwise
     */
    @Override
    public boolean deleteTeam(String teamName) throws KnowledgeGraphConstructorException {
        OWLClass team = manager.getOWLDataFactory().getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + teamName); //Team IRI
        OWLClass persons = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        Set<OWLAxiom> axioms = new HashSet<>();
        reasoner.flush();
        if (EntitySearcher.getSubClasses(persons, populatedOntology).noneMatch(x -> x.asOWLClass().equals(team))) {
            String message = "Team " + team.getIRI() + " does not exists.";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.info("Deleting team " + team.getIRI() + ".");
            remover.visit(team);
            remover.getChanges().forEach(x -> axioms.add(x.getAxiom()));
            remover.reset();
            populatedOntology.removeAxioms(axioms);
        }
        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.addAxioms(axioms);
            throw new KnowledgeGraphConstructorException("Deleting " + team.getIRI() + " led to an inconsistency");
        }
        return true;
    }

    /**
     * Removes a person from the specified team.
     *
     * @param teamName The name of the team from which the person will be removed.
     * @return True if the person was successfully removed from the team, false otherwise.
     */
    @Override
    public boolean removePersonFromTeam(String teamName, String personName) throws KnowledgeGraphConstructorException {
        OWLNamedIndividual person = manager.getOWLDataFactory().getOWLNamedIndividual(DEFAULT_NAMESPACE.getValue(String.class) + personName); //Person IRI
        OWLClass team = manager.getOWLDataFactory().getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + teamName); //Team IRI
        Set<OWLAxiom> axioms = new HashSet<>();
        reasoner.flush();
        if (EntitySearcher.getInstances(team, populatedOntology).noneMatch(x -> x.asOWLNamedIndividual().equals(person))) {
            String message = "Person " + person.getIRI() + " is not a member of team " + team.getIRI() + ".";
            logger.warn(message);
            throw new KnowledgeGraphConstructorException(message);
        } else {
            logger.warn("Deleting person " + person.getIRI() + " from team " + team.getIRI() + ".");
            axioms.add(manager.getOWLDataFactory().getOWLClassAssertionAxiom(team, person));
            populatedOntology.removeAxioms(axioms);
        }
        reasoner.flush();
        if (!reasoner.isConsistent()) {
            logger.error("Ontology is inconsistent, reverting changes.");
            populatedOntology.addAxioms(axioms);
            throw new KnowledgeGraphConstructorException("Removing person " + person.getIRI() + " from team " + team.getIRI() + " led to an inconsistency");
        }
        return true;
    }


    public Model getGraph(KnowledgeLandscapeUpdater updater) {
        // Make every individual different
        populatedOntology.addAxioms(manager.getOWLDataFactory().getOWLDifferentIndividualsAxiom(populatedOntology.getIndividualsInSignature()));
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
        infOnt.asGraphModel().setNsPrefix("kl", DEFAULT_NAMESPACE.getValue(String.class));
        return infOnt.asGraphModel();
    }

    private boolean entityInClass(OWLClass owlClass, OWLNamedIndividual person) {
        reasoner.flush();
        return reasoner.getInstances(owlClass, false).containsEntity(person);
    }


    private boolean checkFeatureValue(OWLClass featureClass, OWLClass valueClass) {
        for (OWLDisjointUnionAxiom axiom : populatedOntology.getAxioms(AxiomType.DISJOINT_UNION)) {
            if (axiom.getOWLClass().equals(featureClass)) {
                return axiom.getOWLDisjointClassesAxiom().contains(valueClass);
            }
        }
        return false;
    }


    private Set<OWLAxiom> removeRespectiveKnowledgeAssetObservations(OWLNamedIndividual individual) {
        Set<OWLAxiom> axioms = new HashSet<>();
        // get related persons
        EntitySearcher.getInstances(ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeObservationClass(), populatedOntology).filter(knowledgeObservation -> EntitySearcher.getObjectPropertyValues(knowledgeObservation, ontoKnowledgeLandscapeObjectPropertyFactory.getHasPerson(), populatedOntology).anyMatch(ind -> ind.equals(individual))).forEach(x -> remover.visit(x.asOWLNamedIndividual()));
        // get related knowledge assets
        EntitySearcher.getInstances(ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeObservationClass(), populatedOntology).filter(knowledgeObservation -> EntitySearcher.getObjectPropertyValues(knowledgeObservation, ontoKnowledgeLandscapeObjectPropertyFactory.getHasKnowledgeAsset(), populatedOntology).anyMatch(ind -> ind.equals(individual))).forEach(x -> remover.visit(x.asOWLNamedIndividual()));
        remover.getChanges().forEach(axiom -> axioms.add(axiom.getAxiom()));
        remover.reset();
        return axioms;
    }


    private Set<OWLAxiom> removeAssignedFeatures(OWLNamedIndividual knowledgeAsset) {
        Set<OWLAxiom> axioms = new HashSet<>();
        for (OWLClassExpression owlClassExpression : EntitySearcher.getSubClasses(ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetFeatureClass(), populatedOntology).collect(Collectors.toSet())) {
            String featureName = owlClassExpression.asOWLClass().getIRI().getFragment();
            OWLObjectProperty featureObjectProperty = manager.getOWLDataFactory().getOWLObjectProperty(DEFAULT_NAMESPACE.getValue(String.class) + "has" + featureName);
            EntitySearcher.getObjectPropertyValues(knowledgeAsset, featureObjectProperty, populatedOntology).forEach(x -> remover.visit(x.asOWLNamedIndividual()));
        }
        remover.getChanges().forEach(x -> axioms.add(x.getAxiom()));
        remover.reset();
        return axioms;
    }


    public OWLOntology getPopulatedOntology() {
        return populatedOntology;
    }


    public void getAxiomsCausingInconsistency(OWLClass owlClass) {
        OWLReasonerFactory factory = new ReasonerFactory() {
            protected OWLReasoner createHermiTOWLReasoner(org.semanticweb.HermiT.Configuration configuration, OWLOntology ontology) {
                // don't throw an exception since otherwise we cannot compute explanations
                configuration.throwInconsistentOntologyException = false;
                return new Reasoner(configuration, ontology);
            }
        };


        BlackBoxExplanation exp = new BlackBoxExplanation(populatedOntology, factory, reasoner);
        TransactionAwareSingleExpGen multExplanator = new HSTExplanationGenerator(exp).getSingleExplanationGenerator();
        // Now we can get explanations for the inconsistency
        Set<OWLAxiom> explanation = multExplanator.getExplanation(owlClass);
        System.out.println("------------------");
        System.out.println("HermiT: Axioms Causing Inconsistency");
        System.out.println("------------------");
        for (OWLAxiom axiom : explanation) {
            System.out.println(axiom);
        }
        System.out.println("------------------");

    }
}
