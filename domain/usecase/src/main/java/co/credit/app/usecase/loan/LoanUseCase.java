package co.credit.app.usecase.loan;

import co.credit.app.model.loan.Loan;
import co.credit.app.model.loan.gateways.LoanRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase {

  private static final String REPORT_STATUS = "approved";
  private final LoanRepository repository;

  public Mono<Loan> getLoanReport() {
    return repository.getReport(REPORT_STATUS);
  }

  public Mono<Loan> saveLoanReport(Loan loan) {
    return repository.getReport(REPORT_STATUS).flatMap(currentReport -> {
      loan.setStatus(REPORT_STATUS);
      loan.setCount(currentReport.getCount() + loan.getCount());
      loan.setTotal(currentReport.getTotal() + loan.getTotal());
      return Mono.just(loan);
    }).flatMap(repository::saveReport);
  }

}