package com.project.api.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "connections")
public class Connection {

    @Id
    @Column(name = "connection_id", nullable = false)
    private String connectionId;

    @Column(name = "departure_city", nullable = false)
    private String departureCity;

    @Column(name = "arrival_city", nullable = false)
    private String arrivalCity;

    @Column(name = "departure_time", nullable = false)
    private String departureTime;

    @Column(name = "arrival_time", nullable = false)
    private String arrivalTime;

    @Column(name = "train_type", nullable = false)
    private String trainType;

    @Column(name = "days_of_operation", nullable = false)
    private String daysOfOperation;

    @Column(name = "first_class_rate", nullable = false)
    private BigDecimal firstClassRate;

    @Column(name = "second_class_rate", nullable = false)
    private BigDecimal secondClassRate;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

}