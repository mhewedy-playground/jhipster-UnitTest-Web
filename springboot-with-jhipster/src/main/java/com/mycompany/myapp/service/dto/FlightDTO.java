package com.mycompany.myapp.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Flight entity.
 */
public class FlightDTO implements Serializable {

    private Long id;

    private String number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FlightDTO flightDTO = (FlightDTO) o;
        if(flightDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), flightDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FlightDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            "}";
    }
}
