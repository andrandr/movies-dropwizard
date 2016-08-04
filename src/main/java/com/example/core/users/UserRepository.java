package com.example.core.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findFirstByLogin(String login);

    User findFirstByAccessToken(String accessToken);
}
