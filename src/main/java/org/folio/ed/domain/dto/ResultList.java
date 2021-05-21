package org.folio.ed.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class ResultList<E> {
  @JsonAlias("total_records")
  private Integer totalRecords;
  @JsonAlias({ "retrievals", "configurations" })
  private List<E> result;

  public boolean isEmpty() {
    return result == null || result.isEmpty();
  }
}
