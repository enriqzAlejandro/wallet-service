package com.playtomic.tests.wallet.application.getwallet;

import com.playtomic.tests.wallet.application.cqrs.Query;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import java.util.UUID;

public class GetWalletQuery implements Query<Optional<Wallet>> {

  private final UUID walletId;

  private GetWalletQuery(final UUID walletId) {
    this.walletId = walletId;
  }

  /**
   * It creates an instance of {@link GetWalletQuery}.
   *
   * @param walletId wallet unique identifier
   * @return an instance of GetWalletQuery
   */
  public static GetWalletQuery of(final UUID walletId) {
    return new GetWalletQuery(walletId);
  }

  public UUID getWalletId() {
    return walletId;
  }
}
