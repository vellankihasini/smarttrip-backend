
package com.smarttrip.controller;

import com.smarttrip.entity.Trip;
import com.smarttrip.service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    public record SaveRequest(boolean saved) {}

    @GetMapping
    public List<Trip> getTrips(Authentication auth) {
        return tripService.getTrips(auth.getName());
    }

    @GetMapping("/{id}")
    public Trip getTripById(
            Authentication auth,
            @PathVariable Long id) {
        return tripService.getOwnedTrip(auth.getName(), id);
    }

    @GetMapping("/saved")
    public List<Trip> getSavedTrips(Authentication auth) {
        return tripService.getSavedTrips(auth.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trip createTrip(
            Authentication auth,
            @RequestBody Trip trip) {
        return tripService.createTrip(auth.getName(), trip);
    }

    @PutMapping("/{id}")
    public Trip updateTrip(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody Trip trip) {
        return tripService.updateTrip(auth.getName(), id, trip);
    }

    @PatchMapping("/{id}/save")
    public Trip saveTrip(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody SaveRequest request) {
        return tripService.saveTrip(
                auth.getName(), id, request.saved());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrip(
            Authentication auth,
            @PathVariable Long id) {
        tripService.deleteTrip(auth.getName(), id);
    }
}
