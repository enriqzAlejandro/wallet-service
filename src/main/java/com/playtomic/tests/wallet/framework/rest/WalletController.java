package com.playtomic.tests.wallet.framework.rest;

import com.playtomic.tests.wallet.application.PreconditionRequiredException;
import com.playtomic.tests.wallet.application.chargewallet.ChargeWalletCommand;
import com.playtomic.tests.wallet.application.cqrs.Bus;
import com.playtomic.tests.wallet.application.getwallet.GetWalletQuery;
import com.playtomic.tests.wallet.domain.Transaction;
import com.playtomic.tests.wallet.domain.Wallet;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

  private final Bus bus;

  @Autowired
  public WalletController(final Bus bus) {
    this.bus = bus;
  }

  @GetMapping(value = "/wallets/{walletId}")
  public WalletDto getWallet(@PathVariable("walletId") final String walletId) {
    final var query = GetWalletQuery.of(UUID.fromString(walletId));

    return bus.executeQuery(query).map(this::toWalletDto).orElse(null);
  }

  @PostMapping(value = "/wallets/{walletId}/recharge")
  public void rechargeWallet(
      @PathVariable("walletId") final String walletId,
      @RequestBody final RechargeDto rechargeDto,
      @RequestHeader("If-Match") final String version) {
    checkIfMatchHeader(version);

    final var command =
        ChargeWalletCommand.of(
            UUID.fromString(walletId),
            Instant.parse(version),
            rechargeDto.creditCardNumber,
            toMoney(rechargeDto.amount));

    bus.executeCommand(command);
  }

  private void checkIfMatchHeader(final String version) {
    if (Objects.isNull(version)) throw new PreconditionRequiredException("Version needed");
  }

  private WalletDto toWalletDto(final Wallet wallet) {
    final var walletDto = new WalletDto();

    walletDto.walletId = wallet.getId().toString();
    walletDto.createdAt = wallet.getCreatedAt().toString();
    walletDto.updatedAt = wallet.getUpdatedAt().toString();
    walletDto.balance = toMoneyDto(wallet.getBalance());
    walletDto.transactions =
        wallet.getTransactions().stream().map(this::toTransactionDto).collect(Collectors.toList());

    return walletDto;
  }

  private Money toMoney(final MoneyDto moneyDto) {
    return Money.of(moneyDto.value, moneyDto.currency);
  }

  private MoneyDto toMoneyDto(final Money money) {
    final var moneyDto = new MoneyDto();

    moneyDto.value = money.getNumberStripped().doubleValue();
    moneyDto.currency = money.getCurrency().getCurrencyCode();

    return moneyDto;
  }

  private TransactionDto toTransactionDto(final Transaction transaction) {
    final var transactionDto = new TransactionDto();

    transactionDto.id = transaction.getId().toString();
    transactionDto.createdAt = transaction.getCreatedAt().toString();
    transactionDto.updatedAt = transaction.getUpdatedAt().toString();
    transactionDto.completedOn = transaction.getCompletedOn().toString();
    transactionDto.operation = transaction.getOperation().name();
    transactionDto.amount = toMoneyDto(transaction.getAmount());

    return transactionDto;
  }
}
