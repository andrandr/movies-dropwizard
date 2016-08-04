package com.example.core.users;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Sha1PasswordEncoderTest {
    private IPasswordEncoder encoder;


    @Before
    public void setup() {
        ISaltSource mockedSaltSource = mock(ISaltSource.class);
        when(mockedSaltSource.getSalt()).thenReturn(new byte[8]);
        encoder = new Sha1PasswordEncoder(mockedSaltSource);
    }

    @Test
    public void encodePassword() throws Exception {
        // given
        String rawPassword = "test1234";

        // when
        String passwordHash = encoder.encodePassword(rawPassword);

        // then
        assertEquals(passwordHash, "{SSHA}sA1npcF+i2VYHj8PGQwRqyy9QIsAAAAAAAAAAA==");
    }

    @Test
    public void isPasswordValid() throws Exception {
        // given
        String rawPassword = "test1234";
        String passwordHash = "{SSHA}ydsVJ2h5WIeqsC+vwUbTBf88XtrQ2MLCb5tmlg==";

        // when
        boolean valid = encoder.isPasswordValid(passwordHash, rawPassword);

        // then
        assertTrue(valid);
    }
}