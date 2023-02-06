package org.folio.ed.util;

import java.util.concurrent.TimeUnit;

public class ConfigurationHelpers {

  private ConfigurationHelpers() {}

  public static long resolvePollingTimeFrame(int delay, String timeUnit) {
    return switch (timeUnit.toLowerCase()) {
      case "minutes" -> delay * TimeUnit.MINUTES.toMillis(1);
      case "hours" -> delay * TimeUnit.HOURS.toMillis(1);
      case "days" -> delay * TimeUnit.DAYS.toMillis(1);
      case "weeks" -> delay * TimeUnit.DAYS.toMillis(7);
      case "months" -> delay * TimeUnit.DAYS.toMillis(30);
      default -> 10_000L;
    };
  }
}
