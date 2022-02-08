package com.playtomic.tests.wallet.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.javamoney.moneta.Money;

public class TransactionEntity extends Entity implements Transaction {
  private final UUID id;
  private final Instant completedOn;
  private final Operation operation;
  private final Money amount;

  private TransactionEntity(final TransactionBuilder builder) {
    super(
        Optional.ofNullable(builder.createdAt).orElse(Instant.now()),
        Optional.ofNullable(builder.updatedAt).orElse(Instant.now()));
    this.id = Optional.ofNullable(builder.id).orElse(UUID.randomUUID());

    this.completedOn = Objects.requireNonNull(builder.completedOn, "completedOn can't be null");
    this.operation = Objects.requireNonNull(builder.operation, "operation can't be null");
    this.amount = Objects.requireNonNull(builder.amount, "amount can't be null");
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public Instant getCompletedOn() {
    return completedOn;
  }

  @Override
  public Operation getOperation() {
    return operation;
  }

  @Override
  public Money getAmount() {
    return amount;
  }

  public static class TransactionBuilder {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private final Operation operation;
    private final Instant completedOn;
    private final Money amount;

    public TransactionBuilder(
        final Operation operation, final Instant completedOn, final Money amount) {
      this.operation = operation;
      this.completedOn = completedOn;
      this.amount = amount;
    }

    public TransactionBuilder withId(final UUID id) {
      this.id = id;
      return this;
    }

    public TransactionBuilder withCreatedAt(final Instant createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public TransactionBuilder withUpdatedAt(final Instant updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public TransactionEntity build() {
      return new TransactionEntity(this);
    }
  }
}
