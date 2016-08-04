package com.example.core.users;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.core.users.UserBuilder.userBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @MockBean
    private IAccessTokenGenerator tokenGenerator;

    @MockBean
    private IPasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository repository;

    @Autowired
    private IUserService service;


    @Configuration
    @Import(UserService.class)
    static class Config {
    }


    @Before
    public void setup() {
        given(tokenGenerator.generateAccessToken()).willReturn("accessToken");
    }


    @Test
    public void findByAccessToken() throws Exception {
        // given
        User user = userBuilder()
                .withId("id")
                .withLogin("login")
                .withPassword("passwordHash")
                .withAccessToken("accessToken")
                .build();

        given(repository.findFirstByAccessToken("accessToken")).willReturn(user);

        // when
        user = service.findByAccessToken("accessToken");

        // then
        assertThat(user.getId()).isEqualTo("id");
        assertThat(user.getLogin()).isEqualTo("login");
        assertThat(user.getPassword()).isEqualTo("passwordHash");
        assertThat(user.getAccessToken()).isEqualTo("accessToken");
    }

    @Test
    public void registerNewUser() throws Exception {
        // given
        User user = userBuilder()
                .withId("id")
                .withLogin("login")
                .withPassword("passwordHash")
                .withAccessToken("accessToken")
                .build();

        given(passwordEncoder.encodePassword("password")).willReturn("passwordHash");
        given(repository.findFirstByLogin("login")).willReturn(null);
        given(repository.save(any(User.class))).willReturn(user);

        // when
        user = service.register("login", "password");

        // then
        assertThat(user.getId()).isEqualTo("id");
        assertThat(user.getLogin()).isEqualTo("login");
        assertThat(user.getPassword()).isEqualTo("passwordHash");
        assertThat(user.getAccessToken()).isEqualTo("accessToken");
    }

    @Test
    public void registerExistingUser() throws Exception {
        // given
        User user = userBuilder()
                .withId("id")
                .withLogin("login")
                .withPassword("passwordHash")
                .withAccessToken("accessToken")
                .build();

        given(passwordEncoder.isPasswordValid("passwordHash", "password")).willReturn(true);
        given(repository.findFirstByLogin("login")).willReturn(user);

        // when
        user = service.register("login", "password");

        // then
        assertThat(user.getId()).isEqualTo("id");
        assertThat(user.getLogin()).isEqualTo("login");
        assertThat(user.getPassword()).isEqualTo("passwordHash");
        assertThat(user.getAccessToken()).isEqualTo("accessToken");
    }

    @Test(expected = WrongPasswordException.class)
    public void registerExistingUserWithWrongPassword() throws Exception {
        // given
        User user = userBuilder()
                .withId("id")
                .withLogin("login")
                .withPassword("passwordHash")
                .withAccessToken("accessToken")
                .build();

        given(passwordEncoder.isPasswordValid("passwordHash", "password")).willReturn(false);
        given(repository.findFirstByLogin("login")).willReturn(user);

        // when
        service.register("login", "password");

        // then
        // throws exception
    }

    @Test
    public void deleteUser() throws Exception {
        // given
        User user = userBuilder()
                .withId("id")
                .withLogin("login")
                .withPassword("password")
                .withAccessToken("accessToken")
                .build();

        // when
        service.delete(user);

        // then
        verify(repository).delete(user);
    }
}
