package com.project.api.Class;

import com.project.api.Entity.Connection;
import com.project.api.Entity.Traveller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDTO {


    private List<Connection> connections;
    private List<Traveller> travellers;
    private String tripStatus;

}
