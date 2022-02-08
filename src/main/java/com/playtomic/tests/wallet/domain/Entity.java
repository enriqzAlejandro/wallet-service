package com.playtomic.tests.wallet.domain;

import java.time.Instant;

public abstract class Entity {

  protected final Instant createdAt;
  protected Instant updatedAt;

  protected Entity(final Instant createdAt, final Instant updatedAt) {
    validateDates(createdAt, updatedAt);

    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  private void validateDates(Instant createdAt, Instant updatedAt) {
    if (createdAt.isAfter(Instant.now()) || updatedAt.isAfter(Instant.now())) {
      throw new InvalidDateException("Invalid createdAt or updatedAt date");
    }

    if (updatedAt.isBefore(createdAt)) {
      throw new InvalidDateException("updatedAt date can't be before createdAt");
    }
  }

  public static class InvalidDateException extends RuntimeException {
    private InvalidDateException(String message) {
      super(message);
    }
  }
}
