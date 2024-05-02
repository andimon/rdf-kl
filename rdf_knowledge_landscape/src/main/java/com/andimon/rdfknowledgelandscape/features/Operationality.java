package com.andimon.rdfknowledgelandscape.features;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;

/**
 * Represents the operationality knowledge asset feature.
 */
public enum Operationality implements Feature {
    /**
     * Declarative operationality value
     */
    DECLARATIVE(DECLARATIVE_OPERATIONALITY_VALUE_IRI.getValue(String.class)),
    /**
     * Procedural operationality value
     */
    PROCEDURAL(PROCEDURAL_OPERATIONALITY_VALUE_IRI.getValue(String.class)),
    /**
     * Causal operationality value
     */
    CAUSAL(CAUSAL_OPERATIONALITY_VALUE_IRI.getValue(String.class)),
    /**
     * Conditional operationality value
     */
    CONDITIONAL(CONDITIONAL_OPERATIONALITY_VALUE_IRI.getValue(String.class)),
    /**
     * Relational operationality value
     */
    RELATIONAL(RELATIONAL_OPERATIONALITY_VALUE_IRI.getValue(String.class));


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
