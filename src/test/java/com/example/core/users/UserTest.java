package com.example.core.users;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Test
    public void testSettersAndGetters() {
        // given
        User user = new User();

        // when
        user.setId("id");
        user.setLogin("login");
        user.setPassword("password");
        user.setAccessToken("accessToken");

        // then
        assertEquals(user.getId(), "id");
        assertEquals(user.getLogin(), "login");
        assertEquals(user.getPassword(), "password");
        assertEquals(user.getAccessToken(), "accessToken");
    }
}