package com.playtomic.tests.wallet.framework.infrastructure.mapper;

import com.playtomic.tests.wallet.framework.infrastructure.dto.TransactionDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import org.javamoney.moneta.Money;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionRowMapper implements RowMapper<TransactionDto> {

  @Override
  public TransactionDto mapRow(ResultSet resultSet, int i) throws SQLException {
    Objects.requireNonNull(resultSet, "resultSet can't be null");

    final var transactionDto = new TransactionDto();

    transactionDto.id = UUID.fromString(resultSet.getString("TRANSACTION_ID"));
    transactionDto.createdAt = resultSet.getTimestamp("CREATED_AT").toInstant();
    transactionDto.updatedAt = resultSet.getTimestamp("UPDATED_AT").toInstant();
    transactionDto.operation = resultSet.getString("NAME");
    transactionDto.completedOn = resultSet.getTimestamp("COMPLETED_ON").toInstant();
    transactionDto.amount =
        Money.of(resultSet.getBigDecimal("AMOUNT_VALUE"), resultSet.getString("AMOUNT_CURRENCY"));

    return transactionDto;
  }
}
