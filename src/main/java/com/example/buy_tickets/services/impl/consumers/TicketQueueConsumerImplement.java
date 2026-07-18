package com.example.buy_tickets.services.impl.consumers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.buy_tickets.services.consumers.TicketQueueConsumer;
import com.example.buy_tickets.repositories.TicketRepository;
import com.example.buy_tickets.repositories.UserRepository;
import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.models.UserEntity;
import com.example.buy_tickets.dto.TicketPurchaseMessage;
import com.example.buy_tickets.helpers.EmailHelper;
import com.example.buy_tickets.helpers.WhatsAppHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

@Service
public class TicketQueueConsumerImplement implements TicketQueueConsumer {

    private final SqsClient sqsClient;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final EmailHelper emailHelper;
    private final WhatsAppHelper whatsAppHelper;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public TicketQueueConsumerImplement(
            SqsClient sqsClient,
            TicketRepository ticketRepository,
            UserRepository userRepository,
            ObjectMapper objectMapper,
            EmailHelper emailHelper,
            WhatsAppHelper whatsAppHelper) {

        this.sqsClient = sqsClient;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.emailHelper = emailHelper;
        this.whatsAppHelper = whatsAppHelper;
    }

    @Override
    @Scheduled(fixedDelay = 5000)
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
            System.out.println("No messages found.");
            return "No messages found.";
        }

        for (Message message : messages) {
            try {
                processMessage(message);

                sqsClient.deleteMessage(
                        DeleteMessageRequest.builder()
                                .queueUrl(queueUrl)
                                .receiptHandle(message.receiptHandle())
                                .build());

            } catch (Exception e) {
                System.err.println(
                        "Failed to process message "
                                + message.messageId() + ": " + e.getMessage());
                // message left undeleted — will be retried after visibility timeout
            }
        }

        return "Processed " + messages.size() + " message(s)";
    }

    @Transactional
    protected void processMessage(Message message) throws Exception {

        TicketPurchaseMessage payload =
                objectMapper.readValue(message.body(), TicketPurchaseMessage.class);

        Optional<TicketEntity> ticketOpt =
                ticketRepository.findById(payload.getTicketId());

        if (ticketOpt.isEmpty()) {
            System.err.println("Ticket not found: " + payload.getTicketId());
            return;
        }

        TicketEntity ticket = ticketOpt.get();

        Optional<UserEntity> userOpt = userRepository.findById(payload.getUserId());
        if (userOpt.isEmpty()) {
            System.err.println("User not found: " + payload.getUserId());
            return;
        }

        ticket.setStatus(TicketEntity.TicketStatus.SOLD);
        ticket.setUser(userOpt.get());

        ticketRepository.save(ticket);

        emailHelper.sendPurchaseConfirmation(payload.getUserEmail(), payload.getTicketId());
        whatsAppHelper.sendPurchaseConfirmation(payload.getUserWhatsapp(), payload.getTicketId());

        System.out.println("Ticket " + payload.getTicketId() + " marked as SOLD for user " + payload.getUserId());
    }
}