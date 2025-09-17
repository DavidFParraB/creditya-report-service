package co.credit.app.dynamodb;

import co.credit.app.dynamodb.helper.TemplateAdapterOperations;
import co.credit.app.model.loan.Loan;
import co.credit.app.model.loan.gateways.LoanRepository;
import lombok.extern.log4j.Log4j2;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;


@Repository
@Log4j2
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Loan, String, ModelEntity>  implements
    LoanRepository {

  public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(connectionFactory, mapper, d -> mapper.map(d, Loan.class /*domain model*/), "loan_report");
    }

    public Mono<List<Loan /*domain model*/>> getEntityBySomeKeys(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return query(queryExpression);
    }

    public Mono<List<Loan /*domain model*/>> getEntityBySomeKeysByIndex(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return queryByIndex(queryExpression, "secondary_index" /*index is optional if you define in constructor*/);
    }

    private QueryEnhancedRequest generateQueryExpression(String partitionKey, String sortKey) {
        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(partitionKey).build()))
                .queryConditional(QueryConditional.sortGreaterThanOrEqualTo(Key.builder().sortValue(sortKey).build()))
                .build();
    }

    @Override
    public Mono<Loan> getReport(String status) {

        return getById(status)
            .doOnSubscribe(l ->  log.info("Fetching loan with id: {}", status))
            .doOnNext(loan -> log.info("Found loan with ID: {}", loan.getStatus()));
    }

    @Override
    public Mono<Loan> saveReport(Loan loan) {
        return save(loan)
            .doOnSubscribe(l ->  log.info("Saving loan with status: {}", loan.getStatus()))
            .doOnNext(loanResult -> log.info("Saved loan with ID: {}", loanResult.getStatus()));
    }
}
