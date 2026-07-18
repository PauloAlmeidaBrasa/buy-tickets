package com.example.buy_tickets.services.impl;
import com.example.buy_tickets.services.SqsPublisherService;

import com.example.buy_tickets.dto.TicketPurchaseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SqsPublisherServiceImplement implements SqsPublisherService {



    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${AWS_SQS_QUEUE_URL:}")
    private String queueUrl;

    public SqsPublisherServiceImplement(
            SqsClient sqsClient,
            ObjectMapper objectMapper) {

        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }


    @Override
    public void publish(
            Long ticketId,
            Long userId,
            String userEmail,
            String userWhatsapp) {

        try {

            TicketPurchaseMessage payload =
                    new TicketPurchaseMessage(
                            ticketId,
                            userId,
                            userEmail,
                            userWhatsapp);

            String body =
                    objectMapper.writeValueAsString(
                            payload);

                            
            System.out.println(
                    "BODY "
                    + body);

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