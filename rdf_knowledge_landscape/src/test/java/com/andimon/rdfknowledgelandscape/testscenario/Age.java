package com.andimon.rdfknowledgelandscape.testscenario;

import com.andimon.rdfknowledgelandscape.features.Feature;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.*;

public enum Age implements Feature {
    OLD(DEFAULT_NAMESPACE+"OldAgeValue"),
    ESTABLISHED(DEFAULT_NAMESPACE+"EstablishedAgeValue"),
    NEW(DEFAULT_NAMESPACE+"NewAgeValue");

    private final String valueIRI;

    Age(String value) {
        this.valueIRI = value;
    }

    @Override
    public String getFeatureIRI() {
        return DEFAULT_NAMESPACE+"Age";
    }

    @Override
    public String getValueIRI() {
        return valueIRI;
    }
}
