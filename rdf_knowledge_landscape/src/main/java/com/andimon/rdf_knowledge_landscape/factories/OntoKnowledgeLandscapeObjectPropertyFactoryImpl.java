package com.andimon.rdf_knowledge_landscape.factories;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class OntoKnowledgeLandscapeObjectPropertyFactoryImpl implements OntoKnowledgeLandscapeObjectPropertyFactory{
    private final PrefixManager pm;
    private final OWLDataFactory owlDataFactory;

    public OntoKnowledgeLandscapeObjectPropertyFactoryImpl() {
        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
        String namespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        pm = new DefaultPrefixManager(null, null, namespace);
    }
    @Override
    public OWLObjectProperty getRelatedToProperty() {
        return owlDataFactory.getOWLObjectProperty(":relatedTo", pm);
    }

    @Override
    public OWLObjectProperty getDependsOnProperty() {
        return owlDataFactory.getOWLObjectProperty(":dependsOn", pm);
    }

    @Override
    public OWLObjectProperty getComposedOfProperty() {
        return owlDataFactory.getOWLObjectProperty(":composedOf", pm);
    }

    @Override
    public OWLObjectProperty getHasCategoryProperty() {
        return owlDataFactory.getOWLObjectProperty(":hasCategory",pm);
    }

    @Override
    public OWLObjectProperty getHasVisibilityProperty() {
        return owlDataFactory.getOWLObjectProperty(":hasVisibility",pm);
    }

    @Override
    public OWLObjectProperty getHasSocialityProperty() {
        return owlDataFactory.getOWLObjectProperty(":hasSociality", pm);
    }

    @Override
    public OWLObjectProperty getHasOperationalityProperty() {
        return owlDataFactory.getOWLObjectProperty(":hasOperationality", pm);
    }

    @Override
    public OWLObjectProperty getHasPerson() {
        return owlDataFactory.getOWLObjectProperty(":hasPerson", pm);
    }

    @Override
    public OWLObjectProperty getHasKnowledgeAsset() {
        return owlDataFactory.getOWLObjectProperty(":hasKnowledgeAsset", pm);
    }
}
