package com.example.buy_tickets.services;

import com.example.buy_tickets.dto.TicketPurchaseMessage;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SqsPublisher {

    private final SqsClient sqsClient;
    private final JsonMapper objectMapper;

    @Value("${AWS_SQS_QUEUE_URL:}")
    private String queueUrl;

    public SqsPublisher(
            SqsClient sqsClient,
            JsonMapper objectMapper) {

        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }

    public void publish(
            Long ticketId,
            Long userId) {

        try {

            TicketPurchaseMessage payload =
                    new TicketPurchaseMessage(
                            ticketId,
                            userId);

            String body =
                    objectMapper.writeValueAsString(
                            payload);

            SendMessageRequest request =
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(body)

                            // FIFO requirement
                            .messageGroupId(
                                    "ticket-" + ticketId)

                            .build();

            SendMessageResponse response =
                    sqsClient.sendMessage(request);

            System.out.println(
                    "Message sent: "
                    + response.messageId());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send SQS message",
                    e);
        }
    }
}