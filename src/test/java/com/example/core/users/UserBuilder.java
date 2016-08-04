package com.example.core.users;

public final class UserBuilder {
    private final User result = new User();


    public static UserBuilder userBuilder() {
        return new UserBuilder();
    }


    public UserBuilder withId(String id) {
        result.setId(id);
        return this;
    }

    public UserBuilder withLogin(String login) {
        result.setLogin(login);
        return this;
    }

    public UserBuilder withPassword(String password) {
        result.setPassword(password);
        return this;
    }

    public UserBuilder withAccessToken(String accessToken) {
        result.setAccessToken(accessToken);
        return this;
    }

    public User build() {
        return result;
    }
}
