package org.folio.ed.security;

import org.folio.edge.api.utils.security.EphemeralStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SecureTenantsProducerTest {

  @Test
  void testGetTenants() {
    var secureStoreProps = new Properties();
    secureStoreProps.put("tenants", "test_tenant");

    var caiaSoftTenants = "caiaSoftTenants";
    var mockSecureStore = Mockito.mock(EphemeralStore.class);
    var tenants = SecureTenantsProducer.getTenants(secureStoreProps, mockSecureStore, caiaSoftTenants);
    assertEquals("test_tenant", tenants.get());

    var mockAwsSecureStore  = Mockito.mock(TenantAwareAWSParamStore.class);
    when(mockAwsSecureStore.getTenants(caiaSoftTenants)).thenReturn(Optional.of("test_tenant_aws"));
    tenants = SecureTenantsProducer.getTenants(secureStoreProps, mockAwsSecureStore, caiaSoftTenants);
    assertEquals("test_tenant_aws", tenants.get());
  }
}
