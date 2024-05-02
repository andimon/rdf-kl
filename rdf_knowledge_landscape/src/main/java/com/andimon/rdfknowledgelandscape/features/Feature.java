package com.andimon.rdfknowledgelandscape.features;


public interface Feature {
    /**
     * Get an IRI of a particular feature.
     * @return IRI of the feature.
     */
    String getFeatureIRI();

    /**
     * Get an IRI of a particular value of a particular feature.
     * @return IRI of the feature value.
     */
    String getValueIRI();
}
