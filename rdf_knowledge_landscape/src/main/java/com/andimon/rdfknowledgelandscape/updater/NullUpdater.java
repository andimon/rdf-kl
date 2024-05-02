package com.andimon.rdfknowledgelandscape.updater;


import com.github.owlcs.ontapi.Ontology;

/**
 * An implementation of KnowledgeGraphUpdater that does nothing,
 * leaving the inferred ontology unchanged.
 */
public class NullUpdater implements Updater {
    @Override
    public void updateKnowledgeGraph(Ontology ontology) {
    }
}
