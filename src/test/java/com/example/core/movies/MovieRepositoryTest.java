package com.example.core.movies;

import com.example.core.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.example.core.movies.MovieBuilder.movieBuilder;
import static com.example.core.users.UserBuilder.userBuilder;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MovieRepository repository;

    private User user1;

    private User user2;

    private Movie movie1;

    private Movie movie2;

    private Movie movie3;


    @Before
    public void setup() {
        user1 = userBuilder()
                .withLogin("login1")
                .withPassword("password1")
                .withAccessToken("accessToken1")
                .build();
        user2 = userBuilder()
                .withLogin("login2")
                .withPassword("password2")
                .withAccessToken("accessToken2")
                .build();
        movie1 = movieBuilder()
                .withUser(user1)
                .withTitle("title1")
                .withDescription("description1")
                .withWatched(FALSE)
                .build();
        movie2 = movieBuilder()
                .withUser(user2)
                .withTitle("title2")
                .withDescription("description2")
                .withWatched(FALSE)
                .build();
        movie3 = movieBuilder()
                .withUser(user2)
                .withTitle("title3")
                .withDescription("description3")
                .withWatched(TRUE)
                .build();
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(movie1);
        entityManager.persist(movie2);
        entityManager.persist(movie3);
    }

    @Test
    public void findByExistingUserShouldReturnHisMovies() throws Exception {
        // given
        // when
        List<Movie> movies = repository.findByUser(user1);

        // then
        assertThat(movies).containsExactly(movie1);
    }

    @Test
    public void findByNonexistingUserShouldReturnEmptyList() throws Exception {
        // given
        User fakeUser = userBuilder()
                .withId("fakeId")
                .withLogin("fakeLogin")
                .withPassword("fakePassword")
                .withAccessToken("fakeAccessToken")
                .build();

        // when
        List<Movie> movies = repository.findByUser(fakeUser);

        // then
        assertThat(movies).isEmpty();
    }

    @Test
    public void findByUserAndNotWatchedShouldReturnHisUnwatchedMovies() throws Exception {
        // given
        // when
        List<Movie> movies = repository.findByUserAndWatched(user2, FALSE);

        // then
        assertThat(movies).containsExactly(movie2);
    }

    @Test
    public void findByUserAndWatchedShouldReturnHisWatchedMovies() throws Exception {
        // given
        // when
        List<Movie> movies = repository.findByUserAndWatched(user2, TRUE);

        // then
        assertThat(movies).containsExactly(movie3);
    }

    @Test
    public void findByNonexistingUserAndWatchedShouldReturnEmptyList() throws Exception {
        // given
        User fakeUser = userBuilder()
                .withId("fakeId")
                .withLogin("fakeLogin")
                .withPassword("fakePassword")
                .withAccessToken("fakeAccessToken")
                .build();

        // when
        List<Movie> movies = repository.findByUserAndWatched(fakeUser, TRUE);

        // then
        assertThat(movies).isEmpty();
    }
}
