package com.project.api.Service;

import com.project.api.Entity.Connection;
import com.project.api.Entity.SearchParameters;
import com.project.api.Repository.ConnectionCustomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionService {
    private final ConnectionCustomRepository connectionCustomRepository;

    public ConnectionService(ConnectionCustomRepository connectionCustomRepository) {
        this.connectionCustomRepository = connectionCustomRepository;
    }

    public List<Connection> searchConnections(SearchParameters searchParameters) {
        return connectionCustomRepository.findConnectionsDynamically(searchParameters);
    }
}
