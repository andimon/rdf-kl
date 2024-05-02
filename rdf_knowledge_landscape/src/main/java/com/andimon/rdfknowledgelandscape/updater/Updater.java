package com.andimon.rdfknowledgelandscape.updater;

import com.github.owlcs.ontapi.Ontology;

/**
 * Defines an interface for classes responsible for generating additional axioms
 * for an inferred ontology, based on specific organisational requirements.
 */
public interface Updater {

    /**
     * Method signature for generating additional axioms to enrich the inferred ontology.
     * Implementing classes should provide logic to update the knowledge graph
     * based on specific organizational requirements.
     */
    void updateKnowledgeGraph(Ontology ontology);

}