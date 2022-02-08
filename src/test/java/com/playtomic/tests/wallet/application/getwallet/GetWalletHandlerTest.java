package com.playtomic.tests.wallet.application.getwallet;

import com.playtomic.tests.wallet.application.WalletRepository;
import com.playtomic.tests.wallet.application.getwallet.GetWalletHandler;
import com.playtomic.tests.wallet.application.getwallet.GetWalletQuery;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetWalletHandlerTest {

  @Mock private WalletRepository repository;

  @InjectMocks private GetWalletHandler handler;

  @Captor
  private ArgumentCaptor<UUID> uuidCaptor;

  @Test
  @DisplayName("null parameter should throw an exception")
  void nullParameterShouldThrowAnException() {
    assertThatThrownBy(() -> handler.handle(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Query can't be null");
  }

  @Test
  @DisplayName("if query not found then empty response")
  void emptyResponseWhenNotFound() {
    when(repository.findById(any())).thenReturn(Optional.empty());

    final var response = handler.handle(mock(GetWalletQuery.class));

    assertThat(response).isEmpty();
  }

  @Test
  @DisplayName("handler returns wallet successfully")
  void walletIsReturnedSuccessfully() {
    final var id = UUID.randomUUID();
    final var query = GetWalletQuery.of(id);
    final var wallet = mock(Wallet.class);

    when(repository.findById(any(UUID.class))).thenReturn(Optional.of(wallet));

    final var response = handler.handle(query);

    assertAll(() -> {
      assertThat(response).isNotEmpty();
      assertThat(response.get()).isEqualTo(wallet);
      verify(repository).findById(uuidCaptor.capture());
      assertThat(uuidCaptor.getValue()).isEqualTo(id);
    });
  }
}
