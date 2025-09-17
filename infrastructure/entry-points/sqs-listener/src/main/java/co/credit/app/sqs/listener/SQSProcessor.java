
package co.credit.app.sqs.listener;

import co.credit.app.model.loan.Loan;
import co.credit.app.sqs.listener.dto.LoanDTO;
import co.credit.app.usecase.loan.LoanUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class SQSProcessor implements Function<Message, Mono<Void>> {

  private final LoanUseCase loanUseCase;
  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> apply(Message message) {
    log.info("Processing message: {}", message.body());
    return Mono.fromCallable(() -> objectMapper.readValue(message.body(), LoanDTO.class))
        .map(loanReceiverDTO -> Loan.builder()
            .status(loanReceiverDTO.getStatus() == null ? "pending" : loanReceiverDTO.getStatus())
            .count(1.0)
            .total(loanReceiverDTO.getAmount())
            .build())
        .flatMap(loanUseCase::saveLoanReport)
        .onErrorResume(e -> {
            log.error("Failed to process message", e);
            return Mono.error(new IllegalArgumentException("Invalid loan or status.", e));
        }).then();
  }
}
