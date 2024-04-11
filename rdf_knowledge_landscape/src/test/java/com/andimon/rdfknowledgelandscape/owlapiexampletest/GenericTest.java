//package com.andimon.rdfknowledgelandscape.owlapiexampletest;
//
//import com.github.owlcs.ontapi.OntManagers;
//import com.github.owlcs.ontapi.Ontology;
//import com.github.owlcs.ontapi.OntologyManager;
//import org.junit.Test;
//import org.semanticweb.HermiT.ReasonerFactory;
//import org.semanticweb.owlapi.model.*;
//import org.semanticweb.owlapi.reasoner.OWLReasoner;
//import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
//import org.semanticweb.owlapi.util.InferredOntologyGenerator;
//
//public class GenericTest {
//    @Test
//    public void test() throws OWLOntologyCreationException {
//        OntologyManager manager = OntManagers.createManager();
//        Ontology ontologyWithRules = manager.createOntology();
//        OWLNamedIndividual X = manager.getOWLDataFactory().getOWLNamedIndividual(":X");
//        OWLNamedIndividual Y = manager.getOWLDataFactory().getOWLNamedIndividual(":Y");
//        OWLNamedIndividual Z = manager.getOWLDataFactory().getOWLNamedIndividual(":Z");
//        OWLObjectProperty r = manager.getOWLDataFactory().getOWLObjectProperty(":r");
//        OWLTransitiveObjectPropertyAxiom r1 = manager.getOWLDataFactory().getOWLTransitiveObjectPropertyAxiom(r);
//        OWLObjectPropertyAssertionAxiom a1 = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(r,X,Y);
//        OWLObjectPropertyAssertionAxiom a2 = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(r,Y,Z);
//        ontologyWithRules.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(X));
//        ontologyWithRules.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(Y));
//        ontologyWithRules.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(Z));
//        ontologyWithRules.addAxiom(manager.getOWLDataFactory().getOWLDeclarationAxiom(r));
//        ontologyWithRules.addAxiom(a1);
//        ontologyWithRules.addAxiom(a2);
//        ontologyWithRules.addAxiom(r1);
//        Ontology ontologyInf = manager.createOntology();
//        OWLReasonerFactory reasonerFactory = new ReasonerFactory();
//        OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyWithRules);
//        OWLDataFactory df = manager.getOWLDataFactory();
//        InferredOntologyGenerator inference = new InferredOntologyGenerator(reasoner);
//        manager.addAxioms(ontologyInf,ontologyWithRules.getAxioms());
//        inference.fillOntology(df, ontologyInf);
//    }
//}
