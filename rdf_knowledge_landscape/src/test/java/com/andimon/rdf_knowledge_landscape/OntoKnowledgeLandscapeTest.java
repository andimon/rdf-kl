package com.andimon.rdf_knowledge_landscape;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.IOException;


public class OntoKnowledgeLandscapeTest {
    OntoKnowledgeLandscape ontoKnowledgeLandscape;

    @Before
    public void setup() throws Exception {
        ontoKnowledgeLandscape = new OntoKnowledgeLandscape();
    }


    @Test
    public void numberOfClasses() throws Exception {
        Assert.assertEquals(22, ontoKnowledgeLandscape.getOntoKnowledgeLandscapeClasses().size());
    }

    @Test
    public void numberOfObjectProperties() throws Exception {
        Assert.assertEquals(9, ontoKnowledgeLandscape.getOntoKnowledgeLandscapeObjectProperties().size());
    }

    @Test
    public void numberOfDataProperties() throws Exception {
        Assert.assertEquals(1, ontoKnowledgeLandscape.getOntoKnowledgeLandscapeDataProperties().size());
    }

    @Test
    public void rdfSerialisation() throws IOException, OWLOntologyStorageException {
        System.out.println(ontoKnowledgeLandscape.serialiseToTurtleSyntax());
    }
}
