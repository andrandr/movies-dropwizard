package com.example.core.users;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.core.users.UserBuilder.userBuilder;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;


    @Before
    public void setup() {
        entityManager.persist(userBuilder()
                .withLogin("login1")
                .withPassword("password1")
                .withAccessToken("accessToken1")
                .build());
        entityManager.persist(userBuilder()
                .withLogin("login2")
                .withPassword("password2")
                .withAccessToken("accessToken2")
                .build());
    }

    @Test
    public void findByExistingLoginShouldReturnAnActualUser() throws Exception {
        User user = repository.findFirstByLogin("login1");
        assertEquals(user.getLogin(), "login1");
        assertEquals(user.getPassword(), "password1");
        assertEquals(user.getAccessToken(), "accessToken1");
        assertNotNull(user.getId());
    }

    @Test
    public void findByNonexistingLoginShouldReturnNull() throws Exception {
        assertNull(repository.findFirstByLogin("unknownLogin"));
    }

    @Test
    public void findByAccessTokenShouldReturnUserWithAccessToken() throws Exception {
        User user = repository.findFirstByAccessToken("accessToken2");
        assertEquals(user.getLogin(), "login2");
        assertEquals(user.getPassword(), "password2");
        assertEquals(user.getAccessToken(), "accessToken2");
        assertNotNull(user.getId());
    }

    @Test
    public void findByNonexistingAccessTokenShouldReturnNull() throws Exception {
        assertNull(repository.findFirstByAccessToken("unknownAccessToken"));
    }
}