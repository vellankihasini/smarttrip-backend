
package com.smarttrip.repository;

import com.smarttrip.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItineraryRepository
        extends JpaRepository<Itinerary, Long> {

    List<Itinerary> findByTrip_IdAndTrip_Owner_Email(
            Long tripId, String email);
}
