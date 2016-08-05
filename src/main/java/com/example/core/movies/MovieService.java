package com.example.core.movies;

import com.example.core.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Service
@Transactional
class MovieService implements IMovieService {
    private final MovieRepository repository;


    @Autowired
    MovieService(MovieRepository repository) {
        checkNotNull(repository);
        this.repository = repository;
    }


    @Override
    public List<Movie> list(User user, Boolean watched) {
        checkNotNull(user);
        if (watched == null) {
            return repository.findByUser(user);
        } else {
            return repository.findByUserAndWatched(user, watched);
        }
    }

    @Override
    public Movie findById(String id) {
        checkArgument(!isNullOrEmpty(id));
        return repository.findOne(id);
    }

    @Override
    public Movie save(Movie movie) {
        checkNotNull(movie);
        return repository.save(movie);
    }

    @Override
    public void delete(Movie movie) {
        checkNotNull(movie);
        repository.delete(movie);
    }
}
