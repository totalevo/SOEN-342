package com.project.api.Service;

import com.project.api.Class.TripDTO;
import com.project.api.Entity.*;
import com.project.api.Repository.ReservationRepository;
import com.project.api.Repository.TicketRepository;
import com.project.api.Repository.TravellerRepository;
import com.project.api.Repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
    public boolean isNewTrip(List<Connection> tripConnections, List<Connection> userConnections){
        if(tripConnections.size()== userConnections.size()){
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
}
