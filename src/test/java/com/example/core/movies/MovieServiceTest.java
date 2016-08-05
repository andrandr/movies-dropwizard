package com.example.core.movies;

import com.example.core.users.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.example.core.movies.MovieBuilder.movieBuilder;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class MovieServiceTest {
    @MockBean
    private MovieRepository repository;

    @Autowired
    private IMovieService service;


    @Configuration
    @Import(MovieService.class)
    static class Config {
    }


    @Test
    public void listMoviesByUser() throws Exception {
        // given
        User user = new User();
        Movie movie = new Movie();

        given(repository.findByUser(user)).willReturn(singletonList(movie));

        // when
        List<Movie> movies = service.list(user, null);

        // then
        verify(repository).findByUser(user);
        assertThat(movies).containsExactly(movie);
    }

    @Test
    public void listMoviesByUserAndWatched() throws Exception {
        // given
        User user = new User();
        Movie movie = new Movie();

        given(repository.findByUserAndWatched(any(User.class), any(Boolean.class)))
                .willReturn(singletonList(movie));

        // when
        List<Movie> movies = service.list(user, FALSE);

        // then
        verify(repository).findByUserAndWatched(user, FALSE);
        assertThat(movies).containsExactly(movie);
    }

    @Test
    public void findMovieById() throws Exception {
        // given
        Movie movie = movieBuilder()
                .withId("id")
                .withTitle("title")
                .withDescription("description")
                .withWatched(TRUE)
                .build();

        given(repository.findOne("login")).willReturn(movie);

        // when
        movie = service.findById("login");

        // then
        assertThat(movie.getId()).isEqualTo("id");
        assertThat(movie.getTitle()).isEqualTo("title");
        assertThat(movie.getDescription()).isEqualTo("description");
        assertThat(movie.getWatched()).isEqualTo(TRUE);
    }

    @Test
    public void saveMovie() throws Exception {
        // given
        Movie movie = movieBuilder()
                .withId("id")
                .withTitle("title")
                .withDescription("description")
                .withWatched(FALSE)
                .build();

        // when
        service.save(movie);

        // then
        verify(repository).save(movie);
    }

    @Test
    public void deleteMovie() throws Exception {
        // given
        Movie movie = movieBuilder()
                .withId("id")
                .withTitle("title")
                .withDescription("description")
                .withWatched(FALSE)
                .build();

        // when
        service.delete(movie);

        // then
        verify(repository).delete(movie);
    }
}
