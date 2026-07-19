package com.example.buy_tickets.services.impl.consumers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.buy_tickets.dto.TicketPurchaseMessage;
import com.example.buy_tickets.helpers.EmailHelper;
import com.example.buy_tickets.helpers.WhatsAppHelper;
import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.models.UserEntity;
import com.example.buy_tickets.repositories.TicketRepository;
import com.example.buy_tickets.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@ExtendWith(MockitoExtension.class)
class TicketQueueConsumerImplementTest {

    @Mock
    private SqsClient sqsClient;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EmailHelper emailHelper;

    @Mock
    private WhatsAppHelper whatsAppHelper;

    @InjectMocks
    private TicketQueueConsumerImplement consumer;

    @Test
    void shouldReturnNoMessagesWhenQueueIsEmpty() {
        ReflectionTestUtils.setField(consumer, "queueUrl", "https://sqs.example.local/queue");

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
            .thenReturn(ReceiveMessageResponse.builder().messages(List.of()).build());

        String response = consumer.consumeTicketQueue();

        assertEquals("No messages found.", response);
        verify(sqsClient, never()).deleteMessage(any(DeleteMessageRequest.class));
        verify(ticketRepository, never()).save(any(TicketEntity.class));
        verify(emailHelper, never()).sendPurchaseConfirmation(any(), any());
        verify(whatsAppHelper, never()).sendPurchaseConfirmation(any(), any());
    }

    @Test
    void shouldProcessAndDeleteMessageSuccessfully() throws Exception {
        ReflectionTestUtils.setField(consumer, "queueUrl", "https://sqs.example.local/queue");

        Message message = Message.builder()
                .messageId("m-1")
                .receiptHandle("rh-1")
                .body("{json}")
                .build();

        TicketPurchaseMessage payload = new TicketPurchaseMessage(1L, 2L, "user@example.com", "5511999999999");

        TicketEntity ticket = new TicketEntity();
        ticket.setId(1L);
        ticket.setStatus(TicketEntity.TicketStatus.RESERVED);

        UserEntity user = new UserEntity();
        user.setId(2L);

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
            .thenReturn(ReceiveMessageResponse.builder().messages(message).build());
        when(objectMapper.readValue("{json}", TicketPurchaseMessage.class)).thenReturn(payload);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        String response = consumer.consumeTicketQueue();

        assertEquals("Processed 1 message(s)", response);

        ArgumentCaptor<TicketEntity> ticketCaptor = ArgumentCaptor.forClass(TicketEntity.class);
        verify(ticketRepository).save(ticketCaptor.capture());

        TicketEntity savedTicket = ticketCaptor.getValue();
        assertEquals(TicketEntity.TicketStatus.SOLD, savedTicket.getStatus());
        assertSame(user, savedTicket.getUser());

        verify(emailHelper).sendPurchaseConfirmation("user@example.com", 1L);
        verify(whatsAppHelper).sendPurchaseConfirmation("5511999999999", 1L);

        ArgumentCaptor<DeleteMessageRequest> deleteCaptor = ArgumentCaptor.forClass(DeleteMessageRequest.class);
        verify(sqsClient).deleteMessage(deleteCaptor.capture());

        DeleteMessageRequest deleteRequest = deleteCaptor.getValue();
        assertEquals("https://sqs.example.local/queue", deleteRequest.queueUrl());
        assertEquals("rh-1", deleteRequest.receiptHandle());
    }

    @Test
    void shouldNotDeleteMessageWhenMessageProcessingThrowsException() throws Exception {
        ReflectionTestUtils.setField(consumer, "queueUrl", "https://sqs.example.local/queue");

        Message message = Message.builder()
                .messageId("m-2")
                .receiptHandle("rh-2")
                .body("invalid")
                .build();

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
            .thenReturn(ReceiveMessageResponse.builder().messages(message).build());
        when(objectMapper.readValue("invalid", TicketPurchaseMessage.class))
                .thenThrow(new RuntimeException("parse failure"));

        String response = consumer.consumeTicketQueue();

        assertEquals("Processed 1 message(s)", response);
        verify(sqsClient, never()).deleteMessage(any(DeleteMessageRequest.class));
        verify(ticketRepository, never()).save(any(TicketEntity.class));
        verify(emailHelper, never()).sendPurchaseConfirmation(any(), any());
        verify(whatsAppHelper, never()).sendPurchaseConfirmation(any(), any());
    }

    @Test
    void shouldDeleteMessageButSkipSaveWhenUserNotFound() throws Exception {
        ReflectionTestUtils.setField(consumer, "queueUrl", "https://sqs.example.local/queue");

        Message message = Message.builder()
                .messageId("m-3")
                .receiptHandle("rh-3")
                .body("{json}")
                .build();

        TicketPurchaseMessage payload = new TicketPurchaseMessage(1L, 99L, "user@example.com", "5511999999999");

        TicketEntity ticket = new TicketEntity();
        ticket.setId(1L);
        ticket.setStatus(TicketEntity.TicketStatus.RESERVED);

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
            .thenReturn(ReceiveMessageResponse.builder().messages(message).build());
        when(objectMapper.readValue("{json}", TicketPurchaseMessage.class)).thenReturn(payload);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        String response = consumer.consumeTicketQueue();

        assertEquals("Processed 1 message(s)", response);
        verify(ticketRepository, never()).save(any(TicketEntity.class));
        verify(emailHelper, never()).sendPurchaseConfirmation(any(), any());
        verify(whatsAppHelper, never()).sendPurchaseConfirmation(any(), any());
        verify(sqsClient).deleteMessage(any(DeleteMessageRequest.class));
    }
}
