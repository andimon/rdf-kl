package com.andimon.rdfknowledgelandscape.factories;

import com.github.owlcs.ontapi.OntManagers;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;

public class DefaultOntoKnowledgeLandscapeObjectPropertyFactory implements OntoKnowledgeLandscapeObjectPropertyFactory {
    private final OWLDataFactory owlDataFactory;

    public DefaultOntoKnowledgeLandscapeObjectPropertyFactory() {
        OWLOntologyManager owlOntologyManager = OntManagers.createManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
        String namespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
    }

    @Override
    public OWLObjectProperty getRelatedToProperty() {
        String IRI = RELATED_TO_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }

    @Override
    public OWLObjectProperty getDependsOnProperty() {
        String IRI = DEPENDS_ON_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }

    @Override
    public OWLObjectProperty getComposedOfProperty() {
        String IRI = COMPOSED_OF_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }

    @Override
    public OWLObjectProperty getHasCategoryProperty() {
        String IRI = HAS_CATEGORY_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }

    @Override
    public OWLObjectProperty getHasVisibilityProperty() {
        String IRI = HAS_VISIBILITY_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }

    @Override
    public OWLObjectProperty getHasSocialityProperty() {
        String IRI = HAS_SOCIALITY_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }

    @Override
    public OWLObjectProperty getHasOperationalityProperty() {
        String IRI = HAS_OPERATIONALITY_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }

    @Override
    public OWLObjectProperty getHasPerson() {
        String IRI = HAS_PERSON_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }

    @Override
    public OWLObjectProperty getHasKnowledgeAsset() {
        String IRI = HAS_KNOWLEDGE_ASSET_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLObjectProperty(IRI);
    }
}
