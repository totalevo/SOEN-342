package com.project.api.Repository;

import com.project.api.Entity.Traveller;
import com.project.api.Entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TravellerRepository  extends JpaRepository<Traveller, String> {
}
