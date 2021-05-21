package org.folio.ed.service;

import lombok.RequiredArgsConstructor;
import org.folio.ed.client.RemoteStorageClient;
import org.folio.ed.domain.dto.AccessionItem;

import org.folio.ed.domain.dto.AccessionRequest;
import org.folio.ed.domain.dto.CheckInItem;
import org.folio.ed.domain.dto.ReturnItemResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoteStorageService {

  private final RemoteStorageClient remoteStorageClient;

  public AccessionItem getAccessionItem(String itemBarcode, String remoteStorageConfigurationId, String xOkapiTenant, String xOkapiToken) {
    return remoteStorageClient.getAccessionItem(new AccessionRequest(remoteStorageConfigurationId, itemBarcode), xOkapiTenant, xOkapiToken);
  }

  public ReturnItemResponse returnItemByBarcode(String itemBarcode, String remoteStorageConfigurationId, String xOkapiTenant, String xOkapiToken) {
    return remoteStorageClient.returnItemById(remoteStorageConfigurationId, new CheckInItem().itemBarcode(itemBarcode), xOkapiTenant, xOkapiToken);
  }
}
