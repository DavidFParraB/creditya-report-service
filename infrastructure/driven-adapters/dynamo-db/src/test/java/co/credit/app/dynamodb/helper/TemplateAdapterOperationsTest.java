package co.credit.app.dynamodb.helper;

import co.credit.app.dynamodb.DynamoDBTemplateAdapter;
import co.credit.app.dynamodb.ModelEntity;
import co.credit.app.model.loan.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class TemplateAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<ModelEntity> customerTable;

    private ModelEntity modelEntity;
    private Loan loan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("loan_report", TableSchema.fromBean(ModelEntity.class)))
                .thenReturn(customerTable);

        modelEntity = new ModelEntity();
        modelEntity.setStatus("approved");
        modelEntity.setCount(0.0);
        modelEntity.setTotal(0.0);

        loan = new Loan("approved", 0.0, 0.0);
    }

    @Test
    void modelEntityPropertiesMustNotBeNull() {
        ModelEntity modelEntityUnderTest = new ModelEntity("approved", 0.0, 0.0);

        assertNotNull(modelEntityUnderTest.getStatus());
        assertNotNull(modelEntityUnderTest.getCount());
    }

    @Test
    void testSave() {
        when(customerTable.putItem(modelEntity)).thenReturn(CompletableFuture.runAsync(()->{}));
        when(mapper.map(modelEntity, ModelEntity.class)).thenReturn(modelEntity);
        when(mapper.map(loan, ModelEntity.class)).thenReturn(modelEntity);

        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(dynamoDBTemplateAdapter.save(loan))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testGetById() {
        String id = "approved";

        when(customerTable.getItem(
                Key.builder().partitionValue(AttributeValue.builder().s(id).build()).build()))
                .thenReturn(CompletableFuture.completedFuture(modelEntity));
        when(mapper.map(modelEntity, Loan.class)).thenReturn(loan);

        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(dynamoDBTemplateAdapter.getById("approved"))
                .expectNext(loan)
                .verifyComplete();
    }

    @Test
    void testDelete() {
        when(mapper.map(loan, ModelEntity.class)).thenReturn(modelEntity);
        when(mapper.map(modelEntity, Loan.class)).thenReturn(loan);

        when(customerTable.deleteItem(modelEntity))
                .thenReturn(CompletableFuture.completedFuture(modelEntity));

        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(dynamoDBTemplateAdapter.delete(loan))
                .expectNext(loan)
                .verifyComplete();
    }
}