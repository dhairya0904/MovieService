package com.crezo.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class Movie {
    String name;
    int yearOfRelease;
    List<String> genre;
}
