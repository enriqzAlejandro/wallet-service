package com.playtomic.tests.wallet.application.cqrs;

public interface CommandHandler<R, C extends Command<R>> {
  R handle(C command);
}
