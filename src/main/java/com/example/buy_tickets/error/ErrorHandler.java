package com.example.buy_tickets.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponse.FieldErrorResponse> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldErrorResponse)
                .collect(Collectors.toList());

        ValidationErrorResponse response = new ValidationErrorResponse(
                Instant.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ValidationErrorResponse.FieldErrorResponse> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(error -> new ValidationErrorResponse.FieldErrorResponse(
                        error.getPropertyPath().iterator().hasNext()
                                ? lastPathSegment(error.getPropertyPath().toString())
                                : "request",
                        error.getMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponse response = new ValidationErrorResponse(
                Instant.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ValidationErrorResponse.FieldErrorResponse toFieldErrorResponse(FieldError error) {
        return new ValidationErrorResponse.FieldErrorResponse(error.getField(), error.getDefaultMessage());
    }

    private String lastPathSegment(String propertyPath) {
        int separator = propertyPath.lastIndexOf('.');
        return separator >= 0 ? propertyPath.substring(separator + 1) : propertyPath;
    }

    @Schema(description = "Validation error response")
    public static class ValidationErrorResponse {
        @Schema(description = "Error timestamp", example = "2026-07-18T14:30:45.185Z")
        private final String timestamp;
        @Schema(description = "HTTP status code", example = "400")
        private final int status;
        @Schema(description = "HTTP status reason", example = "Bad Request")
        private final String error;
        @Schema(description = "Field validation errors")
        private final List<FieldErrorResponse> errors;

        public ValidationErrorResponse(String timestamp, int status, String error, List<FieldErrorResponse> errors) {
             System.out.println(
                    "Message received: "
                            + error);
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.errors = errors;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public List<FieldErrorResponse> getErrors() {
            return errors;
        }

        @Schema(description = "Field-level validation detail")
        public static class FieldErrorResponse {
            @Schema(description = "Invalid field name", example = "userEmail")
            private final String field;
            @Schema(description = "Validation message", example = "must not be null")
            private final String message;

            public FieldErrorResponse(String field, String message) {
                this.field = field;
                this.message = message;
            }

            public String getField() {
                return field;
            }

            public String getMessage() {
                return message;
            }
        }
    }
}
