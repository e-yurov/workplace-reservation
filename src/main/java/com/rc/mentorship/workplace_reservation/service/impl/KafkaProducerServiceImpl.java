package com.rc.mentorship.workplace_reservation.service.impl;

import com.rc.mentorship.workplace_reservation.dto.message.ReservationMessage;
import com.rc.mentorship.workplace_reservation.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, ReservationMessage> kafkaTemplate;

    @Value("${kafka.topics.email-topic}")
    private String topic;

    public void sendMessage(ReservationMessage message) {
        kafkaTemplate.send(topic, message);
    }
}
