package com.playtomic.tests.wallet.application.getwallet;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetWalletQueryTest {

  @Test
  @DisplayName("create query successfully")
  void createQuerySuccessfully() {
    final var walletId = UUID.randomUUID();
    final var query = GetWalletQuery.of(walletId);

    assertThat(query).isNotNull();
    assertThat(query.getWalletId()).isEqualTo(walletId);
  }
}
