package com.ruker.workspace.app.dto.response;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API Response wrapper to standardize responses across the application.
 *
 * @param <T> the type of data being returned
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private final ZonedDateTime timestamp = ZonedDateTime.now();
    private T data;
    private String error;
    private String message;

    /**
     * Constructor to create a successful response with data and a message.
     *
     * @param data    the response data
     * @param message the success message
     */
    public ApiResponse(T data, String message) {
        this(data, null, message);
    }

    /**
     * Constructor to create an error response with an error message and details.
     *
     * @param error   the error message or code
     * @param message additional information about the error
     */
    public ApiResponse(String error, String message) {
        this(null, error, message);
    }
}