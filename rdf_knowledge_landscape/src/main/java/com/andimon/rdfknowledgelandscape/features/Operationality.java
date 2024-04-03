package com.andimon.rdfknowledgelandscape.features;

import static com.andimon.rdfknowledgelandscape.parameters.KnowledgeLandscapeProperties.*;

public enum Operationality implements Feature {
    DECLARATIVE(DECLARATIVE_OPERATIONALITY_VALUE_IRI.getValue(String.class)),
    PROCEDURAL(PROCEDURAL_OPERATIONALITY_VALUE_IRI.getValue(String.class)),
    CAUSAL(CAUSAL_OPERATIONALITY_VALUE_IRI.getValue(String.class)),
    CONDITIONAL(CONDITIONAL_OPERATIONALITY_VALUE_IRI.getValue(String.class)),
    RELATIONAL(RELATIONAL_OPERATIONALITY_VALUE_IRI.getValue(String.class)),

    UNDEFINED(UNDEFINED_OPERATIONALITY_VALUE_IRI.getValue(String.class));

    private final String valueIRI;

    Operationality(String valueIRI) {
        this.valueIRI = valueIRI;
    }

    @Override
    public String getFeatureIRI() {
        return OPERATIONALITY_CLASS_IRI.getValue(String.class);
    }

    @Override
    public String getValueIRI() {
        return valueIRI;
    }
}
