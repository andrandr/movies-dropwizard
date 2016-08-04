package com.example.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

public final class RegistrationData {
    private final String login;
    private final String password;


    @JsonCreator
    public RegistrationData(
            @JsonProperty("login") String login,
            @JsonProperty("password") String password) {
        this.login = login;
        this.password = password;
    }


    @NotBlank
    @JsonProperty
    public String getLogin() {
        return login;
    }

    @NotBlank
    @JsonProperty
    public String getPassword() {
        return password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistrationData that = (RegistrationData) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
