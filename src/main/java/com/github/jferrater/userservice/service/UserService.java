package com.github.jferrater.userservice.service;

import com.github.jferrater.userservice.exceptions.UserNotFoundException;
import com.github.jferrater.userservice.repository.UserRepository;
import com.github.jferrater.userservice.repository.document.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author joffryferrater
 */
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if(user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        user.setCreated(new Date());
        return userRepository.insert(user);
    }

    public User updateUser(User user) {
        user.setUpdated(new Date());
        return userRepository.save(user);
    }

    public User findUserByOrganizationAndUsername(String organization, String username) {
        return userRepository.findByOrganizationAndUsername(organization, username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with organization '%s' and username '%s' is not found!", organization, username)));
    }

    public void deleteUser(String uuid) {
        userRepository.deleteById(uuid);
    }
}
