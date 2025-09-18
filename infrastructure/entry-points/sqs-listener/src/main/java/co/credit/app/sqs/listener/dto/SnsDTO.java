package co.credit.app.sqs.listener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SnsDTO {
  @JsonProperty("Type")
  private String type;
  @JsonProperty("MessageId")
  private String messageId;
  @JsonProperty("TopicArn")
  private String topicArn;
  @JsonProperty("Message")
  private String message;
  @JsonProperty("Timestamp")
  private String timestamp;
  @JsonProperty("SignatureVersion")
  private String signatureVersion;
  @JsonProperty("Signature")
  private String signature;
  @JsonProperty("SigningCertURL")
  private String signingCertURL;
  @JsonProperty("UnsubscribeURL")
  private String unsubscribeURL;
}
