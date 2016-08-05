package com.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class MovieDataSerializationTest {
    private static final ObjectMapper mapper = Jackson.newObjectMapper();


    @Test
    public void deserializesFromJson() throws Exception {
        // given
        MovieData referenceData = new MovieData("testId", "testTitle", "testDescription", true);
        String fixture = fixture("fixtures/movieData.json");

        // when
        MovieData testData = mapper.readValue(fixture, MovieData.class);

        // then
        assertThat(testData).isEqualTo(referenceData);
    }

    @Test
    public void serializesToJson() throws Exception {
        // given
        MovieData testData = new MovieData("testId", "testTitle", "testDescription", true);
        String referenceJson = fixture("fixtures/movieData.json");

        // when
        String testJson = mapper.writeValueAsString(testData);

        // then
        assertThatJson(testJson).isEqualTo(referenceJson);
    }
}
