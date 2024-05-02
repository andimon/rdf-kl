package com.andimon.rdfknowledgelandscape.updater;


import com.github.owlcs.ontapi.Ontology;

/**
 * An implementation of KnowledgeGraphUpdater that does nothing,
 * leaving the ontology unchanged.
 */
public class NullUpdater implements Updater {

    /**
     * This method does nothing and leaves the ontology unchanged.
     *
     * @param ontology the ontology to be updated (which will remain unchanged)
     */
    @Override
    public void updateKnowledgeGraph(Ontology ontology) {
    }
}
