package org.folio.ed.security;

import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.folio.edge.api.utils.security.AwsParamStore;


import java.util.Optional;
import java.util.Properties;

import static org.folio.ed.service.CaiaSoftSecurityManagerService.CAIA_SOFT_CLIENT_AND_USERNAME;


@Log4j2
public class TenantAwareAWSParamStore extends AwsParamStore {

  public static final String DEFAULT_AWS_KEY_PARAMETER = CAIA_SOFT_CLIENT_AND_USERNAME + "_tenants";

  public TenantAwareAWSParamStore(Properties properties) {
    super(properties);
  }

  public Optional<String> getTenants(String tenantsParameter) {
    String key = StringUtils.isNotEmpty(tenantsParameter) ? tenantsParameter : DEFAULT_AWS_KEY_PARAMETER;
    GetParameterRequest req = (new GetParameterRequest()).withName(key)
      .withWithDecryption(true);

    try {
      return Optional.of(this.ssm.getParameter(req)
        .getParameter()
        .getValue());
    } catch (Exception e) {
      log.warn("Cannot get tenants list from key: " + key, e);
      return Optional.empty();
    }
  }
}
