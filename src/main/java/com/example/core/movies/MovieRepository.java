package com.example.core.movies;

import com.example.core.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, String> {
    List<Movie> findByUser(User user);

    List<Movie> findByUserAndWatched(User user, Boolean watched);
}
