package com.andimon.rdfknowledgelandscape.ontology;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.File;
public class SerialisationTest {
    OntoKL ontoKL;

    @BeforeEach
    public void setup() throws Exception {
        ontoKL = new OntoKL();

    }

    @Test
    public void serialiseToTurtle() throws OWLOntologyStorageException {
        File file = new File("./resources/OntoKL.ttl");
        ontoKL.getOntology().saveOntology(IRI.create(file.toURI()));
    }


}


