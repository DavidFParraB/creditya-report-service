package co.credit.app.api;

import co.credit.app.api.mapper.LoanDTOMapper;
import co.credit.app.usecase.loan.LoanUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
private  final LoanUseCase loanUseCase;
private final LoanDTOMapper loanDTOMapper;

  public Mono<ServerResponse> listenGETReport(ServerRequest serverRequest) {
    return loanUseCase.getLoanReport().map(loanDTOMapper::toResponse)
        .flatMap(loanDTOs -> ServerResponse.ok().bodyValue(loanDTOs));
  }
}
