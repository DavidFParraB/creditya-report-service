package co.credit.app.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoanReportDTO {
  private Long id;
  private Double amount;
  private Integer term;
  @JsonProperty("status_id")
  private Long statusId;
  @JsonProperty("loan_type_id")
  private Long loanTypeId;
  private String name;
  @JsonProperty("last_name")
  private String lastName;
  private String email;
  private String document;
  private Double salary;
}
