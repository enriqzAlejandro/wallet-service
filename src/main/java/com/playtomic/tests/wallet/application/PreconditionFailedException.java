package com.playtomic.tests.wallet.application;

public class PreconditionFailedException extends RuntimeException {
  public PreconditionFailedException(String message) {
    super(message);
  }
}
