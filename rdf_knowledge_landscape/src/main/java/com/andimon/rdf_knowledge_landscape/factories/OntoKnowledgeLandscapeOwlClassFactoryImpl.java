package com.andimon.rdf_knowledge_landscape.factories;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


public class OntoKnowledgeLandscapeOwlClassFactoryImpl implements OntoKnowledgeLandscapeOwlClassFactory {
    private String namespace;
    private PrefixManager pm;
    private OWLOntologyManager owlOntologyManager;
    private OWLDataFactory owlDataFactory;

    public OntoKnowledgeLandscapeOwlClassFactoryImpl() {
        //Create OWL ontology manager
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
        namespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        pm = new DefaultPrefixManager(null, null, namespace);
    }

    @Override
    public OWLClass getPersonClass() {
        return owlDataFactory.getOWLClass(":Person", pm);
    }

    @Override
    public OWLClass getKnowledgeAssetClass() {
        return owlDataFactory.getOWLClass(":KnowledgeAsset", pm);
    }

    @Override
    public OWLClass getKnowledgeObservationClass() {
        return owlDataFactory.getOWLClass(":KnowledgeObservation", pm);
    }

    @Override
    public OWLClass getKnowledgeAssetFeatureClass() {
        return owlDataFactory.getOWLClass(":KnowledgeAssetFeature", pm);
    }

    /* Category - Knowledge Asset Feature */
    @Override
    public OWLClass getCategoryClass() {
        return owlDataFactory.getOWLClass(":Category", pm);
    }

    @Override
    public OWLClass getTechnicalCategoryValueClass() {
        return owlDataFactory.getOWLClass(":TechnicalCategoryValue", pm);
    }

    @Override
    public OWLClass getBusinessCategoryValueClass() {
        return owlDataFactory.getOWLClass(":BusinessCategoryValue", pm);
    }

    @Override
    public OWLClass getUndefinedCategoryValueClass() {
        return owlDataFactory.getOWLClass(":UndefinedCategoryValue", pm);
    }


    @Override
    public OWLClass getVisibilityClass() {
        return owlDataFactory.getOWLClass(":Visibility", pm);
    }

    @Override
    public OWLClass getTacitVisibilityValueClass() {
        return owlDataFactory.getOWLClass(":TacitVisibilityValue", pm);
    }

    @Override
    public OWLClass getExplicitVisibilityValueClass() {
        return owlDataFactory.getOWLClass(":ExplicitVisibilityValue", pm);
    }

    @Override
    public OWLClass getUndefinedVisibilityValueClass() {
        return owlDataFactory.getOWLClass(":UndefinedVisibilityValue", pm);

    }

    @Override
    public OWLClass getSocialityClass() {
        return owlDataFactory.getOWLClass(":Sociality", pm);
    }

    @Override
    public OWLClass getIndividualSocialityValueClass() {
        return owlDataFactory.getOWLClass(":IndividualSocialityValue", pm);
    }

    @Override
    public OWLClass getSocialSocialityValueClass() {
        return owlDataFactory.getOWLClass(":SocialSocialityValue", pm);
    }

    @Override
    public OWLClass getUndefinedSocialityValueClass() {
        return owlDataFactory.getOWLClass(":UndefinedSocialityValue", pm);
    }

    @Override
    public OWLClass getOperationalityClass() {
        return owlDataFactory.getOWLClass(":Operationality", pm);
    }

    @Override
    public OWLClass getDeclarativeOperationalityValueClass() {
        return owlDataFactory.getOWLClass(":DeclarativeOperationalityValue", pm);
    }

    @Override
    public OWLClass getProceduralOperationalityValueClass() {
        return owlDataFactory.getOWLClass(":ProceduralOperationalityValue", pm);
    }

    @Override
    public OWLClass getCausalOperationalityValueClass() {
        return owlDataFactory.getOWLClass(":CausalOperationalityValue", pm);
    }

    @Override
    public OWLClass getConditionalOperationalityValueClass() {
        return owlDataFactory.getOWLClass(":ConditionalOperationalityValue", pm);
    }

    @Override
    public OWLClass getUndefinedOperationalityValueClass() {
        return owlDataFactory.getOWLClass(":UndefinedOperationalityValue", pm);
    }
}
