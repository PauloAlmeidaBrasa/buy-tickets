package com.example.buy_tickets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class AwsSqsHelper {

    private final String region;
    private final String queueUrl;
    private final String accessKeyId;
    private final String secretAccessKey;

    public AwsSqsHelper(
            @Value("${AWS_REGION:us-east-1}") String region,
            @Value("${AWS_SQS_QUEUE_URL:}") String queueUrl,
            @Value("${AWS_ACCESS_KEY_ID:}") String accessKeyId,
            @Value("${AWS_SECRET_ACCESS_KEY:}") String secretAccessKey) {
        this.region = region;
        this.queueUrl = queueUrl;
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
    }

    public SqsClient getSqsClient() {
        SqsClientBuilder builder = SqsClient.builder().region(Region.of(region));

        if (StringUtils.hasText(accessKeyId) && StringUtils.hasText(secretAccessKey)) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKeyId, secretAccessKey)));
        }

        return builder.build();
    }

    public void sendReservationMessage(String messageBody) {
        if (!StringUtils.hasText(queueUrl)) {
            return;
        }

        try (SqsClient sqsClient = getSqsClient()) {
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build());
        }
    }
}
