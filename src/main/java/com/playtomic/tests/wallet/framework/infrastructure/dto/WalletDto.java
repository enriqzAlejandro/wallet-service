package com.playtomic.tests.wallet.framework.infrastructure.dto;

import java.time.Instant;
import java.util.UUID;
import org.javamoney.moneta.Money;

public class WalletDto {

  public UUID walletId;
  public Instant createdAt;
  public Instant updatedAt;
  public Money balance;
}
