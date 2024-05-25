package dev.aziz.bankingservice.exceptions;

import dev.aziz.bankingservice.dtos.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorDto.builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(MethodArgumentNotValidException exception) {
        String message = "";
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            message = message + fieldError.getDefaultMessage() + " | ";
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder().message(message).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(ConstraintViolationException exception) {
        String message = "";
        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            message = message + violation.getMessage() + " | ";
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder().message(message).build());
    }
}
