package com.rc.mentorship.workplace_reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDateTime {
    @Column(name = "start_date_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime start;

    @Column(name = "end_date_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime end;
}
