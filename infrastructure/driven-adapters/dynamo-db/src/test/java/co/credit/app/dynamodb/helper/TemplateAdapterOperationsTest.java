package co.credit.app.dynamodb.helper;

import co.credit.app.dynamodb.DynamoDBTemplateAdapter;
import co.credit.app.dynamodb.ModelEntity;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("loan_report", TableSchema.fromBean(ModelEntity.class)))
                .thenReturn(customerTable);

        modelEntity = new ModelEntity();
        modelEntity.setStatus("approved");
        modelEntity.setCount(0.0);
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

        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(dynamoDBTemplateAdapter.save(modelEntity))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testGetById() {
        String id = "approved";

        when(customerTable.getItem(
                Key.builder().partitionValue(AttributeValue.builder().s(id).build()).build()))
                .thenReturn(CompletableFuture.completedFuture(modelEntity));
        when(mapper.map(modelEntity, Object.class)).thenReturn("value");

        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(dynamoDBTemplateAdapter.getById("approved"))
                .expectNext("value")
                .verifyComplete();
    }

    @Test
    void testDelete() {
        when(mapper.map(modelEntity, ModelEntity.class)).thenReturn(modelEntity);
        when(mapper.map(modelEntity, Object.class)).thenReturn("value");

        when(customerTable.deleteItem(modelEntity))
                .thenReturn(CompletableFuture.completedFuture(modelEntity));

        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(dynamoDBTemplateAdapter.delete(modelEntity))
                .expectNext("value")
                .verifyComplete();
    }
}