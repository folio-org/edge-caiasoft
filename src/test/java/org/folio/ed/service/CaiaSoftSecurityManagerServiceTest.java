package org.folio.ed.service;

import org.folio.edgecommonspring.security.SecurityManagerService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CaiaSoftSecurityManagerServiceTest {

  @Mock
  private SecurityManagerService securityManagerService;

  @InjectMocks
  private CaiaSoftSecurityManagerService caiaSoftSecurityManagerService;

  @Test
  void testGetConnectionParameters() {
    caiaSoftSecurityManagerService.getConnectionParameters("tenant");
    verify(securityManagerService, times(1)).getParamsWithToken(isA(String.class));
  }
}
