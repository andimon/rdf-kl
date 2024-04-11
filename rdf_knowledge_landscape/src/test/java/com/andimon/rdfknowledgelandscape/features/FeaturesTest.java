package com.andimon.rdfknowledgelandscape.features;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FeaturesTest {
    @Test
    public void featureTest(){
        Feature visibilityFeature = Visibility.EXPLICIT;
        Assertions.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Visibility",visibilityFeature.getFeatureIRI());
        Assertions.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ExplicitVisibilityValue",visibilityFeature.getValueIRI());
    }
}
