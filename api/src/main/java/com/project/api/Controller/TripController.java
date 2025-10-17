package com.project.api.Controller;

import com.project.api.Class.TripDTO;
import com.project.api.Class.TripSearchRequest;
import com.project.api.Service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/trip")
@CrossOrigin(origins = "http://localhost:4200")
public class TripController {

    private static final Logger log = LoggerFactory.getLogger(TripController.class);

    @Autowired
    TripService tripService;

    @PostMapping()
    public ResponseEntity<String> createTrip(@RequestBody TripDTO tripDTO) {
        try {
            tripService.createTrip(tripDTO);

            return ResponseEntity.ok("Successfully created trip");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating trip: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<TripDTO>> searchTripsByTraveller(@RequestBody TripSearchRequest request) {
        log.info("Search trips request: id='{}', name='{}'", request.getId(), request.getName());
        List<TripDTO> trips = tripService.searchTripsByTraveller(request.getId(), request.getName());
        log.info("Search trips result count: {}", trips.size());
        return ResponseEntity.ok(trips);
    }


}
