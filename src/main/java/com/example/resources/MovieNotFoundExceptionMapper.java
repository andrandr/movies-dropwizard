package com.example.resources;

import com.example.core.movies.MovieNotFoundException;
import io.dropwizard.jersey.errors.ErrorMessage;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
@Component
public class MovieNotFoundExceptionMapper implements ExceptionMapper<MovieNotFoundException> {
    @Override
    public Response toResponse(MovieNotFoundException exception) {
        Response.Status status = NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage(status.getStatusCode(), "movie not found");
        return Response.status(status)
                .type(APPLICATION_JSON_TYPE)
                .entity(errorMessage)
                .build();
    }
}
