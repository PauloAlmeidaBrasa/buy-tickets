package com.example.buy_tickets.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.buy_tickets.dto.TicketPurchaseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@ExtendWith(MockitoExtension.class)
class SqsPublisherServiceImplementTest {

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SqsPublisherServiceImplement sqsPublisherService;

    @Test
    void shouldPublishMessageToSqsWithExpectedPayload() throws Exception {
        ReflectionTestUtils.setField(sqsPublisherService, "queueUrl", "https://sqs.example.local/queue");

        when(objectMapper.writeValueAsString(any(TicketPurchaseMessage.class))).thenReturn("{\"ticketId\":1}");
        when(sqsClient.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(SendMessageResponse.builder().messageId("msg-1").build());

        sqsPublisherService.publish(1L, 7L, "user@example.com", "5511999999999");

        ArgumentCaptor<SendMessageRequest> requestCaptor = ArgumentCaptor.forClass(SendMessageRequest.class);
        verify(sqsClient).sendMessage(requestCaptor.capture());

        SendMessageRequest request = requestCaptor.getValue();
        assertEquals("https://sqs.example.local/queue", request.queueUrl());
        assertEquals("{\"ticketId\":1}", request.messageBody());
        assertEquals("ticket-1", request.messageGroupId());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenSerializationFails() throws Exception {
        when(objectMapper.writeValueAsString(any(TicketPurchaseMessage.class)))
                .thenThrow(new JsonProcessingException("serialization failed") {
                });

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> sqsPublisherService.publish(1L, 7L, "user@example.com", "5511999999999"));

        assertEquals("Failed to send SQS message", ex.getMessage());
        verify(sqsClient, never()).sendMessage(any(SendMessageRequest.class));
    }
}
