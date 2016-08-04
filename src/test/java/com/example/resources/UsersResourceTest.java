package com.example.resources;

import com.example.api.AccessTokenData;
import com.example.api.RegistrationData;
import com.example.core.users.IUserService;
import com.example.core.users.User;
import com.example.core.users.WrongPasswordException;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static com.example.resources.Headers.ACCESS_TOKEN;
import static com.example.core.users.UserBuilder.userBuilder;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UsersResourceTest {
    private final IUserService service = mock(IUserService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UsersResource(service))
            .addProvider(new UserNotFoundExceptionMapper())
            .addProvider(new WrongPasswordExceptionMapper())
            .build();


    @Test
    public void registerUser() throws Exception {
        // given
        User user = userBuilder()
                .withId("id")
                .withLogin("login")
                .withPassword("passwordHash")
                .withAccessToken("accessToken")
                .build();
        given(service.register("login", "password")).willReturn(user);
        RegistrationData registrationData = new RegistrationData("login", "password");
        Entity<RegistrationData> entity = entity(registrationData, APPLICATION_JSON_TYPE);

        // when
        AccessTokenData response = resources.client()
                .target("/users")
                .request(APPLICATION_JSON_TYPE)
                .post(entity, AccessTokenData.class);

        // then
        verify(service).register("login", "password");
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
    }

    @Test
    public void registerExistingUserWithWrongPassword() throws Exception {
        // given
        given(service.register("login", "password")).willThrow(new WrongPasswordException());
        RegistrationData registrationData = new RegistrationData("login", "password");
        Entity<RegistrationData> entity = entity(registrationData, APPLICATION_JSON_TYPE);

        // when
        Response response = resources.client()
                .target("/users")
                .request(APPLICATION_JSON_TYPE)
                .post(entity);

        // then
        verify(service).register("login", "password");
        assertThat(response.getStatus()).isEqualTo(FORBIDDEN.getStatusCode());
    }

    @Test
    public void deleteExistingUser() throws Exception {
        // given
        User user = userBuilder()
                .withId("id")
                .withLogin("login")
                .withPassword("passwordHash")
                .withAccessToken("accessToken")
                .build();
        given(service.findByAccessToken("accessToken")).willReturn(user);

        // when
        Response response = resources.client()
                .target("/users")
                .request(APPLICATION_JSON_TYPE)
                .header(ACCESS_TOKEN, "accessToken")
                .delete();

        // then
        assertThat(response.getStatusInfo().getFamily()).isEqualTo(SUCCESSFUL);
        verify(service).delete(user);
    }

    @Test
    public void deleteNonexistingUser() throws Exception {
        // given
        given(service.findByAccessToken("accessToken")).willReturn(null);

        // when
        Response response = resources.client()
                .target("/users")
                .request(APPLICATION_JSON_TYPE)
                .header(ACCESS_TOKEN, "accessToken")
                .delete();

        // then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }
}