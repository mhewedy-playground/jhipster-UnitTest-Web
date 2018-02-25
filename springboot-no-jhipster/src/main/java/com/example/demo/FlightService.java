package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public FlightDto createFlight(FlightDto flightDto) {
        Flight flight = new Flight();
        flight.setNumber(flightDto.getNumber());
        flight = flightRepository.save(flight);
        return FlightDto.from(flight);
    }
}
