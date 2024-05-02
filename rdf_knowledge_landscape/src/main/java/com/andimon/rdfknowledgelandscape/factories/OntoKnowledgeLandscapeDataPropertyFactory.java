package com.andimon.rdfknowledgelandscape.factories;

import org.semanticweb.owlapi.model.OWLDataProperty;

public interface OntoKnowledgeLandscapeDataPropertyFactory {
    /**
     * Data property used to assign a magnitude to a knowledge observation.
     * @return The OWL data property.
     */
    OWLDataProperty getHasMagnitudeProperty();
}
