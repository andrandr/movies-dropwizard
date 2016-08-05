package com.example.resources;

import com.example.api.MovieData;
import com.example.core.movies.IMovieService;
import com.example.core.movies.Movie;
import com.example.core.users.IUserService;
import com.example.core.users.User;
import com.example.core.users.UserNotFoundException;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import java.util.List;

import static com.example.core.movies.MovieBuilder.movieBuilder;
import static com.example.core.users.UserBuilder.userBuilder;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MoviesResourceTest {
    private final IUserService userService = mock(IUserService.class);
    private final IMovieService movieService = mock(IMovieService.class);
    private final MoviesResource moviesResource = new MoviesResource(userService, movieService);
    private final User user = userBuilder()
            .withId("id")
            .withLogin("login")
            .withPassword("passwordHash")
            .withAccessToken("accessToken")
            .build();

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(moviesResource)
            .addProvider(new MovieNotFoundExceptionMapper())
            .addProvider(new UserNotFoundExceptionMapper())
            .build();


    @Test
    public void findExistingUserDoesntThrowException() throws Exception {
        // given
        given(userService.findByAccessToken("accessToken")).willReturn(user);

        // when
        User foundUser = moviesResource.findUserOrThrow("accessToken");

        // then
        verify(userService).findByAccessToken("accessToken");
        assertThat(foundUser).isEqualTo(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void findNonexistingUserThrowsException() throws Exception {
        // given
        given(userService.findByAccessToken("accessToken")).willReturn(null);

        // when
        moviesResource.findUserOrThrow("accessToken");

        // then
        // exception
    }

    @Test
    public void mapFromData() throws Exception {
        // given
        MovieData data = new MovieData("id", "title", "description", TRUE);

        // when
        Movie movie = moviesResource.mapFromData(data, new Movie());

        // then
        assertThat(movie.getId()).isNull();
        assertThat(movie.getUser()).isNull();
        assertThat(movie.getTitle()).isEqualTo("title");
        assertThat(movie.getDescription()).isEqualTo("description");
        assertThat(movie.getWatched()).isEqualTo(TRUE);
    }

    @Test
    public void mapToData() throws Exception {
        // given
        Movie movie = movieBuilder()
                .withId("id")
                .withUser(user)
                .withTitle("title")
                .withDescription("description")
                .withWatched(TRUE)
                .build();

        // when
        MovieData data = moviesResource.mapToData(movie);

        // then
        assertThat(data.getId()).isEqualTo("id");
        assertThat(data.getTitle()).isEqualTo("title");
        assertThat(data.getDescription()).isEqualTo("description");
        assertThat(data.getWatched()).isEqualTo(TRUE);
    }

    @Test
    public void listMoviesWithoutAccessToken() throws Exception {
        // given
        given(userService.findByAccessToken("accessToken")).willReturn(null);

        // when
        Response response = resources.client()
                .target("/movies")
                .request(APPLICATION_JSON_TYPE)
                .header(Headers.ACCESS_TOKEN, "accessToken")
                .get();

        // then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void listMovies() throws Exception {
        // given
        Movie movie = movieBuilder()
                .withId("id")
                .withTitle("title")
                .withDescription("description")
                .withWatched(TRUE)
                .build();
        given(userService.findByAccessToken("accessToken")).willReturn(user);
        given(movieService.list(eq(user), any())).willReturn(singletonList(movie));

        // when
        Response response = resources.client()
                .target("/movies")
                .request(APPLICATION_JSON_TYPE)
                .header(Headers.ACCESS_TOKEN, "accessToken")
                .get();
        List<MovieData> list = response.readEntity(new GenericType<List<MovieData>>() {});

        // then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

        MovieData data = new MovieData("id", "title", "description", TRUE);
        assertThat(list).containsExactly(data);
    }

    @Test
    public void addMovieWithoutAccessToken() throws Exception {
        // given
        MovieData data = new MovieData("id", "title", "description", TRUE);
        given(userService.findByAccessToken("accessToken")).willReturn(null);

        // when
        Response response = resources.client()
                .target("/movies")
                .request(APPLICATION_JSON_TYPE)
                .header(Headers.ACCESS_TOKEN, "accessToken")
                .post(entity(data, APPLICATION_JSON_TYPE));

        // then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void addMovie() throws Exception {
        // given
        MovieData data = new MovieData(null, "title", "description", TRUE);
        Movie savedMovie = movieBuilder()
                .withId("id")
                .withUser(user)
                .withTitle("title")
                .withDescription("description")
                .withWatched(TRUE)
                .build();

        ArgumentCaptor<Movie> movieToSaveCaptor = ArgumentCaptor.forClass(Movie.class);
        given(userService.findByAccessToken("accessToken")).willReturn(user);
        given(movieService.save(movieToSaveCaptor.capture())).willReturn(savedMovie);

        // when
        Response response = resources.client()
                .target("/movies")
                .request(APPLICATION_JSON_TYPE)
                .header(Headers.ACCESS_TOKEN, "accessToken")
                .post(entity(data, APPLICATION_JSON_TYPE));
        MovieData returnedData = response.readEntity(MovieData.class);

        // then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

        Movie movieToSave = movieToSaveCaptor.getValue();
        movieToSave.setId(savedMovie.getId());
        assertThat(movieToSave).isEqualTo(savedMovie);

        data.setId(returnedData.getId());
        assertThat(returnedData).isEqualTo(data);
    }

    @Test
    public void editMovieWithoutAccessToken() throws Exception {
        // given
        MovieData data = new MovieData(null, "title", "description", TRUE);
        given(userService.findByAccessToken("accessToken")).willReturn(null);

        // when
        Response response = resources.client()
                .target("/movies/id")
                .request(APPLICATION_JSON_TYPE)
                .header(Headers.ACCESS_TOKEN, "accessToken")
                .put(entity(data, APPLICATION_JSON_TYPE));

        // then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void editMovie() throws Exception {
        // given
        MovieData data = new MovieData(null, "newTitle", "newDescription", TRUE);
        Movie movie = movieBuilder()
                .withId("id")
                .withUser(user)
                .withTitle("title")
                .withDescription("description")
                .withWatched(FALSE)
                .build();
        Movie savedMovie = movieBuilder()
                .withId("id")
                .withUser(user)
                .withTitle("newTitle")
                .withDescription("newDescription")
                .withWatched(TRUE)
                .build();

        ArgumentCaptor<Movie> updatedMovieCaptor = ArgumentCaptor.forClass(Movie.class);
        given(userService.findByAccessToken("accessToken")).willReturn(user);
        given(movieService.findById("id")).willReturn(movie);
        given(movieService.save(updatedMovieCaptor.capture())).willReturn(savedMovie);

        // when
        Response response = resources.client()
                .target("/movies/id")
                .request(APPLICATION_JSON_TYPE)
                .header(Headers.ACCESS_TOKEN, "accessToken")
                .put(entity(data, APPLICATION_JSON_TYPE));
        MovieData returnedData = response.readEntity(MovieData.class);

        // then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

        assertThat(updatedMovieCaptor.getValue()).isEqualTo(savedMovie);

        data.setId(returnedData.getId());
        assertThat(returnedData).isEqualTo(data);
    }

    @Test
    public void deleteMovieWithoutAccessToken() throws Exception {
        // given
        given(userService.findByAccessToken("accessToken")).willReturn(null);

        // when
        Response response = resources.client()
                .target("/movies/id")
                .request(APPLICATION_JSON_TYPE)
                .header(Headers.ACCESS_TOKEN, "accessToken")
                .delete();

        // then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
        verify(movieService, never()).delete(any());
    }

    @Test
    public void deleteMovie() throws Exception {
        // given
        Movie movie = movieBuilder()
                .withId("id")
                .withUser(user)
                .withTitle("title")
                .withDescription("description")
                .withWatched(FALSE)
                .build();
        given(userService.findByAccessToken("accessToken")).willReturn(user);
        given(movieService.findById("id")).willReturn(movie);

        // when
        Response response = resources.client()
                .target("/movies/id")
                .request(APPLICATION_JSON_TYPE)
                .header(Headers.ACCESS_TOKEN, "accessToken")
                .delete();

        // then
        assertThat(response.getStatusInfo().getFamily()).isEqualTo(Family.SUCCESSFUL);
        verify(movieService).delete(movie);
    }
}