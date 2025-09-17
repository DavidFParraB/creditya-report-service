package co.credit.app.api.dto;

import lombok.Data;

@Data
public class LoanDTO {
  private Double total;
  private Double count;
  private String status;
}
