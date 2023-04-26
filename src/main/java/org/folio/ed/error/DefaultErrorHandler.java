package org.folio.ed.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class DefaultErrorHandler {
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolation(final ConstraintViolationException exception) {
    log.debug("handleConstraintViolation:: Constraint violation occurred: {}", exception.getMessage());
    var errorMessage = exception.getConstraintViolations().stream()
      .map(ConstraintViolation::getMessage)
      .collect(Collectors.joining("; "));
    log.info("handleConstraintViolation:: Error message: {}", errorMessage);
    return ResponseEntity
      .badRequest()
      .body(errorMessage);
  }
}
