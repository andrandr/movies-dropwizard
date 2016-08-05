package com.example.resources;

import com.codahale.metrics.annotation.Timed;
import com.example.api.MovieData;
import com.example.core.movies.IMovieService;
import com.example.core.movies.Movie;
import com.example.core.movies.MovieNotFoundException;
import com.example.core.users.IUserService;
import com.example.core.users.User;
import com.example.core.users.UserNotFoundException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class MoviesResource {
    private final IUserService userService;
    private final IMovieService movieService;


    @Autowired
    public MoviesResource(IUserService userService, IMovieService movieService) {
        checkNotNull(userService);
        checkNotNull(movieService);
        this.userService = userService;
        this.movieService = movieService;
    }


    User findUserOrThrow(String accessToken) {
        User result = userService.findByAccessToken(accessToken);
        if (result != null) {
            return result;
        } else {
            throw new UserNotFoundException();
        }
    }

    Movie mapFromData(MovieData data, Movie result) {
        checkNotNull(data);
        result.setTitle(data.getTitle());
        result.setDescription(data.getDescription());
        result.setWatched(data.getWatched());
        return result;
    }

    MovieData mapToData(Movie movie) {
        checkNotNull(movie);
        return new MovieData(movie.getId(), movie.getTitle(), movie.getDescription(),
                movie.getWatched());
    }

    @GET
    @Timed
    public List<MovieData> listMovies(
            @NotEmpty @HeaderParam(Headers.ACCESS_TOKEN) String accessToken,
            @QueryParam("watched") Boolean watched) {
        User user = findUserOrThrow(accessToken);
        List<Movie> movieList = movieService.list(user, watched);
        return movieList.stream()
                .map(this::mapToData)
                .collect(toList());
    }

    @POST
    @Timed
    public MovieData addMovie(
            @NotEmpty @HeaderParam(Headers.ACCESS_TOKEN) String accessToken,
            @NotNull @Valid MovieData data) {
        User user = findUserOrThrow(accessToken);
        Movie movie = mapFromData(data, new Movie());
        movie.setUser(user);
        movie = movieService.save(movie);
        return mapToData(movie);
    }

    @PUT
    @Path("/{id}")
    @Timed
    public MovieData editMovie(
            @NotEmpty @HeaderParam(Headers.ACCESS_TOKEN) String accessToken,
            @PathParam("id") String movieId,
            @NotNull @Valid MovieData data) {
        User user = findUserOrThrow(accessToken);
        Movie movie = movieService.findById(movieId);
        if (movie == null || !user.idEquals(movie.getUser())) {
            throw new MovieNotFoundException();
        }
        movie = mapFromData(data, movie);
        movieService.save(movie);
        return mapToData(movie);
    }

    @DELETE
    @Path("/{id}")
    @Timed
    public void deleteMovie(
            @NotEmpty @HeaderParam(Headers.ACCESS_TOKEN) String accessToken,
            @PathParam("id") String movieId) {
        User user = findUserOrThrow(accessToken);
        Movie movie = movieService.findById(movieId);
        if (movie == null || !user.idEquals(movie.getUser())) {
            throw new MovieNotFoundException();
        }
        movieService.delete(movie);
    }
}
