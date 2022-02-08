package com.playtomic.tests.wallet.application;

public class PreconditionRequiredException extends RuntimeException {
  public PreconditionRequiredException(String message) {
    super(message);
  }
}
