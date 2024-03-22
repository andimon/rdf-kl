package com.andimon.rdf_knowledge_landscape;

import openllet.owlapi.OWLHelper;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.jena.base.Sys;
import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class GenericTest {
    @Test
    public void individualDeclarationEntityInSignature(){
        OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = owlManager.getOWLDataFactory();
        OWLNamedIndividual entity = owlManager.getOWLDataFactory().getOWLNamedIndividual(IRI.create(":entity"));
        OWLAxiom axiom  = factory.getOWLDeclarationAxiom(entity);
        OWLNamedIndividual entity2 = owlManager.getOWLDataFactory().getOWLNamedIndividual(IRI.create(":entity"));
        Assert.assertTrue(axiom.containsEntityInSignature(entity2));
    }

    @Test
    public void objectPropertyAssertionAxiomEntityInSignature(){
        OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = owlManager.getOWLDataFactory();
        OWLNamedIndividual a = owlManager.getOWLDataFactory().getOWLNamedIndividual(IRI.create(":a"));
        OWLNamedIndividual b = owlManager.getOWLDataFactory().getOWLNamedIndividual(IRI.create(":b"));
        OWLObjectProperty c = owlManager.getOWLDataFactory().getOWLObjectProperty(IRI.create(":r"));
        OWLAxiom axiom  = factory.getOWLObjectPropertyAssertionAxiom(c,a,b);
        OWLNamedIndividual entity2 = owlManager.getOWLDataFactory().getOWLNamedIndividual(IRI.create(":b"));
        Assert.assertTrue(axiom.containsEntityInSignature(entity2));
    }

    @Test
    public void reasonerQueryTest() throws OWLOntologyCreationException {
        OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
        OWLOntology o = owlManager.createOntology();
        ReasonerFactory reasonerFactory = new ReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(o);
        OWLDataFactory factory = owlManager.getOWLDataFactory();
        OWLNamedIndividual a = owlManager.getOWLDataFactory().getOWLNamedIndividual(IRI.create(":a"));
        OWLClass b = owlManager.getOWLDataFactory().getOWLClass(IRI.create(":b"));
        OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(b,a);
        owlManager.addAxiom(o,classAssertionAxiom);
        reasoner.flush();
        System.out.println(reasoner.getInstances(b,false).getFlattened());
    }

}
