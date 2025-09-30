package com.project.api.Service;

import com.project.api.Entity.Connection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectionService {
    int HOURS_IN_A_DAY=24;
    int MINUTES_IN_AN_HOUR=60;

    public List<Connection>  returnConnectionsWithDurationCalculated(List<Connection> connections) {
        return connections.stream().map(connection -> {
            String arrivalTime = connection.getArrivalTime();
            if (arrivalTime.contains("(+1d)")) {
                arrivalTime = arrivalTime.replaceAll("\\(\\+1d(ay)?\\)", "").trim();
            }
            String [] departureParts = connection.getDepartureTime().split(":");
            String [] arrivalParts = arrivalTime.split(":");

            int totalMinutes = getTotalMinutes(departureParts, arrivalParts);

            connection.setDurationMinutes(totalMinutes);
            connection.setArrivalTime(arrivalTime);
            return connection;
        }).collect(Collectors.toList());



    }

    private int getTotalMinutes(String[] departureParts, String[] arrivalParts) {
        int departureHours = Integer.parseInt(departureParts[0]);
        int departureMinutes = Integer.parseInt(departureParts[1]);
        int arrivalHours = Integer.parseInt(arrivalParts[0]);
        int arrivalMinutes = Integer.parseInt(arrivalParts[1]);
        int totalMinutes=0;
        if(arrivalHours< departureHours){
            totalMinutes= (((HOURS_IN_A_DAY+arrivalHours)*MINUTES_IN_AN_HOUR)+arrivalMinutes)-(departureHours*MINUTES_IN_AN_HOUR+departureMinutes);
        }
        else{
            totalMinutes= (arrivalHours*MINUTES_IN_AN_HOUR+arrivalMinutes) - (departureHours*MINUTES_IN_AN_HOUR+departureMinutes);
        }
        return totalMinutes;
    }
}
