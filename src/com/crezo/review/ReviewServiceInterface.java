package com.crezo.review;

import com.crezo.pojo.Status;

import java.util.List;

public interface ReviewServiceInterface {
    void addReview(String userName, String movie, int rating) throws IllegalArgumentException;
    List<String> getTopMoviesInYear(int n, int year, Status status);
    List<String> getTopMoviesInGenre(int n, String genre, Status status);
    Float getAverageReviewScore(String movie);
}
