package com.example.buy_tickets.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.buy_tickets.services.SqsConsumerService;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Service
public class SqsConsumerServiceImplement implements SqsConsumerService {

    private final SqsClient sqsClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public SqsConsumerServiceImplement(
            SqsClient sqsClient) {

        this.sqsClient = sqsClient;
    }

    @Override
//     @Scheduled(fixedDelay = 5000)
    public String consumeTicketQueue() {

        ReceiveMessageRequest request =
                ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(10)
                        .waitTimeSeconds(5)
                        .build();

        List<Message> messages =
                sqsClient.receiveMessage(request)
                        .messages();

        if (messages.isEmpty()) {

            System.out.println(
                    "No messages found.");

            return "No messages found.";
        }

        for (Message message : messages) {

            System.out.println(
                    "Message received: "
                            + message.body());
        }

        return "Processed "
                + messages.size()
                + " message(s)";
    }
}