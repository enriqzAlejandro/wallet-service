package com.playtomic.tests.wallet.application.chargewallet;

import com.playtomic.tests.wallet.application.PreconditionFailedException;
import com.playtomic.tests.wallet.application.ValidationException;
import com.playtomic.tests.wallet.application.WalletRepository;
import com.playtomic.tests.wallet.application.cqrs.CommandHandler;
import com.playtomic.tests.wallet.domain.Operation;
import com.playtomic.tests.wallet.domain.TransactionEntity;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletEntity;
import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import java.time.Instant;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargeWalletHandler implements CommandHandler<Void, ChargeWalletCommand> {

  private final StripeService stripeService;

  private final WalletRepository repository;

  @Autowired
  public ChargeWalletHandler(final StripeService stripeService, final WalletRepository repository) {
    this.stripeService = stripeService;
    this.repository = repository;
  }

  @Override
  public Void handle(final ChargeWalletCommand command) {
    Objects.requireNonNull(command, "Command can't be null");

    try {
      final var wallet = repository.findById(command.getWalletId());

      wallet.ifPresent(
          walletData -> {
            checkRaceCondition(walletData, command.getVersion());

            stripeService.charge(
                command.getAccountNumber(), command.getAmount().getNumberStripped());

            final var walletEntity =
                new WalletEntity.WalletBuilder(walletData.getBalance())
                    .withId(walletData.getId())
                    .withCreatedAt(walletData.getCreatedAt())
                    .withUpdatedAt(walletData.getUpdatedAt())
                    .withTransactions(walletData.getTransactions())
                    .build();

            walletEntity.addTransaction(
                new TransactionEntity.TransactionBuilder(
                        Operation.RECHARGE, Instant.now(), command.getAmount())
                    .build());
            walletEntity.addAmount(command.getAmount());

            repository.update(walletEntity, walletData.getUpdatedAt());
          });

    } catch (StripeAmountTooSmallException stripeAmountTooSmallException) {
      throw new AmountTooSmallException(
          "Amount lower than allowed. Please, charge with an amount bigger than 16 Euros");
    } catch (StripeServiceException stripeServiceException) {
      throw new UnexpectedStripeServiceException("Unexpected error occurred. Please, try again");
    }

    return null;
  }

  private void checkRaceCondition(final Wallet wallet, final Instant version) {
    if (version.isBefore(wallet.getUpdatedAt()) || version.isAfter(wallet.getUpdatedAt())) {
      throw new PreconditionFailedException("Error updating wallet.");
    }
  }

  public static class AmountTooSmallException extends ValidationException {
    public AmountTooSmallException(String message) {
      super(message);
    }
  }

  public static class UnexpectedStripeServiceException extends RuntimeException {
    public UnexpectedStripeServiceException(String message) {
      super(message);
    }
  }
}
