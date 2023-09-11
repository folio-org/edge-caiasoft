package org.folio.ed.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.ed.domain.dto.CaiaSoftRequest;
import org.folio.ed.domain.dto.CaiaSoftRequestItem;
import org.folio.ed.domain.dto.Configuration;
import org.folio.ed.domain.dto.RetrievalQueueRecord;
import org.folio.ed.service.CaiaSoftSecurityManagerService;
import org.folio.ed.service.RemoteStorageService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Log4j2
public class RetrievalQueueRecordHandler {

  private static final String CAIA_SOFT_CIRCULATION_REQUEST_PATH = "api/circrequests/v1/";
  private static final String CAIA_SOFT_API_KEY_HEADER_NAME = "X-API-Key";

  private final RestTemplate restTemplate;
  private final CaiaSoftSecurityManagerService sms;
  private final RemoteStorageService remoteStorageService;

  public Object handle(RetrievalQueueRecord retrievalRecord, Configuration configuration) {
    log.debug("handle:: Handle retrieval record item barcode {} and request id {}", retrievalRecord.getItemBarcode(), retrievalRecord.getHoldId());
    var headers = new HttpHeaders();
    headers.add(CAIA_SOFT_API_KEY_HEADER_NAME, configuration.getApiKey());
    headers.setContentType(MediaType.APPLICATION_JSON);
    var caiaSoftRequest = getCaiaSoftRequest(retrievalRecord);
    var caiaSoftCircUrl = getCirculationUrl(configuration);
    var response = post(caiaSoftCircUrl, headers, caiaSoftRequest);
    if (response.getStatusCode() == HttpStatus.OK) {
      var okapiToken = sms.getConnectionParameters(configuration.getTenantId()).getOkapiToken().accessToken();
      remoteStorageService.setRetrieved(retrievalRecord.getItemBarcode(), configuration.getTenantId(), okapiToken);
      log.info("handle:: Retrieval record with item barcode {} and request id {} set retrieved", retrievalRecord.getItemBarcode(), retrievalRecord.getHoldId());
    }
    return null;
  }

  private String getCirculationUrl(Configuration configuration) {
    return configuration.getUrl().endsWith("/") ? configuration.getUrl() + CAIA_SOFT_CIRCULATION_REQUEST_PATH
      : configuration.getUrl() + "/" +CAIA_SOFT_CIRCULATION_REQUEST_PATH;
  }

  private CaiaSoftRequest getCaiaSoftRequest(RetrievalQueueRecord retrievalRecord) {
    log.debug("getCaiaSoftRequest:: Getting Caiasoft Request");
    var requestItem = CaiaSoftRequestItem.of(retrievalRecord.getHoldId(),
      retrievalRecord.getRequestType(), retrievalRecord.getItemBarcode(), retrievalRecord.getPickupLocation());
    log.info("getCaiaSoftRequest:: Retrieved Caiasoft Request");
    return CaiaSoftRequest.of(Collections.singletonList(requestItem));
  }

  private ResponseEntity<String> post(String url, HttpHeaders headers, Object entity) {
    log.debug("post:: Sending POST request to URL: {}", url);
    return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(entity, headers), String.class);
  }
}
