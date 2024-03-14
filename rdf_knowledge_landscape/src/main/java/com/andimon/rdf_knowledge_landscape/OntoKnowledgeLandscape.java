package com.andimon.rdf_knowledge_landscape;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class OntoKnowledgeLandscape {
    // OWLOntologyManager, which is used to load, create and access ontologies.
    private OWLOntologyManager owlOntologyManager;
    // An interface for creating entities, class expressions and axioms.
    private OWLDataFactory owlDataFactory;

    private Set<OWLClass> ontoKnowledgeLandscapeConcepts;

    private String namespace;
    PrefixManager pm;

    private final OWLOntology ontoKnowledgeLandscape;

    public Set<OWLClass> getOntoKnowledgeLandscapeClasses() {
        Set<OWLClass> classes = new HashSet<OWLClass>();
        classes.add(owlDataFactory.getOWLClass(":Person", pm));
        classes.add(owlDataFactory.getOWLClass(":KnowledgeAsset", pm));
        classes.add(owlDataFactory.getOWLClass(":KnowledgeObservation", pm));
        classes.add(owlDataFactory.getOWLClass(":Category", pm));
        classes.add(owlDataFactory.getOWLClass(":TechnicalCategoryValue", pm));
        classes.add(owlDataFactory.getOWLClass(":BusinessCategoryValue", pm));
        classes.add(owlDataFactory.getOWLClass(":UndefinedCategoryValue", pm));
        classes.add(owlDataFactory.getOWLClass(":Visibility", pm));
        classes.add(owlDataFactory.getOWLClass(":TacitVisibilityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":ExplicitVisibilityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":UndefinedVisibilityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":Sociality", pm));
        classes.add(owlDataFactory.getOWLClass(":IndividualSocialityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":SocialSocialityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":UndefinedSocialityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":Operationality", pm));
        classes.add(owlDataFactory.getOWLClass(":DeclarativeOperationalityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":ProceduralOperationalityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":CausalOperationalityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":ConditionalOperationalityValue", pm));
        classes.add(owlDataFactory.getOWLClass(":UndefinedOperationalityValue", pm));
        return classes;
    }

    public Set<OWLObjectProperty> getOntoKnowledgeLandscapeObjectProperties() {
        Set<OWLObjectProperty> properties = new HashSet<OWLObjectProperty>();
        properties.add(owlDataFactory.getOWLObjectProperty(":relatedTo", pm));
        properties.add(owlDataFactory.getOWLObjectProperty(":dependsOn", pm));
        properties.add(owlDataFactory.getOWLObjectProperty(":composedOf", pm));
        properties.add(owlDataFactory.getOWLObjectProperty(":hasCategory", pm));
        properties.add(owlDataFactory.getOWLObjectProperty(":hasVisibility", pm));
        properties.add(owlDataFactory.getOWLObjectProperty(":hasSociality", pm));
        properties.add(owlDataFactory.getOWLObjectProperty(":hasOperationality", pm));
        properties.add(owlDataFactory.getOWLObjectProperty(":hasPerson", pm));
        properties.add(owlDataFactory.getOWLObjectProperty(":hasKnowledgeAsset", pm));
        return properties;
    }

    public Set<OWLDataProperty> getOntoKnowledgeLandscapeDataProperties() {
        Set<OWLDataProperty> properties = new HashSet<OWLDataProperty>();
        properties.add(owlDataFactory.getOWLDataProperty(":hasMagnitude", pm));
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

    private OWLAxiom getOWLObjectPropertyTransitiveAxiom(OWLObjectProperty owlObjectProperty) throws Exception {
        if (getOntoKnowledgeLandscapeObjectProperties().contains(owlObjectProperty)) {
            return owlDataFactory.getOWLTransitiveObjectPropertyAxiom(owlObjectProperty);
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
        if(!getOntoKnowledgeLandscapeClasses().contains(mainClass)){
            throw new Exception("Couldn't create domain axiom property or domain not declared");
        }
        for(OWLClassExpression owlClassExpression : subclasses){
            if(!getOntoKnowledgeLandscapeClasses().contains(owlClassExpression)){
                throw new Exception("Couldn't create domain axiom property or domain not declared");
            }
        }
        return owlDataFactory.getOWLDisjointUnionAxiom(mainClass,subclasses);
    }

    private OWLClass getClass(String name){
        return owlDataFactory.getOWLClass(":"+name, pm);
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
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":relatedTo", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":relatedTo", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":dependsOn", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":dependsOn", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":composedOf", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":composedOf", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":hasCategory", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":hasCategory", pm), owlDataFactory.getOWLClass(":Category", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":hasVisibility", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":hasVisibility", pm), owlDataFactory.getOWLClass(":Visibility", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":hasSociality", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":hasSociality", pm), owlDataFactory.getOWLClass(":Sociality", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":hasOperationality", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":hasOperationality", pm), owlDataFactory.getOWLClass(":Operationality", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":hasPerson", pm), owlDataFactory.getOWLClass(":KnowledgeObservation", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":hasPerson", pm), owlDataFactory.getOWLClass(":Person", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyDomainAxiom(owlDataFactory.getOWLObjectProperty(":hasKnowledgeAsset", pm), owlDataFactory.getOWLClass(":KnowledgeObservation", pm)));
        domainsAndRangesAxioms.add(getObjectPropertyRangeAxiom(owlDataFactory.getOWLObjectProperty(":hasKnowledgeAsset", pm), owlDataFactory.getOWLClass(":KnowledgeAsset", pm)));
        domainsAndRangesAxioms.add(owlDataFactory.getOWLDataPropertyDomainAxiom(owlDataFactory.getOWLDataProperty(":hasMagnitude", pm), owlDataFactory.getOWLClass(":KnowledgeObservation", pm)));
        domainsAndRangesAxioms.add(owlDataFactory.getOWLDataPropertyRangeAxiom(owlDataFactory.getOWLDataProperty(":hasMagnitude", pm), OWL2Datatype.XSD_NON_NEGATIVE_INTEGER));
        Set<OWLAxiom> propertyCharacteristics = new HashSet<OWLAxiom>();
        propertyCharacteristics.add(getOWLObjectPropertySymmetricAxiom(owlDataFactory.getOWLObjectProperty(":relatedTo", pm)));
        propertyCharacteristics.add(getOWLObjectPropertyAsymmetricAxiom(owlDataFactory.getOWLObjectProperty(":dependsOn", pm)));
        propertyCharacteristics.add(getOWLObjectPropertyTransitiveAxiom(owlDataFactory.getOWLObjectProperty(":dependsOn", pm)));
        propertyCharacteristics.add(getOWLSubObjectPropertyOfAxiom(owlDataFactory.getOWLObjectProperty(":composedOf", pm),owlDataFactory.getOWLObjectProperty(":dependsOn", pm)));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(owlDataFactory.getOWLObjectProperty(":hasCategory", pm)));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(owlDataFactory.getOWLObjectProperty(":hasVisibility", pm)));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(owlDataFactory.getOWLObjectProperty(":hasSociality", pm)));
        propertyCharacteristics.add(getOWLObjectPropertyFunctionalAxiom(owlDataFactory.getOWLObjectProperty(":hasOperationality", pm)));
        Set<OWLAxiom> valueSetsAxioms = new HashSet<OWLAxiom>();
        OWLClass categoryClass = getClass("Category");
        List<OWLClassExpression> categoryClassValues = new ArrayList<OWLClassExpression>();
        categoryClassValues.add(getClass("TechnicalCategoryValue"));
        categoryClassValues.add(getClass("BusinessCategoryValue"));
        categoryClassValues.add(getClass("UndefinedCategoryValue"));
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(categoryClass,categoryClassValues));
        OWLClass visibilityClass = getClass("Visibility");
        List<OWLClassExpression> visibilityClassValues = new ArrayList<OWLClassExpression>();
        visibilityClassValues.add(getClass("TacitVisibilityValue"));
        visibilityClassValues.add(getClass("ExplicitVisibilityValue"));
        visibilityClassValues.add(getClass("UndefinedVisibilityValue"));
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(visibilityClass,visibilityClassValues));
        OWLClass socialityClass = getClass("Sociality");
        List<OWLClassExpression> socialityClassValues = new ArrayList<OWLClassExpression>();
        socialityClassValues.add(getClass("IndividualSocialityValue"));
        socialityClassValues.add(getClass("SocialSocialityValue"));
        socialityClassValues.add(getClass("UndefinedSocialityValue"));
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(socialityClass,socialityClassValues));
        OWLClass operationalityClass = getClass("Operationality");
        List<OWLClassExpression> operationalityClassValues = new ArrayList<OWLClassExpression>();
        operationalityClassValues.add(getClass("DeclarativeOperationalityValue"));
        operationalityClassValues.add(getClass("ProceduralOperationalityValue"));
        operationalityClassValues.add(getClass("CausalOperationalityValue"));
        operationalityClassValues.add(getClass("ConditionalOperationalityValue"));
        operationalityClassValues.add(getClass("UndefinedOperationalityValue"));
        valueSetsAxioms.add(getOWLDisjointUnionPropertyAxiom(operationalityClass,operationalityClassValues));

        /* Add all axioms  */
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape, declarationAxioms);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape,domainsAndRangesAxioms);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape,propertyCharacteristics);
        owlOntologyManager.addAxioms(ontoKnowledgeLandscape,valueSetsAxioms);
    }

    public OntoKnowledgeLandscape() throws Exception {
        //Create OWL ontology manager
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
        namespace = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        pm = new DefaultPrefixManager(null, null, namespace);
        ontoKnowledgeLandscape = owlOntologyManager.createOntology(IRI.create(namespace));
        //Build Ontology
        buildOntology();

    }

    /**
     *
     * @return the current instance of the knowledge landscape ontology
     */
    public OWLOntology getOntology() {
        return ontoKnowledgeLandscape;
    }
}
