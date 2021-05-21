package org.folio.ed.service;

import lombok.RequiredArgsConstructor;
import org.folio.ed.domain.dto.Configuration;
import org.folio.ed.domain.dto.RetrievalQueueRecord;
import org.folio.ed.handler.RetrievalQueueRecordHandler;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static org.folio.ed.util.ConfigurationHelpers.resolvePollingTimeFrame;

@Service
@RequiredArgsConstructor
public class CaiaSoftIntegrationService {

  private final IntegrationFlowContext integrationFlowContext;
  private final CaiaSoftSecurityManagerService sms;
  private final RemoteStorageService remoteStorageService;
  private final RetrievalQueueRecordHandler retrievalHandler;

  @Scheduled(fixedDelayString = "${configurations.update.timeframe}")
  public void updateIntegrationFlows() {
    removeExistingFlows();
    for (String tenantId : sms.getCaiaSoftUserTenants()) {
      for (Configuration configuration : remoteStorageService.getCaiaSoftConfigurations(tenantId, sms.getConnectionParameters(tenantId).getOkapiToken())) {
        createFlows(configuration);
      }
    }
  }

  private void createFlows(Configuration configuration) {
    registerRetrievalsPoller(configuration);
    registerRetrievalQueueRecordFlow(configuration);
  }

  protected void removeExistingFlows() {
    integrationFlowContext.getRegistry().keySet().forEach(key -> {
      integrationFlowContext.getRegistrationById(key).stop();
      integrationFlowContext.remove(key);
    });
  }

  public IntegrationFlowContext.IntegrationFlowRegistration registerRetrievalsPoller(Configuration configuration) {
    return integrationFlowContext
      .registration(IntegrationFlows
        .from(() -> remoteStorageService.getRetrievalQueueRecords(configuration.getId(), configuration.getTenantId(),
          sms.getConnectionParameters(configuration.getTenantId()).getOkapiToken()),
          p -> p.poller(Pollers.fixedDelay(resolvePollingTimeFrame(configuration.getAccessionDelay(),
            configuration.getAccessionTimeUnit()))))
        .split()
        .channel("retrievalQueueRecordFlow")
        .get())
      .register();
  }

  public IntegrationFlowContext.IntegrationFlowRegistration registerRetrievalQueueRecordFlow(Configuration configuration) {
    return integrationFlowContext
      .registration(IntegrationFlows
        .from("retrievalQueueRecordFlow")
        .handle(RetrievalQueueRecord.class,(p, h) -> retrievalHandler.handle(p, configuration))
        .get())
      .register();
  }
}
