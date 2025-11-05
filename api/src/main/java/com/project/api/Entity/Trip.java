package com.project.api.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trip_id", nullable = false)
    private Integer id;

    @ManyToMany
    @JoinTable(
            name = "trip_connections",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    private List<Connection> connections;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservationList;

    @Column(name = "trip_status", nullable = false)
    private String tripStatus;
}
