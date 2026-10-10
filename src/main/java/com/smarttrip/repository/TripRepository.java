
package com.smarttrip.repository;

import com.smarttrip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByOwnerId(Long ownerId);
    List<Trip> findByOwnerIdAndSavedTrue(Long ownerId);
    Optional<Trip> findByIdAndOwnerId(Long id, Long ownerId);
}
