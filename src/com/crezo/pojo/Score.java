package com.crezo.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@Setter
@ToString
public class Score {

    String movieName;
    int totalScore;
    Map<Status, Integer> userStatusScoreMap;
    int totalReviews;
    float totalRating;
    Map<Status, Float> userStatusRatingMap;
}
