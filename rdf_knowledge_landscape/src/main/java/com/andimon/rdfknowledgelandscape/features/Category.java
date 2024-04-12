package com.andimon.rdfknowledgelandscape.features;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;

public enum Category implements Feature {
    TECHNICAL(TECHNICAL_CATEGORY_VALUE_IRI.getValue(String.class)),
    GENERAL(GENERAL_CATEGORY_VALUE_IRI.getValue(String.class)),
    BUSINESS(BUSINESS_CATEGORY_VALUE_IRI.getValue(String.class)),
    UNDEFINED(UNDEFINED_CATEGORY_VALUE_IRI.getValue(String.class));

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
