package com.andimon.rdf_knowledge_landscape;

import com.andimon.rdf_knowledge_landscape.factories.OntoKnowledgeLandscapeOwlClassFactory;
import com.andimon.rdf_knowledge_landscape.factories.OntoKnowledgeLandscapeOwlClassFactoryImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KnowledgeGraphConstructor implements KnowledgeEvents {
    private OWLOntology populatedOntology;
    private final PrefixManager prefixManager;

    private final OWLOntologyManager owlManager;

    private OWLReasonerFactory reasonerFactory;

    private OWLReasoner reasoner;

    private OntoKnowledgeLandscapeOwlClassFactory ontoKnowledgeLandscapeOwlClassFactory;

    protected static final Logger logger = LogManager.getLogger(KnowledgeGraphConstructor.class);

    public KnowledgeGraphConstructor(OntoKnowledgeLandscape ontoKnowledgeLandscape) {
        ontoKnowledgeLandscapeOwlClassFactory = new OntoKnowledgeLandscapeOwlClassFactoryImpl();
        populatedOntology = ontoKnowledgeLandscape.getOntology();
        prefixManager = ontoKnowledgeLandscape.getPrefixManager();
        owlManager = ontoKnowledgeLandscape.getOntology().getOWLOntologyManager();
        reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(populatedOntology);
    }

    /**
     * Check if person already exists
     *
     * @param personName
     * @return
     */
    private boolean entityInClass(OWLClass owlClass, OWLNamedIndividual person) {
        reasoner.flush();
        System.out.println(owlClass.getIRI().getFragment() + " instances " + reasoner.getInstances(owlClass, false));
        return reasoner.getInstances(owlClass, false).containsEntity(person);
    }

    private void makeIndividualDifferentFromOtherIndividualsInAClass(OWLNamedIndividual owlNamedIndividual, OWLClass owlClass) {
        OWLReasonerFactory reasonerFactory = new ReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(populatedOntology);
        OWLClass personClass = ontoKnowledgeLandscapeOwlClassFactory.getPersonClass();
        OWLDataFactory owlDataFactory = owlManager.getOWLDataFactory();
        for (OWLNamedIndividual individual : reasoner.getInstances(personClass, false).getFlattened()) {
            owlManager.addAxiom(populatedOntology, owlDataFactory.getOWLDifferentIndividualsAxiom(owlNamedIndividual, individual));
        }
    }

    private void removeAllAxiomsContainingSomeEntityInAnOntology(OWLOntology ontology, OWLEntity entity) {
        for (OWLAxiom axiom : ontology.getAxioms()) {
            if (axiom.containsEntityInSignature(entity)) {
                populatedOntology.removeAxiom(axiom);
            }
        }
    }

    @Override
    public boolean personJoinsOrganisation(String personName) {
        OWLClass personClass = owlManager.getOWLDataFactory().getOWLClass(":Person", prefixManager);
        OWLNamedIndividual person = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + personName, prefixManager);
        if (entityInClass(personClass, person)) {
            logger.warn(person.getIRI() + " is already an instance of class " + personClass.getIRI());
            return false;
        } else {
            logger.info("Creating person with IRI " + person.getIRI());
            OWLDeclarationAxiom declarationAxiom = owlManager.getOWLDataFactory().getOWLDeclarationAxiom(person);
            OWLClassAssertionAxiom classAssertion = owlManager.getOWLDataFactory().getOWLClassAssertionAxiom(personClass, person);
            owlManager.addAxiom(populatedOntology, declarationAxiom);
            owlManager.addAxiom(populatedOntology, classAssertion);
            makeIndividualDifferentFromOtherIndividualsInAClass(person, personClass);
            return true;
        }
    }

    @Override
    public boolean personLeavesOrganisation(String personName) {
        OWLClass personClass = owlManager.getOWLDataFactory().getOWLClass(":Person", prefixManager);
        OWLNamedIndividual person = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + personName, prefixManager);
        if (!entityInClass(personClass, person)) {
            logger.warn(prefixManager.getDefaultPrefix() + personName + " is not an instance of class " + prefixManager.getDefaultPrefix() + "Person");
            return false;
        } else {
            logger.info("Remove all axioms containing person with IRI " + prefixManager.getDefaultPrefix() + personName);
            removeAllAxiomsContainingSomeEntityInAnOntology(populatedOntology, person);
            return true;
        }
    }


    private String suffixRemover(String target, String suffix) {
        int index = target.lastIndexOf(suffix);
        if (index > 0) {
            return target.substring(0, index);
        }
        return target;
    }

    private void checkValueForKnowledgeAssetFeature(OWLClass knowledgeAssetFeature, String featuresValue) throws Exception {
        String featureName = knowledgeAssetFeature.getIRI().getFragment();
        String knowledgeAssetFeatureValuesSuffix = featureName + "Value";
        Set<OWLClass> featureValues = reasoner.getSubClasses(knowledgeAssetFeature, true).getFlattened();
        Set<String> featureValuesCondensed = featureValues.stream().map(x -> suffixRemover(x.getIRI().getFragment(), knowledgeAssetFeatureValuesSuffix)).collect(Collectors.toSet());
        if (!featureValuesCondensed.contains(featuresValue)) {
            throw new Exception("Value " + featuresValue + " is not valid for feature " + featureName + ". Possible values for this feature are: " + featureValuesCondensed);
        }
    }

    private void addKnowledgeAssetFeatures(OWLNamedIndividual knowledgeAsset, Map<String, String> features) {
        String knowledgeAssetName = knowledgeAsset.getIRI().getFragment();
        OWLClass knowledgeAssetFeatures = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetFeatureClass();
        for (OWLClass feature : reasoner.getSubClasses(knowledgeAssetFeatures, true).getFlattened()) {
            String featureName = feature.getIRI().getFragment();
            String value = features.get(featureName);
            String valueClassName = value + featureName + "Value";
            OWLClass valueClass = owlManager.getOWLDataFactory().getOWLClass(":" + valueClassName, prefixManager);
            OWLObjectProperty objectProperty = owlManager.getOWLDataFactory().getOWLObjectProperty(":has" + featureName, prefixManager);
            OWLNamedIndividual knowledgeAssetFeatureAssignment = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAssetName + featureName + "Assignment", prefixManager);
            OWLDeclarationAxiom knowledgeAssetFeatureAssignmentDeclaration = owlManager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeAssetFeatureAssignment);
            OWLClassAssertionAxiom knowledgeAssetFeatureAssignmentClass = owlManager.getOWLDataFactory().getOWLClassAssertionAxiom(valueClass, knowledgeAssetFeatureAssignment);
            OWLObjectPropertyAssertionAxiom objectPropertyAssertionAxiom = owlManager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(objectProperty, knowledgeAsset, knowledgeAssetFeatureAssignment);
            makeIndividualDifferentFromOtherIndividualsInAClass(knowledgeAssetFeatureAssignment, valueClass);
            owlManager.addAxiom(populatedOntology, knowledgeAssetFeatureAssignmentDeclaration);
            owlManager.addAxiom(populatedOntology, knowledgeAssetFeatureAssignmentClass);
            owlManager.addAxiom(populatedOntology, objectPropertyAssertionAxiom);
        }
    }

    @Override
    public boolean knowledgeAssetIdentification(String knowledgeAssetName, Map<String, String> features) throws Exception {
        OWLClass knowledgeAssetFeatures = owlManager.getOWLDataFactory().getOWLClass(":KnowledgeAssetFeature", prefixManager);
        OWLNamedIndividual knowledgeAsset = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAssetName, prefixManager);
        OWLClass knowledgeAssetClass = owlManager.getOWLDataFactory().getOWLClass(":KnowledgeAsset", prefixManager);
        if (entityInClass(knowledgeAssetClass, knowledgeAsset)) {
            logger.warn("Knowledge Asset " + knowledgeAsset.getIRI() + " is already an instance of class " + knowledgeAssetClass.getIRI());
            return false;
        }
        for (OWLClass feature : reasoner.getSubClasses(knowledgeAssetFeatures, true).getFlattened()) {
            String featureName = feature.getIRI().getFragment();
            if (!features.containsKey(featureName)) {
                throw new Exception("Knowledge asset feature: " + featureName + " not specified for knowledge asset " + knowledgeAssetName);
            }
            checkValueForKnowledgeAssetFeature(feature, features.get(featureName));
        }
        logger.info("Creating knowledge asset with IRI " + knowledgeAsset.getIRI());
        OWLDeclarationAxiom declarationAxiom = owlManager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeAsset);
        OWLClassAssertionAxiom classAssertion = owlManager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeAssetClass, knowledgeAsset);
        owlManager.addAxiom(populatedOntology, declarationAxiom);
        owlManager.addAxiom(populatedOntology, classAssertion);
        addKnowledgeAssetFeatures(knowledgeAsset, features);
        return true;
    }

    @Override
    public boolean knowledgeAssetDiscarded(String knowledgeAssetName) {
        OWLClass knowledgeAssetClass = owlManager.getOWLDataFactory().getOWLClass(":KnowledgeAsset", prefixManager);
        OWLNamedIndividual knowledgeAsset = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAssetName, prefixManager);
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
    public boolean knowledgeObservation(String knowledgeAssetName, String personName, int n) throws Exception {
        OWLClass knowledgeObservationClass = owlManager.getOWLDataFactory().getOWLClass(":KnowledgeObservation", prefixManager);
        OWLClass knowledgeAssetClass = owlManager.getOWLDataFactory().getOWLClass(":KnowledgeAsset", prefixManager);
        OWLClass personClass = owlManager.getOWLDataFactory().getOWLClass(":Person", prefixManager);
        OWLNamedIndividual knowledgeAsset = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAssetName, prefixManager);
        OWLNamedIndividual person = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + personName, prefixManager);
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset)) {
            logger.warn("Knowledge Asset " + knowledgeAsset.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(personClass, person)) {
            logger.warn("Person " + knowledgeAsset.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The person is needs to be identified/created first.");
            return false;
        } else if (n < 0) {
            throw new Exception(n + " is not a non-negative integer.");
        } else {
            /* Create observation */
            OWLNamedIndividual knowledgeObservation = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + personName + knowledgeAssetName, prefixManager);
            OWLObjectProperty hasPerson = OWLManager.getOWLDataFactory().getOWLObjectProperty(":hasPerson", prefixManager);
            OWLObjectProperty hasKnowledgeAsset = OWLManager.getOWLDataFactory().getOWLObjectProperty(":hasKnowledgeAsset", prefixManager);
            OWLDataProperty hasMagnitude = OWLManager.getOWLDataFactory().getOWLDataProperty(":hasKnowledgeAsset", prefixManager);
            makeIndividualDifferentFromOtherIndividualsInAClass(knowledgeObservation, knowledgeObservationClass);
            OWLDeclarationAxiom declarationAxiom = owlManager.getOWLDataFactory().getOWLDeclarationAxiom(knowledgeObservation);
            OWLClassAssertionAxiom classAssertion = owlManager.getOWLDataFactory().getOWLClassAssertionAxiom(knowledgeObservationClass, knowledgeObservation);
            OWLObjectPropertyAssertionAxiom hasPersonAssertion = owlManager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(hasPerson, knowledgeObservation, person);
            OWLObjectPropertyAssertionAxiom hasKnowledgeAssetAssertion = owlManager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(hasKnowledgeAsset, knowledgeObservation, knowledgeAsset);
            OWLDataPropertyAssertionAxiom hasMagnitudeAssertion = owlManager.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(hasMagnitude, knowledgeObservation, owlManager.getOWLDataFactory().getOWLLiteral(n));
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
        OWLNamedIndividual knowledgeAsset1 = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset1Name, prefixManager);
        OWLNamedIndividual knowledgeAsset2 = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset2Name, prefixManager);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " related to " + knowledgeAsset2.getIRI());
            OWLObjectProperty relatedTo = owlManager.getOWLDataFactory().getOWLObjectProperty(":relatedTo", prefixManager);
            OWLObjectPropertyAssertionAxiom relatedToAssertion = owlManager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(relatedTo, knowledgeAsset1, knowledgeAsset2);
            populatedOntology.addAxiom(relatedToAssertion);
            return true;
        }
    }

    @Override
    public boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name) {
        OWLNamedIndividual knowledgeAsset1 = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset1Name, prefixManager);
        OWLNamedIndividual knowledgeAsset2 = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset2Name, prefixManager);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " dependent on " + knowledgeAsset2.getIRI());
            OWLObjectProperty dependentOn = owlManager.getOWLDataFactory().getOWLObjectProperty(":dependsOn", prefixManager);
            OWLObjectPropertyAssertionAxiom dependentOnAssertion = owlManager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(dependentOn, knowledgeAsset1, knowledgeAsset2);
            populatedOntology.addAxiom(dependentOnAssertion);
            return true;
        }
    }

    @Override
    public boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name) {
        OWLNamedIndividual knowledgeAsset1 = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset1Name, prefixManager);
        OWLNamedIndividual knowledgeAsset2 = owlManager.getOWLDataFactory().getOWLNamedIndividual(":" + knowledgeAsset2Name, prefixManager);
        OWLClass knowledgeAssetClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass();
        if (!entityInClass(knowledgeAssetClass, knowledgeAsset1)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else if (!entityInClass(knowledgeAssetClass, knowledgeAsset2)) {
            logger.warn("Knowledge Asset " + knowledgeAsset1.getIRI() + " is not an instance of class " + knowledgeAssetClass.getIRI() + " The knowledge is needs to be identified/created first.");
            return false;
        } else {
            logger.info("Creating relation: " + knowledgeAsset1.getIRI() + " composed of " + knowledgeAsset2.getIRI());
            OWLObjectProperty composedOf = owlManager.getOWLDataFactory().getOWLObjectProperty(":composedOf", prefixManager);
            OWLObjectPropertyAssertionAxiom composedOfAssertion = owlManager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(composedOf, knowledgeAsset1, knowledgeAsset2);
            populatedOntology.addAxiom(composedOfAssertion);
            return true;
        }
    }

    public String getPopulatedOntology() throws OWLOntologyStorageException, IOException, OWLOntologyCreationException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        owlManager.saveOntology(populatedOntology, new TurtleDocumentFormat(), new StreamDocumentTarget(out));
        String data = out.toString();
        out.close();
        return data;
    }

    public String generateGraph() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
        reasoner.flush();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OWLOntology infOnt = owlManager.createOntology(populatedOntology.getAxioms());
        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
        iog.fillOntology(owlManager.getOWLDataFactory(), infOnt);
        owlManager.saveOntology(infOnt, new TurtleDocumentFormat(), new StreamDocumentTarget(out));
        String data = out.toString();
        out.close();
        return data;
    }
}
