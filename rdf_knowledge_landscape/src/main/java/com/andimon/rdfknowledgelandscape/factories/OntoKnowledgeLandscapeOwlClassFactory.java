package com.andimon.rdfknowledgelandscape.factories;

import org.semanticweb.owlapi.model.OWLClass;


public interface OntoKnowledgeLandscapeOwlClassFactory {
    /**
     * Class of persons.
     *
     * @return The OWL class.
     */
    OWLClass getPersonClass();

    /**
     * Class of knowledge assets.
     *
     * @return The OWL class.
     */
    OWLClass getKnowledgeAssetClass();

    /**
     * Class of knowledge observations.
     *
     * @return The OWL class.
     */
    OWLClass getKnowledgeObservationClass();

    /**
     * Class of knowledge asset features.
     *
     * @return The OWL class.
     */
    OWLClass getKnowledgeAssetFeatureClass();

    /**
     * Class representing the category knowledge asset feature.
     *
     * @return The OWL class.
     */
    OWLClass getCategoryClass();

    /**
     * A class forming part of the disjoint union of the category knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getTechnicalCategoryValueClass();

    /**
     * A class forming part of the disjoint union of the category knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getBusinessCategoryValueClass();

    /**
     * A class forming part of the disjoint union of the category knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getGeneralCategoryValueClass();

    /**
     * Class representing the visibility knowledge asset feature.
     *
     * @return The OWL class.
     */
    OWLClass getVisibilityClass();

    /**
     * A class forming part of the disjoint union of the visibility knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getTacitVisibilityValueClass();

    /**
     * A class forming part of the disjoint union of the visibility knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getExplicitVisibilityValueClass();

    /**
     * Class representing the sociality knowledge asset feature.
     *
     * @return The OWL class.
     */
    OWLClass getSocialityClass();

    /**
     * A class forming part of the disjoint union of the sociality knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getIndividualSocialityValueClass();

    /**
     * A class forming part of the disjoint union of the sociality knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getSocialSocialityValueClass();

    /**
     * Class representing the operationality knowledge asset feature.
     *
     * @return The OWL class.
     */
    OWLClass getOperationalityClass();

    /**
     * A class forming part of the disjoint union of the operationality knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getDeclarativeOperationalityValueClass();

    /**
     * A class forming part of the disjoint union of the operationality knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getProceduralOperationalityValueClass();

    /**
     * A class forming part of the disjoint union of the operationality knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getCausalOperationalityValueClass();

    /**
     * A class forming part of the disjoint union of the operationality knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getConditionalOperationalityValueClass();

    /**
     * A class forming part of the disjoint union of the operationality knowledge asset feature class.
     *
     * @return The OWL class.
     */
    OWLClass getRelationalOperationalityValueClass();

}
