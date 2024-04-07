package com.andimon.rdfknowledgelandscape.ontology;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class OntoKnowledgeLandscapeTest {
    OntoKL ontoKnowledgeLandscape;

    @Before
    public void setup() throws Exception {
        ontoKnowledgeLandscape = new OntoKL();
    }


    @Test
    public void numberOfClasses() {
        Assert.assertEquals(22, ontoKnowledgeLandscape.getClasses().size());
    }

    @Test
    public void numberOfObjectProperties() {
        Assert.assertEquals(9, ontoKnowledgeLandscape.getObjectProperties().size());
    }

    @Test
    public void numberOfDataProperties() {
        Assert.assertEquals(1, ontoKnowledgeLandscape.getDataProperties().size());
    }
}
