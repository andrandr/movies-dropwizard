package com.example.core.users;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class DefaultSaltSource implements ISaltSource {
    private static final SecureRandom prng;

    static {
        try {
            prng = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public byte[] getSalt() {
        byte[] result = new byte[8];
        prng.nextBytes(result);
        return result;
    }
}
