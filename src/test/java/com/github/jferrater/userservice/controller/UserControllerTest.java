package com.github.jferrater.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jferrater.userservice.repository.UserRepository;
import com.github.jferrater.userservice.repository.document.User;
import com.github.jferrater.userservice.repository.document.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author joffryferrater
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;

    private static final List<String> USER_ID_LIST = List.of("b2b1f340-cba2-11e8-ad5d-873445c542a2", "bd5dd3a4-cba2-11e8-9594-3356a2e7ef10");

    @Test
    void shouldCreateUser() throws Exception {
        User alice = new User(USER_ID_LIST.get(0), "SOMA-Clinic", "alice", "password",
                UserType.VETERINARIAN, new String[]{"vet"}, new String[]{"POST:petprofiles", "GET:petprofiles"},
                "Alice Bane", "New York City", "1234567", new Date(), null);
        when(userRepository.insert(any(User.class))).thenReturn(alice);

        String requestBody = requestBody();
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(USER_ID_LIST.get(0))))
                .andExpect(jsonPath("$.organization", is("SOMA-Clinic")))
                .andExpect(jsonPath("$.username", is("alice")))
                .andExpect(jsonPath("$.password", is("password")))
                .andExpect(jsonPath("$.userType", is("VETERINARIAN")))
                .andExpect(jsonPath("$.roles[0]", is("vet")))
                .andExpect(jsonPath("$.permissions[0]", is("POST:petprofiles")))
                .andExpect(jsonPath("$.fullName", is("Alice Bane")))
                .andExpect(jsonPath("$.address", is("New York City")))
                .andExpect(jsonPath("$.created", is(notNullValue())));
    }

    @Test
    void shouldReturnConflictStatus() throws Exception {
        when(userRepository.insert(any(User.class))).thenThrow(DuplicateKeyException.class);

        String requestBody = requestBody();
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequestStatus() throws Exception {
        when(userRepository.insert(any(User.class))).thenThrow(IllegalArgumentException.class);

        String requestBody = requestBody();
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalServerStatus() throws Exception {
        when(userRepository.insert(any(User.class))).thenAnswer(invocationOnMock -> new Exception("some internal error"));

        String requestBody = requestBody();
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User alice = new User(USER_ID_LIST.get(0), "SOMA-Clinic", "alice", "password",
                UserType.VETERINARIAN, new String[]{"vet"}, new String[]{"POST:petprofiles", "GET:petprofiles"},
                "Alice Bane", "New York City", "1234567", new Date(), new Date());
        when(userRepository.save(any(User.class))).thenReturn(alice);

        String requestBody = requestBody();
        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_ID_LIST.get(0))))
                .andExpect(jsonPath("$.organization", is("SOMA-Clinic")))
                .andExpect(jsonPath("$.username", is("alice")))
                .andExpect(jsonPath("$.password", is("password")))
                .andExpect(jsonPath("$.userType", is("VETERINARIAN")))
                .andExpect(jsonPath("$.roles[0]", is("vet")))
                .andExpect(jsonPath("$.permissions[0]", is("POST:petprofiles")))
                .andExpect(jsonPath("$.fullName", is("Alice Bane")))
                .andExpect(jsonPath("$.address", is("New York City")))
                .andExpect(jsonPath("$.created", is(notNullValue())))
                .andExpect(jsonPath("$.updated", is(notNullValue())));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userRepository).deleteById(USER_ID_LIST.get(1));

        mockMvc.perform(delete("/users/" + USER_ID_LIST.get(1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindUserByOrganizationAndUsername() throws Exception {
        User alice = new User(USER_ID_LIST.get(0), "SOMA-Clinic", "alice", "password",
                UserType.VETERINARIAN, new String[]{"vet"}, new String[]{"POST:petprofiles", "GET:petprofiles"},
                "Alice Bane", "New York City", "1234567", new Date(), new Date());
        when(userRepository.findByOrganizationAndUsername(anyString(), anyString())).thenReturn(Optional.of(alice));

        mockMvc.perform(get("/users?organization=SOMA-Clinic&username=alice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_ID_LIST.get(0))))
                .andExpect(jsonPath("$.organization", is("SOMA-Clinic")))
                .andExpect(jsonPath("$.username", is("alice")))
                .andExpect(jsonPath("$.password", is("password")))
                .andExpect(jsonPath("$.userType", is("VETERINARIAN")))
                .andExpect(jsonPath("$.roles[0]", is("vet")))
                .andExpect(jsonPath("$.permissions[0]", is("POST:petprofiles")))
                .andExpect(jsonPath("$.fullName", is("Alice Bane")))
                .andExpect(jsonPath("$.address", is("New York City")))
                .andExpect(jsonPath("$.created", is(notNullValue())))
                .andExpect(jsonPath("$.updated", is(notNullValue())));
    }

    private static String requestBody() throws JsonProcessingException {
        User user = new User(USER_ID_LIST.get(0), "SOMA-Clinic", "alice", "password",
                UserType.VETERINARIAN, new String[]{"vet"}, new String[]{"POST:petprofiles", "GET:petprofiles"},
                "Alice Bane", "New York City", "1234567", new Date(), new Date());
        user.setOrganization("SOMA-Clinic");
        user.setUsername("alice");
        user.setPassword("password");
        user.setUserType(UserType.VETERINARIAN);
        user.setRoles( new String[]{"vet"});
        user.setPermissions(new String[]{"POST:petprofiles", "GET:petprofiles"});
        user.setFullName("Alice Bane");
        user.setAddress("New York City");
        user.setContactNumber("1234567");
        return new ObjectMapper().writeValueAsString(user);
    }
}