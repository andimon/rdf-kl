package com.andimon.rdfknowledgelandscape.features;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;

/**
 * Represents the sociality knowledge asset feature.
 */
public enum Sociality implements Feature {
    /**
     * Individual sociality value
     */
    INDIVIDUAL(INDIVIDUAL_SOCIALITY_VALUE_IRI.getValue(String.class)),
    /**
     * Social sociality value
     */
    SOCIAL(SOCIAL_SOCIALITY_VALUE_IRI.getValue(String.class));

    private final String valueIRI;

    Sociality(String valueIRI) {
        this.valueIRI = valueIRI;
    }

    @Override
    public String getFeatureIRI() {
        return SOCIALITY_CLASS_IRI.getValue(String.class);
    }

    @Override
    public String getValueIRI() {
        return valueIRI;
    }
}
