package com.andimon.rdf_knowledge_landscape;

import java.util.Map;

public interface KnowledgeEvents {
    boolean personJoinsOrganisation(String personName);
    boolean personLeavesOrganisation(String personName);
    boolean knowledgeAssetIdentification(String knowledgeAssetName, Map<String, String> features) throws Exception;
    boolean knowledgeAssetDiscarded(String knowledgeAssetName);
    boolean knowledgeObservation(String knowledgeAssetName, String personName, int n) throws Exception;
    boolean relatedTo(String knowledgeAsset1Name, String knowledgeAsset2Name);
    boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name);
    boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name);
}
