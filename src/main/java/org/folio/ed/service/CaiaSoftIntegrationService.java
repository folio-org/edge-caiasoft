package org.folio.ed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.ed.domain.dto.Configuration;
import org.folio.ed.domain.dto.RetrievalQueueRecord;
import org.folio.ed.handler.RetrievalQueueRecordHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static org.folio.ed.util.ConfigurationHelpers.resolvePollingTimeFrame;

@Service
@RequiredArgsConstructor
@Log4j2
public class CaiaSoftIntegrationService {

  private final IntegrationFlowContext integrationFlowContext;
  private final CaiaSoftSecurityManagerService sms;
  private final RemoteStorageService remoteStorageService;
  private final RetrievalQueueRecordHandler retrievalHandler;

  @Scheduled(fixedDelayString = "${configurations.update.timeframe}")
  public void updateIntegrationFlows() {
    log.debug("updateIntegrationFlows:: Updating integration flows");
    removeExistingFlows();
    for (String tenantId : sms.getCaiaSoftUserTenants()) {
      for (Configuration configuration : remoteStorageService.getCaiaSoftConfigurations(tenantId, sms.getConnectionParameters(tenantId).getOkapiToken().accessToken())) {
        createFlows(configuration);
      }
    }
  }

  private void createFlows(Configuration configuration) {
    log.debug("createFlows:: Creating flows for configuration: {}", configuration.getId());
    registerRetrievalsPoller(configuration);
    registerRetrievalQueueRecordFlow(configuration);
  }

  protected void removeExistingFlows() {
    log.debug("removeExistingFlows:: Trying to remove if there are any Existing Flows");
    integrationFlowContext.getRegistry().keySet().forEach(key -> {
      integrationFlowContext.getRegistrationById(key).stop();
      integrationFlowContext.remove(key);
    });
  }

  public IntegrationFlowContext.IntegrationFlowRegistration registerRetrievalsPoller(Configuration configuration) {
    log.debug("registerRetrievalsPoller:: Registering retrievals poller for configuration: {}", configuration.getId());
    return integrationFlowContext
      .registration(IntegrationFlow
        .fromSupplier(() -> remoteStorageService.getRetrievalQueueRecords(configuration.getId(), configuration.getTenantId(),
          sms.getConnectionParameters(configuration.getTenantId()).getOkapiToken().accessToken()),
          p -> p.poller(Pollers.fixedDelay(resolvePollingTimeFrame(configuration.getAccessionDelay(),
            configuration.getAccessionTimeUnit()))))
        .split()
        .channel("retrievalQueueRecordFlow")
        .get())
      .register();
  }

  public IntegrationFlowContext.IntegrationFlowRegistration registerRetrievalQueueRecordFlow(Configuration configuration) {
    log.debug("registerRetrievalQueueRecordFlow:: Registering retrieval queue record flow for configuration: {}", configuration.getId());
    return integrationFlowContext
      .registration(IntegrationFlow
        .from("retrievalQueueRecordFlow")
        .handle(RetrievalQueueRecord.class,(p, h) -> retrievalHandler.handle(p, configuration))
        .get())
      .register();
  }
}
