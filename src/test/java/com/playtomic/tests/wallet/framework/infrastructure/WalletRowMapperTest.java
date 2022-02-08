package com.playtomic.tests.wallet.framework.infrastructure;

import com.playtomic.tests.wallet.framework.infrastructure.mapper.WalletRowMapper;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WalletRowMapperTest {

  private WalletRowMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new WalletRowMapper();
  }

  @Test
  @DisplayName("null parameter should throws an exception")
  void nullParameterShouldThrowAnException() {
    assertThatThrownBy(() -> mapper.mapRow(null, 0))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("resultSet can't be null");
  }

  @Test
  @DisplayName("map is done successfully")
  void mapIsDoneSuccessfully() throws SQLException {
    final var resultSet = createResultSet();

    final var walletDto = mapper.mapRow(resultSet, 0);

    assertAll(
        () -> {
          assertThat(walletDto).isNotNull();
          assertThat(walletDto.walletId.toString()).isEqualTo(resultSet.getString("WALLET_ID"));
          assertEquals(
              0,
              walletDto
                  .balance
                  .getNumberStripped()
                  .compareTo(resultSet.getBigDecimal("BALANCE_VALUE")));
          assertThat(walletDto.balance.getCurrency().getCurrencyCode())
              .isEqualTo(resultSet.getString("BALANCE_CURRENCY"));
        });
  }

  private ResultSet createResultSet() throws SQLException {
    final var resultSet = mock(ResultSet.class);

    when(resultSet.getString("WALLET_ID")).thenReturn(UUID.randomUUID().toString());
    when(resultSet.getBigDecimal("BALANCE_VALUE")).thenReturn(BigDecimal.TEN);
    when(resultSet.getString("BALANCE_CURRENCY")).thenReturn("EUR");
    when(resultSet.getTimestamp("CREATED_AT")).thenReturn(Timestamp.from(Instant.now()));
    when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(Timestamp.from(Instant.now()));

    return resultSet;
  }
}
