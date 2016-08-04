package com.example.core.users;

import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
class Sha1PasswordEncoder extends LdapShaPasswordEncoder implements IPasswordEncoder {
    private final ISaltSource saltSource;


    Sha1PasswordEncoder(ISaltSource saltSource) {
        checkNotNull(saltSource);
        this.saltSource = saltSource;
    }

    Sha1PasswordEncoder() {
        this(new DefaultSaltSource());
    }


    @Override
    public String encodePassword(String rawPass) {
        byte[] salt = saltSource.getSalt();
        return super.encodePassword(rawPass, salt);
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass) {
        return isPasswordValid(encPass, rawPass, null);
    }
}
