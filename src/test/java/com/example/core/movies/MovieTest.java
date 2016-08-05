package com.example.core.movies;

import com.example.core.users.User;
import org.junit.Test;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;

public class MovieTest {
    @Test
    public void testSettersAndGetters() {
        // given
        User user = new User();
        Movie movie = new Movie();

        // when
        movie.setId("id");
        movie.setUser(user);
        movie.setTitle("title");
        movie.setDescription("description");
        movie.setWatched(TRUE);

        // then
        assertEquals(movie.getId(), "id");
        assertEquals(movie.getUser(), user);
        assertEquals(movie.getTitle(), "title");
        assertEquals(movie.getDescription(), "description");
        assertEquals(movie.getWatched(), TRUE);
    }
}