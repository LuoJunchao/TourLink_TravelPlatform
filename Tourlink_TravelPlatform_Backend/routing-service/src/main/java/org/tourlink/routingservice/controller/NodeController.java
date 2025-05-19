package org.tourlink.routingservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.routingservice.dto.NodeRequest;
import org.tourlink.routingservice.dto.NodeResponse;

import java.util.List;

@RestController
@RequestMapping("/api/nodes")
public class NodeController {

    @GetMapping
    public ResponseEntity<List<NodeResponse>> getNodesByCity(@RequestParam Integer cityId) {
        return null;
    }

    @GetMapping("/{nodeId}")
    public ResponseEntity<NodeResponse> getNodeById(@PathVariable String nodeId) {
        return null;
    }

    @PostMapping
    public ResponseEntity<NodeResponse> addNode(@RequestBody NodeRequest nodeRequest) {
        return null;
    }
}

