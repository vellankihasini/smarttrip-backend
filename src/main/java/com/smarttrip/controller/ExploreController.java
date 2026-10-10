
package com.smarttrip.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/explore")
public class ExploreController {

    public record Destination(
            String name, String country,
            String description, double suggestedBudget) {}

    @GetMapping
    public List<Destination> explore() {
        return List.of(
            new Destination("Paris", "France",
                "Art, cafes and iconic landmarks", 120000),
            new Destination("Tokyo", "Japan",
                "Modern city life and traditional culture", 150000),
            new Destination("Bali", "Indonesia",
                "Beaches, temples and tropical scenery", 65000),
            new Destination("Manali", "India",
                "Mountains, valleys and adventure", 18000),
            new Destination("Jaipur", "India",
                "Forts, palaces and colourful markets", 15000),
            new Destination("Goa", "India",
                "Beaches, food and coastal escapes", 20000)
        );
    }
}
