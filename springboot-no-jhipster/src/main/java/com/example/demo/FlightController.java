package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/flight")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FlightDto flightDto) {
        FlightDto flight = flightService.createFlight(flightDto);

        URI uri = fromCurrentRequest().path("/{id}")
                .buildAndExpand(flight.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
