package com.andimon.rdfknowledgelandscape.features;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;

/**
 * Represents the category knowledge asset feature.
 */
public enum Category implements Feature {
    /**
     * Technical category value
     */
    TECHNICAL(TECHNICAL_CATEGORY_VALUE_IRI.getValue(String.class)),
    /**
     * General category value
     */
    GENERAL(GENERAL_CATEGORY_VALUE_IRI.getValue(String.class)),
    /**
     * Business category value
     */
    BUSINESS(BUSINESS_CATEGORY_VALUE_IRI.getValue(String.class));

    private final String valueIRI;

    Category(String value) {
        this.valueIRI = value;
    }

    @Override
    public String getFeatureIRI() {
        return CATEGORY_CLASS_IRI.getValue(String.class);
    }

    @Override
    public String getValueIRI() {
        return valueIRI;
    }
}
