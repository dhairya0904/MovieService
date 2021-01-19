package com.crezo.movie;

import com.crezo.pojo.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieService implements MovieServiceInterface {

    Map<String, Movie> movies;
    Map<String, List<Movie>> secondaryIndexGenre;
    Map<Integer, List<Movie>> secondaryIndexYear;

    String MOVIE_ALREADY_EXISTS = "Movie already exists";
    String MOVIE_DOES_NOT_EXIST = "Movie does not exist";

    public MovieService() {
        movies = new HashMap<>();
        secondaryIndexGenre = new HashMap<>();
        secondaryIndexYear = new HashMap<>();
    }

    @Override
    public void addMovie(String name, int yearOfRelease, List<String> genres) {
        if (movies.containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException(MOVIE_ALREADY_EXISTS);
        }

        Movie movie = Movie.builder().
                name(name.toLowerCase()).
                yearOfRelease(yearOfRelease).
                genre(genres).build();

        movies.put(name.toLowerCase(), movie);

        for (String genre : genres) {
            if (!secondaryIndexGenre.containsKey(genre)) {
                secondaryIndexGenre.put(genre, new ArrayList<Movie>());
            }
            secondaryIndexGenre.get(genre).add(movie);
        }

        if (!secondaryIndexYear.containsKey(yearOfRelease)) {
            secondaryIndexYear.put(yearOfRelease, new ArrayList<>());
        }
        secondaryIndexYear.get(yearOfRelease).add(movie);
    }

    @Override
    public Movie getMovie(String name) {
        if (!movies.containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException(MOVIE_DOES_NOT_EXIST);
        }
        return movies.get(name.toLowerCase());
    }

    @Override
    public List<Movie> getMoviesByYear(int year) {
        return secondaryIndexYear.get(year);
    }

    @Override
    public List<Movie> getMoviesByGenre(String genre) {
        return secondaryIndexGenre.get(genre);
    }
}
