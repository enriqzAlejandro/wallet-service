package com.playtomic.tests.wallet.framework.infrastructure;

import com.playtomic.tests.wallet.domain.Transaction;
import com.playtomic.tests.wallet.domain.TransactionEntity;
import com.playtomic.tests.wallet.domain.Wallet;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.javamoney.moneta.Money;

public final class WalletImpl implements Wallet {

  private final Instant createdAt;

  private final Instant updatedAt;

  private final UUID id;

  private final Money balance;

  private final List<Transaction> transactions;

  private WalletImpl(
      final Instant createdAt,
      final Instant updatedAt,
      final UUID id,
      final Money balance,
      final List<Transaction> transactions) {
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.id = id;
    this.balance = balance;
    this.transactions = transactions;
  }

  public static WalletImpl of(
      final Instant createdAt, final Instant updatedAt, final UUID id, final Money balance) {
    return new WalletImpl(createdAt, updatedAt, id, balance, Collections.emptyList());
  }

  public static WalletImpl ofWalletWithTransactions(
      final Instant createdAt,
      final Instant updatedAt,
      final UUID id,
      final Money balance,
      final List<Transaction> transactions) {
    return new WalletImpl(createdAt, updatedAt, id, balance, transactions);
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
  public Money getBalance() {
    return balance;
  }

  @Override
  public List<Transaction> getTransactions() {
    return transactions;
  }
}
