package org.folio.ed.controller;

import org.folio.ed.TestBase;
import org.folio.ed.domain.dto.AccessionItem;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


import java.util.Base64;

import static org.folio.ed.service.CaiaSoftSecurityManagerService.CAIA_SOFT_CLIENT_AND_USERNAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AccessionControllerTest extends TestBase {

  private static final String ACCESSION_URL = "http://localhost:%s/caiasoftService/ItemBarcodes/%s/accessioned/%s?apikey=%s";
  private static final String REMOTE_STORAGE_CONFIGURATION_ID = "de17bad7-2a30-4f1c-bee5-f653ded15629";
  private static final String ITEM_BARCODE = "1001";
  private static final String TENANT_USER_DATA_FOR_APIKEY = "{\"t\":\"test_tenant\", \"u\":\"" + CAIA_SOFT_CLIENT_AND_USERNAME + "\", \"s\":\"salt\"}";

  @Test
  public void canGetAccessionItem() {
    var apikey = Base64.getEncoder().encodeToString(TENANT_USER_DATA_FOR_APIKEY.getBytes());
    var accessionUrl = String.format(ACCESSION_URL, edgePort, ITEM_BARCODE, REMOTE_STORAGE_CONFIGURATION_ID, apikey);
    var response = get(accessionUrl, new HttpHeaders(), AccessionItem.class);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }
}
