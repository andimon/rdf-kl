package com.andimon.rdfknowledgelandscape.updater;

import com.github.owlcs.ontapi.Ontology;

/**
 * Defines an interface for classes responsible for generating additional axioms
 * for an inferred ontology, based on specific organisational requirements.
 */
public interface Updater {

    /**
     * Given a populated ontology, which is essentially isomorphic to a knowledge graph,
     * update it according to some custom specification.
     * @param populatedOntology The populated ontology representing the populated ontology acted against a reasoner.
     */
    void updateKnowledgeGraph(Ontology populatedOntology);

}