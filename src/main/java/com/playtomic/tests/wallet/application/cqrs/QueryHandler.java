package com.playtomic.tests.wallet.application.cqrs;

public interface QueryHandler<R, C extends Query<R>> {
  R handle(C query);
}
