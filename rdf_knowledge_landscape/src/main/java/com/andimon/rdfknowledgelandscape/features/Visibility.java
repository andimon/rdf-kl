package com.andimon.rdfknowledgelandscape.features;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;

/**
 * Represents the visibility knowledge asset feature.
 */
public enum Visibility implements Feature {
    /**
     * Tacit visibility value
     */
    TACIT(TACIT_VISIBILITY_VALUE_IRI.getValue(String.class)),
    /**
     * Explicit visibility value
     */
    EXPLICIT(EXPLICIT_VISIBILITY_VALUE_IRI.getValue(String.class));

    private final String valueIRI;

    Visibility(String valueIRI) {
        this.valueIRI = valueIRI;
    }

    @Override
    public String getFeatureIRI() {
        return VISIBILITY_CLASS_IRI.getValue(String.class);
    }

    @Override
    public String getValueIRI() {
        return valueIRI;
    }
}
