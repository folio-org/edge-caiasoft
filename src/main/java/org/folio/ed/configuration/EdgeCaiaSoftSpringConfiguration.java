package org.folio.ed.configuration;

import org.folio.spring.liquibase.FolioSpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;

@Configuration
public class EdgeCaiaSoftSpringConfiguration {

  @Bean
  public FolioSpringLiquibase folioSpringLiquibase() {
    return new FolioSpringLiquibase();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ExecutorChannel retrievalQueueRecordFlow() {
    return new ExecutorChannel(Executors.newCachedThreadPool());
  }
}
