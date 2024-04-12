package com.andimon.rdfknowledgelandscape.factories;

import com.github.owlcs.ontapi.OntManagers;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;


public class DefaultOntoKnowledgeLandscapeOwlClassFactory implements OntoKnowledgeLandscapeOwlClassFactory {
    private final OWLDataFactory owlDataFactory;

    public DefaultOntoKnowledgeLandscapeOwlClassFactory() {
        //Create OWL ontology manager
        OWLOntologyManager owlOntologyManager = OntManagers.createManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }

    @Override
    public OWLClass getPersonClass() {
        String IRI = PERSON_CLASS_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getKnowledgeAssetClass() {
        String IRI = KNOWLEDGE_ASSET_CLASS_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getKnowledgeObservationClass() {
        String IRI = KNOWLEDGE_OBSERVATION_CLASS_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getKnowledgeAssetFeatureClass() {
        String IRI = KNOWLEDGE_ASSET_FEATURE_CLASS_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    /* Category - Knowledge Asset Feature */
    @Override
    public OWLClass getCategoryClass() {
        String IRI = CATEGORY_CLASS_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getTechnicalCategoryValueClass() {
        String IRI = TECHNICAL_CATEGORY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getBusinessCategoryValueClass() {
        String IRI = BUSINESS_CATEGORY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getGeneralCategoryValueClass() {
        String IRI = GENERAL_CATEGORY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }



    @Override
    public OWLClass getVisibilityClass() {
        String IRI = VISIBILITY_CLASS_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getTacitVisibilityValueClass() {
        String IRI = TACIT_VISIBILITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getExplicitVisibilityValueClass() {
        String IRI = EXPLICIT_VISIBILITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }


    @Override
    public OWLClass getSocialityClass() {
        String IRI = SOCIALITY_CLASS_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getIndividualSocialityValueClass() {
        String IRI = INDIVIDUAL_SOCIALITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getSocialSocialityValueClass() {
        String IRI = SOCIAL_SOCIALITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }


    @Override
    public OWLClass getOperationalityClass() {
        String IRI = OPERATIONALITY_CLASS_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getDeclarativeOperationalityValueClass() {
        String IRI = DECLARATIVE_OPERATIONALITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getProceduralOperationalityValueClass() {
        String IRI = PROCEDURAL_OPERATIONALITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getCausalOperationalityValueClass() {
        String IRI = CAUSAL_OPERATIONALITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getConditionalOperationalityValueClass() {
        String IRI = CONDITIONAL_OPERATIONALITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }

    @Override
    public OWLClass getRelationalOperationalityValueClass() {
        String IRI = RELATIONAL_OPERATIONALITY_VALUE_IRI.getValue(String.class);
        return owlDataFactory.getOWLClass(IRI);
    }




}
