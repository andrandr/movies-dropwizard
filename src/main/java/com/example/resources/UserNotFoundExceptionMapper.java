package com.example.resources;

import com.example.core.users.UserNotFoundException;
import io.dropwizard.jersey.errors.ErrorMessage;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
@Component
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {
    @Override
    public Response toResponse(UserNotFoundException exception) {
        Response.Status status = NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage(status.getStatusCode(), "user not found");
        return Response.status(status)
                .type(APPLICATION_JSON_TYPE)
                .entity(errorMessage)
                .build();
    }
}
