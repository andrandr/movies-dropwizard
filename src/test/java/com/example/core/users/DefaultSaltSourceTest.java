package com.example.core.users;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultSaltSourceTest {
    @Test
    public void getSalt() {
        // given
        ISaltSource saltSource = new DefaultSaltSource();

        // when
        byte[] salt = saltSource.getSalt();

        // then
        assertEquals(salt.length, 8);
    }
}