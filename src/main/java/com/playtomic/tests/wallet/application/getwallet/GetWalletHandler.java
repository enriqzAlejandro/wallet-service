package com.playtomic.tests.wallet.application.getwallet;

import com.playtomic.tests.wallet.application.WalletRepository;
import com.playtomic.tests.wallet.application.cqrs.QueryHandler;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetWalletHandler implements QueryHandler<Optional<Wallet>, GetWalletQuery> {

  private final WalletRepository repository;

  @Autowired
  public GetWalletHandler(final WalletRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<Wallet> handle(GetWalletQuery query) {
    Objects.requireNonNull(query, "Query can't be null");

    return repository.findById(query.getWalletId());
  }
}
