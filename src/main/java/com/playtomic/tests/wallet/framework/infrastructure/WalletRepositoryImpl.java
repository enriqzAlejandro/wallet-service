package com.playtomic.tests.wallet.framework.infrastructure;

import com.playtomic.tests.wallet.application.PreconditionFailedException;
import com.playtomic.tests.wallet.application.WalletRepository;
import com.playtomic.tests.wallet.domain.Operation;
import com.playtomic.tests.wallet.domain.Transaction;
import com.playtomic.tests.wallet.domain.TransactionEntity;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.framework.infrastructure.mapper.TransactionRowMapper;
import com.playtomic.tests.wallet.framework.infrastructure.mapper.WalletRowMapper;
import static com.playtomic.tests.wallet.framework.infrastructure.queries.WalletQueries.ADD_TRANSACTION;
import static com.playtomic.tests.wallet.framework.infrastructure.queries.WalletQueries.DELETE_TRANSACTIONS;
import static com.playtomic.tests.wallet.framework.infrastructure.queries.WalletQueries.SELECT_OPERATION_ID;
import static com.playtomic.tests.wallet.framework.infrastructure.queries.WalletQueries.SELECT_TRANSACTIONS;
import static com.playtomic.tests.wallet.framework.infrastructure.queries.WalletQueries.SELECT_WALLET;
import static com.playtomic.tests.wallet.framework.infrastructure.queries.WalletQueries.UPDATE_WALLET;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletRepositoryImpl implements WalletRepository {

  private final NamedParameterJdbcTemplate template;

  private final WalletRowMapper walletRowMapper;

  private final TransactionRowMapper transactionRowMapper;

  @Autowired
  public WalletRepositoryImpl(
      final NamedParameterJdbcTemplate template,
      final WalletRowMapper walletRowMapper,
      final TransactionRowMapper transactionRowMapper) {
    this.template = template;
    this.walletRowMapper = walletRowMapper;
    this.transactionRowMapper = transactionRowMapper;
  }

  @Override
  public Optional<Wallet> findById(UUID walletId) {
    Objects.requireNonNull(walletId, "WalletId can't be null");

    try {
      final var parameters = new MapSqlParameterSource().addValue("WALLET_ID", walletId.toString());

      final var walletDto = template.queryForObject(SELECT_WALLET, parameters, walletRowMapper);
      final var transactions = getTransactions(walletId);

      return Optional.of(
          WalletImpl.ofWalletWithTransactions(
              walletDto.createdAt,
              walletDto.updatedAt,
              walletDto.walletId,
              walletDto.balance,
              transactions));
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      return Optional.empty();
    } catch (DataAccessException dataAccessException) {
      throw new WalletRepositoryException("Internal Server Error");
    }
  }

  @Transactional
  @Override
  public void update(final Wallet wallet, final Instant oldUpdatedAt) {
    Objects.requireNonNull(wallet, "Wallet can't be null");
    Objects.requireNonNull(oldUpdatedAt, "oldUpdatedAt can't be null");

    try {
      final var parameters =
          new MapSqlParameterSource()
              .addValue("WALLET_ID", wallet.getId().toString())
              .addValue("BALANCE_VALUE", wallet.getBalance().getNumberStripped())
              .addValue("OLD_UPDATED_AT", Timestamp.from(oldUpdatedAt))
              .addValue("UPDATED_AT", Timestamp.from(wallet.getUpdatedAt()));

      checkWriteOperation(template.update(UPDATE_WALLET, parameters));
      updateTransactions(wallet);
    } catch (DataAccessException dataAccessException) {
      throw new WalletRepositoryException("Internal server error");
    }
  }

  private void checkWriteOperation(final int affectedRows) {
    if (affectedRows != 1) {
      throw new PreconditionFailedException("Error updating wallet.");
    }
  }

  private void updateTransactions(final Wallet wallet) {
    final var parameters =
        new MapSqlParameterSource().addValue("WALLET_ID", wallet.getId().toString());

    template.update(DELETE_TRANSACTIONS, parameters);

    wallet
        .getTransactions()
        .forEach(transaction -> addTransaction(wallet.getId().toString(), transaction));
  }

  private void addTransaction(final String walletId, final Transaction transaction) {
    final var operationId = getOperationId(transaction.getOperation().name());

    final var parameters =
        new MapSqlParameterSource()
            .addValue("TRANSACTION_ID", transaction.getId().toString())
            .addValue("CREATED_AT", transaction.getCreatedAt().toString())
            .addValue("UPDATED_AT", transaction.getUpdatedAt().toString())
            .addValue("WALLET_ID", walletId)
            .addValue("OPERATION_ID", operationId)
            .addValue("COMPLETED_ON", transaction.getCompletedOn().toString())
            .addValue("AMOUNT_VALUE", transaction.getAmount().getNumberStripped())
            .addValue("AMOUNT_CURRENCY", transaction.getAmount().getCurrency().getCurrencyCode());

    template.update(ADD_TRANSACTION, parameters);
  }

  private Long getOperationId(final String name) {
    final var parameters = new MapSqlParameterSource().addValue("NAME", name);

    return template.queryForObject(SELECT_OPERATION_ID, parameters, Long.class);
  }

  private List<Transaction> getTransactions(final UUID walletId) {
    final var parameters = new MapSqlParameterSource().addValue("WALLET_ID", walletId.toString());

    final var transactions = template.query(SELECT_TRANSACTIONS, parameters, transactionRowMapper);

    return transactions.stream()
        .map(
            transaction ->
                new TransactionEntity.TransactionBuilder(
                        Operation.valueOf(transaction.operation),
                        transaction.completedOn,
                        transaction.amount)
                    .withId(transaction.id)
                    .withCreatedAt(transaction.createdAt)
                    .withUpdatedAt(transaction.updatedAt)
                    .build())
        .collect(Collectors.toList());
  }

  public static class WalletRepositoryException extends RuntimeException {
    public WalletRepositoryException(String message) {
      super(message);
    }
  }
}
