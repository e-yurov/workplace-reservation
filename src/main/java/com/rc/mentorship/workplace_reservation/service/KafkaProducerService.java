package com.rc.mentorship.workplace_reservation.service;

import com.rc.mentorship.workplace_reservation.dto.message.ReservationMessage;

public interface KafkaProducerService {
    void sendMessage(ReservationMessage message);
}
