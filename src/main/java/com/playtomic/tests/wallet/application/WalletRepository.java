package com.playtomic.tests.wallet.application;

import com.playtomic.tests.wallet.domain.Wallet;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {

  /**
   * It returns the {@link Wallet} associated with the given identifier.
   *
   * @param walletId wallet unique identifier
   * @return wallet information
   */
  Optional<Wallet> findById(final UUID walletId);

  /**
   * It updated the {@link Wallet} with the information received from parameters.
   *
   * @param wallet wallet information to be updated
   * @param oldUpdatedAt entity last update date
   */
  void update(final Wallet wallet, final Instant oldUpdatedAt);
}
