package org.folio.ed.security;

import lombok.extern.log4j.Log4j2;
import org.folio.edge.api.utils.security.SecureStore;


import java.util.Optional;
import java.util.Properties;

@Log4j2
public class SecureTenantsProducer {

  private SecureTenantsProducer() {}

  public static Optional<String> getTenants(Properties secureStoreProps, SecureStore secureStore, String caiaSoftTenants) {
    log.debug("getTenants:: Retrieving Tenants");
    if (secureStore instanceof TenantAwareAWSParamStore) {
      var stringOptional = ((TenantAwareAWSParamStore) secureStore).getTenants(caiaSoftTenants);
      if (stringOptional.isEmpty()) {
        log.warn("Tenants list not found in AWS Param store. Please create variable, which contains comma separated list of tenants");
      }
      log.info("getTenants:: Retrieved tenants from AWS Param store");
      return stringOptional;
    }
    log.info("getTenants:: Retrieved tenants from secure store properties : {}", secureStoreProps.get("tenants"));
    return Optional.of((String) secureStoreProps.get("tenants"));
  }
}
