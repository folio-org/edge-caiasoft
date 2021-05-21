package org.folio.ed.client;

import static org.folio.edge.api.utils.Constants.APPLICATION_JSON;
import static org.folio.spring.integration.XOkapiHeaders.TENANT;
import static org.folio.spring.integration.XOkapiHeaders.TOKEN;

import org.folio.ed.domain.dto.AccessionItem;
import org.folio.ed.domain.dto.AccessionRequest;
import org.folio.ed.domain.dto.CheckInItem;
import org.folio.ed.domain.dto.ReturnItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "remote-storage")
public interface RemoteStorageClient {

  @PostMapping(path = "/accessions", produces = "application/json")
  AccessionItem getAccessionItem (AccessionRequest accessionRequest, @RequestHeader(TENANT) String tenantId,
    @RequestHeader(TOKEN) String okapiToken);

  @PostMapping(path = "/return/{remoteStorageConfigurationId}", produces = APPLICATION_JSON)
  ReturnItemResponse returnItemById(@PathVariable("remoteStorageConfigurationId") String remoteStorageConfigurationId,
    @RequestBody CheckInItem checkInItem, @RequestHeader(TENANT) String tenantId, @RequestHeader(TOKEN) String okapiToken);
}
