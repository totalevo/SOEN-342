package com.project.api.Class;

import com.project.api.Entity.Connection;
import com.project.api.Entity.Traveller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDTO {

    private UUID tripId;
    private List<Connection> connections;
    private List<Traveller> travellers;
    private String tripStatus;

}
