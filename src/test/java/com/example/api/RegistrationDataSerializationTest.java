package com.example.api;

import com.example.api.RegistrationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationDataSerializationTest {
    private static final ObjectMapper mapper = Jackson.newObjectMapper();


    @Test
    public void deserializesFromJson() throws Exception {
        // given
        RegistrationData referenceData = new RegistrationData("login1", "password1");
        String fixture = fixture("fixtures/registrationData.json");

        // when
        RegistrationData testData = mapper.readValue(fixture, RegistrationData.class);

        // then
        assertThat(testData).isEqualTo(referenceData);
    }

    @Test
    public void serializesToJson() throws Exception {
        // given
        RegistrationData testData = new RegistrationData("login1", "password1");
        String referenceJson = fixture("fixtures/registrationData.json");

        // when
        String testJson = mapper.writeValueAsString(testData);

        // then
        assertThatJson(testJson).isEqualTo(referenceJson);
    }
}
