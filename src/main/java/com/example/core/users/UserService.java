package com.example.core.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Service
@Transactional
class UserService implements IUserService {
    private final IAccessTokenGenerator accessTokenGenerator;
    private final IPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Autowired
    public UserService(
            IAccessTokenGenerator accessTokenGenerator,
            IPasswordEncoder passwordEncoder,
            UserRepository userRepository) {
        checkNotNull(accessTokenGenerator);
        checkNotNull(passwordEncoder);
        checkNotNull(userRepository);
        this.accessTokenGenerator = accessTokenGenerator;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Override
    public User findByAccessToken(String accessToken) {
        checkArgument(!isNullOrEmpty(accessToken));
        return userRepository.findFirstByAccessToken(accessToken);
    }

    @Override
    public User register(String login, String password) {
        checkArgument(!isNullOrEmpty(login));
        checkArgument(!isNullOrEmpty(password));

        User user = userRepository.findFirstByLogin(login);
        if (user == null) {
            user = new User();
            user.setLogin(login);
            user.setPassword(passwordEncoder.encodePassword(password));
            user.setAccessToken(accessTokenGenerator.generateAccessToken());
            user = userRepository.save(user);
        } else {
            if (!passwordEncoder.isPasswordValid(user.getPassword(), password)) {
                throw new WrongPasswordException();
            }
        }
        return user;
    }

    @Override
    public void delete(User user) {
        checkNotNull(user);
        userRepository.delete(user);
    }
}
