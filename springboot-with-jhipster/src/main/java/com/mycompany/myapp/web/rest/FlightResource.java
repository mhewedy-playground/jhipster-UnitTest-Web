package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.FlightService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.service.dto.FlightDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Flight.
 */
@RestController
@RequestMapping("/api")
public class FlightResource {

    private final Logger log = LoggerFactory.getLogger(FlightResource.class);

    private static final String ENTITY_NAME = "flight";

    private final FlightService flightService;

    public FlightResource(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * POST  /flights : Create a new flight.
     *
     * @param flightDTO the flightDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new flightDTO, or with status 400 (Bad Request) if the flight has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/flights")
    @Timed
    public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightDTO flightDTO) throws URISyntaxException {
        log.debug("REST request to save Flight : {}", flightDTO);
        if (flightDTO.getId() != null) {
            throw new BadRequestAlertException("A new flight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlightDTO result = flightService.save(flightDTO);
        return ResponseEntity.created(new URI("/api/flights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flights : Updates an existing flight.
     *
     * @param flightDTO the flightDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated flightDTO,
     * or with status 400 (Bad Request) if the flightDTO is not valid,
     * or with status 500 (Internal Server Error) if the flightDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/flights")
    @Timed
    public ResponseEntity<FlightDTO> updateFlight(@RequestBody FlightDTO flightDTO) throws URISyntaxException {
        log.debug("REST request to update Flight : {}", flightDTO);
        if (flightDTO.getId() == null) {
            return createFlight(flightDTO);
        }
        FlightDTO result = flightService.save(flightDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, flightDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flights : get all the flights.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of flights in body
     */
    @GetMapping("/flights")
    @Timed
    public List<FlightDTO> getAllFlights() {
        log.debug("REST request to get all Flights");
        return flightService.findAll();
        }

    /**
     * GET  /flights/:id : get the "id" flight.
     *
     * @param id the id of the flightDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the flightDTO, or with status 404 (Not Found)
     */
    @GetMapping("/flights/{id}")
    @Timed
    public ResponseEntity<FlightDTO> getFlight(@PathVariable Long id) {
        log.debug("REST request to get Flight : {}", id);
        FlightDTO flightDTO = flightService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(flightDTO));
    }

    /**
     * DELETE  /flights/:id : delete the "id" flight.
     *
     * @param id the id of the flightDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/flights/{id}")
    @Timed
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        log.debug("REST request to delete Flight : {}", id);
        flightService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
