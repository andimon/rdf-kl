package com.andimon.rdf_knowledge_landscape.factories;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class OntoKnowledgeLandscapeDataPropertyFactoryImpl implements  OntoKnowledgeLandscapeDataPropertyFactory{
    private final PrefixManager pm;
    private final OWLDataFactory owlDataFactory;

    public OntoKnowledgeLandscapeDataPropertyFactoryImpl() {
        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
        String namespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        pm = new DefaultPrefixManager(null, null, namespace);
    }


    @Override
    public OWLDataProperty getHasMagnitudeProperty() {
        return owlDataFactory.getOWLDataProperty(":hasMagnitude", pm);
    }
}
