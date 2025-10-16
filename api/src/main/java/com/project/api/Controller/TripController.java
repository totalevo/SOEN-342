package com.project.api.Controller;

import com.project.api.Class.TripDTO;
import com.project.api.Entity.Connection;
import com.project.api.Entity.Trip;
import com.project.api.Repository.TripRepository;
import com.project.api.Service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip")
@CrossOrigin(origins = "http://localhost:4200")
public class TripController {

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


}
