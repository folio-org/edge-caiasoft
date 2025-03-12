package org.folio.ed.security;

import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.folio.edge.api.utils.security.AwsParamStore;

import java.util.Optional;
import java.util.Properties;

@Log4j2
public class TenantAwareAWSParamStore extends AwsParamStore {

  public static final String DEFAULT_AWS_KEY_PARAMETER = "caiaSoftClient_tenants";

  public TenantAwareAWSParamStore(Properties properties) {
    super(properties);
  }

  public Optional<String> getTenants(String tenantsParameter) {
    log.debug("getTenants:: Retrieving Tenants with tenantsParameter: {}", tenantsParameter);
    String key = StringUtils.isNotEmpty(tenantsParameter) ? tenantsParameter : DEFAULT_AWS_KEY_PARAMETER;
    GetParameterRequest req = GetParameterRequest.builder()
      .name(key)
      .withDecryption(true)
      .build();

    try {
      log.warn("Trying to Retrieve tenants from AWS SSM parameter store");
      return Optional.of(this.ssm.getParameter(req)
        .parameter()
        .value());
    } catch (Exception e) {
      log.warn("Cannot get tenants list from key: " + key, e);
      return Optional.empty();
    }
  }
}
