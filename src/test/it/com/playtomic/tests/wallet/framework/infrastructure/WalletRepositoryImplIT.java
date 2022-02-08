package com.playtomic.tests.wallet.framework.infrastructure;

import com.playtomic.tests.wallet.application.WalletRepository;
import com.playtomic.tests.wallet.domain.Operation;
import com.playtomic.tests.wallet.domain.Transaction;
import com.playtomic.tests.wallet.domain.TransactionEntity;
import com.playtomic.tests.wallet.domain.WalletEntity;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import org.javamoney.moneta.Money;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WalletRepositoryImplIT extends IntegrationTestBase {

  @Autowired private WalletRepository repository;

  @Test
  @DisplayName("update wallet adding transaction successfully")
  void updateWalletAddingTransactionSuccessfully() {
    final var response =
        repository.findById(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));

    assertThat(response).isNotEmpty();
    final var wallet = response.get();

    final var walletEntity =
        new WalletEntity.WalletBuilder(wallet.getBalance())
            .withId(wallet.getId())
            .withCreatedAt(wallet.getCreatedAt())
            .withUpdatedAt(wallet.getUpdatedAt())
            .withTransactions(wallet.getTransactions())
            .build();
    final var transaction =
        new TransactionEntity.TransactionBuilder(
                Operation.RECHARGE, Instant.now(), Money.of(200, "EUR"))
            .build();
    walletEntity.addTransaction(transaction);

    repository.update(walletEntity, wallet.getUpdatedAt());

    final var walletUpdated = repository.findById(response.get().getId());

    assertAll(
        () -> {
          assertThat(walletUpdated).isNotEmpty();
          assertThat(walletUpdated.get().getId()).isEqualTo(response.get().getId());
          assertThat(transaction.getId())
              .isIn(
                  walletUpdated.get().getTransactions().stream()
                      .map(Transaction::getId)
                      .collect(Collectors.toList()));
        });
  }
}
