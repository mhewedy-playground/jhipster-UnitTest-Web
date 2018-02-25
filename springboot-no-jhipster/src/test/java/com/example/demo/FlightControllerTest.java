package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class FlightControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Test
    public void testCreate() throws Exception {

        FlightDto expected = new FlightDto();
        ReflectionTestUtils.setField(expected, "id", 1L);

        when(flightService.createFlight(any(FlightDto.class))).thenReturn(expected);

        FlightDto flightDto = new FlightDto();
        flightDto.setNumber("CAI-123400");

        this.mockMvc.perform(post("/api/flight")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/flight/1")));
    }

}
