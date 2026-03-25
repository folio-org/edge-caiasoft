package org.folio.ed.controller;


import lombok.RequiredArgsConstructor;
import org.folio.ed.domain.dto.AccessionItem;
import org.folio.ed.rest.resource.ItemBarcodesApi;
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
public class AccessionController implements ItemBarcodesApi {

  private final RemoteStorageService remoteStorageService;

  @Override
  public ResponseEntity<AccessionItem> getAccessionItem(String itemBarcode,
      String remoteStorageConfigurationId, String xOkapiToken, String xOkapiTenant) {

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    var accessionItem = remoteStorageService.getAccessionItem(itemBarcode, remoteStorageConfigurationId, xOkapiTenant, xOkapiToken);
    return new ResponseEntity<>(accessionItem, headers, HttpStatus.OK);
  }
}
