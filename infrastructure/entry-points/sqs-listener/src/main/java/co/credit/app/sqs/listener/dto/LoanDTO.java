package co.credit.app.sqs.listener.dto;

import lombok.Data;

@Data
public class LoanDTO {
  private Double amount;
  private String status;
}
