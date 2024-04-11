package com.andimon.rdfknowledgelandscape.ontology;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OntoKnowledgeLandscapeTest {
    OntoKL ontoKnowledgeLandscape;

    @BeforeEach
    public void setup() throws Exception {
        ontoKnowledgeLandscape = new OntoKL();
    }


    @Test
    public void numberOfClasses() {
        Assertions.assertEquals(22, ontoKnowledgeLandscape.getClasses().size());
    }

    @Test
    public void numberOfObjectProperties() {
        Assertions.assertEquals(9, ontoKnowledgeLandscape.getObjectProperties().size());
    }

    @Test
    public void numberOfDataProperties() {
        Assertions.assertEquals(1, ontoKnowledgeLandscape.getDataProperties().size());
    }
}
