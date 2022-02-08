package com.playtomic.tests.wallet.framework.infrastructure;

import com.playtomic.tests.wallet.domain.Operation;
import com.playtomic.tests.wallet.domain.Transaction;
import com.playtomic.tests.wallet.framework.infrastructure.dto.TransactionDto;
import com.playtomic.tests.wallet.framework.infrastructure.dto.WalletDto;
import com.playtomic.tests.wallet.framework.infrastructure.mapper.TransactionRowMapper;
import com.playtomic.tests.wallet.framework.infrastructure.mapper.WalletRowMapper;
import static com.playtomic.tests.wallet.framework.infrastructure.queries.WalletQueries.SELECT_TRANSACTIONS;
import static com.playtomic.tests.wallet.framework.infrastructure.queries.WalletQueries.SELECT_WALLET;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.javamoney.moneta.Money;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
class WalletRepositoryImplTest {

  @Mock private NamedParameterJdbcTemplate template;

  @Mock private WalletRowMapper walletRowMapper;

  @Mock private TransactionRowMapper transactionRowMapper;

  @InjectMocks private WalletRepositoryImpl repository;

  @Test
  @DisplayName("null parameter calling findById should throw an exception")
  void nullParameterCallingFindByIdShouldThrowAnException() {
    assertThatThrownBy(() -> repository.findById(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("WalletId can't be null");
  }

  @Test
  @DisplayName("wallet identifier not found should return empty response")
  void walletNotFound() {
    when(template.queryForObject(
            eq(SELECT_WALLET), any(MapSqlParameterSource.class), any(WalletRowMapper.class)))
        .thenThrow(mock(EmptyResultDataAccessException.class));

    final var response = repository.findById(UUID.randomUUID());

    assertThat(response).isEmpty();
  }

  @Test
  @DisplayName("retrieve wallet successfully without transactions")
  void retrieveWalletWithoutTransactionsSuccessfully() {
    final var walletDto = createWalletDto();

    when(template.queryForObject(
            eq(SELECT_WALLET), any(MapSqlParameterSource.class), any(WalletRowMapper.class)))
        .thenReturn(walletDto);

    final var response = repository.findById(UUID.randomUUID());

    assertAll(
        () -> {
          assertThat(response).isNotEmpty();
          assertThat(response.get().getId()).isEqualTo(walletDto.walletId);
          assertThat(response.get().getCreatedAt()).isEqualTo(walletDto.createdAt);
          assertThat(response.get().getUpdatedAt()).isEqualTo(walletDto.updatedAt);
          assertThat(response.get().getBalance()).isEqualTo(walletDto.balance);
        });
  }

  @Test
  @DisplayName("retrieve wallet successfully")
  void retrieveWalletSuccessfully() {
    final var walletDto = createWalletDto();
    final var transactionDto = createTransactionDto();

    when(template.queryForObject(
            eq(SELECT_WALLET), any(MapSqlParameterSource.class), any(WalletRowMapper.class)))
        .thenReturn(walletDto);
    when(template.query(
            eq(SELECT_TRANSACTIONS),
            any(MapSqlParameterSource.class),
            any(TransactionRowMapper.class)))
        .thenReturn(List.of(transactionDto));
    final var response = repository.findById(UUID.randomUUID());

    assertAll(
        () -> {
          assertThat(response).isNotEmpty();
          assertThat(response.get().getId()).isEqualTo(walletDto.walletId);
          assertThat(response.get().getCreatedAt()).isEqualTo(walletDto.createdAt);
          assertThat(response.get().getUpdatedAt()).isEqualTo(walletDto.updatedAt);
          assertThat(response.get().getBalance()).isEqualTo(walletDto.balance);
          assertTransaction(response.get().getTransactions().get(0), transactionDto);
        });
  }

  private void assertTransaction(final Transaction current, final TransactionDto expected) {
    assertThat(current.getId()).isEqualTo(expected.id);
    assertThat(current.getCreatedAt()).isEqualTo(expected.createdAt);
    assertThat(current.getUpdatedAt()).isEqualTo(expected.updatedAt);
    assertThat(current.getCompletedOn()).isEqualTo(expected.completedOn);
    assertThat(current.getOperation().name()).isEqualTo(expected.operation);
    assertThat(current.getAmount()).isEqualTo(expected.amount);
  }

  private WalletDto createWalletDto() {
    final var walletDto = new WalletDto();

    walletDto.walletId = UUID.randomUUID();
    walletDto.createdAt = Instant.now();
    walletDto.updatedAt = Instant.now();
    walletDto.balance = Money.of(10, "EUR");

    return walletDto;
  }

  private TransactionDto createTransactionDto() {
    final var transactionDto = new TransactionDto();

    transactionDto.id = UUID.randomUUID();
    transactionDto.createdAt = Instant.now();
    transactionDto.updatedAt = Instant.now();
    transactionDto.completedOn = Instant.now();
    transactionDto.operation = Operation.RECHARGE.name();
    transactionDto.amount = Money.of(100, "EUR");

    return transactionDto;
  }
}
