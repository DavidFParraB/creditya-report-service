package co.credit.app.model.loan.gateways;

import co.credit.app.model.loan.Loan;
import reactor.core.publisher.Mono;

public interface LoanRepository {
  Mono<Loan> getReport();

  Mono<Loan> saveReport(Loan loan);
}
