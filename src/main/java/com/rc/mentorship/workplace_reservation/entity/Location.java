package com.rc.mentorship.workplace_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "location", schema = "workplace_reservation")
@Getter
@Setter
public class Location extends EntityInMemory {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "location")
    private List<Office> offices;
}
