package com.example.core.users;

public interface IUserService {
    User findByAccessToken(String accessToken);

    User register(String login, String password);

    void delete(User user);
}
