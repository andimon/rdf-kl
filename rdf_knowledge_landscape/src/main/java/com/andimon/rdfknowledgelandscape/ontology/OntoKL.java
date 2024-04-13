package com.andimon.rdfknowledgelandscape.ontology;

import com.andimon.rdfknowledgelandscape.constructionmethods.KnowledgeLandscapeConstructor;
import com.andimon.rdfknowledgelandscape.factories.*;
import com.github.owlcs.ontapi.OntManagers;
import com.github.owlcs.ontapi.Ontology;
import com.github.owlcs.ontapi.OntologyManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.util.*;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.DEFAULT_NAMESPACE;

public class OntoKL {
    private final OntologyManager owlOntologyManager;
    private final OWLDataFactory owlDataFactory;
    protected static final Logger logger = LogManager.getLogger(OntoKL.class);

    protected OntoKnowledgeLandscapeOwlClassFactory classFactory;

    // Factory to get object properties used in OntoKnowledgeLandscape ontology.
    protected OntoKnowledgeLandscapeObjectPropertyFactory objectPropertyFactory;

    // Factory to get data properties used in OntoKnowledge Landscape ontology.
    protected OntoKnowledgeLandscapeDataPropertyFactory dataPropertyFactory;

    // The OntoKnowledgeLandscape OWL Ontology.
    protected Ontology ontoKnowledgeLandscape;
    protected PrefixManager prefixManager;


    public OntoKL() throws Exception {
        classFactory = new DefaultOntoKnowledgeLandscapeOwlClassFactory();
        objectPropertyFactory = new DefaultOntoKnowledgeLandscapeObjectPropertyFactory();
        dataPropertyFactory = new DefaultOntoKnowledgeLandscapeDataPropertyFactory();
        owlOntologyManager = OntManagers.createManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
        String namespace = DEFAULT_NAMESPACE.getValue(String.class);
        prefixManager = new DefaultPrefixManager(null, null, namespace);
        ontoKnowledgeLandscape = owlOntologyManager.createOntology(IRI.create(namespace));
        get();
    }


    public Set<OWLClass> getClasses() {
        Set<OWLClass> classes = new HashSet<>();
        classes.add(classFactory.getPersonClass());
        classes.add(classFactory.getKnowledgeAssetClass());
        classes.add(classFactory.getKnowledgeObservationClass());
        classes.add(classFactory.getKnowledgeAssetFeatureClass());
        classes.add(classFactory.getCategoryClass());
        classes.add(classFactory.getTechnicalCategoryValueClass());
        classes.add(classFactory.getBusinessCategoryValueClass());
        classes.add(classFactory.getGeneralCategoryValueClass());
        classes.add(classFactory.getVisibilityClass());
        classes.add(classFactory.getTacitVisibilityValueClass());
        classes.add(classFactory.getExplicitVisibilityValueClass());
        classes.add(classFactory.getSocialityClass());
        classes.add(classFactory.getIndividualSocialityValueClass());
        classes.add(classFactory.getSocialSocialityValueClass());
        classes.add(classFactory.getOperationalityClass());
        classes.add(classFactory.getDeclarativeOperationalityValueClass());
        classes.add(classFactory.getProceduralOperationalityValueClass());
        classes.add(classFactory.getCausalOperationalityValueClass());
        classes.add(classFactory.getConditionalOperationalityValueClass());
        classes.add(classFactory.getRelationalOperationalityValueClass());

        return classes;
    }

    public Set<OWLObjectProperty> getObjectProperties() {
        Set<OWLObjectProperty> objectProperties = new HashSet<>();
        objectProperties.add(objectPropertyFactory.getRelatedToProperty());
        objectProperties.add(objectPropertyFactory.getDependsOnProperty());
        objectProperties.add(objectPropertyFactory.getComposedOfProperty());
        objectProperties.add(objectPropertyFactory.getHasCategoryProperty());
        objectProperties.add(objectPropertyFactory.getHasVisibilityProperty());
        objectProperties.add(objectPropertyFactory.getHasSocialityProperty());
        objectProperties.add(objectPropertyFactory.getHasOperationalityProperty());
        objectProperties.add(objectPropertyFactory.getHasPerson());
        objectProperties.add(objectPropertyFactory.getHasKnowledgeAsset());
        return objectProperties;
    }

    public Set<OWLDataProperty> getDataProperties() {
        Set<OWLDataProperty> dataProperties = new HashSet<>();
        dataProperties.add(dataPropertyFactory.getHasMagnitudeProperty());
        return dataProperties;
    }

    public void addAxiom(OWLAxiom axiom) {
        ontoKnowledgeLandscape.addAxiom(axiom);
    }


