package org.folio.ed.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CaiaSoftRequest {
  private List<CaiaSoftRequestItem> requests;
}
