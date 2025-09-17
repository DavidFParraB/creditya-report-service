package co.credit.app.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

/* Enhanced DynamoDB annotations are incompatible with Lombok #1932
         https://github.com/aws/aws-sdk-java-v2/issues/1932*/
@DynamoDbBean
public class ModelEntity {

    private String status;
    private Double count;
    private Double total;

    public ModelEntity() {
    }

    public ModelEntity(String status, Double count, Double total) {
        this.status = status;
        this.count = count;
        this.total = total;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDbAttribute("count")
    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    @DynamoDbAttribute("total_amount")
    public Double getTotal() {
      return total;
    }

    public void setTotal(Double total) {
      this.total = total;
    }
}
