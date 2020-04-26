package com.github.jferrater.userservice.repository.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * @author joffryferrater
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String organization;
    @Indexed(unique = true)
    private String username;
    private String password;
    private UserType userType;
    private String[] roles;
    private String[] permissions;
    private String fullName;
    private String address;
    private String contactNumber;
    @DateTimeFormat(iso = DATE_TIME)
    private Date created;
    @DateTimeFormat(iso = DATE_TIME)
    private Date updated;
}
