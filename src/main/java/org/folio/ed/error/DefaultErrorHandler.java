package org.folio.ed.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultErrorHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolation(final ConstraintViolationException exception) {
    var errorMessage = exception.getConstraintViolations().stream()
      .map(ConstraintViolation::getMessage)
      .collect(Collectors.joining("; "));
    return ResponseEntity
      .badRequest()
      .body(errorMessage);
  }
}
