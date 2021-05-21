package org.folio.ed.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationHelpersTest {

  @Test
  void testResolvePollingTimeFrame() {
    assertEquals(60000L, ConfigurationHelpers.resolvePollingTimeFrame(1, "minutes"));
    assertEquals(3600000L, ConfigurationHelpers.resolvePollingTimeFrame(1, "hours"));
    assertEquals(86400000L, ConfigurationHelpers.resolvePollingTimeFrame(1, "days"));
    assertEquals(604800000L, ConfigurationHelpers.resolvePollingTimeFrame(1, "weeks"));
    assertEquals(2592000000L, ConfigurationHelpers.resolvePollingTimeFrame(1, "months"));
    assertEquals(10000L, ConfigurationHelpers.resolvePollingTimeFrame(1, "default"));
  }
}
