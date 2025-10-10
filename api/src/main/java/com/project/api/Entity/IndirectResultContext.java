package com.project.api.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IndirectResultContext {
    private List<Connection> connections;
    private int totalDuration;
    private int[] timeBetweenConnections;
    private int totalFirstClassRate;
    private int totalSecondClassRate;
}
