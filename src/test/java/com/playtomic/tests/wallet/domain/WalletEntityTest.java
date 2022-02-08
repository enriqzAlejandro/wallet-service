package com.playtomic.tests.wallet.domain;

import java.time.Instant;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.javamoney.moneta.Money;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

class WalletEntityTest {

  @Test
  @DisplayName("null wallet entity balance should throw an exception")
  void nullBalanceShouldThrowAnException() {
    assertThatThrownBy(() -> new WalletEntity.WalletBuilder(null).build())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("WalletBalance can't be null");
  }

  @Test
  @DisplayName("wallet entity with mandatory data created successfully")
  void walletEntityWithMandatoryDataCreatedSuccessfully() {
    final var wallet = new WalletEntity.WalletBuilder(Money.of(1, "EUR")).build();

    assertAll(
        () -> {
          assertThat(wallet).isNotNull();
          assertThat(wallet.getId()).isNotNull();
          assertThat(wallet.getCreatedAt()).isNotNull();
          assertThat(wallet.getUpdatedAt()).isNotNull();
        });
  }

  @Test
  @DisplayName("wallet entity with all data created successfully")
  void walletEntityWithAllDataCreatedSuccessfully() {
    final var id = UUID.randomUUID();
    final var balance = Money.of(1, "EUR");
    final var createdAt = Instant.now();
    final var updatedAt = Instant.now();

    final var wallet =
        new WalletEntity.WalletBuilder(balance)
            .withId(id)
            .withCreatedAt(createdAt)
            .withUpdatedAt(updatedAt)
            .build();

    assertAll(
        () -> {
          assertThat(wallet).isNotNull();
          assertThat(wallet.getId()).isEqualTo(id);
          assertThat(wallet.getBalance()).isEqualTo(balance);
          assertThat(wallet.getCreatedAt()).isEqualTo(createdAt);
          assertThat(wallet.getUpdatedAt()).isEqualTo(updatedAt);
        });
  }

  @Test
  @DisplayName("add amount successfully")
  void addAmountSuccessfully() {
    final var wallet = new WalletEntity.WalletBuilder(Money.of(1, "EUR")).build();

    wallet.addAmount(Money.of(10, "EUR"));

    assertThat(wallet.getBalance()).isEqualTo(Money.of(11, "EUR"));
  }

  @Test
  @DisplayName("add transaction successfully")
  void addTransactionSuccessfully() {
    final var wallet = new WalletEntity.WalletBuilder(Money.of(1, "EUR")).build();

    final var transaction = mock(Transaction.class);
    wallet.addTransaction(transaction);

    assertThat(wallet.getTransactions().get(0)).isEqualTo(transaction);
  }
}
