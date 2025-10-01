package com.project.api.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchParameters {
    private String departureCity;
    private String arrivalCity;
    private String departureTime;
    private String arrivalTime;
    private String trainType;
    private BigDecimal firstClassRate;
    private BigDecimal secondClassRate;
    private String sortBy;
    private String sortOrder;
    private int bitmapDays;
    private int duration;
}
