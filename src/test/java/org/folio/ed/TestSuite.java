package org.folio.ed;

import org.folio.ed.controller.AccessionControllerTest;
import org.folio.ed.controller.ReturnControllerTest;
import org.folio.ed.service.RemoteStorageServiceTest;
import org.junit.jupiter.api.Nested;

public class TestSuite {

  @Nested
  class AccessionControllerTestNested extends AccessionControllerTest {
  }

  @Nested
  class ReturnControllerTestNested extends ReturnControllerTest {
  }

  @Nested
  class RemoteStorageServiceTestNested extends RemoteStorageServiceTest {
  }
}
