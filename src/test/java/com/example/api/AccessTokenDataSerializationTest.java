package com.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class AccessTokenDataSerializationTest {
    private static final ObjectMapper mapper = Jackson.newObjectMapper();


    @Test
    public void deserializesFromJson() throws Exception {
        // given
        AccessTokenData referenceData = new AccessTokenData("testAccessToken");
        String fixture = fixture("fixtures/accessTokenData.json");

        // when
        AccessTokenData testData = mapper.readValue(fixture, AccessTokenData.class);

        // then
        assertThat(testData).isEqualTo(referenceData);
    }

    @Test
    public void serializesToJson() throws Exception {
        // given
        AccessTokenData testData = new AccessTokenData("testAccessToken");
        String referenceJson = fixture("fixtures/accessTokenData.json");

        // when
        String testJson = mapper.writeValueAsString(testData);

        // then
        assertThatJson(testJson).isEqualTo(referenceJson);
    }
}
