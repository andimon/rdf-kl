package com.andimon.rdfknowledgelandscape.factories;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Properties;

public enum KnowledgeLandscapeProperties {
    /**
     * The default name space used for all IRIs.
     */
    KL_NAMESPACE("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#"),
    /**
     * IRI used for the related relationship between two knowledge assets.
     */
    RELATED_TO_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#relatedTo"),
    /**
     * IRI used for the dependency relationship between two knowledge assets.
     */
    DEPENDS_ON_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#dependsOn"),
    /**
     * IRI used for the composition relationship between two knowledge assets.
     */
    COMPOSED_OF_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#composedOf"),
    /**
     * IRI used for the category feature assignment relationship.
     */
    HAS_CATEGORY_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasCategory"),
    /**
     * IRI used for the visibility feature assignment relationship.
     */
    HAS_VISIBILITY_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasVisibility"),
    /**
     * IRI used for the sociality feature assignment relationship.
     */
    HAS_SOCIALITY_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasSociality"),
    /**
     * IRI used for the operationality feature assignment relationship.
     */
    HAS_OPERATIONALITY_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasOperationality"),
    /**
     * IRI used for the relationship used to assign a person to a knowledge observation.
     */
    HAS_PERSON_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasPerson"),
    /**
     * IRI used for the relationship used to assign a knowledge asset to a knowledge observation.
     */
    HAS_KNOWLEDGE_ASSET_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasKnowledgeAsset"),
    /**
     * IRI used for the relationship used to assign a magnitude to a knowledge observation.
     */
    HAS_MAGNITUDE_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasMagnitude"),
    /**
     * IRI used for the class of persons.
     */
    PERSON_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Person"),
    /**
     * IRI used for the class of knowledge assets.
     */
    KNOWLEDGE_ASSET_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeAsset"),
    /**
     * IRI used for the class of knowledge observations.
     */
    KNOWLEDGE_OBSERVATION_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeObservation"),
    /**
     * IRI used for the class of knowledge asset observations
     */
    KNOWLEDGE_ASSET_FEATURE_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeAssetFeature"),
    /**
     * IRI used for the category feature class.
     */
    CATEGORY_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Category"),
    /**
     * IRI used for the technical category feature value class.
     */
    TECHNICAL_CATEGORY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#TechnicalCategoryValue"),
    /**
     * IRI used for the general category feature value class.
     */
    GENERAL_CATEGORY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#GeneralCategoryValue"),
    /**
     * IRI used for the business category feature value class.
     */
    BUSINESS_CATEGORY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#BusinessCategoryValue"),
    /**
     * IRI used for the visibility feature class.
     */
    VISIBILITY_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Visibility"),
    /**
     * IRI used for the tacit visibility feature value class.
     */
    TACIT_VISIBILITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#TacitVisibilityValue"),
    /**
     * IRI used for the explicit visibility feature value class.
     */
    EXPLICIT_VISIBILITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ExplicitVisibilityValue"),
    /**
     * IRI used for the sociality feature class.
     */
    SOCIALITY_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Sociality"),
    /**
     * IRI used for individual sociality feature value class.
     */
    INDIVIDUAL_SOCIALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#IndividualSocialityValue"),
    /**
     * IRI used for the social sociality feature value class.
     */
    SOCIAL_SOCIALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#SocialSocialityValue"),
    /**
     * IRI used for the operationality feature class.
     */
    OPERATIONALITY_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Operationality"),
    /**
     * IRI used for the declarative operationality feature value class.
     */
    DECLARATIVE_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#DeclarativeOperationalityValue"),
    /**
     * IRI used for the procedural operationality feature value class.
     */
    PROCEDURAL_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ProceduralOperationalityValue"),
    /**
     * IRI used for the causal operationality feature value class.
     */
    CAUSAL_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#CausalOperationalityValue"),
    /**
     * IRI used for the conditional operationality feature value class.
     */
    CONDITIONAL_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ConditionalOperationalityValue"),
    /**
     * IRI used for the relational operationality feature value class.
     */
    RELATIONAL_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#RelationalOperationalityValue");

    private final Object defaultValue;
    private static final String PREFIX = "com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.";
    private static final EnumMap<KnowledgeLandscapeProperties, Object> owlapiProperties = loadProperties();
    private static final Logger logger = LogManager.getLogger(KnowledgeLandscapeProperties.class);


    KnowledgeLandscapeProperties(Object o) {
        defaultValue = o;
    }

    /**
     * Values for properties are evaluated in the following order:
     * 1. First check if a system variable with the expected name is set, that is a system variable PREFIX+&lt;PROPERTY-NAME&gt;. If not found, then go to step 2.
     * 2. Check if the value is defined in the properties file ./knowledgelandscape.properties. If not found go to step 3.
     * 3. Use the default value defined in this enumeration.
     *
     * @param type The type of the value to retrieve.
     * @return The value in the specified type.
     */
    public <T> T getValue(Class<T> type) {
        Object fromSystemProperties = System.getProperty(PREFIX + name());
        if (fromSystemProperties != null) {
            return parse(type, fromSystemProperties);
        }
        Object fromConfigFile = owlapiProperties.get(this);
        if (fromConfigFile != null) {
            return parse(type, fromConfigFile);
        }
        return type.cast(defaultValue);
    }


    private <T> T parse(Class<T> type, Object value) {
        if (String.class.equals(type)) {
            return type.cast(String.valueOf(value.toString()));
        }
        return type.cast(value);
    }

    private static EnumMap<KnowledgeLandscapeProperties, Object> loadProperties() {
        EnumMap<KnowledgeLandscapeProperties, Object> map = new EnumMap<>(KnowledgeLandscapeProperties.class);
        Properties props = new Properties();
        try (InputStream stream =
                     KnowledgeLandscapeProperties.class.getResourceAsStream("/knowledgelandscape.properties")) {
            if (stream != null) {
                props.load(stream);
            }
        } catch (IOException e) {
            logger.error("Properties cannot be loaded", e);
        }
        props.forEach((name, value) -> {
            KnowledgeLandscapeProperties property = find(name.toString());
            if (property != null) {
                map.put(property, value);
            }
        });
        return map;
    }

    private static KnowledgeLandscapeProperties find(String parameterName) {
        if (!parameterName.startsWith(PREFIX)) {
            return null;
        }
        return valueOf(parameterName.substring(PREFIX.length()));
    }
}
