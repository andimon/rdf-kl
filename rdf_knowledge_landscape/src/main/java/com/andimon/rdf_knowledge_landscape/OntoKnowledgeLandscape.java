package com.andimon.rdf_knowledge_landscape;


import com.andimon.rdf_knowledge_landscape.factories.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class OntoKnowledgeLandscape {
    private final OntoKnowledgeLandscapeOwlClassFactory ontoKnowledgeLandscapeOwlClassFactory;
    private final OntoKnowledgeLandscapeObjectPropertyFactory ontoKnowledgeLandscapeObjectPropertyFactory;
    private final OntoKnowledgeLandscapeDataPropertyFactory ontoKnowledgeLandscapeDataPropertyFactory;
    // OWLOntologyManager, which is used to load, create and access ontologies.
    private OWLOntologyManager owlOntologyManager;
    // An interface for creating entities, class expressions and axioms.
    private OWLDataFactory owlDataFactory;

    private Set<OWLClass> ontoKnowledgeLandscapeConcepts;

    private String namespace;
    PrefixManager pm;

    private final OWLOntology ontoKnowledgeLandscape;

    public OntoKnowledgeLandscape() throws Exception {
        ontoKnowledgeLandscapeOwlClassFactory = new OntoKnowledgeLandscapeOwlClassFactoryImpl();
        ontoKnowledgeLandscapeObjectPropertyFactory = new OntoKnowledgeLandscapeObjectPropertyFactoryImpl();
        ontoKnowledgeLandscapeDataPropertyFactory = new OntoKnowledgeLandscapeDataPropertyFactoryImpl();
        //Create OWL ontology manager
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
        namespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        pm = new DefaultPrefixManager(null, null, namespace);
        ontoKnowledgeLandscape = owlOntologyManager.createOntology(IRI.create(namespace));
        buildOntology();
    }


    public Set<OWLClass> getOntoKnowledgeLandscapeClasses() throws InvocationTargetException, IllegalAccessException {
        Set<OWLClass> classes = new HashSet<>();
        Method[] methods = OntoKnowledgeLandscapeOwlClassFactory.class.getDeclaredMethods();
        for (Method method : methods) {
            OWLClass owlClass = (OWLClass) method.invoke(ontoKnowledgeLandscapeOwlClassFactory);
            classes.add(owlClass);
        }
        return classes;
    }


    public Set<OWLObjectProperty> getOntoKnowledgeLandscapeObjectProperties() throws InvocationTargetException, IllegalAccessException {
        Set<OWLObjectProperty> properties = new HashSet<>();
        Method[] methods = OntoKnowledgeLandscapeObjectPropertyFactory.class.getDeclaredMethods();
        for (Method method : methods) {
            OWLObjectProperty property = (OWLObjectProperty) method.invoke(ontoKnowledgeLandscapeObjectPropertyFactory);
            properties.add(property);
        }
        return properties;
    }

    public Set<OWLDataProperty> getOntoKnowledgeLandscapeDataProperties() throws InvocationTargetException, IllegalAccessException {
        Set<OWLDataProperty> properties = new HashSet<>();
        Method[] methods = OntoKnowledgeLandscapeDataPropertyFactory.class.getDeclaredMethods();
        for (Method method : methods) {
            OWLDataProperty property = (OWLDataProperty) method.invoke(ontoKnowledgeLandscapeDataPropertyFactory);
            properties.add(property);
        }
        return properties;
    }

    private OWLAxiom getObjectPropertyDomainAxiom(OWLObjectProperty owlObjectProperty, OWLClass domain) throws Exception {
        if (getOntoKnowledgeLandscapeObjectProperties().contains(owlObjectProperty) && getOntoKnowledgeLandscapeClasses().contains(domain)) {
            return owlDataFactory.getOWLObjectPropertyDomainAxiom(owlObjectProperty, domain);
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private OWLAxiom getObjectPropertyRangeAxiom(OWLObjectProperty owlObjectProperty, OWLClass domain) throws Exception {
        if (getOntoKnowledgeLandscapeObjectProperties().contains(owlObjectProperty) && getOntoKnowledgeLandscapeClasses().contains(domain)) {
            return owlDataFactory.getOWLObjectPropertyRangeAxiom(owlObjectProperty, domain);
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private OWLAxiom getDataPropertyDomainAxiom(OWLDataProperty owlDataProperty, OWLClass domain) throws Exception {
        if (getOntoKnowledgeLandscapeDataProperties().contains(owlDataProperty) && getOntoKnowledgeLandscapeClasses().contains(domain)) {
            return owlDataFactory.getOWLDataPropertyDomainAxiom(owlDataProperty, domain);
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private OWLAxiom getOWLDataPropertyRangeAxiom(OWLDataProperty owlDataProperty, OWL2Datatype owl2Datatype) throws Exception {
        if (getOntoKnowledgeLandscapeDataProperties().contains(owlDataProperty) && getOntoKnowledgeLandscapeClasses().contains(owl2Datatype)) {
            return owlDataFactory.getOWLDataPropertyRangeAxiom(owlDataProperty, owl2Datatype);
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private OWLAxiom getOWLObjectPropertySymmetricAxiom(OWLObjectProperty owlObjectProperty) throws Exception {
        if (getOntoKnowledgeLandscapeObjectProperties().contains(owlObjectProperty)) {
            return owlDataFactory.getOWLSymmetricObjectPropertyAxiom(owlObjectProperty);
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private OWLAxiom getOWLObjectPropertyAsymmetricAxiom(OWLObjectProperty owlObjectProperty) throws Exception {
        if (getOntoKnowledgeLandscapeObjectProperties().contains(owlObjectProperty)) {
            return owlDataFactory.getOWLAsymmetricObjectPropertyAxiom(owlObjectProperty);
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private SWRLRule getOWLObjectPropertyTransitiveAxiom(OWLObjectProperty owlObjectProperty) throws Exception {
        if (getOntoKnowledgeLandscapeObjectProperties().contains(owlObjectProperty)) {
            /* We create a SWRL rule for transitive property */
            SWRLVariable x = owlDataFactory.getSWRLVariable(pm.getDefaultPrefix() + "x");
            SWRLVariable y = owlDataFactory.getSWRLVariable(pm.getDefaultPrefix() + "y");
            SWRLVariable z = owlDataFactory.getSWRLVariable(pm.getDefaultPrefix() + "z");
            SWRLObjectPropertyAtom r1 = owlDataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, x, y);
            SWRLObjectPropertyAtom r2 = owlDataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, y, z);
            SWRLObjectPropertyAtom r3 = owlDataFactory.getSWRLObjectPropertyAtom(owlObjectProperty, x, z);
            Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
            antecedent.add(r1);
            antecedent.add(r2);
            return owlDataFactory.getSWRLRule(antecedent, Collections.singleton(r3));
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private OWLAxiom getOWLObjectPropertyFunctionalAxiom(OWLObjectProperty owlObjectProperty) throws Exception {
        if (getOntoKnowledgeLandscapeObjectProperties().contains(owlObjectProperty)) {
            return owlDataFactory.getOWLFunctionalObjectPropertyAxiom(owlObjectProperty);
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private OWLAxiom getOWLSubObjectPropertyOfAxiom(OWLObjectProperty subOwlObjectProperty, OWLObjectProperty owlObjectProperty) throws Exception {
        if (getOntoKnowledgeLandscapeObjectProperties().contains(subOwlObjectProperty) && getOntoKnowledgeLandscapeObjectProperties().contains(owlObjectProperty)) {
            return owlDataFactory.getOWLSubObjectPropertyOfAxiom(subOwlObjectProperty, owlObjectProperty);
        } else throw new Exception("Couldn't create domain axiom property or domain not declared");
    }

    private OWLAxiom getOWLDisjointUnionPropertyAxiom(OWLClass mainClass, List<OWLClassExpression> subclasses) throws Exception {
        if (!getOntoKnowledgeLandscapeClasses().contains(mainClass)) {
            throw new Exception("Couldn't create domain axiom property or domain not declared");
        }
        for (OWLClassExpression owlClassExpression : subclasses) {
            if (!getOntoKnowledgeLandscapeClasses().contains(owlClassExpression)) {
                throw new Exception("Couldn't create domain axiom property or domain not declared");
            }
        }
        return owlDataFactory.getOWLDisjointUnionAxiom(mainClass, subclasses);
    }

    private OWLAxiom getOWLSubclassAxiom(OWLClass subclass, OWLClass superclass) throws Exception {
        if (!getOntoKnowledgeLandscapeClasses().contains(subclass)) {
            throw new Exception("Class" + subclass.getIRI() + " not declared in OntoKnowledgeLandscape");
        } else if (!getOntoKnowledgeLandscapeClasses().contains(superclass)) {
            throw new Exception("Class" + superclass.getIRI() + " not declared in OntoKnowledgeLandscape");
        }
        return owlDataFactory.getOWLSubClassOfAxiom(subclass, superclass);
    }


    private OWLAxiom getOWLDisjointCLassAxiom(OWLClass class1, OWLClass class2) throws Exception {
        if (!getOntoKnowledgeLandscapeClasses().contains(class1) && !getOntoKnowledgeLandscapeClasses().contains(class2)) {
            throw new Exception("Couldn't create domain axiom property or domain not declared");
        }
        return owlDataFactory.getOWLDisjointClassesAxiom(class1, class2);
    }

    private void buildOntology() throws Exception {
        /* Class and Properties Declaration Axioms */
        Set<OWLAxiom> declarationAxioms = new HashSet<OWLAxiom>();
        for (OWLClass owlClass : getOntoKnowledgeLandscapeClasses()) {
            declarationAxioms.add(owlDataFactory.getOWLDeclarationAxiom(owlClass));
        }
        for (OWLObjectProperty owlObjectProperty : getOntoKnowledgeLandscapeObjectProperties()) {
            declarationAxioms.add(owlDataFactory.getOWLDeclarationAxiom(owlObjectProperty));
        }
        for (OWLDataProperty owlDataProperty : getOntoKnowledgeLandscapeDataProperties()) {
            declarationAxioms.add(owlDataFactory.getOWLDeclarationAxiom(owlDataProperty));
        }
        /* Property Characteristics Axioms */
        Set<OWLAxiom> domainsAndRangesAxioms = new HashSet<OWLAxiom>();
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getRelatedToProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getRelatedToProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getDependsOnProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getDependsOnProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getComposedOfProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getComposedOfProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasCategoryProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasCategoryProperty(), ontoKnowledgeLandscapeOwlClassFactory.getCategoryClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasVisibilityProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasVisibilityProperty(), ontoKnowledgeLandscapeOwlClassFactory.getVisibilityClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasSocialityProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasSocialityProperty(), ontoKnowledgeLandscapeOwlClassFactory.getSocialityClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasOperationalityProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasOperationalityProperty(), ontoKnowledgeLandscapeOwlClassFactory.getOperationalityClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasPerson(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeObservationClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasPerson(), ontoKnowledgeLandscapeOwlClassFactory.getPersonClass()));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasKnowledgeAsset(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeObservationClass()));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasKnowledgeAsset(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
        domainsAndRangesAxioms.add(owlDataFactory.getOWLDataPropertyDomainAxiom(ontoKnowledgeLandscapeDataPropertyFactory.getHasMagnitudeProperty(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeObservationClass()));
        domainsAndRangesAxioms.add(owlDataFactory.getOWLDataPropertyRangeAxiom(ontoKnowledgeLandscapeDataPropertyFactory.getHasMagnitudeProperty(), OWL2Datatype.XSD_NON_NEGATIVE_INTEGER));
        Set<OWLAxiom> propertyCharacteristics = new HashSet<OWLAxiom>();
        propertyCharacteristics.add(getOWLObjectPropertySymmetricAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getRelatedToProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyAsymmetricAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getDependsOnProperty()));
        propertyCharacteristics.add(getOWLSubObjectPropertyOfAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getComposedOfProperty(), ontoKnowledgeLandscapeObjectPropertyFactory.getDependsOnProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasCategoryProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasVisibilityProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasSocialityProperty()));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(ontoKnowledgeLandscapeObjectPropertyFactory.getHasOperationalityProperty()));
        Set<OWLAxiom> valueSetsAxioms = new HashSet<>();
        OWLClass knowledgeAssetFeatureClass = ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetFeatureClass();
        OWLClass categoryClass = ontoKnowledgeLandscapeOwlClassFactory.getCategoryClass();
        List<OWLClassExpression> categoryClassValues = new ArrayList<>();
        categoryClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getTechnicalCategoryValueClass());
        categoryClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getBusinessCategoryValueClass());
        categoryClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getUndefinedCategoryValueClass());
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(categoryClass, categoryClassValues));
        OWLClass visibilityClass = ontoKnowledgeLandscapeOwlClassFactory.getVisibilityClass();
        List<OWLClassExpression> visibilityClassValues = new ArrayList<>();
        visibilityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getTacitVisibilityValueClass());
        visibilityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getExplicitVisibilityValueClass());
        visibilityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getUndefinedVisibilityValueClass());
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(visibilityClass, visibilityClassValues));
        OWLClass socialityClass = ontoKnowledgeLandscapeOwlClassFactory.getSocialityClass();
        List<OWLClassExpression> socialityClassValues = new ArrayList<OWLClassExpression>();
        socialityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getIndividualSocialityValueClass());
        socialityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getSocialSocialityValueClass());
        socialityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getUndefinedSocialityValueClass());
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(socialityClass, socialityClassValues));
        OWLClass operationalityClass = ontoKnowledgeLandscapeOwlClassFactory.getOperationalityClass();
        List<OWLClassExpression> operationalityClassValues = new ArrayList<>();
        operationalityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getDeclarativeOperationalityValueClass());
        operationalityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getProceduralOperationalityValueClass());
        operationalityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getCausalOperationalityValueClass());
        operationalityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getConditionalOperationalityValueClass());
        operationalityClassValues.add(ontoKnowledgeLandscapeOwlClassFactory.getUndefinedOperationalityValueClass());
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(operationalityClass, operationalityClassValues));
        Set<OWLAxiom> disjointClassesAxioms = new HashSet<OWLAxiom>();
        disjointClassesAxioms.add(getOWLDisjointCLassAxiom(ontoKnowledgeLandscapeOwlClassFactory.getPersonClass(), ontoKnowledgeLandscapeOwlClassFactory.getKnowledgeAssetClass()));
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
        owlOntologyManager.applyChange(new AddAxiom(ontoKnowledgeLandscape, getOWLObjectPropertyTransitiveAxiom(owlDataFactory.getOWLObjectProperty(":dependsOn", pm))));
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, valueSetsAxioms);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, disjointClassesAxioms);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, subClassAxioms);
    }


    /**
     * @return the current instance of the knowledge landscape ontology
     */
    public OWLOntology getOntology() {
        return ontoKnowledgeLandscape;
    }

    public PrefixManager getPrefixManager() {
        return pm;
    }


    public String serialiseToTurtleSyntax() throws IOException, OWLOntologyStorageException {
        return serialiseOntology(new TurtleDocumentFormat());
    }


    public void serialiseToTurtleSyntax(String path) throws IOException, OWLOntologyStorageException {
        serialiseOntology(path, new TurtleDocumentFormat());
    }

    private String serialiseOntology(OWLDocumentFormat owlDocumentFormat) throws OWLOntologyStorageException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        owlOntologyManager.saveOntology(ontoKnowledgeLandscape, owlDocumentFormat, new StreamDocumentTarget(out));
        String data = out.toString();
        out.close();
        return data;
    }

    private void serialiseOntology(String path, OWLDocumentFormat owlDocumentFormat) throws IOException, OWLOntologyStorageException {
        File file = new File(path);
        if (file.createNewFile()) {
            System.out.println("Created new file for ontology");
        } else {
            System.out.println("File for ontology already exists. Updating the file.");
        }
        owlOntologyManager.saveOntology(ontoKnowledgeLandscape, owlDocumentFormat, IRI.create(file.toURI()));
    }

}
