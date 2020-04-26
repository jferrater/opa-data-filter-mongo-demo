package com.github.jferrater.userservice.model;

import com.github.jferrater.userservice.repository.document.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * @author joffryferrater
 */
@Data
@NoArgsConstructor
public class UserDto {

    @Schema(description = "The id of the user in uuid format")
    private String id;
    @NotNull
    @Schema(description = "The organization or the clinic name")
    private String organization;
    @NotNull
    @Schema(description = "The username")
    private String username;
    @NotNull
    @Schema(description = "The password")
    private String password;
    @Schema(description = "The user type.")
    private UserType userType;
    @Schema(description = "The roles assigned to a user")
    private String[] roles;
    @Schema(description = "The permissions given to a role")
    private String[] permissions;
    @Schema(description = "The user's full name")
    private String fullName;
    @Schema(description = "The user's addreess")
    private String address;
    @Schema(description = "The user's contact number")
    private String contactNumber;
    @Schema(description = "The date created")
    @DateTimeFormat(iso = DATE_TIME)
    private Date created;
    @Schema(description = "The date updated")
    @DateTimeFormat(iso = DATE_TIME)
    private Date updated;
}
