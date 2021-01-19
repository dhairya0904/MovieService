package com.crezo.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class Review {
    User user;
    Movie movie;
    int rating;
}
