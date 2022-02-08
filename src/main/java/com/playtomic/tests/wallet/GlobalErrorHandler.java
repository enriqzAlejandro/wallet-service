package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.application.PreconditionFailedException;
import com.playtomic.tests.wallet.application.PreconditionRequiredException;
import com.playtomic.tests.wallet.application.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalErrorHandler {

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleValidationException(Exception exception) {
    return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PreconditionFailedException.class)
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  public ResponseEntity<ErrorResponse> handlePreconditionFailedException(Exception exception) {
    return buildErrorResponse(exception, HttpStatus.PRECONDITION_FAILED);
  }

  @ExceptionHandler(PreconditionRequiredException.class)
  @ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
  public ResponseEntity<ErrorResponse> handlePreconditionRequiredException(Exception exception) {
    return buildErrorResponse(exception, HttpStatus.PRECONDITION_REQUIRED);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception) {
    return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(
      Exception exception, HttpStatus httpStatus) {
    ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), exception.getMessage());

    return ResponseEntity.status(httpStatus).body(errorResponse);
  }
}
