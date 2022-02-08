package com.playtomic.tests.wallet.framework.infrastructure.dto;

import java.time.Instant;
import java.util.UUID;
import org.javamoney.moneta.Money;

public class TransactionDto {

  public UUID id;
  public Instant createdAt;
  public Instant updatedAt;
  public Instant completedOn;
  public Money amount;
  public String operation;
}
