package com.andimon.rdfknowledgelandscape.factories;

import com.github.owlcs.ontapi.OntManagers;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import static com.andimon.rdfknowledgelandscape.parameters.KnowledgeLandscapeProperties.HAS_MAGNITUDE_PROPERTY_IRI;

public class DefaultOntoKnowledgeLandscapeDataPropertyFactory implements OntoKnowledgeLandscapeDataPropertyFactory {
    private final OWLDataFactory owlDataFactory;

    public DefaultOntoKnowledgeLandscapeDataPropertyFactory() {
        OWLOntologyManager owlOntologyManager = OntManagers.createManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }


    @Override
    public OWLDataProperty getHasMagnitudeProperty() {
        String IRI = HAS_MAGNITUDE_PROPERTY_IRI.getValue(String.class);
        return owlDataFactory.getOWLDataProperty(IRI);
    }
}
