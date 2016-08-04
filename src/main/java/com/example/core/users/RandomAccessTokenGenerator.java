package com.example.core.users;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Thread-safe.
 */
@Component
public class RandomAccessTokenGenerator implements IAccessTokenGenerator {
    private static final int ACCESS_TOKEN_BYTE_LENGTH = 8;
    private final SecureRandom random;


    protected RandomAccessTokenGenerator(SecureRandom random) {
        checkNotNull(random);
        this.random = random;
    }

    public RandomAccessTokenGenerator() {
        this(new SecureRandom());
    }


    @Override
    public String generateAccessToken() {
        byte[] randomBytes = new byte[ACCESS_TOKEN_BYTE_LENGTH];
        random.nextBytes(randomBytes);
        return String.valueOf(Hex.encode(randomBytes));
    }
}
