package com.rc.mentorship.workplace_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "office", schema = "workplace_reservation")
@Getter
@Setter
public class Office extends EntityInMemory {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "start_time")
    @Temporal(value = TemporalType.TIME)
    private LocalTime startTime;

    @Column(name = "end_time")
    @Temporal(value = TemporalType.TIME)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToMany(mappedBy = "office")
    private List<Workplace> workplaces;
}
