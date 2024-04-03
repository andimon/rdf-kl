package com.andimon.rdfknowledgelandscape.features;

import org.junit.Assert;
import org.junit.Test;

public class FeaturesTest {
    @Test
    public void featureTest(){
        Feature visibilityFeature = Visibility.EXPLICIT;
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Visibility",visibilityFeature.getFeatureIRI());
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ExplicitVisibilityValue",visibilityFeature.getValueIRI());
    }
}
