package com.github.jferrater.userservice.repository;

import com.github.jferrater.userservice.repository.document.User;
import com.github.jferrater.userservice.repository.document.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author joffryferrater
 */
@DataMongoTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository target;

    private static final List<String> USER_ID_LIST = List.of("b2b1f340-cba2-11e8-ad5d-873445c542a2", "bd5dd3a4-cba2-11e8-9594-3356a2e7ef10");

    @BeforeEach
    void setUp() {
        User alice = new User(USER_ID_LIST.get(0), "SOMA-Clinic", "alice", "password",
                UserType.VETERINARIAN, new String[]{"vet"}, new String[]{"POST:petprofiles", "GET:petprofiles"},
                "Alice Bane", "New York City", "1234567", new Date(), new Date());
        User dodong = new User(USER_ID_LIST.get(1), null, "dodong", "password",
                UserType.PET_OWNER, new String[]{"user"}, new String[]{"POST:petprofiles/{name}", "GET:petprofiles/{name}"},
                "Dodong Francis", "Cebu City", "1234567", new Date(), new Date());

        target.save(alice);
        target.save(dodong);
    }

    @Test
    void shouldGetUserAliceInformation() {
        User result = target.findById(USER_ID_LIST.get(0)).orElse(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getOrganization(), is("SOMA-Clinic"));
        assertThat(result.getUsername(), is("alice"));
        assertThat(result.getPassword(), is("password"));
        assertThat(result.getUserType(), is(UserType.valueOf("VETERINARIAN")));
        assertThat(result.getRoles()[0], is("vet"));
        assertThat(result.getPermissions()[0], is("POST:petprofiles"));
        assertThat(result.getFullName(), is("Alice Bane"));
        assertThat(result.getAddress(), is("New York City"));
        assertThat(result.getContactNumber(), is("1234567"));
        assertThat(result.getCreated(), is(notNullValue()));
        assertThat(result.getUpdated(), is(notNullValue()));
    }

    @Test
    void shouldGetUserByOrganizationAndUsername() {
        User result = target.findByOrganizationAndUsername(null, "dodong").orElse(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getOrganization(), is(nullValue()));
        assertThat(result.getUsername(), is("dodong"));
        assertThat(result.getPassword(), is("password"));
        assertThat(result.getUserType(), is(UserType.valueOf("PET_OWNER")));
        assertThat(result.getRoles()[0], is("user"));
        assertThat(result.getPermissions()[0], is("POST:petprofiles/{name}"));
        assertThat(result.getFullName(), is("Dodong Francis"));
        assertThat(result.getAddress(), is("Cebu City"));
        assertThat(result.getContactNumber(), is("1234567"));
        assertThat(result.getCreated(), is(notNullValue()));
        assertThat(result.getUpdated(), is(notNullValue()));
    }

}