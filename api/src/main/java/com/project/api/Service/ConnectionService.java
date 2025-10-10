package com.project.api.Service;

import com.project.api.Class.ConnectionDuration;
import com.project.api.Class.DaysBitMap;
import com.project.api.Entity.Connection;
import com.project.api.Entity.IndirectResultContext;
import com.project.api.Entity.SearchParameters;
import com.project.api.Repository.ConnectionCustomRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectionService {
    private final ConnectionCustomRepository connectionCustomRepository;

    String[] DAYS_OF_THE_WEEK={"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    public ConnectionService(ConnectionCustomRepository connectionCustomRepository) {
        this.connectionCustomRepository = connectionCustomRepository;
    }

    public List<Connection> processAndCleanUpRawConnections(List<Connection> connections){
        List<Connection> cleanedConnections= storingBitMapForDaysOfOperation(connections);
        cleanedConnections = returnConnectionsWithDurationCalculated(connections);
        return cleanedConnections;
    }

    public List<Connection> storingBitMapForDaysOfOperation(List<Connection> connections){
        return connections.stream().map(connection -> {
            int bitmap = DaysBitMap.convertToBitmap(connection.getDaysOfOperation());
            connection.setBitmapDaysOfOperation(bitmap);
            return connection;
        }).collect(Collectors.toList());
    }

    public List<Connection>  returnConnectionsWithDurationCalculated(List<Connection> connections) {
        return connections.stream().map(connection -> {
            String arrivalTime = connection.getArrivalTime();
            if (arrivalTime.contains("(+1d)")) {
                arrivalTime = arrivalTime.replaceAll("\\(\\+1d(ay)?\\)", "").trim();
            }
            String [] departureParts = connection.getDepartureTime().split(":");
            String [] arrivalParts = arrivalTime.split(":");

            int totalMinutes = ConnectionDuration.getTotalMinutes(departureParts, arrivalParts);

            connection.setDurationMinutes(totalMinutes);
            return connection;
        }).collect(Collectors.toList());
    }

    public List<Connection> searchConnections(SearchParameters searchParameters) {
        return connectionCustomRepository.findConnectionsDynamically(searchParameters);
    }

    // CHANGED: return type for indirect connections
    public List<List<IndirectResultContext>> searchIndirectConnections(SearchParameters searchParameters) {
        return connectionCustomRepository.findIndirectConnections(searchParameters);
    }
}
