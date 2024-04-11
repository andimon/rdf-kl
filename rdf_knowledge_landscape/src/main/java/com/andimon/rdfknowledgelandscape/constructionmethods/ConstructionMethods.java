package com.andimon.rdfknowledgelandscape.constructionmethods;

import com.andimon.rdfknowledgelandscape.features.Feature;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.util.Set;

public interface ConstructionMethods {
    boolean personIdentification(String personName);

    boolean removePerson(String personName) throws OWLOntologyStorageException;

    boolean knowledgeAssetIdentification(String knowledgeAssetName, Set<Feature> featureSet) throws Exception;

    boolean removeKnowledgeAsset(String knowledgeAssetName);

    boolean knowledgeObservation(String personName, String knowledgeAssetName, int n) throws Exception;

    boolean relatedTo(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name);

    boolean createTeam(String teamName);

    boolean addPersonToTeam(String teamName, String personName);

    boolean deleteTeam(String teamName);

    boolean removePersonFromTeam(String teamName, String personName);
}
