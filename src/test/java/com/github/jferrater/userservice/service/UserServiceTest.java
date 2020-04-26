package com.github.jferrater.userservice.service;

import com.github.jferrater.userservice.exceptions.UserNotFoundException;
import com.github.jferrater.userservice.repository.UserRepository;
import com.github.jferrater.userservice.repository.document.User;
import com.github.jferrater.userservice.repository.document.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author joffryferrater
 */
class UserServiceTest {

    private UserService target;
    private UserRepository userRepository;

    private static final List<String> USER_ID_LIST = List.of("b2b1f340-cba2-11e8-ad5d-873445c542a2", "bd5dd3a4-cba2-11e8-9594-3356a2e7ef10");

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        target = new UserService(userRepository);
    }

    @Test
    void shouldCreateUser() {
        User alice = new User(USER_ID_LIST.get(0), "SOMA-Clinic", "alice", "password",
                UserType.VETERINARIAN, new String[]{"vet"}, new String[]{"POST:petprofiles", "GET:petprofiles"},
                "Alice Bane", "New York City", "1234567", new Date(), null);
        when(userRepository.insert(eq(alice))).thenReturn(alice);

        User result = target.createUser(alice);

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
        assertThat(result.getUpdated(), is(nullValue()));
    }

    @Test
    void shouldUpdateUser() {
        User updated = new User(USER_ID_LIST.get(0), "SOMA-Clinic-updated", "alice", "password",
                UserType.VETERINARIAN, new String[]{"vet"}, new String[]{"POST:petprofiles", "GET:petprofiles"},
                "Alice Bane Update", "San Francisco City", "1234567", new Date(), new Date());

        when(userRepository.save(eq(updated))).thenReturn(updated);

        User result = target.updateUser(updated);

        assertThat(result, is(notNullValue()));
        assertThat(result.getOrganization(), is("SOMA-Clinic-updated"));
        assertThat(result.getUsername(), is("alice"));
        assertThat(result.getPassword(), is("password"));
        assertThat(result.getUserType(), is(UserType.valueOf("VETERINARIAN")));
        assertThat(result.getRoles()[0], is("vet"));
        assertThat(result.getPermissions()[0], is("POST:petprofiles"));
        assertThat(result.getFullName(), is("Alice Bane Update"));
        assertThat(result.getAddress(), is("San Francisco City"));
        assertThat(result.getContactNumber(), is("1234567"));
        assertThat(result.getCreated(), is(notNullValue()));
        assertThat(result.getUpdated(), is(notNullValue()));
    }

    @Test
    void shouldFindUserByOrganizationAndUsername() {
        User updated = new User(USER_ID_LIST.get(0), "SOMA-Clinic", "alice", "password",
                UserType.VETERINARIAN, new String[]{"vet"}, new String[]{"POST:petprofiles", "GET:petprofiles"},
                "Alice Bane Update", "San Francisco City", "1234567", new Date(), new Date());

        when(userRepository.findByOrganizationAndUsername("SOMA-Clinic", "alice")).thenReturn(Optional.of(updated));

        User result = target.findUserByOrganizationAndUsername("SOMA-Clinic", "alice");

        assertThat(result, is(notNullValue()));
        assertThat(result.getOrganization(), is("SOMA-Clinic"));
        assertThat(result.getUsername(), is("alice"));
        assertThat(result.getPassword(), is("password"));
        assertThat(result.getUserType(), is(UserType.valueOf("VETERINARIAN")));
        assertThat(result.getRoles()[0], is("vet"));
        assertThat(result.getPermissions()[0], is("POST:petprofiles"));
        assertThat(result.getFullName(), is("Alice Bane Update"));
        assertThat(result.getAddress(), is("San Francisco City"));
        assertThat(result.getContactNumber(), is("1234567"));
        assertThat(result.getCreated(), is(notNullValue()));
        assertThat(result.getUpdated(), is(notNullValue()));
    }

    @Test
    void shouldThrowUserNotFoundException() {
        when(userRepository.findByOrganizationAndUsername("SOMA-Clinic", "alice")).thenThrow(new UserNotFoundException("Not found!"));

        assertThrows(UserNotFoundException.class, () -> target.findUserByOrganizationAndUsername("some org", "dodong"));
    }

    @Test
    void shouldDeleteUserById() {
        doNothing().when(userRepository).deleteById(isA(String.class));

        target.deleteUser(USER_ID_LIST.get(0));

        verify(userRepository, times(1)).deleteById(USER_ID_LIST.get(0));
    }
}