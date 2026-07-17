package com.example.buy_tickets.error;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

    private ValidationErrorResponse.FieldErrorResponse toFieldErrorResponse(FieldError error) {
        return new ValidationErrorResponse.FieldErrorResponse(error.getField(), error.getDefaultMessage());
    }

    public static class ValidationErrorResponse {
        private final String timestamp;
        private final int status;
        private final String error;
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

        public static class FieldErrorResponse {
            private final String field;
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
