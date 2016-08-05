package com.example.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieData {
    private String id;
    private String title;
    private String description;
    private Boolean watched;


    private MovieData() {
        // Jackson constructor
    }

    public MovieData(String id, String title, String description, Boolean watched) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.watched = watched;
    }


    @JsonProperty
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotBlank
    @JsonProperty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @JsonProperty
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
        MovieData movieData = (MovieData) o;
        return Objects.equals(id, movieData.id) &&
                Objects.equals(title, movieData.title) &&
                Objects.equals(description, movieData.description) &&
                Objects.equals(watched, movieData.watched);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, watched);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("description", description)
                .add("watched", watched)
                .toString();
    }
}
