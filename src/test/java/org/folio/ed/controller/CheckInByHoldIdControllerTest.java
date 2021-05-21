package org.folio.ed.controller;

import org.folio.ed.TestBase;
import org.folio.ed.domain.dto.CheckInRequest;
import org.folio.spring.integration.XOkapiHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CheckInByHoldIdControllerTest extends TestBase {

  private static final String CHECK_IN_URL = "http://localhost:%s/remote-storage/%s/checkInItemByHoldId";
  private static final String REMOTE_STORAGE_CONFIGURATION_ID = "de17bad7-2a30-4f1c-bee5-f653ded15629";

  @Test
  public void canCheckInByHoldId() {
    var checkInUrl = String.format(CHECK_IN_URL, OKAPI_PORT, REMOTE_STORAGE_CONFIGURATION_ID);
    var headers = new HttpHeaders();
    headers.add(XOkapiHeaders.TOKEN, "test_token");
    headers.add(XOkapiHeaders.TENANT, "test_tenant");
    var response = post(checkInUrl, headers,
      new CheckInRequest(UUID.fromString("de58bad9-1a31-5f1c-bee6-f653ded15629")), String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }
}