    protected void get() {
        /* Class and Properties Declaration Axioms */
        Set<OWLAxiom> declarationAxioms = new HashSet<OWLAxiom>();
        for (OWLClass owlClass : getClasses()) {
            declarationAxioms.add(owlDataFactory.getOWLDeclarationAxiom(owlClass));
        }
        for (OWLObjectProperty owlObjectProperty : getObjectProperties()) {
            declarationAxioms.add(owlDataFactory.getOWLDeclarationAxiom(owlObjectProperty));
        }
        for (OWLDataProperty owlDataProperty : getDataProperties()) {
            declarationAxioms.add(owlDataFactory.getOWLDeclarationAxiom(owlDataProperty));
        }
        /* Property Characteristics Axioms */
        Set<OWLAxiom> domainsAndRangesAxioms = new HashSet<OWLAxiom>();
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getRelatedToProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getRelatedToProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getDependsOnProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getDependsOnProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getComposedOfProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getComposedOfProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getHasCategoryProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getHasCategoryProperty(), classFactory.getCategoryClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getHasVisibilityProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getHasVisibilityProperty(), classFactory.getVisibilityClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getHasSocialityProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getHasSocialityProperty(), classFactory.getSocialityClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getHasOperationalityProperty(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getHasOperationalityProperty(), classFactory.getOperationalityClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getHasPerson(), classFactory.getKnowledgeObservationClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getHasPerson(), classFactory.getPersonClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(objectPropertyFactory.getHasKnowledgeAsset(), classFactory.getKnowledgeObservationClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(objectPropertyFactory.getHasKnowledgeAsset(), classFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(owlDataFactory.getOWLDataPropertyDomainAxiom(dataPropertyFactory.getHasMagnitudeProperty(), classFactory.getKnowledgeObservationClass()));
        domainsAndRangesAxioms.add(owlDataFactory.getOWLDataPropertyRangeAxiom(dataPropertyFactory.getHasMagnitudeProperty(), OWL2Datatype.XSD_NON_NEGATIVE_INTEGER));
        Set<OWLAxiom> propertyCharacteristics = new HashSet<>();
        propertyCharacteristics.add(getOWLObjectPropertySymmetricAxiom(objectPropertyFactory.getRelatedToProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyAsymmetricAxiom(objectPropertyFactory.getDependsOnProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyAsymmetricAxiom(objectPropertyFactory.getComposedOfProperty()));
        propertyCharacteristics.add(getOWLSubObjectPropertyOfAxiom(objectPropertyFactory.getComposedOfProperty(), objectPropertyFactory.getDependsOnProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(objectPropertyFactory.getHasCategoryProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(objectPropertyFactory.getHasVisibilityProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(objectPropertyFactory.getHasSocialityProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(objectPropertyFactory.getHasOperationalityProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(objectPropertyFactory.getHasPerson()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(objectPropertyFactory.getHasKnowledgeAsset()));
        propertyCharacteristics.add(owlDataFactory.getOWLFunctionalDataPropertyAxiom(dataPropertyFactory.getHasMagnitudeProperty()));
        propertyCharacteristics.add(getOWLSubObjectPropertyOfAxiom(objectPropertyFactory.getComposedOfProperty(), objectPropertyFactory.getRelatedToProperty()));
        propertyCharacteristics.add(getOWLSubObjectPropertyOfAxiom(objectPropertyFactory.getDependsOnProperty(), objectPropertyFactory.getRelatedToProperty()));
        propertyCharacteristics.add(owlDataFactory.getOWLHasKeyAxiom(classFactory.getKnowledgeObservationClass(),Set.of(objectPropertyFactory.getHasPerson(),objectPropertyFactory.getHasKnowledgeAsset(),dataPropertyFactory.getHasMagnitudeProperty())));


        Set<OWLAxiom> valueSetsAxioms = new HashSet<>();
        OWLClass knowledgeAssetFeatureClass = classFactory.getKnowledgeAssetFeatureClass();
        OWLClass categoryClass = classFactory.getCategoryClass();
        List<OWLClassExpression> categoryClassValues = new ArrayList<>();
        categoryClassValues.add(classFactory.getTechnicalCategoryValueClass());
        categoryClassValues.add(classFactory.getBusinessCategoryValueClass());
        categoryClassValues.add(classFactory.getGeneralCategoryValueClass());
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(categoryClass, categoryClassValues));
        OWLClass visibilityClass = classFactory.getVisibilityClass();
        List<OWLClassExpression> visibilityClassValues = new ArrayList<>();
        visibilityClassValues.add(classFactory.getTacitVisibilityValueClass());
        visibilityClassValues.add(classFactory.getExplicitVisibilityValueClass());
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(visibilityClass, visibilityClassValues));
        OWLClass socialityClass = classFactory.getSocialityClass();
        List<OWLClassExpression> socialityClassValues = new ArrayList<OWLClassExpression>();
        socialityClassValues.add(classFactory.getIndividualSocialityValueClass());
        socialityClassValues.add(classFactory.getSocialSocialityValueClass());
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(socialityClass, socialityClassValues));
        OWLClass operationalityClass = classFactory.getOperationalityClass();
        List<OWLClassExpression> operationalityClassValues = new ArrayList<>();
        operationalityClassValues.add(classFactory.getDeclarativeOperationalityValueClass());
        operationalityClassValues.add(classFactory.getProceduralOperationalityValueClass());
        operationalityClassValues.add(classFactory.getCausalOperationalityValueClass());
        operationalityClassValues.add(classFactory.getConditionalOperationalityValueClass());
        operationalityClassValues.add(classFactory.getRelationalOperationalityValueClass());
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(operationalityClass, operationalityClassValues));
        Set<OWLAxiom> disjointClassesAxioms = new HashSet<>();
        disjointClassesAxioms.add(getOWLDisjointCLassAxiom(classFactory.getPersonClass(), classFactory.getKnowledgeAssetClass()));
        /* Subclass axioms */
        Set<OWLAxiom> subClassAxioms = new HashSet<OWLAxiom>();
        subClassAxioms.add(getOWLSubclassAxiom(visibilityClass, knowledgeAssetFeatureClass));
        subClassAxioms.add(getOWLSubclassAxiom(categoryClass, knowledgeAssetFeatureClass));
        subClassAxioms.add(getOWLSubclassAxiom(socialityClass, knowledgeAssetFeatureClass));
        subClassAxioms.add(getOWLSubclassAxiom(operationalityClass, knowledgeAssetFeatureClass));
        /* Add all axioms  */
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, declarationAxioms);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, domainsAndRangesAxioms);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, propertyCharacteristics);
        owlOntologyManager.applyChange(new AddAxiom(ontoKnowledgeLandscape, getOWLObjectPropertyTransitiveAxiom(objectPropertyFactory.getComposedOfProperty())));
        owlOntologyManager.applyChange(new AddAxiom(ontoKnowledgeLandscape, getOWLObjectPropertyTransitiveAxiom(objectPropertyFactory.getDependsOnProperty())));
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, valueSetsAxioms);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, disjointClassesAxioms);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, subClassAxioms);
    }

    private OWLAxiom getObjectPropertyDomainAxiom(OWLObjectProperty owlObjectProperty, OWLClass domain) {
        return owlDataFactory.getOWLObjectPropertyDomainAxiom(owlObjectProperty, domain);
    }

    private OWLAxiom getObjectPropertyRangeAxiom(OWLObjectProperty owlObjectProperty, OWLClass domain) {
        return owlDataFactory.getOWLObjectPropertyRangeAxiom(owlObjectProperty, domain);
    }

    private OWLAxiom getDataPropertyDomainAxiom(OWLDataProperty owlDataProperty, OWLClass domain) {
        return owlDataFactory.getOWLDataPropertyDomainAxiom(owlDataProperty, domain);
    }

    private OWLAxiom getOWLDataPropertyRangeAxiom(OWLDataProperty owlDataProperty, OWL2Datatype owl2Datatype) {
        return owlDataFactory.getOWLDataPropertyRangeAxiom(owlDataProperty, owl2Datatype);
    }

    private OWLAxiom getOWLObjectPropertySymmetricAxiom(OWLObjectProperty owlObjectProperty) {
        return owlDataFactory.getOWLSymmetricObjectPropertyAxiom(owlObjectProperty);
    }

    private OWLAxiom getOWLObjectPropertyAsymmetricAxiom(OWLObjectProperty owlObjectProperty) {
        return owlDataFactory.getOWLAsymmetricObjectPropertyAxiom(owlObjectProperty);
    }

    private OWLAxiom getOWLObjectPropertyFunctionalAxiom(OWLObjectProperty owlObjectProperty) {
        return owlDataFactory.getOWLFunctionalObjectPropertyAxiom(owlObjectProperty);
    }

    private OWLAxiom getOWLSubObjectPropertyOfAxiom(OWLObjectProperty subOwlObjectProperty, OWLObjectProperty owlObjectProperty) {
        return owlDataFactory.getOWLSubObjectPropertyOfAxiom(subOwlObjectProperty, owlObjectProperty);
    }


    private OWLAxiom getOWLDisjointUnionPropertyAxiom(OWLClass mainClass, List<OWLClassExpression> subclasses) {
        return owlDataFactory.getOWLDisjointUnionAxiom(mainClass, subclasses);
    }

    private OWLAxiom getOWLSubclassAxiom(OWLClass subclass, OWLClass superclass) {
        return owlDataFactory.getOWLSubClassOfAxiom(subclass, superclass);
    }


    private OWLAxiom getOWLDisjointCLassAxiom(OWLClass class1, OWLClass class2) {
        return owlDataFactory.getOWLDisjointClassesAxiom(class1, class2);
    }

    private SWRLRule getOWLObjectPropertyTransitiveAxiom(OWLObjectProperty owlObjectProperty) {
        /* We create a SWRL rule for transitive property. */
        SWRLVariable x = owlDataFactory.getSWRLVariable(prefixManager.getDefaultPrefix() + "x");
        SWRLVariable y = owlDataFactory.getSWRLVariable(prefixManager.getDefaultPrefix() + "y");
        SWRLVariable z = owlDataFactory.getSWRLVariable(prefixManager.getDefaultPrefix() + "z");
        SWRLObjectPropertyAtom r1 = owlDataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, x, y);
        SWRLObjectPropertyAtom r2 = owlDataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, y, z);
        SWRLObjectPropertyAtom r3 = owlDataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, x, z);
        Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
        antecedent.add(r1);
        antecedent.add(r2);
        return owlDataFactory.getSWRLRule(antecedent, Collections.singleton(r3));
    }


    public boolean addFeature(String featureName, Set<String> valueNames) {
        OWLClass feature = owlDataFactory.getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + featureName);
        if (EntitySearcher.getSubClasses(classFactory.getKnowledgeAssetFeatureClass(), ontoKnowledgeLandscape).anyMatch(x -> x.asOWLClass().equals(feature))) {
            logger.warn("Feature " + feature.getIRI() + " already exists.");
            return false;
        } else {
            logger.info("Creating feature " + feature.getIRI() + " with values " + valueNames);
            Set<OWLAxiom> axioms = new HashSet<>();
            // create feature class and declare
            axioms.add(owlDataFactory.getOWLDeclarationAxiom(feature));
            // declare subclass of knowledge asset feature
            axioms.add(owlDataFactory.getOWLSubClassOfAxiom(feature, classFactory.getKnowledgeAssetFeatureClass()));
            // get value classes
            Set<OWLClass> values = new HashSet<>();
            for (String valueName : valueNames) {
                OWLClass value = owlDataFactory.getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + valueName + featureName + "Value");
                values.add(value);
                axioms.add(owlDataFactory.getOWLDeclarationAxiom(value));
            }
            // feature disjoint union of values
            axioms.add(owlDataFactory.getOWLDisjointUnionAxiom(feature, values));
            // add object property
            OWLObjectProperty featureProperty = owlDataFactory.getOWLObjectProperty(DEFAULT_NAMESPACE.getValue(String.class) + "has" + featureName);
            axioms.add(owlDataFactory.getOWLDeclarationAxiom(featureProperty));
            axioms.add(owlDataFactory.getOWLObjectPropertyDomainAxiom(featureProperty, classFactory.getKnowledgeAssetClass()));
            axioms.add(owlDataFactory.getOWLObjectPropertyRangeAxiom(featureProperty, feature));
            axioms.add(owlDataFactory.getOWLFunctionalObjectPropertyAxiom(featureProperty));
            // add axioms
            ontoKnowledgeLandscape.addAxioms(axioms);
            return true;
        }
    }

    public void addValue(String featureName, String valueName) {
        // feature class
        OWLClass feature = owlDataFactory.getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + featureName);
        OWLClass value = owlDataFactory.getOWLClass(DEFAULT_NAMESPACE.getValue(String.class) + valueName + featureName + "Value");
        Set<OWLAxiom> axiomsToRemove = new HashSet<>();
        Set<OWLAxiom> axiomsToAdd = new HashSet<>();
        Set<OWLClassExpression> disjointUnionOperands = new HashSet<>();
        // check if featureName exists
        if (EntitySearcher.getSubClasses(classFactory.getKnowledgeAssetFeatureClass(), ontoKnowledgeLandscape).noneMatch(x -> x.asOWLClass().equals(feature))) {
            logger.warn("Feature " + feature.getIRI() + " does not exists.");
        } else {
            logger.info("Adding value "+ value.getIRI()+ " to feature "+feature.getIRI());
            for (OWLDisjointUnionAxiom axiom : ontoKnowledgeLandscape.getAxioms(AxiomType.DISJOINT_UNION)) {
                if (axiom.getOWLClass().equals(feature)) {
                    disjointUnionOperands.addAll(axiom.getClassExpressions());
                    axiomsToRemove.add(axiom);
                }
            }
            // add value
            disjointUnionOperands.add(value);
            axiomsToAdd.add(owlDataFactory.getOWLDisjointUnionAxiom(feature, disjointUnionOperands));
            ontoKnowledgeLandscape.addAxioms(axiomsToAdd);
            ontoKnowledgeLandscape.removeAxioms(axiomsToRemove);
        }

    }

    public Ontology getOntology() {
        return ontoKnowledgeLandscape;
    }
}
