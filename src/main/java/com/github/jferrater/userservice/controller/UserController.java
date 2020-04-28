package com.github.jferrater.userservice.controller;

import com.github.jferrater.userservice.model.ApiError;
import com.github.jferrater.userservice.model.UserDto;
import com.github.jferrater.userservice.repository.document.User;
import com.github.jferrater.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author joffryferrater
 */
@RestController
@Tag(name = "user_service", description = "The User Service API")
@CrossOrigin(maxAge = 3600)
public class UserController {

    private UserService userService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Operation(
            summary = "Ping api",
            description = "Returns pong if the service is ready",
            tags = "user_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }


    @Operation(
            summary = "Fill mongo database with initial data for testing purposes",
            description = "This is used for integration test",
            tags = "user_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PostMapping("/init")
    public ResponseEntity<Void> createTestData() {
        userService.initTestData();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete all users",
            description = "Delete all users API",
            tags = "user_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteAll() {
        userService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Create new user",
            description = "Returns the user info",
            tags = "user_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = convertToUser(userDto);
        UserDto response = convertToUserDto(userService.createUser(user));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update the user info",
            description = "Returns the user information",
            tags = "user_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PutMapping("/users")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        User user = convertToUser(userDto);
        UserDto response = convertToUserDto(userService.updateUser(user));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Find the user by organization and username",
            description = "Returns the user  information",
            tags = "user_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findUserByOrganizationAndUsername(
            @Parameter(description = "The organization of the user.") @RequestParam("organization") @Nullable String organization,
            @Parameter(description = "The username of the user") @Nullable @RequestParam("username") String username) {
        List<User> users;
        if (organization == null && username == null) {
            users = new ArrayList<>(userService.findAllUsers());
        } else {
            users = new ArrayList<>(userService.findUserByOrganizationAndUsername(organization, username));
        }
        List<UserDto> userDtos = users.stream().map(this::convertToUserDto).collect(toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user by id",
            description = "Delete a user API",
            tags = "user_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "The id of the user.") @PathVariable("id") String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
