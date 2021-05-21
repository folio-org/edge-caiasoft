package org.folio.ed.service;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.folio.ed.client.RemoteStorageClient;
import org.folio.ed.domain.dto.AccessionItem;
import org.folio.ed.domain.dto.AccessionRequest;
import org.folio.ed.domain.dto.CheckInItem;
import org.folio.ed.domain.dto.ReturnItemResponse;
import org.folio.ed.domain.dto.CheckInRequest;
import org.folio.ed.domain.dto.Configuration;
import org.folio.ed.domain.dto.ResultList;
import org.folio.ed.domain.dto.RetrievalQueueRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoteStorageServiceTest {

  @Mock
  private RemoteStorageClient remoteStorageClient;

  @InjectMocks
  private RemoteStorageService remoteStorageService;

  @Test
  void testGetAccessionItem() {
    var accessionItem = new AccessionItem();
    accessionItem.setId("accession_id");

    when(remoteStorageClient.getAccessionItem(isA(AccessionRequest.class), isA(String.class), isA(String.class)))
      .thenReturn(accessionItem);

    remoteStorageService.getAccessionItem("itemBarcode", "remoteStorageConfigurationId", "xOkapiTenant", "xOkapiToken");
    verify(remoteStorageClient, times(1)).getAccessionItem(isA(AccessionRequest.class), isA(String.class), isA(String.class));
  }

  @Test
  void testReturnItem() {
    var returnItemResponse = new ReturnItemResponse().isHoldRecallRequestExist(false);

    when(remoteStorageClient.returnItemById(isA(String.class), isA(CheckInItem.class), isA(String.class), isA(String.class)))
      .thenReturn(returnItemResponse);

    remoteStorageService.returnItemByBarcode("itemBarcode", "remoteStorageConfigurationId", "xOkapiTenant", "xOkapiToken");
    verify(remoteStorageClient).returnItemById(isA(String.class), isA(CheckInItem.class), isA(String.class), isA(String.class));
  }

  void testCheckInByHoldId() {
    remoteStorageService.checkInByHoldId("be16bad8-2a30-4f1c-bee6-f653ded15627", "remoteStorageConfigurationId", "xOkapiTenant", "xOkapiToken");
    verify(remoteStorageClient, times(1)).checkInByHoldId("remoteStorageConfigurationId",
      new CheckInRequest(UUID.fromString("be16bad8-2a30-4f1c-bee6-f653ded15627")), "xOkapiTenant", "xOkapiToken");
  }

  @Test
  void testGetCaiaSoftConfigurations() {
    var result = new ResultList<Configuration>();
    var firstConfiguration = new Configuration();
    firstConfiguration.setProviderName("CAIA_SOFT");
    var secondConfiguration = new Configuration();
    secondConfiguration.setProviderName("DEMATIC");
    result.setResult(List.of(firstConfiguration, secondConfiguration));

    when(remoteStorageClient.getStorageConfigurations(isA(String.class), isA(String.class))).thenReturn(result);

    var configurations = remoteStorageService.getCaiaSoftConfigurations("tenantId", "okapiToken");
    assertEquals(1, configurations.size());
    var actual = configurations.get(0);
    assertEquals("CAIA_SOFT", actual.getProviderName());
  }

  @Test
  void testGetRetrievalQueueRecords() {
    var result = new ResultList<RetrievalQueueRecord>();
    result.setResult(Collections.singletonList(new RetrievalQueueRecord()));

    when(remoteStorageClient.getRetrievalsByQuery(isA(String.class), eq(false), eq(Integer.MAX_VALUE), isA(String.class), isA(String.class))).thenReturn(result);

    var actual = remoteStorageService.getRetrievalQueueRecords("storageId", "tenantId", "okapiToken");
    assertEquals(1, actual.size());
  }
}
