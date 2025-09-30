package com.project.api.Controller;

import com.project.api.Entity.Connection;
import com.project.api.Entity.SearchParameters;
import com.project.api.Repository.ConnectionRepository;
import com.project.api.Service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
@CrossOrigin(origins = "http://localhost:4200")
public class ConnectionController {

    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadConnections(@RequestBody List<Connection> connections) {
        try {
            List<Connection> savedConnections = connectionRepository.saveAll(connections);
            return ResponseEntity.ok("Successfully saved " + savedConnections.size() + " connections");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving connections: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<Connection>> searchConnections(@RequestBody SearchParameters connection) {
        return ResponseEntity.ok(connectionService.searchConnections(connection));
    }
}