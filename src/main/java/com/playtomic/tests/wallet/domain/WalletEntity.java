package com.playtomic.tests.wallet.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.javamoney.moneta.Money;

public class WalletEntity extends Entity implements Wallet {

  private final UUID id;

  private Money balance;

  private List<Transaction> transactions;

  private WalletEntity(WalletBuilder builder) {
    super(
        Optional.ofNullable(builder.createdAt).orElse(Instant.now()),
        Optional.ofNullable(builder.updatedAt).orElse(Instant.now()));

    this.id = Optional.ofNullable(builder.id).orElse(UUID.randomUUID());
    this.balance = Objects.requireNonNull(builder.balance, "WalletBalance can't be null");
    this.transactions =
        Objects.isNull(builder.transactions) ? new ArrayList<>() : builder.transactions;
  }

  /**
   * This method add new transaction to the wallet.
   *
   * @param transaction the transaction to be added
   */
  public void addTransaction(final Transaction transaction) {
    transactions.add(transaction);
    this.updatedAt = Instant.now();
  }

  /**
   * This method add amount to wallet balance.
   *
   * @param amount amount to be added to the wallet balance
   */
  public void addAmount(final Money amount) {
    balance = balance.add(amount);
    this.updatedAt = Instant.now();
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Money getBalance() {
    return balance;
  }

  @Override
  public List<Transaction> getTransactions() {
    return transactions;
  }

  @Override
  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public static class WalletBuilder {
    private Instant createdAt;
    private Instant updatedAt;
    private UUID id;
    private final Money balance;
    private List<Transaction> transactions;

    public WalletBuilder(final Money balance) {
      this.balance = balance;
    }

    public WalletBuilder withId(final UUID id) {
      this.id = id;
      return this;
    }

    public WalletBuilder withCreatedAt(final Instant createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public WalletBuilder withUpdatedAt(final Instant updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public WalletBuilder withTransactions(final List<Transaction> transactions) {
      this.transactions = new ArrayList<>(transactions);
      return this;
    }

    public WalletEntity build() {
      return new WalletEntity(this);
    }
  }
}
