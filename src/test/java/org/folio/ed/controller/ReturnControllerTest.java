package org.folio.ed.controller;

import static org.folio.ed.service.CaiaSoftSecurityManagerService.CAIA_SOFT_CLIENT_AND_USERNAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.folio.ed.TestBase;
import org.folio.ed.domain.dto.ReturnItemResponse;
import org.folio.spring.integration.XOkapiHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Base64;

public class ReturnControllerTest extends TestBase {

  private static final String RETURN_URL = "http://localhost:%s/caiasoftService/RequestBarcodes/%s/reshelved/%s?apikey=%s";
  private static final String REMOTE_STORAGE_CONFIGURATION_ID = "de17bad7-2a30-4f1c-bee5-f653ded15629";
  private static final String INVALID_REMOTE_STORAGE_CONFIGURATION_ID = "invalid-uuid";
  private static final String ITEM_BARCODE = "1001";
  private static final String TENANT_USER_DATA_FOR_APIKEY = "{\"t\":\"test_tenant\", \"u\":\"" + CAIA_SOFT_CLIENT_AND_USERNAME + "\", \"s\":\"salt\"}";

  @Test
  void canReturnItem() {
    var apikey = Base64.getEncoder().encodeToString(TENANT_USER_DATA_FOR_APIKEY.getBytes());
    var returnUrl = String.format(RETURN_URL, edgePort, ITEM_BARCODE, REMOTE_STORAGE_CONFIGURATION_ID, apikey);
    var response = post(returnUrl, new HttpHeaders(), "", ReturnItemResponse.class);
    assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
  }

  @Test
  void shouldReturnBadRequestWhenRemoteStorageIdIsInvalid() {
    var apikey = Base64.getEncoder().encodeToString(TENANT_USER_DATA_FOR_APIKEY.getBytes());
    var returnUrl = String.format(RETURN_URL, edgePort, ITEM_BARCODE, INVALID_REMOTE_STORAGE_CONFIGURATION_ID, apikey);
    var headers = new HttpHeaders();
    var exception = assertThrows(HttpClientErrorException.class,
      () -> post(returnUrl, headers, "", ReturnItemResponse.class));
    assertThat(exception.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
  }
}
