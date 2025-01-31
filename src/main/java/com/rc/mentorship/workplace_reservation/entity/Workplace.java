package com.rc.mentorship.workplace_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workplace")
@Getter
@Setter
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "computer_present")
    @Accessors(fluent = true)
    private Boolean isComputerPresent;

    @Column(name = "available")
    @Accessors(fluent = true)
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    private Office office;

    @OneToMany(mappedBy = "workplace")
    private List<Reservation> reservations;

    public enum Type {
        DESK,
        ROOM
    }
}
