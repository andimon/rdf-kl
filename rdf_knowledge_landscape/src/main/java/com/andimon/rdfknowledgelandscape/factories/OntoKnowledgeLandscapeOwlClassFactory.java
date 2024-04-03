package com.andimon.rdfknowledgelandscape.factories;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;


public interface OntoKnowledgeLandscapeOwlClassFactory {
    OWLClass getPersonClass();

    OWLClass getKnowledgeAssetClass();

    OWLClass getKnowledgeObservationClass();

    OWLClass getKnowledgeAssetFeatureClass();

    OWLClass getCategoryClass();

    OWLClass getTechnicalCategoryValueClass();

    OWLClass getBusinessCategoryValueClass();

    OWLClassExpression getGeneralCategoryValueClass();

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

    OWLClassExpression getRelationalOperationalityValueClass();

    OWLClass getUndefinedOperationalityValueClass();


}
