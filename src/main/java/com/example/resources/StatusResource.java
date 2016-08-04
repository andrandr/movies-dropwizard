package com.example.resources;

import com.codahale.metrics.annotation.Timed;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static java.util.Collections.singletonMap;

@Path("/status")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class StatusResource {
    @GET
    @Timed
    public Map<String, String> getStatus() {
        return singletonMap("status", "ok");
    }
}
