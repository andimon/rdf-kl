package com.andimon.rdfknowledgelandscape.factories;

import org.semanticweb.owlapi.model.OWLClass;


public interface OntoKnowledgeLandscapeOwlClassFactory {
    OWLClass getPersonClass();

    OWLClass getKnowledgeAssetClass();

    OWLClass getKnowledgeObservationClass();

    OWLClass getKnowledgeAssetFeatureClass();

    OWLClass getCategoryClass();

    OWLClass getTechnicalCategoryValueClass();

    OWLClass getBusinessCategoryValueClass();

    OWLClass getGeneralCategoryValueClass();


    OWLClass getVisibilityClass();

    OWLClass getTacitVisibilityValueClass();

    OWLClass getExplicitVisibilityValueClass();


    OWLClass getSocialityClass();

    OWLClass getIndividualSocialityValueClass();

    OWLClass getSocialSocialityValueClass();


    OWLClass getOperationalityClass();

    OWLClass getDeclarativeOperationalityValueClass();

    OWLClass getProceduralOperationalityValueClass();

    OWLClass getCausalOperationalityValueClass();

    OWLClass getConditionalOperationalityValueClass();

    OWLClass getRelationalOperationalityValueClass();


}
