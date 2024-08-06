package com.rc.mentorship.workplace_reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
public class OfficeWorkTime {
    @Column(name = "start_time")
    @Temporal(value = TemporalType.TIME)
    private LocalTime startTime;

    @Column(name = "end_time")
    @Temporal(value = TemporalType.TIME)
    private LocalTime endTime;
}
