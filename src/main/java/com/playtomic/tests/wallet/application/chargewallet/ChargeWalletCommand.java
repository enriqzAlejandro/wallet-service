package com.playtomic.tests.wallet.application.chargewallet;

import com.playtomic.tests.wallet.application.cqrs.Command;
import java.time.Instant;
import java.util.UUID;
import org.javamoney.moneta.Money;

public class ChargeWalletCommand implements Command<Void> {

  private final UUID walletId;

  private final Instant version;

  private final String accountNumber;

  private final Money amount;

  private ChargeWalletCommand(
      final UUID walletId, final Instant version, final String accountNumber, final Money amount) {
    this.walletId = walletId;
    this.version = version;
    this.accountNumber = accountNumber;
    this.amount = amount;
  }

  public static ChargeWalletCommand of(
      final UUID walletId, final Instant version, final String accountNumber, final Money amount) {
    return new ChargeWalletCommand(walletId, version, accountNumber, amount);
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public Money getAmount() {
    return amount;
  }

  public UUID getWalletId() {
    return walletId;
  }

  public Instant getVersion() {
    return version;
  }
}
