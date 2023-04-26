package org.folio.ed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.ed.client.RemoteStorageClient;
import org.folio.ed.domain.dto.AccessionItem;

import org.folio.ed.domain.dto.AccessionRequest;
import org.folio.ed.domain.dto.CheckInItem;
import org.folio.ed.domain.dto.ReturnItemResponse;
import org.folio.ed.domain.dto.CheckInRequest;
import org.folio.ed.domain.dto.Configuration;
import org.folio.ed.domain.dto.RetrievalQueueRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class RemoteStorageService {

  private static final String CAIA_SOFT_NAME = "CAIA_SOFT";

  private final RemoteStorageClient remoteStorageClient;

  public AccessionItem getAccessionItem(String itemBarcode, String remoteStorageConfigurationId, String xOkapiTenant, String xOkapiToken) {
    log.debug("getAccessionItem:: Getting accession item with barcode: {}", itemBarcode);
    return remoteStorageClient.getAccessionItem(new AccessionRequest(remoteStorageConfigurationId, itemBarcode), xOkapiTenant, xOkapiToken);
  }

  public ReturnItemResponse returnItemByBarcode(String itemBarcode, String remoteStorageConfigurationId, String xOkapiTenant, String xOkapiToken) {
    log.debug("returnItemByBarcode:: Returning item with barcode: {}", itemBarcode);
    return remoteStorageClient.returnItemById(remoteStorageConfigurationId, new CheckInItem().itemBarcode(itemBarcode), xOkapiTenant, xOkapiToken);
  }

  public ResponseEntity<String> checkInByHoldId(String requestId, String remoteStorageConfigurationId, String xOkapiTenant, String xOkapiToken) {
    log.debug("checkInByHoldId:: Checking in item with requestId: {}", requestId);
    return remoteStorageClient.checkInByHoldId(remoteStorageConfigurationId, new CheckInRequest(UUID.fromString(requestId)), xOkapiTenant, xOkapiToken);
  }

  public List<Configuration> getCaiaSoftConfigurations(String tenantId, String okapiToken) {
    log.debug("getCaiaSoftConfigurations:: Getting CaiaSoft configurations for tenantId: {}", tenantId);
    var configurations = new ArrayList<Configuration>();
    remoteStorageClient.getStorageConfigurations(tenantId, okapiToken)
      .getResult()
      .forEach(configuration -> {
        configuration.setTenantId(tenantId);
        if (CAIA_SOFT_NAME.equals(configuration.getProviderName())) {
          configurations.add(configuration);
        }
      });
    return configurations;
  }

  public List<RetrievalQueueRecord> getRetrievalQueueRecords(String storageId, String tenantId, String okapiToken) {
    log.debug("getRetrievalQueueRecords:: Getting retrieval queue records for storageId: {}", storageId);
    return remoteStorageClient.getRetrievalsByQuery(storageId, false, Integer.MAX_VALUE, tenantId, okapiToken).getResult();
  }

  public ResponseEntity<String> setRetrieved(String itemBarcode, String tenantId, String okapiToken) {
    log.debug("setRetrieved:: Setting item with barcode: {} as retrieved", itemBarcode);
    return remoteStorageClient.setRetrievalByBarcode(itemBarcode, tenantId, okapiToken);
  }
}
