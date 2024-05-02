package com.andimon.rdfknowledgelandscape.factories;

import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Factory for creating knowledge landscape owl object properties.
 */
public interface OntoKnowledgeLandscapeObjectPropertyFactory {
    /**
     * Object property used to relate to knowledge assets via a related relationship.
     * @return The OWL object property.
     */
    OWLObjectProperty getRelatedToProperty();
    /**
     * Object property used to relate to knowledge assets via a dependency relationship.
     * @return The OWL object property.
     */
    OWLObjectProperty getDependsOnProperty();
    /**
     * Object property used to relate to knowledge assets via a composition relationship.
     * @return The OWL object property.
     */
    OWLObjectProperty getComposedOfProperty();
    /**
     * Object property used to assign a category to a knowledge asset.
     * @return The OWL object property.
     */
    OWLObjectProperty getHasCategoryProperty();
    /**
     * Object property used to assign a visibility to a knowledge asset.
     * @return The OWL object property.
     */
    OWLObjectProperty getHasVisibilityProperty();
    /**
     * Object property used to assign a sociality to a knowledge asset.
     * @return The OWL object property.
     */
    OWLObjectProperty getHasSocialityProperty();
    /**
     * Object property used to assign an operationality to a knowledge asset.
     * @return The OWL object property.
     */
    OWLObjectProperty getHasOperationalityProperty();
    /**
     * Object property used to assign a person to a knowledge observation.
     * @return The OWL object property.
     */
    OWLObjectProperty getHasPerson();
    /**
     * Object property used to assign a knowledge asset to a knowledge observation.
     * @return The OWL object property.
     */
    OWLObjectProperty getHasKnowledgeAsset();
}
