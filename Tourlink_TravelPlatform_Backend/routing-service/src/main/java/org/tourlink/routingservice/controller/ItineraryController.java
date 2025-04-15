package org.tourlink.routingservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.routingservice.dto.ItineraryAdjustmentRequest;
import org.tourlink.routingservice.dto.ItineraryRequest;
import org.tourlink.routingservice.dto.ItineraryResponse;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    @PostMapping("/plan")
    public ResponseEntity<ItineraryResponse> createItinerary(@RequestBody ItineraryRequest itineraryRequest) {
        return null;
    }

    @PutMapping("/{itineraryId}")
    public ResponseEntity<ItineraryResponse> updateItinerary(@PathVariable Long itineraryId, @RequestBody ItineraryRequest itineraryRequest) {
        return null;
    }

    @PostMapping("/adjust")
    public ResponseEntity<ItineraryResponse> adjustItinerary(@RequestBody ItineraryAdjustmentRequest adjustmentRequest) {
        return null;
    }
}

