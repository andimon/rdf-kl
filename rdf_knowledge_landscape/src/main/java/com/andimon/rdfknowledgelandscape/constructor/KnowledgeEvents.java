package com.andimon.rdfknowledgelandscape.constructor;

import com.andimon.rdfknowledgelandscape.features.Feature;

import java.util.Map;
import java.util.Set;

public interface KnowledgeEvents {
    boolean personJoinsOrganisation(String personName);

    boolean personLeavesOrganisation(String personName);

    boolean knowledgeAssetIdentification(String knowledgeAssetName, Set<Feature> featureSet) throws Exception;

    boolean knowledgeAssetDiscarded(String knowledgeAssetName);

    boolean knowledgeObservation(String knowledgeAssetName, String personName, double n) throws Exception;

    boolean relatedTo(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean createTeam(String teamName);

    boolean addPersonToTeam(String teamName, String personName);
}
