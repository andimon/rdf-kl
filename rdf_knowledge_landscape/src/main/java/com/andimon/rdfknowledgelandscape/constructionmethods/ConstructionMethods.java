package com.andimon.rdfknowledgelandscape.constructionmethods;

import com.andimon.rdfknowledgelandscape.features.Feature;

import java.util.Set;

public interface ConstructionMethods {
    /**
     * Create a new person. The personName should not correspond to a person that is already an instance of the class of persons.
     * Creation of a new person should not lead to any inconsistency.
     *
     * @param personName fragment identifier of IRI used to represent the person to be created i.e. kl:personName.
     * @return True if the person is successfully created.
     * @throws KnowledgeGraphConstructorException If the person is already identified, or an inconsistency occurs upon creating a new person.
     */
    boolean personIdentification(String personName) throws KnowledgeGraphConstructorException;

    /**
     * Delete a person. The personName should correspond to a person that is already an instance of the class of Persons.
     * Removing a person, should also result in the removal of knowledge observations associated with it.
     * Removal of a person should not lead to any inconsistency.
     *
     * @param personName fragment identifier of IRI used to represent the person to be created i.e. kl:personName.
     * @return True if the person is removed successfully.
     * @throws KnowledgeGraphConstructorException If the person is not identified, or an inconsistency occurs upon removing an existing person.
     */
    boolean removePerson(String personName) throws KnowledgeGraphConstructorException;


    /**
     * Create a new knowledge asset. The knowledgeAssetName should not correspond to a knowledge asset that is already an instance of the class of knowledge assets.
     * The knowledge asset is associated with a set of knowledge asset features which should be defined in OntoKL.
     *
     * @param knowledgeAssetName Fragment identifier of IRI used to represent the knowledge asset identified i.e. kl:KnowledgeAssetName.
     * @param featureSet         Set of knowledge asset features associated with a knowledge asset.
     * @return True if the knowledge asset is created successfully.
     * @throws KnowledgeGraphConstructorException If the knowledge asset is already identified, features/values not defined in OntoKL, or an inconsistency occurs upon creating the knowledge asset.
     */
    boolean knowledgeAssetIdentification(String knowledgeAssetName, Set<Feature> featureSet) throws KnowledgeGraphConstructorException;

    /**
     * Removes a knowledge asset. The knowledge asset should correspond to a knowledge asset that is already an instance of the class of knowledge assets.
     * Removing a knowledge asset, should result in the removal of feature value and knowledge observations associated with it.
     *
     * @param knowledgeAssetName The name of the knowledge asset to be removed.
     * @return True if the knowledge asset is removed successfully.
     * @throws KnowledgeGraphConstructorException If the knowledge asset is not identified, or an inconsistency occurs upon removing the knowledge asset.
     */
    boolean removeKnowledgeAsset(String knowledgeAssetName) throws KnowledgeGraphConstructorException;

    /**
     * Create a knowledge observation. The personName should correspond to a person that is already an instance of the class of persons, the knowledgeAssetName should correspond to an instance of the class
     * of knowledge assets. The integer n represents the extent of which the given person knows the given knowledge asset.
     * @param personName         Fragment identifier of IRI used to represent the person identified i.e. kl:PersonName.
     * @param knowledgeAssetName Fragment identifier of IRI used to represent the knowledge asset identified i.e. kl:KnowledgeAssetName.
     * @param n                  Value of magnitude.
     * @return True if the knowledge observation is created successfully.
     * @throws KnowledgeGraphConstructorException If the person or knowledge asset is not identified, or an inconsistency occurs upon creating the knowledge observation.
     */
    boolean knowledgeObservation(String personName, String knowledgeAssetName, int n) throws KnowledgeGraphConstructorException;

