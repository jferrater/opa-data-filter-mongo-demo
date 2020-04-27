package com.github.jferrater.userservice.repository;

import com.github.jferrater.opadatafiltermongospringbootstarter.repository.OpaDataFilterMongoRepository;
import com.github.jferrater.userservice.repository.document.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author joffryferrater
 */
@Repository
public interface UserRepository extends OpaDataFilterMongoRepository<User, String> {

    List<User> findByOrganizationAndUsername(String organization, String username);
}
