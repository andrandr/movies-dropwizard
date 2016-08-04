package com.example.resources;

import com.example.core.users.WrongPasswordException;
import io.dropwizard.jersey.errors.ErrorMessage;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Provider
@Component
public class WrongPasswordExceptionMapper implements ExceptionMapper<WrongPasswordException> {
    @Override
    public Response toResponse(WrongPasswordException exception) {
        Response.Status status = FORBIDDEN;
        ErrorMessage errorMessage = new ErrorMessage(status.getStatusCode(), "wrong password");
        return Response.status(status)
                .type(APPLICATION_JSON_TYPE)
                .entity(errorMessage)
                .build();
    }
}
