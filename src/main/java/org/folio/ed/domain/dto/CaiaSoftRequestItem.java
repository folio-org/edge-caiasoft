package org.folio.ed.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CaiaSoftRequestItem {
  @JsonProperty("request_id")
  private String requestId;
  @JsonProperty("request_type")
  private String requestType;
  private String barcode;
  private String stop;
}
