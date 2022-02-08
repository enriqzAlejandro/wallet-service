package com.playtomic.tests.wallet.framework.infrastructure.mapper;

import com.playtomic.tests.wallet.framework.infrastructure.dto.WalletDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import org.javamoney.moneta.Money;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class WalletRowMapper implements RowMapper<WalletDto> {

  @Override
  public WalletDto mapRow(ResultSet resultSet, int i) throws SQLException {
    Objects.requireNonNull(resultSet, "resultSet can't be null");

    final var walletDto = new WalletDto();

    walletDto.walletId = UUID.fromString(resultSet.getString("WALLET_ID"));
    walletDto.createdAt = resultSet.getTimestamp("CREATED_AT").toInstant();
    walletDto.updatedAt = resultSet.getTimestamp("UPDATED_AT").toInstant();
    walletDto.balance =
        Money.of(resultSet.getBigDecimal("BALANCE_VALUE"), resultSet.getString("BALANCE_CURRENCY"));

    return walletDto;
  }
}
