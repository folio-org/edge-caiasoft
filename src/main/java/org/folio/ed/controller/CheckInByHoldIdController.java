package org.folio.ed.controller;

import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.folio.ed.rest.resource.RequestsApi;
import org.folio.ed.service.RemoteStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/caiasoftService/")
public class CheckInByHoldIdController implements RequestsApi {

  private final RemoteStorageService remoteStorageService;

  @Override
  public ResponseEntity<String> checkInByHoldId(
      @ApiParam(required = true) @PathVariable("requestId") String requestId,
      @ApiParam(required = true) @PathVariable("remoteStorageConfigurationId") String remoteStorageConfigurationId,
      @ApiParam(required = true) @RequestHeader(value = "x-okapi-token") String xOkapiToken,
      @ApiParam(required = true) @RequestHeader(value = "x-okapi-tenant") String xOkapiTenant) {

    return remoteStorageService.checkInByHoldId(requestId, remoteStorageConfigurationId, xOkapiTenant, xOkapiToken);
  }

}
