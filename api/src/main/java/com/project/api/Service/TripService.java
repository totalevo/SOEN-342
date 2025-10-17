package com.project.api.Service;

import com.project.api.Class.TripDTO;
import com.project.api.Entity.*;
import com.project.api.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TripService {
    @Autowired
    TravellerRepository travellerRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TripRepository tripRepository;

    public void createTrip(TripDTO  tripDTO) throws Exception {
        List<Traveller> travellers = tripDTO.getTravellers();
        List<Connection> connections = tripDTO.getConnections();
        List<Reservation> oldReservations= null;
        List<Reservation> newReservations= new ArrayList<>();
        if(travellers.isEmpty() || connections.isEmpty()){
            throw new Exception("Invalid list of traveller or connections");
        }
        Trip trip = new Trip();
        for(Traveller traveller:travellers){
            travellerRepository.findById(traveller.getId()).orElseGet(()->travellerRepository.save(traveller));
            oldReservations = reservationRepository.findAllByTravellerId(traveller.getId());
            if(!oldReservations.isEmpty()){
                for(Reservation reservation:oldReservations){
                    if(!isNewTrip(connections,reservation.getTrip().getConnections())){
                        throw new Exception("A traveller already has a reservation for this connection");
                    }
                }
            }
            Reservation reservation = new Reservation();
            reservation.setTraveller(traveller);
            reservation.setTrip(trip);

            Ticket ticket = new Ticket();
            ticket.setReservation(reservation);
            reservation.setTicket(ticket);

            newReservations.add(reservation);
        }

        trip.setConnections(connections);
        trip.setReservationList(newReservations);
        trip.setTripStatus("RESERVED");

        tripRepository.save(trip);



    }

    // Checkup that a traveller does not book the same connection twice
    public boolean isNewTrip(List<Connection> tripConnections, List<Connection> userConnections){
        if(tripConnections.size() == userConnections.size()){
            for(int i=0;i<userConnections.size();i++){
                if(!userConnections.get(i).getId().equals(tripConnections.get(i).getId())){
                    return true;
                }
            }
            return false;
        }
        else{
            return true;
        }
    }

    public List<TripDTO> searchTripsByTraveller(String travellerId, String lastName) {
        // Validate traveller
        Traveller traveller = travellerRepository.findById(travellerId)
                .orElseThrow(() -> new RuntimeException("Traveller not found with id: " + travellerId));

        // Verify last name matches
        if (!traveller.getName().equalsIgnoreCase(lastName)) {
            throw new RuntimeException("Traveller last name does not match ID");
        }

        // Find all reservations for this traveller
        List<Reservation> reservations = reservationRepository.findAllByTravellerId(travellerId);
        if (reservations.isEmpty()) {
            return new ArrayList<>();
        }

        // Collect unique trips from those reservations
        List<Trip> trips = reservations.stream()
                .map(Reservation::getTrip)
                .distinct()
                .collect(Collectors.toList());

        // Build TripDTOs (connections + travellers + status)
        List<TripDTO> tripDTOs = new ArrayList<>();
        for (Trip trip : trips) {
            TripDTO dto = new TripDTO();
            dto.setTripId(trip.getId());
            dto.setConnections(trip.getConnections());
            dto.setTravellers(trip.getReservationList()
                    .stream()
                    .map(Reservation::getTraveller)
                    .distinct()
                    .collect(Collectors.toList()));

            dto.setTripStatus(trip.getTripStatus());
            tripDTOs.add(dto);
        }
        return tripDTOs;
    }

    public TripDTO completeTrip(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found: " + tripId));

        if (!"COMPLETED".equalsIgnoreCase(trip.getTripStatus())) {
            trip.setTripStatus("COMPLETED");
            tripRepository.save(trip);
        }

        TripDTO dto = new TripDTO();
        dto.setTripId(trip.getId());                         // UUID
        dto.setConnections(trip.getConnections());
        dto.setTravellers(trip.getReservationList()
                .stream()
                .map(Reservation::getTraveller)
                .distinct()
                .collect(Collectors.toList()));
        dto.setTripStatus(trip.getTripStatus());
        return dto;
    }

}
