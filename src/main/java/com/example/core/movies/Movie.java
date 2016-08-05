package com.example.core.movies;


import com.example.core.users.User;
import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

import java.util.Objects;

import static java.lang.Boolean.FALSE;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Table(
        name = "movies",
        uniqueConstraints = @UniqueConstraint(
                name = "uni_movies_user_id_title",
                columnNames = {"user_id", "title"}))
public final class Movie {
    private String id;
    private User user;
    private String title;
    private String description;
    private Boolean watched = FALSE;


    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = CASCADE)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "title", length = 120, nullable = false)
    @NotEmpty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", length = 3000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "watched", nullable = false)
    public Boolean getWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) &&
                Objects.equals(user, movie.user) &&
                Objects.equals(title, movie.title) &&
                Objects.equals(description, movie.description) &&
                Objects.equals(watched, movie.watched);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, title, description, watched);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("user", user)
                .add("title", title)
                .add("description", description)
                .add("watched", watched)
                .toString();
    }
}
