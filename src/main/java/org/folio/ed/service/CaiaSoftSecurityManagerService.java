package org.folio.ed.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.folio.ed.security.SecureTenantsProducer;
import org.folio.edge.api.utils.security.SecureStoreFactory;
import org.folio.edgecommonspring.domain.entity.ConnectionSystemParameters;
import org.folio.edgecommonspring.security.SecurityManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toSet;
import static org.folio.edge.api.utils.util.PropertiesUtil.getProperties;

@Service
@RequiredArgsConstructor
public class CaiaSoftSecurityManagerService {

  private final SecurityManagerService sms;

  public static final String CAIA_SOFT_CLIENT_AND_USERNAME = "caiaSoftClient";
  private static final Pattern COMMA = Pattern.compile(",");
  private static final String API_KEY_TEMPLATE = "{\"t\":\"%s\", \"u\":\"%s\", \"s\":\"%s\"}";

  @Value("${secure_store}")
  private String secureStoreType;

  @Value("${secure_store_props}")
  private String secureStorePropsFile;

  @Value("${caia_soft_tenants}")
  private String caiaSoftTenants;

  @Getter
  private Set<String> caiaSoftUserTenants = new HashSet<>();

  @PostConstruct
  public void init() {
    var secureStoreProps = getProperties(secureStorePropsFile);
    var secureStore = SecureStoreFactory.getSecureStore(secureStoreType, secureStoreProps);
    var tenants = SecureTenantsProducer.getTenants(secureStoreProps, secureStore, caiaSoftTenants);
    tenants.ifPresent(tenantsStr -> caiaSoftUserTenants = Arrays.stream(COMMA.split(tenantsStr))
      .collect(toSet()));
  }

  public ConnectionSystemParameters getConnectionParameters(String tenantId) {
    var apikey = Base64.getEncoder()
      .encodeToString(String.format(API_KEY_TEMPLATE, tenantId, CAIA_SOFT_CLIENT_AND_USERNAME, CAIA_SOFT_CLIENT_AND_USERNAME)
      .getBytes(StandardCharsets.UTF_8));
    return sms.getParamsWithToken(apikey);
  }
}
