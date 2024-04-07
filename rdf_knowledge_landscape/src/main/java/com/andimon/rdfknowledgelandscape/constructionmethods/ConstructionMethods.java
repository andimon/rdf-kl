package com.andimon.rdfknowledgelandscape.constructionmethods;

import com.andimon.rdfknowledgelandscape.features.Feature;

import java.util.Set;

public interface ConstructionMethods {
    boolean personIdentification(String personName);

    boolean personLeavesOrganisation(String personName);

    boolean knowledgeAssetIdentification(String knowledgeAssetName, Set<Feature> featureSet) throws Exception;

    boolean knowledgeAssetDiscarded(String knowledgeAssetName);

    boolean knowledgeObservation(String personName, String knowledgeAssetName, double n) throws Exception;

    boolean relatedTo(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean createTeam(String teamName);

    boolean addPersonToTeam(String teamName, String personName);
}
