package org.folio.ed.controller;

import lombok.RequiredArgsConstructor;
import org.folio.ed.rest.resource.RequestsApi;
import org.folio.ed.service.RemoteStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/caiasoftService/")
public class CheckInByHoldIdController implements RequestsApi {

  private final RemoteStorageService remoteStorageService;

  @Override
  public ResponseEntity<String> checkInByHoldId(String requestId,
      String remoteStorageConfigurationId, String xOkapiToken, String xOkapiTenant) {

    return remoteStorageService.checkInByHoldId(requestId, remoteStorageConfigurationId, xOkapiTenant, xOkapiToken);
  }

}
