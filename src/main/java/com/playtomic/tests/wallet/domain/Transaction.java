package com.playtomic.tests.wallet.domain;

import java.time.Instant;
import java.util.UUID;
import org.javamoney.moneta.Money;

public interface Transaction {

  /**
   * It returns unique transaction identifier
   *
   * @return unique transaction identifier
   */
  UUID getId();

  /**
   * It returns {@link Transaction} created date.
   *
   * @return created date instant
   */
  Instant getCreatedAt();

  /**
   * It returns {@link Transaction} last updated date.
   *
   * @return updated date instant
   */
  Instant getUpdatedAt();

  /**
   * It returns instant when transaction were completed.
   *
   * @return an instant
   */
  Instant getCompletedOn();

  /**
   * It returns kind of transaction operation
   *
   * @return kind of operation
   */
  Operation getOperation();

  /**
   * It returns amount of the specific transaction
   *
   * @return money amount
   */
  Money getAmount();
}
