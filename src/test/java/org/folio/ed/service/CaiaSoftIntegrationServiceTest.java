package org.folio.ed.service;

import org.folio.ed.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;


import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestPropertySource(properties = "app.scheduling.enable=true")
@DirtiesContext
public class CaiaSoftIntegrationServiceTest extends TestBase {

  @Autowired
  private IntegrationFlowContext integrationFlowContext;

  @Autowired
  private CaiaSoftIntegrationService integrationService;

  @Test
  void shouldGetConfigurationAndCreateIntegrationFlows() {
    await().atMost(1, TimeUnit.SECONDS).untilAsserted(() ->
      assertThat(integrationFlowContext.getRegistry().size(), is(2)));
  }

  @Test
  void shouldRemoveIntegrationFlows() {
    integrationService.removeExistingFlows();
    assertThat(integrationFlowContext.getRegistry().size(), is(0));
  }
}
