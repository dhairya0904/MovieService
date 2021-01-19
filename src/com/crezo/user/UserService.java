package com.crezo.user;

import com.crezo.pojo.Status;
import com.crezo.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class UserService implements UserServiceInterface {

    Map<String, User> users;

    final static int THRESHOLD = 3;
    String USER_ALREADY_EXISTS = "User already exists";
    String USER_DOES_NOT_EXIST = "User does not exist";

    public UserService() {
        users = new HashMap<>();
    }

    @Override
    public void addUser(String name) {
        if (users.containsKey((name.toLowerCase()))) {
            throw new IllegalArgumentException(USER_ALREADY_EXISTS);
        }

        users.put(name.toLowerCase(), User.builder().name(name.toLowerCase()).
                status(Status.Viewer).
                totalReviews(1).
                build());
    }

    @Override
    public User getUser(String name) {
        if (!users.containsKey((name.toLowerCase()))) {
            throw new IllegalArgumentException(USER_DOES_NOT_EXIST);
        }
        return users.get(name.toLowerCase());
    }

    @Override
    public void incrementReviewCount(User user) {
        user.setTotalReviews(user.getTotalReviews() + 1);

        if(user.getTotalReviews() > THRESHOLD) {
            user.setStatus(Status.Critic);
        }
    }
}
