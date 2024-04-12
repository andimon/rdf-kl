package com.andimon.rdfknowledgelandscape.factories;

import com.andimon.rdfknowledgelandscape.constructionmethods.KnowledgeLandscapeConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Properties;

public enum KnowledgeLandscapeProperties {
    DEFAULT_NAMESPACE("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#"),

    RELATED_TO_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#relatedTo"),
    DEPENDS_ON_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#dependsOn"),

    COMPOSED_OF_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#composedOf"),

    HAS_CATEGORY_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasCategory"),

    HAS_VISIBILITY_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasVisibility"),

    HAS_SOCIALITY_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasSociality"),

    HAS_OPERATIONALITY_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasOperationality"),

    HAS_PERSON_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasPerson"),

    HAS_KNOWLEDGE_ASSET_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasKnowledgeAsset"),

    HAS_MAGNITUDE_PROPERTY_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#hasMagnitude"),
    PERSON_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Person"),

    KNOWLEDGE_ASSET_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeAsset"),

    KNOWLEDGE_OBSERVATION_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeObservation"),

    KNOWLEDGE_ASSET_FEATURE_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#KnowledgeAssetFeature"),

    CATEGORY_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Category"),

    TECHNICAL_CATEGORY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#TechnicalCategoryValue"),
    GENERAL_CATEGORY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#GeneralCategoryValue"),

    BUSINESS_CATEGORY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#BusinessCategoryValue"),
    UNDEFINED_CATEGORY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#UndefinedCategoryValue"),
    VISIBILITY_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Visibility"),
    TACIT_VISIBILITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#TacitVisibilityValue"),
    EXPLICIT_VISIBILITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ExplicitVisibilityValue"),

    UNDEFINED_VISIBILITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#UndefinedVisibilityValue"),
    SOCIALITY_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Sociality"),

    INDIVIDUAL_SOCIALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#IndividualSocialityValue"),
    SOCIAL_SOCIALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#SocialSocialityValue"),
    UNDEFINED_SOCIALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#UndefinedSocialityValue"),

    OPERATIONALITY_CLASS_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#Operationality"),

    DECLARATIVE_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#DeclarativeOperationalityValue"),
    PROCEDURAL_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ProceduralOperationalityValue"),
    CAUSAL_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#CausalOperationalityValue"),
    CONDITIONAL_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#ConditionalOperationalityValue"),
    RELATIONAL_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#RelationalOperationalityValue"),

    UNDEFINED_OPERATIONALITY_VALUE_IRI("https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#UndefinedOperationalityValue");

    private Object defaultValue;
    private static final String PREFIX = "com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.";
    private static final EnumMap<KnowledgeLandscapeProperties, Object> owlapiProperties = loadProperties();
    private static final Logger logger = LogManager.getLogger(KnowledgeLandscapeConstructor.class);


    KnowledgeLandscapeProperties(Object o) {
        defaultValue = o;
    }

    /**
     * Values for properties are evaluated in the following order:
     * <ol>
     * <li>First check if a system variable with the expected name is set, that is, a system variable of name PREFIX+&lt;PROPERTY-NAME&gt;. If not found, then go to step 2.</li>
     * <li>Check if the value is defined in the properties file ./knowledgelandscape.properties. If not found, then go to step 3.</li>
     * <li>Use the default value defined in this enumeration.</li>
     * </ol>
     *
     * @param type       The class of the value to retrieve.
     * @param properties A map of KnowledgeLandscapeProperties.
     * @param <T>        The type of the value to retrieve.
     * @return The value of the specified type.
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

    public static KnowledgeLandscapeProperties find(String parameterName) {
        if (!parameterName.startsWith(PREFIX)) {
            return null;
        }
        return valueOf(parameterName.substring(PREFIX.length()));
    }


}
