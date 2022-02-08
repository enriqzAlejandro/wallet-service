package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.application.cqrs.Bus;
import com.playtomic.tests.wallet.application.cqrs.BusImpl;
import com.playtomic.tests.wallet.application.cqrs.Registry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public Registry registry(ApplicationContext applicationContext) {
    return new Registry(applicationContext);
  }

  @Bean
  public Bus commandBus(Registry registry) {
    return new BusImpl(registry);
  }
}
