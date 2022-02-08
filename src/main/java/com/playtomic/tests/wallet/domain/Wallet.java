package com.playtomic.tests.wallet.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.javamoney.moneta.Money;

public interface Wallet {

  /**
   * It returns unique Wallet identifier.
   *
   * @return unique wallet identifier
   */
  UUID getId();

  /**
   * It returns {@link Wallet} created date.
   *
   * @return created date instant
   */
  Instant getCreatedAt();

  /**
   * It returns {@link Wallet} last updated date.
   *
   * @return updated date instant
   */
  Instant getUpdatedAt();

  /**
   * It returns current wallet's balance.
   *
   * @return current wallet's balance
   */
  Money getBalance();

  /**
   * It returns associated {@link TransactionEntity}.
   *
   * @return a list of transactions
   */
  List<Transaction> getTransactions();
}
