package com.github.jferrater.userservice.service;

import com.github.jferrater.userservice.repository.UserRepository;
import com.github.jferrater.userservice.repository.document.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void initTestData() {
        TestData testData = new TestData(userRepository);
        testData.createTestData();
    }

    public void deleteAll() {
        userRepository.deleteAll();
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

    public List<User> findUserByOrganizationAndUsername(String organization, String username) {
        return userRepository.findByOrganizationAndUsername(organization, username);
    }

    public void deleteUser(String uuid) {
        userRepository.deleteById(uuid);
    }
}
