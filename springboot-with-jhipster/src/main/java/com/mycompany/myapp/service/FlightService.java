package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Flight;
import com.mycompany.myapp.repository.FlightRepository;
import com.mycompany.myapp.service.dto.FlightDTO;
import com.mycompany.myapp.service.mapper.FlightMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Flight.
 */
@Service
@Transactional
public class FlightService {

    private final Logger log = LoggerFactory.getLogger(FlightService.class);

    private final FlightRepository flightRepository;

    private final FlightMapper flightMapper;

    public FlightService(FlightRepository flightRepository, FlightMapper flightMapper) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
    }

    /**
     * Save a flight.
     *
     * @param flightDTO the entity to save
     * @return the persisted entity
     */
    public FlightDTO save(FlightDTO flightDTO) {
        log.debug("Request to save Flight : {}", flightDTO);
        Flight flight = flightMapper.toEntity(flightDTO);
        flight = flightRepository.save(flight);
        return flightMapper.toDto(flight);
    }

    /**
     * Get all the flights.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FlightDTO> findAll() {
        log.debug("Request to get all Flights");
        return flightRepository.findAll().stream()
            .map(flightMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one flight by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FlightDTO findOne(Long id) {
        log.debug("Request to get Flight : {}", id);
        Flight flight = flightRepository.findOne(id);
        return flightMapper.toDto(flight);
    }

    /**
     * Delete the flight by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Flight : {}", id);
        flightRepository.delete(id);
    }
}
