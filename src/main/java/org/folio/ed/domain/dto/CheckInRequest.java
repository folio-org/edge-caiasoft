package org.folio.ed.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CheckInRequest {
  private UUID holdId;
}
