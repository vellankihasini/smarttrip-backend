
package com.smarttrip.controller;

import com.smarttrip.entity.Itinerary;
import com.smarttrip.service.ItineraryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(
            ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping
    public List<Itinerary> getItinerary(
            Authentication auth,
            @PathVariable Long tripId) {

        return itineraryService.getItinerary(
                auth.getName(), tripId);
    }
}
