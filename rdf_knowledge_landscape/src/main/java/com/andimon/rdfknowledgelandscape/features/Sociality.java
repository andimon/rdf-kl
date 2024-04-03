package com.andimon.rdfknowledgelandscape.features;

import static com.andimon.rdfknowledgelandscape.parameters.KnowledgeLandscapeProperties.*;

public enum Sociality implements Feature {
    INDIVIDUAL(INDIVIDUAL_SOCIALITY_VALUE_IRI.getValue(String.class)),
    SOCIAL(SOCIAL_SOCIALITY_VALUE_IRI.getValue(String.class)),
    UNDEFINED(UNDEFINED_SOCIALITY_VALUE_IRI.getValue(String.class));


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
