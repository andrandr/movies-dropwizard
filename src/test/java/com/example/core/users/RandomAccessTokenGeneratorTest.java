package com.example.core.users;

import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

public class RandomAccessTokenGeneratorTest {
    @Test
    public void generateAccessToken() {
        //given
        RandomAccessTokenGenerator generator = new RandomAccessTokenGenerator(new FakeRandomGenerator());

        // when
        String token = generator.generateAccessToken();

        // then
        assertEquals(token, "0001020304050607");
    }


    private static class FakeRandomGenerator extends SecureRandom {
        @Override
        public synchronized void nextBytes(byte[] bytes) {
            int n = bytes.length;
            for (int i = 0; i < n; ++i) {
                bytes[i] = (byte) (i & 0xFF);
            }
        }
    }
}
