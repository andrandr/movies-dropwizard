package com.example.core.movies;

import com.example.core.users.User;

import java.util.List;

public interface IMovieService {
    List<Movie> list(User user, Boolean watched);

    Movie findById(String id);

    Movie save(Movie movie);

    void delete(Movie movie);
}
