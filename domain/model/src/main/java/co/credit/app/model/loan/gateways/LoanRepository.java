package co.credit.app.model.loan.gateways;

import co.credit.app.model.loan.Loan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanRepository {
  Mono<Loan> getReport(String status);

  Mono<Loan> saveReport(Loan loan);
}
