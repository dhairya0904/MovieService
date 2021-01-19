package com.crezo.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class User {
    String name;
    Status status;
    int totalReviews;
}