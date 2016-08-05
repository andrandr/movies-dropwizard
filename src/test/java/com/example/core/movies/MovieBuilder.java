package com.example.core.movies;

import com.example.core.users.User;

public class MovieBuilder {
    private final Movie result = new Movie();


    public static MovieBuilder movieBuilder() {
        return new MovieBuilder();
    }


    public MovieBuilder withId(String id) {
        result.setId(id);
        return this;
    }

    public MovieBuilder withUser(User user) {
        result.setUser(user);
        return this;
    }

    public MovieBuilder withTitle(String title) {
        result.setTitle(title);
        return this;
    }

    public MovieBuilder withDescription(String description) {
        result.setDescription(description);
        return this;
    }

    public MovieBuilder withWatched(Boolean watched) {
        result.setWatched(watched);
        return this;
    }

    public Movie build() {
        return result;
    }
}
