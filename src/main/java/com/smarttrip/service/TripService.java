
package com.smarttrip.service;

import com.smarttrip.entity.Trip;
import com.smarttrip.entity.User;
import com.smarttrip.repository.TripRepository;
import com.smarttrip.repository.UserRepository;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripService(TripRepository tripRepository,
                       UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    private Trip getTrip(Long id, Long userId) {
        return tripRepository.findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trip not found"));
    }

    public List<Trip> getTrips(String email) {
        return tripRepository.findByOwnerId(getUser(email).getId());
    }

@GetMapping("/{id}")
public Trip getTripById(Authentication auth,
                        @PathVariable Long id) {
    TripService tripService = null;
    return tripService.getOwnedTrip(auth.name(), id);
}


    public List<Trip> getSavedTrips(String email) {
        return tripRepository.findByOwnerIdAndSavedTrue(
                getUser(email).getId());
    }

    public Trip createTrip(String email, Trip trip) {
        trip.setOwner(getUser(email));
        return tripRepository.save(trip);
    }

    public Trip updateTrip(String email, Long id, Trip updated) {
        User user = getUser(email);
        Trip trip = getTrip(id, user.getId());

        trip.setTitle(updated.getTitle());
        trip.setDestination(updated.getDestination());
        trip.setStartDate(updated.getStartDate());
        trip.setEndDate(updated.getEndDate());
        trip.setBudget(updated.getBudget());
        trip.setNotes(updated.getNotes());

        return tripRepository.save(trip);
    }

    public Trip saveTrip(String email, Long id, boolean saved) {
        Trip trip = getTrip(id, getUser(email).getId());
        trip.setSaved(saved);
        return tripRepository.save(trip);
    }

    public void deleteTrip(String email, Long id) {
        Trip trip = getTrip(id, getUser(email).getId());
        tripRepository.delete(trip);
    }

    public Trip getOwnedTrip(String email, Long id) {
        return getTrip(id, getUser(email).getId());
    }
}
