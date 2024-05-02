package com.andimon.rdfknowledgelandscape.parameters;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.andimon.rdfknowledgelandscape.factories.KnowledgeLandscapeProperties.KL_NAMESPACE;

public class KnowledgeLandscapePropertiesTest {


    @Test
    public void test() {
        Integer x = Integer.getInteger("2");

    }

    @Test
    public void getNamespaceDefaultValue() {
        String value = KL_NAMESPACE.getValue(String.class);
        String expected = "https://andimon.github.io/rdf-knowledge-landscape/onto-knowledge-landscape#";
        Assertions.assertEquals(expected, value);
    }

}
