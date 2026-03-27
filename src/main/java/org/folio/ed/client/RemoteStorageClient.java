package org.folio.ed.client;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.folio.spring.integration.XOkapiHeaders.TENANT;
import static org.folio.spring.integration.XOkapiHeaders.TOKEN;

import org.folio.ed.domain.dto.AccessionItem;
import org.folio.ed.domain.dto.AccessionRequest;
import org.folio.ed.domain.dto.CheckInItem;
import org.folio.ed.domain.dto.ReturnItemResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.folio.ed.domain.dto.CheckInRequest;
import org.folio.ed.domain.dto.Configuration;
import org.folio.ed.domain.dto.ResultList;
import org.folio.ed.domain.dto.RetrievalQueueRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange(url = "remote-storage", accept = APPLICATION_JSON_VALUE, contentType = APPLICATION_JSON_VALUE)
public interface RemoteStorageClient {

  @PostExchange(url = "/accessions", accept = APPLICATION_JSON_VALUE)
  AccessionItem getAccessionItem(@RequestBody AccessionRequest accessionRequest, @RequestHeader(TENANT) String tenantId,
    @RequestHeader(TOKEN) String okapiToken);

  @PostExchange(url = "/return/{remoteStorageConfigurationId}", accept = APPLICATION_JSON_VALUE)
  ReturnItemResponse returnItemById(@PathVariable("remoteStorageConfigurationId") String remoteStorageConfigurationId,
    @RequestBody CheckInItem checkInItem, @RequestHeader(TENANT) String tenantId, @RequestHeader(TOKEN) String okapiToken);

  @PostExchange(url = "/retrieve/{remoteConfigurationId}/checkInItemByHoldId", accept = "text/plain")
  ResponseEntity<String> checkInByHoldId(@PathVariable("remoteConfigurationId") String configurationId,
    @RequestBody CheckInRequest checkInRequest, @RequestHeader(TENANT) String tenantId,
    @RequestHeader(TOKEN) String okapiToken);

  @GetExchange("/configurations")
  ResultList<Configuration> getStorageConfigurations(@RequestHeader(TENANT) String tenantId,
    @RequestHeader(TOKEN) String okapiToken);

  @GetExchange(url = "/retrievals", accept = APPLICATION_JSON_VALUE)
  ResultList<RetrievalQueueRecord> getRetrievalsByQuery(@RequestParam("storageId") String storageId,
    @RequestParam("retrieved") Boolean retrieved, @RequestParam("limit") int limit, @RequestHeader(TENANT) String tenantId,
    @RequestHeader(TOKEN) String okapiToken);

  @PutExchange("/retrievals/barcode/{barcode}")
  ResponseEntity<String> setRetrievalByBarcode(@PathVariable("barcode") String barcode, @RequestHeader(TENANT) String tenantId,
    @RequestHeader(TOKEN) String okapiToken);

}
