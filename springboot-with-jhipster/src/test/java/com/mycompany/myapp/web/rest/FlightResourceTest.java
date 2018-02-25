package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.FlightService;
import com.mycompany.myapp.service.dto.FlightDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class FlightResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Test
    public void testCreate() throws Exception {

        FlightDTO expected = new FlightDTO();
        ReflectionTestUtils.setField(expected, "id", 1L);

        when(flightService.save(any(FlightDTO.class))).thenReturn(expected);

        FlightDTO flightDto = new FlightDTO();
        flightDto.setNumber("CAI-123400");

        this.mockMvc.perform(post("/api/flight")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flightDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", endsWith("/api/flight/1")));
    }

}
