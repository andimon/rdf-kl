package com.andimon.rdfknowledgelandscape.constructionmethods;

import com.andimon.rdfknowledgelandscape.features.Feature;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.util.Set;

public interface ConstructionMethods {
    boolean personIdentification(String personName) throws KnowledgeGraphConstructorException;

    boolean removePerson(String personName) throws OWLOntologyStorageException, KnowledgeGraphConstructorException;

    boolean knowledgeAssetIdentification(String knowledgeAssetName, Set<Feature> featureSet) throws Exception;

    boolean removeKnowledgeAsset(String knowledgeAssetName) throws KnowledgeGraphConstructorException;

    boolean knowledgeObservation(String personName, String knowledgeAssetName, int n) throws Exception;

    boolean relatedTo(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException;

    boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException;

    boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException;

    boolean createTeam(String teamName) throws KnowledgeGraphConstructorException;

    boolean addPersonToTeam(String teamName, String personName) throws KnowledgeGraphConstructorException;

    boolean deleteTeam(String teamName) throws KnowledgeGraphConstructorException;

    boolean removePersonFromTeam(String teamName, String personName) throws KnowledgeGraphConstructorException;
}
