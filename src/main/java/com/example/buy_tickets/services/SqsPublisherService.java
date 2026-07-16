package com.example.buy_tickets.services;

import tools.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;


@Service
public interface SqsPublisherService {

    public void publish( Long ticketId, Long userId);

}
