package org.folio.ed;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.yml")
@Log4j2
public class TestBase {

  private static RestTemplate restTemplate;
  public static WireMockServer wireMockServer;

  @LocalServerPort
  protected int edgePort;

  // This value must correspond port from testing properties okapi url
  public final static int OKAPI_PORT = 3333;

  @BeforeEach
  void setUp() {
    wireMockServer.resetAll();
  }

  @BeforeAll
  static void testSetup() {
    restTemplate = new RestTemplate();
    wireMockServer = new WireMockServer(OKAPI_PORT);
    wireMockServer.start();
  }

  @AfterAll
  static void tearDown() {
    wireMockServer.stop();
  }

  public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> clazz) {
    return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), clazz);
  }

  public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object entity, Class<T> clazz) {
    return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(entity, headers), clazz);
  }
}
