package com.crezo.user;

import com.crezo.pojo.User;

public interface UserServiceInterface {

    void addUser(String name);
    User getUser(String name);
    void incrementReviewCount(User user);
}
