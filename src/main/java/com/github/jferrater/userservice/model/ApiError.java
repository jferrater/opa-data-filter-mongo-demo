package com.github.jferrater.userservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author joffryferrater
 */
@Data
@AllArgsConstructor
public class ApiError {

    @Schema(description = "The status code", example = "200, 400, 404, 500")
    private int code;
    @Schema(description = "The status message", example = "OK, Bad Request, Not Found, Internal Server Error")
    private String status;
    @Schema(description = "The error message")
    private String message;

}
