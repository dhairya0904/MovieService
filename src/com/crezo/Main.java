package com.crezo;

import com.crezo.movie.MovieService;
import com.crezo.movie.MovieServiceInterface;
import com.crezo.pojo.Status;
import com.crezo.review.ReviewService;
import com.crezo.review.ReviewServiceInterface;
import com.crezo.user.UserService;
import com.crezo.user.UserServiceInterface;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {


        MovieServiceInterface movieService = new MovieService();
        UserServiceInterface userService = new UserService();

        ReviewServiceInterface reviewService = new ReviewService(userService, movieService);

        userService.addUser("SRK");
        userService.addUser("Salman");
        userService.addUser("Deepika");

        movieService.addMovie("Don", 2006, Arrays.asList("Action", "Comedy"));
        movieService.addMovie("Tiger", 2008, Arrays.asList("Drama"));
        movieService.addMovie("Padmavat", 2006, Arrays.asList("Comedy"));
        movieService.addMovie("Lunchbox", 2021, Arrays.asList("Drama"));
        movieService.addMovie("Guru", 2006, Arrays.asList("Drama"));
        movieService.addMovie("Metro", 2006, Arrays.asList("Romance"));

        reviewService.addReview("SRK", "Don", 2);
        reviewService.addReview("SRK", "Padmavat", 8);
        reviewService.addReview("Salman", "Don", 5);
        reviewService.addReview("Deepika", "Don", 9);
        reviewService.addReview("Deepika", "Guru", 6);
//        reviewService.addReview("SRK", "Don", 10); ///// Will throw exception
        reviewService.addReview("Deepika", "LunchBox", 5);
        reviewService.addReview("SRK", "Tiger", 5);
        reviewService.addReview("SRK", "Metro", 7);

        System.out.println(reviewService.getTopMoviesInYear(3, 2006, Status.Viewer));
        System.out.println(reviewService.getTopMoviesInGenre(1, "Drama", Status.Viewer));
        System.out.println(reviewService.getAverageReviewScore("Don"));
    }
}
