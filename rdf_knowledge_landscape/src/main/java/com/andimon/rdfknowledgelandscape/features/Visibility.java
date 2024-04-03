package com.andimon.rdfknowledgelandscape.features;

import static com.andimon.rdfknowledgelandscape.parameters.KnowledgeLandscapeProperties.*;

public enum Visibility implements Feature {
    TACIT(TACIT_VISIBILITY_VALUE_IRI.getValue(String.class)),
    EXPLICIT(EXPLICIT_VISIBILITY_VALUE_IRI.getValue(String.class)),

    UNDEFINED(UNDEFINED_VISIBILITY_VALUE_IRI.getValue(String.class));

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