    /**
     * Establishes a generic relation between two given knowledge assets, knowledgeAsset1 related to knowledgeAsset2, provided that both are instances of kl:KnowledgeAsset.
     * @param knowledgeAsset1Name Fragment identifier of IRI used to represent the subject of relationship, i.e. kl:KnowledgeAsset1Name.
     * @param knowledgeAsset2Name  Fragment identifier of IRI used to represent the object of relationship, i.e. kl:KnowledgeAsset2Name.
     * @return True if the knowledge asset relationship is established successfully.
     * @throws KnowledgeGraphConstructorException If any of the two given knowledge assets is not identified, or an inconsistency occurs upon creating the knowledge asset relationship.
     */
    boolean relatedTo(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException;


    /**
     * Establishes a dependency between two given knowledge assets, knowledgeAsset1 related to knowledgeAsset2, provided that both are instances of kl:KnowledgeAsset.
     * @param knowledgeAsset1Name Fragment identifier of IRI used to represent the subject of relationship, i.e. kl:KnowledgeAsset1Name.
     * @param knowledgeAsset2Name  Fragment identifier of IRI used to represent the object of relationship, i.e. kl:KnowledgeAsset2Name.
     * @return True if the knowledge asset relationship is established successfully.
     * @throws KnowledgeGraphConstructorException If any of the two given knowledge assets is not identified, or an inconsistency occurs upon creating the knowledge asset relationship.
     */
    boolean dependentOn(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException;


    /**
     * Establishes a dependency between two given knowledge assets, knowledgeAsset1 related to knowledgeAsset2, provided that both are instances of kl:KnowledgeAsset.
     * @param knowledgeAsset1Name Fragment identifier of IRI used to represent the subject of relationship, i.e. kl:KnowledgeAsset1Name.
     * @param knowledgeAsset2Name  Fragment identifier of IRI used to represent the object of relationship, i.e. kl:KnowledgeAsset2Name.
     * @return True if the knowledge asset relationship is established successfully.
     * @throws KnowledgeGraphConstructorException If any of the two given knowledge assets is not identified, or an inconsistency occurs upon creating the knowledge asset relationship.
     */
    boolean composedOf(String knowledgeAsset1Name, String knowledgeAsset2Name) throws KnowledgeGraphConstructorException;

    /**
     * Creates a team. The teamName should not be a declared entity,
     * @param teamName  Fragment identifier of IRI used to represent the team, i.e. kl:teamName
     * @return True if the team is created successfully.
     * @throws KnowledgeGraphConstructorException If the team is already a subclass of the class of persons, or an inconsistency occurs upon creating the team
     */
    boolean createTeam(String teamName) throws KnowledgeGraphConstructorException;

    /**
     * Adds a person to a team. The given person should be an instance of the class of persons. The team should be a subclass
     * of the class of persons.
     * @param teamName  Fragment identifier of IRI used to represent the team, i.e. kl:teamName
     * @param personName  Fragment identifier of IRI used to represent the person to be added to the team, i.e. kl:personName
     * @return True if the team is created successfully.
     * @throws KnowledgeGraphConstructorException If the person or team are not identified, or if the person is already a member of the team, or if an inconsistency occurs upon adding the person to the team.
     */
    boolean addPersonToTeam(String teamName, String personName) throws KnowledgeGraphConstructorException;
    /**
     * Deletes an existing team.
     * @param teamName  Fragment identifier of IRI used to represent the team, i.e. kl:teamName
     * @return True if the team is deleted successfully.
     * @throws KnowledgeGraphConstructorException If the team is not identified, or a person is already a member of team, or if an inconsistency occurs upon deleting the team.
     */
    boolean deleteTeam(String teamName) throws KnowledgeGraphConstructorException;

    /**
     * Removes a person from team.
     * @param teamName  Fragment identifier of IRI used to represent the team, i.e. kl:teamName
     * @param personName  Fragment identifier of IRI used to represent the person to be removed from team, i.e. kl:personName
     * @return True if the person is removed from team successfully.
     * @throws KnowledgeGraphConstructorException If the person or team is not identified, or if the person is not a member of the team, or if an inconsistency occurs upon deleting the person from the team.
     */
    boolean removePersonFromTeam(String teamName, String personName) throws KnowledgeGraphConstructorException;
}
