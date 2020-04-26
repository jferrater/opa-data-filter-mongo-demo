package com.github.jferrater.userservice.repository;

import com.github.jferrater.userservice.repository.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author joffryferrater
 */
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByOrganizationAndUsername(String organization, String username);
}
