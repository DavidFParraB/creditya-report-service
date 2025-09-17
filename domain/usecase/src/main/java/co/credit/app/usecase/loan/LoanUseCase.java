package co.credit.app.usecase.loan;

import co.credit.app.model.loan.Loan;
import co.credit.app.model.loan.gateways.LoanRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase {

  private final LoanRepository repository;

  public Mono<Loan> getLoanReport() {
    return repository.getReport();
  }

}