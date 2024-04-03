package com.andimon.rdfknowledgelandscape.queries;

import org.apache.jena.query.ResultSetRewindable;

/**
 * This interface defines base queries for retrieving information about a Knowledge Landscape.
 */
public interface BaseQueries {

    /**
     * Retrieves all knowledge assets.
     *
     * @return
     */
    ResultSetRewindable getKnowledgeAssets();

    /**
     * Retrieves all persons.
     *
     * @return
     */
    ResultSetRewindable getPersons();

    /**
     * Retrieves the Knowledge Asset features defined in the ontology.
     */
    ResultSetRewindable getKnowledgeAssetDefinedFeatures();

    /**
     * Retrieves knowledge assets composed of a specific knowledge asset.
     *
     * @param knowledgeAssetName the name of the knowledge asset to retrieve composed assets for
     */
    ResultSetRewindable getComposedOf(String knowledgeAssetName);

    /**
     * Retrieves knowledge assets dependent on a specific knowledge asset.
     *
     * @param knowledgeAssetName the name of the knowledge asset to retrieve dependent assets for
     * @return
     */
    ResultSetRewindable getDependentKnowledgeAsset(String knowledgeAssetName);

    /**
     * Retrieves knowledge assets related to a specific knowledge asset.
     *
     * @param knowledgeAssetName the name of the knowledge asset to retrieve related assets for
     * @return
     */
    ResultSetRewindable getRelatedKnowledgeAssets(String knowledgeAssetName);

    /**
     * Retrieves knowledge assets known by a specific knowledge asset.
     *
     * @param knowledgeAssetName the name of the knowledge asset to retrieve known assets for
     */
    void knownBy(String knowledgeAssetName);

}