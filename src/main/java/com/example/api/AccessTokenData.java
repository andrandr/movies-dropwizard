package com.example.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

public class AccessTokenData {
    private final String accessToken;


    public AccessTokenData(@JsonProperty("accessToken") String accessToken) {
        this.accessToken = accessToken;
    }


    @NotBlank
    @JsonProperty
    public String getAccessToken() {
        return accessToken;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessTokenData that = (AccessTokenData) o;
        return Objects.equals(accessToken, that.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("accessToken", accessToken)
                .toString();
    }
}
