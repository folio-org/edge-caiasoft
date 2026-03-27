package org.folio.ed.controller;

import lombok.RequiredArgsConstructor;
import org.folio.ed.domain.dto.ReturnItemResponse;
import org.folio.ed.rest.resource.RequestBarcodesApi;
import org.folio.ed.service.RemoteStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/caiasoftService/")
public class ReturnController implements RequestBarcodesApi {
  private final RemoteStorageService remoteStorageService;

  @Override
  public ResponseEntity<ReturnItemResponse> returnItemByBarcode(String itemBarcode,
      String remoteStorageConfigurationId, String xOkapiToken, String xOkapiTenant, String body) {

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    var returnItemResponse = remoteStorageService
      .returnItemByBarcode(itemBarcode, remoteStorageConfigurationId, xOkapiTenant, xOkapiToken);
    return new ResponseEntity<>(returnItemResponse, headers, HttpStatus.CREATED);
  }
}
