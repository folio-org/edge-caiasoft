package org.folio.ed.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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

  @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
  public ResponseEntity<String> handleInternal(final RuntimeException exception) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(exception.getMessage());
  }
}
