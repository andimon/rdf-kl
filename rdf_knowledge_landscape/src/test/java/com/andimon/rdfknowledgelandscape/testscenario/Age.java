package com.andimon.rdfknowledgelandscape.testscenario;

import com.andimon.rdfknowledgelandscape.features.Feature;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;

public enum Age implements Feature {
    OLD(KL_NAMESPACE.getValue(String.class)+"OldAgeValue"),
    ESTABLISHED(KL_NAMESPACE.getValue(String.class)+"EstablishedAgeValue"),
    NEW(KL_NAMESPACE.getValue(String.class)+"NewAgeValue");

    private final String valueIRI;

    Age(String value) {
        this.valueIRI = value;
    }

    @Override
    public String getFeatureIRI() {
        return KL_NAMESPACE.getValue(String.class)+"Age";
    }

    @Override
    public String getValueIRI() {
        return valueIRI;
    }
}
