package org.folio.ed.util;

import java.util.concurrent.TimeUnit;

public class ConfigurationHelpers {

  private ConfigurationHelpers() {}

  public static long resolvePollingTimeFrame(int delay, String timeUnit) {
    switch (timeUnit.toLowerCase()) {
      case "minutes" : return delay * TimeUnit.MINUTES.toMillis(1);
      case "hours" : return delay * TimeUnit.HOURS.toMillis(1);
      case "days" : return delay * TimeUnit.DAYS.toMillis(1);
      case "weeks" : return delay * TimeUnit.DAYS.toMillis(7);
      case "months": return delay * TimeUnit.DAYS.toMillis(30);
      default: return 10_000L;
    }
  }
}
