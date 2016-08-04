package com.example.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.Map;

import static com.example.resources.Headers.ACCESS_TOKEN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

public class StatusResourceTest {
    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new StatusResource())
            .build();


    @Test
    public void getStatus() throws Exception {
        // given
        // when
        Map<String, String> response = resources.client()
                .target("/status")
                .request(APPLICATION_JSON_TYPE)
                .get(new GenericType<Map<String, String>>() {});

        // then
        assertThat(response).hasSize(1).containsEntry("status", "ok");
    }
}