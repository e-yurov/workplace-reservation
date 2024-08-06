package com.rc.mentorship.workplace_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

//    @Column(name = "start_date_time")
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private LocalDateTime startDateTime;
//
//    @Column(name = "end_date_time")
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private LocalDateTime endDateTime;
    @Embedded
    private ReservationDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "workplace_id", referencedColumnName = "id")
    private Workplace workplace;
}
