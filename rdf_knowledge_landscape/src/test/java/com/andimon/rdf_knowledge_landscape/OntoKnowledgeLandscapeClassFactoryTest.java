package com.andimon.rdf_knowledge_landscape;

import com.andimon.rdf_knowledge_landscape.factories.OntoKnowledgeLandscapeOwlClassFactory;
import com.andimon.rdf_knowledge_landscape.factories.OntoKnowledgeLandscapeOwlClassFactoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OntoKnowledgeLandscapeClassFactoryTest {
    private OntoKnowledgeLandscapeOwlClassFactory ontoKnowledgeLandscapeClassFactory;

    @Before
    public void setup() {
        ontoKnowledgeLandscapeClassFactory = new OntoKnowledgeLandscapeOwlClassFactoryImpl();
    }

    @Test
    public void numberOfClasses() throws InvocationTargetException, IllegalAccessException {
        Method[] methods  = OntoKnowledgeLandscapeOwlClassFactory.class.getDeclaredMethods();
        Assert.assertEquals(22,methods.length);
    }


    @Test
    public void personClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Person", ontoKnowledgeLandscapeClassFactory.getPersonClass().getIRI().toString());
    }

    @Test
    public void knowledgeAssetClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeAsset", ontoKnowledgeLandscapeClassFactory.getKnowledgeAssetClass().getIRI().toString());
    }

    @Test
    public void knowledgeObservationClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeObservation", ontoKnowledgeLandscapeClassFactory.getKnowledgeObservationClass().getIRI().toString());
    }

    @Test
    public void knowledgeAssetFeatureClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeAssetFeature", ontoKnowledgeLandscapeClassFactory.getKnowledgeAssetFeatureClass().getIRI().toString());
    }

    @Test
    public void categoryClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Category", ontoKnowledgeLandscapeClassFactory.getCategoryClass().getIRI().toString());
    }

    @Test
    public void technicalCategoryValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#TechnicalCategoryValue", ontoKnowledgeLandscapeClassFactory.getTechnicalCategoryValueClass().getIRI().toString());
    }

    @Test
    public void businessCategoryValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#BusinessCategoryValue", ontoKnowledgeLandscapeClassFactory.getBusinessCategoryValueClass().getIRI().toString());
    }

    @Test
    public void undefinedCategoryValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#UndefinedCategoryValue", ontoKnowledgeLandscapeClassFactory.getUndefinedCategoryValueClass().getIRI().toString());
    }

    @Test
    public void visibilityClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Visibility", ontoKnowledgeLandscapeClassFactory.getVisibilityClass().getIRI().toString());
    }

    @Test
    public void tacitVisibilityValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#TacitVisibilityValue", ontoKnowledgeLandscapeClassFactory.getTacitVisibilityValueClass().getIRI().toString());
    }

    @Test
    public void explicitVisibilityValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ExplicitVisibilityValue", ontoKnowledgeLandscapeClassFactory.getExplicitVisibilityValueClass().getIRI().toString());
    }

    @Test
    public void undefinedVisibilityValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#UndefinedVisibilityValue", ontoKnowledgeLandscapeClassFactory.getUndefinedVisibilityValueClass().getIRI().toString());
    }

    @Test
    public void socialityClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Sociality", ontoKnowledgeLandscapeClassFactory.getSocialityClass().getIRI().toString());
    }

    @Test
    public void individualSocialityValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#IndividualSocialityValue", ontoKnowledgeLandscapeClassFactory.getIndividualSocialityValueClass().getIRI().toString());
    }

    @Test
    public void socialSocialityValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#SocialSocialityValue", ontoKnowledgeLandscapeClassFactory.getSocialSocialityValueClass().getIRI().toString());
    }

    @Test
    public void undefinedSocialityValueClassIRI() {
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#UndefinedSocialityValue", ontoKnowledgeLandscapeClassFactory.getUndefinedSocialityValueClass().getIRI().toString());
    }
    @Test
    public void OperationalityClassIRI(){
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Operationality",ontoKnowledgeLandscapeClassFactory.getOperationalityClass().getIRI().toString());
    }

    @Test
    public void declarativeOperationalityValueClassIRI(){
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#DeclarativeOperationalityValue",ontoKnowledgeLandscapeClassFactory.getDeclarativeOperationalityValueClass().getIRI().toString());
    }

    @Test
    public void proceduralOperationalityValueClassIRI(){
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ProceduralOperationalityValue",ontoKnowledgeLandscapeClassFactory.getProceduralOperationalityValueClass().getIRI().toString());
    }

    @Test
    public void causalOperationalityValueClassIRI(){
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#CausalOperationalityValue",ontoKnowledgeLandscapeClassFactory.getCausalOperationalityValueClass().getIRI().toString());
    }
    @Test
    public void conditionalOperationalityValueClassIRI(){
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ConditionalOperationalityValue",ontoKnowledgeLandscapeClassFactory.getConditionalOperationalityValueClass().getIRI().toString());
    }
    @Test
    public void undefinedOperationalityValueClassIRI(){
        Assert.assertEquals("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#UndefinedOperationalityValue",ontoKnowledgeLandscapeClassFactory.getUndefinedOperationalityValueClass().getIRI().toString());
    }
}
