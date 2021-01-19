package com.crezo.movie;

import com.crezo.pojo.Movie;

import java.util.List;

public interface MovieServiceInterface {

    void addMovie(String name, int yearOfRelease, List<String> genre);
    Movie getMovie(String name);
    List<Movie> getMoviesByYear(int year);
    List<Movie> getMoviesByGenre(String genre);
}
