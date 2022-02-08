package com.playtomic.tests.wallet.framework.rest;

import java.util.List;

public class WalletDto {

  public String walletId;

  public String createdAt;

  public String updatedAt;

  public MoneyDto balance;

  public List<TransactionDto> transactions;
}
