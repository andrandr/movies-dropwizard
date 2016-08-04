package com.example.resources;

import com.codahale.metrics.annotation.Timed;
import com.example.api.AccessTokenData;
import com.example.api.RegistrationData;
import com.example.core.users.IUserService;
import com.example.core.users.User;
import com.example.core.users.UserNotFoundException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.google.common.base.Preconditions.checkNotNull;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UsersResource {
    private final IUserService userService;


    @Autowired
    public UsersResource(IUserService userService) {
        checkNotNull(userService);
        this.userService = userService;
    }


    @POST
    @Timed
    public AccessTokenData registerUser(@Valid RegistrationData data) {
        User user = userService.register(data.getLogin(), data.getPassword());
        return new AccessTokenData(user.getAccessToken());
    }

    @DELETE
    @Timed
    public void deleteUser(@HeaderParam(Headers.ACCESS_TOKEN) @NotEmpty String accessToken) {
        User user = userService.findByAccessToken(accessToken);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.delete(user);
    }
}
