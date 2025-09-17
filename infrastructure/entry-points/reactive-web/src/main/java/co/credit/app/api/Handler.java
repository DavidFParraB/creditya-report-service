package co.credit.app.api;

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
//private  final UseCase2 useCase2;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return loanUseCase.getLoanReport().flatMap(loan -> ServerResponse.ok().bodyValue(loan));
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
