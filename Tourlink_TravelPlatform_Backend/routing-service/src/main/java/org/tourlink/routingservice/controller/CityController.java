package org.tourlink.routingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tourlink.routingservice.dto.CityResponse;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {


    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {
        return null;
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable Integer cityId) {
        return null;
    }
}

