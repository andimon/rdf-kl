package com.andimon.rdfknowledgelandscape.factories;

import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * Factory for creating knowledge landscape owl data properties.
 */
public interface OntoKnowledgeLandscapeDataPropertyFactory {
    /**
     * Data property used to assign a magnitude to a knowledge observation.
     * @return The OWL data property.
     */
    OWLDataProperty getHasMagnitudeProperty();
}
