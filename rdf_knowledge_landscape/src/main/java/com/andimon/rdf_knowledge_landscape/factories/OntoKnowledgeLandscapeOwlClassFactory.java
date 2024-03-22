package com.andimon.rdf_knowledge_landscape.factories;

import org.semanticweb.owlapi.model.OWLClass;



public interface OntoKnowledgeLandscapeOwlClassFactory {
    OWLClass getPersonClass();

    OWLClass getKnowledgeAssetClass();

    OWLClass getKnowledgeObservationClass();

    OWLClass getKnowledgeAssetFeatureClass();

    OWLClass getCategoryClass();

    OWLClass getTechnicalCategoryValueClass();

    OWLClass getBusinessCategoryValueClass();

    OWLClass getUndefinedCategoryValueClass();


    OWLClass getVisibilityClass();

    OWLClass getTacitVisibilityValueClass();

    OWLClass getExplicitVisibilityValueClass();

    OWLClass getUndefinedVisibilityValueClass();

    OWLClass getSocialityClass();

    OWLClass getIndividualSocialityValueClass();

    OWLClass getSocialSocialityValueClass();

    OWLClass getUndefinedSocialityValueClass();

    OWLClass getOperationalityClass();

    OWLClass getDeclarativeOperationalityValueClass();

    OWLClass getProceduralOperationalityValueClass();

    OWLClass getCausalOperationalityValueClass();

    OWLClass getConditionalOperationalityValueClass();

    OWLClass getUndefinedOperationalityValueClass();
}
