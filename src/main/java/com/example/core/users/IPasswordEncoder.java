package com.example.core.users;

public interface IPasswordEncoder {
    String encodePassword(String rawPass);

    boolean isPasswordValid(String encPass, String rawPass);
}
