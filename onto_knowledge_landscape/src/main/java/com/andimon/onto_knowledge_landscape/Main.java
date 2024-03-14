package com.andimon.onto_knowledge_landscape;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.File;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        OntoKnowledgeLandscape ontoKnowledgeLandscape = new OntoKnowledgeLandscape();
        OWLOntology ontology= ontoKnowledgeLandscape.getOntology();
        File fileformated = new File("/home/andimon/Documents/test-format.owl");
        fileformated.createNewFile();
        ontology.getOWLOntologyManager().saveOntology(ontology, new FunctionalSyntaxDocumentFormat(), IRI.create(fileformated.toURI()));
    }
}
