
package com.smarttrip.service;

import com.smarttrip.entity.Itinerary;
import com.smarttrip.repository.ItineraryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripService tripService;

    public ItineraryService(
            ItineraryRepository itineraryRepository,
            TripService tripService) {
        this.itineraryRepository = itineraryRepository;
        this.tripService = tripService;
    }

    public List<Itinerary> getItinerary(
            String email, Long tripId) {

        // Confirm this trip belongs to the logged-in user.
        tripService.getOwnedTrip(email, tripId);

        return itineraryRepository
                .findByTrip_IdAndTrip_Owner_Email(
                        tripId, email);
    }
}
