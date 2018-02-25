package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.FlightDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Flight and its DTO FlightDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FlightMapper extends EntityMapper<FlightDTO, Flight> {



    default Flight fromId(Long id) {
        if (id == null) {
            return null;
        }
        Flight flight = new Flight();
        flight.setId(id);
        return flight;
    }
}
