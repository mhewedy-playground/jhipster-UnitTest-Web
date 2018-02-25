package com.example.demo;

public class FlightDto {
    private Long id;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }


    public static FlightDto from(Flight flight) {
        FlightDto flightDto = new FlightDto();
        flightDto.id = flight.getId();
        flightDto.setNumber(flight.getNumber());
        return flightDto;
    }
}
