package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhUnittestRestApp;

import com.mycompany.myapp.domain.Flight;
import com.mycompany.myapp.repository.FlightRepository;
import com.mycompany.myapp.service.FlightService;
import com.mycompany.myapp.service.dto.FlightDTO;
import com.mycompany.myapp.service.mapper.FlightMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FlightResource REST controller.
 *
 * @see FlightResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhUnittestRestApp.class)
public class FlightResourceIntTest {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private FlightService flightService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFlightMockMvc;

    private Flight flight;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FlightResource flightResource = new FlightResource(flightService);
        this.restFlightMockMvc = MockMvcBuilders.standaloneSetup(flightResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createEntity(EntityManager em) {
        Flight flight = new Flight()
            .number(DEFAULT_NUMBER);
        return flight;
    }

    @Before
    public void initTest() {
        flight = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlight() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // Create the Flight
        FlightDTO flightDTO = flightMapper.toDto(flight);
        restFlightMockMvc.perform(post("/api/flights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flightDTO)))
            .andExpect(status().isCreated());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate + 1);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void createFlightWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // Create the Flight with an existing ID
        flight.setId(1L);
        FlightDTO flightDTO = flightMapper.toDto(flight);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlightMockMvc.perform(post("/api/flights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flightDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFlights() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList
        restFlightMockMvc.perform(get("/api/flights?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flight.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", flight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(flight.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFlight() throws Exception {
        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Update the flight
        Flight updatedFlight = flightRepository.findOne(flight.getId());
        // Disconnect from session so that the updates on updatedFlight are not directly saved in db
        em.detach(updatedFlight);
        updatedFlight
            .number(UPDATED_NUMBER);
        FlightDTO flightDTO = flightMapper.toDto(updatedFlight);

        restFlightMockMvc.perform(put("/api/flights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flightDTO)))
            .andExpect(status().isOk());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Create the Flight
        FlightDTO flightDTO = flightMapper.toDto(flight);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFlightMockMvc.perform(put("/api/flights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flightDTO)))
            .andExpect(status().isCreated());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        int databaseSizeBeforeDelete = flightRepository.findAll().size();

        // Get the flight
        restFlightMockMvc.perform(delete("/api/flights/{id}", flight.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Flight.class);
        Flight flight1 = new Flight();
        flight1.setId(1L);
        Flight flight2 = new Flight();
        flight2.setId(flight1.getId());
        assertThat(flight1).isEqualTo(flight2);
        flight2.setId(2L);
        assertThat(flight1).isNotEqualTo(flight2);
        flight1.setId(null);
        assertThat(flight1).isNotEqualTo(flight2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlightDTO.class);
        FlightDTO flightDTO1 = new FlightDTO();
        flightDTO1.setId(1L);
        FlightDTO flightDTO2 = new FlightDTO();
        assertThat(flightDTO1).isNotEqualTo(flightDTO2);
        flightDTO2.setId(flightDTO1.getId());
        assertThat(flightDTO1).isEqualTo(flightDTO2);
        flightDTO2.setId(2L);
        assertThat(flightDTO1).isNotEqualTo(flightDTO2);
        flightDTO1.setId(null);
        assertThat(flightDTO1).isNotEqualTo(flightDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(flightMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(flightMapper.fromId(null)).isNull();
    }
}
