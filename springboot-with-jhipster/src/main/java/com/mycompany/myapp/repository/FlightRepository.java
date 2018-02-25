package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Flight;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Flight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

}
