package com.example.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class FlightControllerIntTest {

    @Autowired
    private FlightService flightService;

    private MockMvc mockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FlightController flightController = new FlightController(flightService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(flightController)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {

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
