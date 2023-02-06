package org.folio.ed.controller;

import static org.folio.edge.api.utils.Constants.APPLICATION_JSON;
import static org.folio.edge.api.utils.Constants.TEXT_PLAIN;

import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.folio.ed.domain.dto.ReturnItemResponse;
import org.folio.ed.rest.resource.RequestBarcodesApi;
import org.folio.ed.service.RemoteStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/caiasoftService/")
public class ReturnController implements RequestBarcodesApi {
  private final RemoteStorageService remoteStorageService;

  @Override
  @PostMapping(value = "/RequestBarcodes/{itemBarcode}/reshelved/{remoteStorageConfigurationId}",
    produces = {APPLICATION_JSON, TEXT_PLAIN},
    consumes = TEXT_PLAIN)
  public ResponseEntity<ReturnItemResponse> returnItemByBarcode(
      @ApiParam(required = true) @PathVariable("itemBarcode") String itemBarcode,
      @ApiParam(required = true) @PathVariable("remoteStorageConfigurationId") String remoteStorageConfigurationId,
      @ApiParam(required = true) @RequestHeader(value = "x-okapi-token") String xOkapiToken,
      @ApiParam(required = true) @RequestHeader(value = "x-okapi-tenant") String xOkapiTenant,
      @RequestBody(required = false) String body) {

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    var returnItemResponse = remoteStorageService
      .returnItemByBarcode(itemBarcode, remoteStorageConfigurationId, xOkapiTenant, xOkapiToken);
    return new ResponseEntity<>(returnItemResponse, headers, HttpStatus.CREATED);
  }
}
