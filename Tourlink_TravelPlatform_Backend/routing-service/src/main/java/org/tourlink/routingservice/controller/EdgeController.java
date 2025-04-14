package org.tourlink.routingservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.routingservice.dto.EdgeRequest;
import org.tourlink.routingservice.dto.EdgeResponse;

import java.util.List;

@RestController
@RequestMapping("/api/edges")
public class EdgeController {

    @GetMapping
    public ResponseEntity<List<EdgeResponse>> getEdgesByNode(@RequestParam String nodeId) {
        return null;
    }

    @PostMapping
    public ResponseEntity<EdgeResponse> addEdge(@RequestBody EdgeRequest edgeRequest) {
        return null;
    }
}

