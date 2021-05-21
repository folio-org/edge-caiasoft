package org.folio.ed.handler;

import org.folio.ed.domain.dto.Configuration;
import org.folio.ed.domain.dto.RetrievalQueueRecord;
import org.folio.ed.service.CaiaSoftSecurityManagerService;
import org.folio.ed.service.RemoteStorageService;
import org.folio.edgecommonspring.domain.entity.ConnectionSystemParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RetrievalQueueRecordHandlerTest {

  @Mock
  private RestTemplate restTemplate;
  @Mock
  private CaiaSoftSecurityManagerService sms;
  @Mock
  private RemoteStorageService remoteStorageService;

  @InjectMocks
  private RetrievalQueueRecordHandler retrievalQueueRecordHandler;

  @Test
  void testHandleIfCaiaSoftResponseStatusOk() {
    var configuration = new Configuration();
    configuration.setUrl("url");
    configuration.setApiKey("apiKey");
    configuration.setTenantId("tenantId");
    var record = new RetrievalQueueRecord();
    record.setItemBarcode("itemBarcode");
    record.setRequestType("requestType");
    record.setHoldId("holdId");
    record.setPickupLocation("pickupLocation");
    var responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);
    var connectionSystemParameters = new ConnectionSystemParameters();
    connectionSystemParameters.setTenantId("tenantId");
    connectionSystemParameters.setOkapiToken("okapiToken");

    when(sms.getConnectionParameters(anyString())).thenReturn(connectionSystemParameters);
    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), ArgumentMatchers.<Class<String>>any())).thenReturn(responseEntity);

    retrievalQueueRecordHandler.handle(record, configuration);

    verify(remoteStorageService, times(1)).setRetrieved("itemBarcode", "tenantId", "okapiToken");
  }

  @Test
  void testHandleIfCaiaSoftResponseStatusNotOk() {
    var configuration = new Configuration();
    configuration.setUrl("url");
    configuration.setApiKey("apiKey");
    configuration.setTenantId("tenantId");
    var record = new RetrievalQueueRecord();
    record.setItemBarcode("itemBarcode");
    record.setRequestType("requestType");
    record.setHoldId("holdId");
    record.setPickupLocation("pickupLocation");
    var responseEntity = new ResponseEntity<>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);

    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), ArgumentMatchers.<Class<String>>any())).thenReturn(responseEntity);

    retrievalQueueRecordHandler.handle(record, configuration);

    verify(remoteStorageService, times(0)).setRetrieved(any(), any(), any());
  }
}
