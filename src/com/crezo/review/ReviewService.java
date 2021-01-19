package com.crezo.review;

import com.crezo.movie.MovieServiceInterface;
import com.crezo.pojo.Movie;
import com.crezo.pojo.Review;
import com.crezo.pojo.Score;
import com.crezo.pojo.Status;
import com.crezo.pojo.User;
import com.crezo.user.UserServiceInterface;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ReviewService implements ReviewServiceInterface {

    UserServiceInterface userService;
    MovieServiceInterface movieService;

    Map<String, Review> reviewDb;
    Map<String, Score> scoreDb;
    Map<String, Score> genreToRatingData;
    Map<Integer, Score> yearToRatingData;

    public ReviewService(UserServiceInterface userService, MovieServiceInterface movieService) {
        this.userService = userService;
        this.movieService = movieService;
        this.reviewDb = new HashMap<>();
        this.scoreDb = new HashMap<>();
        this.genreToRatingData = new HashMap<>();
        this.yearToRatingData = new HashMap<>();
    }


    @Override
    public void addReview(String userName, String movieName, int rating) throws IllegalArgumentException {
        User user = userService.getUser(userName);
        Movie movie = movieService.getMovie(movieName);

        CompletableFuture<Void> updateScoreFuture = CompletableFuture.runAsync(() -> updateScore(movie, user, rating));

        String key = String.join("_", userName, movieName).toLowerCase();
        Review review = Review.builder().movie(movie).user(user).rating(rating).build();

        if (reviewDb.containsKey(key)) {
            throw new IllegalArgumentException("One user can not review same movie more than once");
        }
        reviewDb.put(key, review);
        try {
            updateScoreFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userService.incrementReviewCount(user);
    }

    @Override
    public List<String> getTopMoviesInYear(int n, int year, Status status) {
        List<Movie> movies = movieService.getMoviesByYear(year);
        return getTopNMovies(n, status, movies);
    }

    @Override
    public List<String> getTopMoviesInGenre(int n, String genre, Status status) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        return getTopNMovies(n, status, movies);
    }

    @Override
    public Float getAverageReviewScore(String movieName) {
        Movie movie = movieService.getMovie(movieName);
        return scoreDb.get(movie.getName()).getTotalRating();
    }

    private List<String> getTopNMovies(int n, Status status, List<Movie> movies) {
        return movies.stream().
                map(movie -> scoreDb.get(movie.getName())).
                filter(x -> x.getUserStatusScoreMap().containsKey(status)).
                sorted((e1, e2) -> Integer.compare(e2.getUserStatusScoreMap().get(status), e1.getUserStatusScoreMap().get(status))).
                limit(n).
                map((e) -> String.format("%s\t%d", e.getMovieName(), e.getUserStatusScoreMap().get(status))).
                collect(Collectors.toList());
    }

    private void updateScore(Movie movie, User user, int rating) {

        if (!scoreDb.containsKey(movie.getName())) {
            addRowToScoreDb(movie, user, rating);
            return;
        }

        Score currentScore = scoreDb.get(movie.getName());

        currentScore.setTotalScore(currentScore.getTotalScore() + getNormalizedRating(user, rating));
        currentScore.setTotalReviews(currentScore.getTotalReviews() + 1);

        if (!currentScore.getUserStatusScoreMap().containsKey(user.getStatus())) {
            currentScore.getUserStatusScoreMap().put(user.getStatus(), 0);
        }
        currentScore.getUserStatusScoreMap().put(user.getStatus(), currentScore.getUserStatusScoreMap().get(user.getStatus()) + rating);

        if (!currentScore.getUserStatusRatingMap().containsKey(user.getStatus())) {
            currentScore.getUserStatusScoreMap().put(user.getStatus(), 0);
        }
        float newScoreForSpecificUserStatus = currentScore.getUserStatusScoreMap().get(user.getStatus()) / currentScore.getTotalReviews();
        currentScore.getUserStatusRatingMap().put(user.getStatus(), newScoreForSpecificUserStatus);

        float totalAverageRating = currentScore.getTotalRating() / currentScore.getTotalReviews();
        currentScore.setTotalRating(totalAverageRating);

        if(!genreToRatingData.containsKey(movie.getName())){
            genreToRatingData.put(movie.getName(), Score.builder().
                    totalScore(getNormalizedRating(user, rating)).
                    totalReviews(1).
                    totalRating(getNormalizedRating(user, rating)).
                    build());
        } else{
            Score score = genreToRatingData.get(movie.getName());
            score.setTotalReviews(score.getTotalReviews() + 1);
            score.setTotalScore(score.getTotalScore() + getNormalizedRating(user, rating));
            score.setTotalRating(score.getTotalScore() / score.getTotalReviews());
        }

        if(!yearToRatingData.containsKey(movie.getYearOfRelease())){
            yearToRatingData.put(movie.getYearOfRelease(), Score.builder().
                    totalScore(getNormalizedRating(user, rating)).
                    totalReviews(1).
                    totalRating(getNormalizedRating(user, rating)).
                    build());
        } else{
            Score score = yearToRatingData.get(movie.getYearOfRelease());
            score.setTotalReviews(score.getTotalReviews() + 1);
            score.setTotalScore(score.getTotalScore() + getNormalizedRating(user, rating));
            score.setTotalRating(score.getTotalScore() / score.getTotalReviews());
        }
    }

    private void addRowToScoreDb(Movie movie, User user, int rating) {

        EnumMap<Status, Integer> userStatusScoreMap = new EnumMap<>(Status.class);
        userStatusScoreMap.put(user.getStatus(), getNormalizedRating(user, rating));
        EnumMap<Status, Float> userStatusRatingMap = new EnumMap<>(Status.class);
        userStatusRatingMap.put(user.getStatus(), (float) getNormalizedRating(user, rating));

        scoreDb.put(movie.getName(), Score.builder().totalReviews(1).
                totalScore(getNormalizedRating(user, rating)).
                userStatusRatingMap(userStatusRatingMap).
                userStatusScoreMap(userStatusScoreMap).
                totalRating((float) getNormalizedRating(user, rating)).
                movieName(movie.getName()).
                build());
    }

    private int getNormalizedRating(User user, int rating) {
        return user.getStatus() == Status.Viewer ? rating : rating * 2;
    }
}
