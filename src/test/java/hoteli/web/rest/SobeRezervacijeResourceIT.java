package hoteli.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hoteli.IntegrationTest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SobeRezervacijeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SobeRezervacijeResourceIT {

    private static final Integer DEFAULT_BROJ_SOBE = 1;
    private static final Integer UPDATED_BROJ_SOBE = 2;
    private static final Integer SMALLER_BROJ_SOBE = 1 - 1;

    private static final Double DEFAULT_CIJENA = 1D;
    private static final Double UPDATED_CIJENA = 2D;
    private static final Double SMALLER_CIJENA = 1D - 1D;

    private static final LocalDate DEFAULT_DATUM_DOLASKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_DOLASKA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATUM_DOLASKA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATUM_ODLASKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_ODLASKA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATUM_ODLASKA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/sobe-rezervacijes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SobeRezervacijeRepository sobeRezervacijeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSobeRezervacijeMockMvc;

    private SobeRezervacije sobeRezervacije;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SobeRezervacije createEntity(EntityManager em) {
        SobeRezervacije sobeRezervacije = new SobeRezervacije()
            .brojSobe(DEFAULT_BROJ_SOBE)
            .cijena(DEFAULT_CIJENA)
            .datumDolaska(DEFAULT_DATUM_DOLASKA)
            .datumOdlaska(DEFAULT_DATUM_ODLASKA);
        return sobeRezervacije;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SobeRezervacije createUpdatedEntity(EntityManager em) {
        SobeRezervacije sobeRezervacije = new SobeRezervacije()
            .brojSobe(UPDATED_BROJ_SOBE)
            .cijena(UPDATED_CIJENA)
            .datumDolaska(UPDATED_DATUM_DOLASKA)
            .datumOdlaska(UPDATED_DATUM_ODLASKA);
        return sobeRezervacije;
    }

    @BeforeEach
    public void initTest() {
        sobeRezervacije = createEntity(em);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijes() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList
        restSobeRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sobeRezervacije.getId().intValue())))
            .andExpect(jsonPath("$.[*].brojSobe").value(hasItem(DEFAULT_BROJ_SOBE)))
            .andExpect(jsonPath("$.[*].cijena").value(hasItem(DEFAULT_CIJENA.doubleValue())))
            .andExpect(jsonPath("$.[*].datumDolaska").value(hasItem(DEFAULT_DATUM_DOLASKA.toString())))
            .andExpect(jsonPath("$.[*].datumOdlaska").value(hasItem(DEFAULT_DATUM_ODLASKA.toString())));
    }

    @Test
    @Transactional
    void getSobeRezervacije() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get the sobeRezervacije
        restSobeRezervacijeMockMvc
            .perform(get(ENTITY_API_URL_ID, sobeRezervacije.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sobeRezervacije.getId().intValue()))
            .andExpect(jsonPath("$.brojSobe").value(DEFAULT_BROJ_SOBE))
            .andExpect(jsonPath("$.cijena").value(DEFAULT_CIJENA.doubleValue()))
            .andExpect(jsonPath("$.datumDolaska").value(DEFAULT_DATUM_DOLASKA.toString()))
            .andExpect(jsonPath("$.datumOdlaska").value(DEFAULT_DATUM_ODLASKA.toString()));
    }

    @Test
    @Transactional
    void getSobeRezervacijesByIdFiltering() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        Long id = sobeRezervacije.getId();

        defaultSobeRezervacijeShouldBeFound("id.equals=" + id);
        defaultSobeRezervacijeShouldNotBeFound("id.notEquals=" + id);

        defaultSobeRezervacijeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSobeRezervacijeShouldNotBeFound("id.greaterThan=" + id);

        defaultSobeRezervacijeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSobeRezervacijeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByBrojSobeIsEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where brojSobe equals to DEFAULT_BROJ_SOBE
        defaultSobeRezervacijeShouldBeFound("brojSobe.equals=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeRezervacijeList where brojSobe equals to UPDATED_BROJ_SOBE
        defaultSobeRezervacijeShouldNotBeFound("brojSobe.equals=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByBrojSobeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where brojSobe not equals to DEFAULT_BROJ_SOBE
        defaultSobeRezervacijeShouldNotBeFound("brojSobe.notEquals=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeRezervacijeList where brojSobe not equals to UPDATED_BROJ_SOBE
        defaultSobeRezervacijeShouldBeFound("brojSobe.notEquals=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByBrojSobeIsInShouldWork() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where brojSobe in DEFAULT_BROJ_SOBE or UPDATED_BROJ_SOBE
        defaultSobeRezervacijeShouldBeFound("brojSobe.in=" + DEFAULT_BROJ_SOBE + "," + UPDATED_BROJ_SOBE);

        // Get all the sobeRezervacijeList where brojSobe equals to UPDATED_BROJ_SOBE
        defaultSobeRezervacijeShouldNotBeFound("brojSobe.in=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByBrojSobeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where brojSobe is not null
        defaultSobeRezervacijeShouldBeFound("brojSobe.specified=true");

        // Get all the sobeRezervacijeList where brojSobe is null
        defaultSobeRezervacijeShouldNotBeFound("brojSobe.specified=false");
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByBrojSobeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where brojSobe is greater than or equal to DEFAULT_BROJ_SOBE
        defaultSobeRezervacijeShouldBeFound("brojSobe.greaterThanOrEqual=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeRezervacijeList where brojSobe is greater than or equal to UPDATED_BROJ_SOBE
        defaultSobeRezervacijeShouldNotBeFound("brojSobe.greaterThanOrEqual=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByBrojSobeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where brojSobe is less than or equal to DEFAULT_BROJ_SOBE
        defaultSobeRezervacijeShouldBeFound("brojSobe.lessThanOrEqual=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeRezervacijeList where brojSobe is less than or equal to SMALLER_BROJ_SOBE
        defaultSobeRezervacijeShouldNotBeFound("brojSobe.lessThanOrEqual=" + SMALLER_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByBrojSobeIsLessThanSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where brojSobe is less than DEFAULT_BROJ_SOBE
        defaultSobeRezervacijeShouldNotBeFound("brojSobe.lessThan=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeRezervacijeList where brojSobe is less than UPDATED_BROJ_SOBE
        defaultSobeRezervacijeShouldBeFound("brojSobe.lessThan=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByBrojSobeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where brojSobe is greater than DEFAULT_BROJ_SOBE
        defaultSobeRezervacijeShouldNotBeFound("brojSobe.greaterThan=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeRezervacijeList where brojSobe is greater than SMALLER_BROJ_SOBE
        defaultSobeRezervacijeShouldBeFound("brojSobe.greaterThan=" + SMALLER_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByCijenaIsEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where cijena equals to DEFAULT_CIJENA
        defaultSobeRezervacijeShouldBeFound("cijena.equals=" + DEFAULT_CIJENA);

        // Get all the sobeRezervacijeList where cijena equals to UPDATED_CIJENA
        defaultSobeRezervacijeShouldNotBeFound("cijena.equals=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByCijenaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where cijena not equals to DEFAULT_CIJENA
        defaultSobeRezervacijeShouldNotBeFound("cijena.notEquals=" + DEFAULT_CIJENA);

        // Get all the sobeRezervacijeList where cijena not equals to UPDATED_CIJENA
        defaultSobeRezervacijeShouldBeFound("cijena.notEquals=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByCijenaIsInShouldWork() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where cijena in DEFAULT_CIJENA or UPDATED_CIJENA
        defaultSobeRezervacijeShouldBeFound("cijena.in=" + DEFAULT_CIJENA + "," + UPDATED_CIJENA);

        // Get all the sobeRezervacijeList where cijena equals to UPDATED_CIJENA
        defaultSobeRezervacijeShouldNotBeFound("cijena.in=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByCijenaIsNullOrNotNull() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where cijena is not null
        defaultSobeRezervacijeShouldBeFound("cijena.specified=true");

        // Get all the sobeRezervacijeList where cijena is null
        defaultSobeRezervacijeShouldNotBeFound("cijena.specified=false");
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByCijenaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where cijena is greater than or equal to DEFAULT_CIJENA
        defaultSobeRezervacijeShouldBeFound("cijena.greaterThanOrEqual=" + DEFAULT_CIJENA);

        // Get all the sobeRezervacijeList where cijena is greater than or equal to UPDATED_CIJENA
        defaultSobeRezervacijeShouldNotBeFound("cijena.greaterThanOrEqual=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByCijenaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where cijena is less than or equal to DEFAULT_CIJENA
        defaultSobeRezervacijeShouldBeFound("cijena.lessThanOrEqual=" + DEFAULT_CIJENA);

        // Get all the sobeRezervacijeList where cijena is less than or equal to SMALLER_CIJENA
        defaultSobeRezervacijeShouldNotBeFound("cijena.lessThanOrEqual=" + SMALLER_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByCijenaIsLessThanSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where cijena is less than DEFAULT_CIJENA
        defaultSobeRezervacijeShouldNotBeFound("cijena.lessThan=" + DEFAULT_CIJENA);

        // Get all the sobeRezervacijeList where cijena is less than UPDATED_CIJENA
        defaultSobeRezervacijeShouldBeFound("cijena.lessThan=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByCijenaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where cijena is greater than DEFAULT_CIJENA
        defaultSobeRezervacijeShouldNotBeFound("cijena.greaterThan=" + DEFAULT_CIJENA);

        // Get all the sobeRezervacijeList where cijena is greater than SMALLER_CIJENA
        defaultSobeRezervacijeShouldBeFound("cijena.greaterThan=" + SMALLER_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumDolaskaIsEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumDolaska equals to DEFAULT_DATUM_DOLASKA
        defaultSobeRezervacijeShouldBeFound("datumDolaska.equals=" + DEFAULT_DATUM_DOLASKA);

        // Get all the sobeRezervacijeList where datumDolaska equals to UPDATED_DATUM_DOLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumDolaska.equals=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumDolaskaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumDolaska not equals to DEFAULT_DATUM_DOLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumDolaska.notEquals=" + DEFAULT_DATUM_DOLASKA);

        // Get all the sobeRezervacijeList where datumDolaska not equals to UPDATED_DATUM_DOLASKA
        defaultSobeRezervacijeShouldBeFound("datumDolaska.notEquals=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumDolaskaIsInShouldWork() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumDolaska in DEFAULT_DATUM_DOLASKA or UPDATED_DATUM_DOLASKA
        defaultSobeRezervacijeShouldBeFound("datumDolaska.in=" + DEFAULT_DATUM_DOLASKA + "," + UPDATED_DATUM_DOLASKA);

        // Get all the sobeRezervacijeList where datumDolaska equals to UPDATED_DATUM_DOLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumDolaska.in=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumDolaskaIsNullOrNotNull() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumDolaska is not null
        defaultSobeRezervacijeShouldBeFound("datumDolaska.specified=true");

        // Get all the sobeRezervacijeList where datumDolaska is null
        defaultSobeRezervacijeShouldNotBeFound("datumDolaska.specified=false");
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumDolaskaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumDolaska is greater than or equal to DEFAULT_DATUM_DOLASKA
        defaultSobeRezervacijeShouldBeFound("datumDolaska.greaterThanOrEqual=" + DEFAULT_DATUM_DOLASKA);

        // Get all the sobeRezervacijeList where datumDolaska is greater than or equal to UPDATED_DATUM_DOLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumDolaska.greaterThanOrEqual=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumDolaskaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumDolaska is less than or equal to DEFAULT_DATUM_DOLASKA
        defaultSobeRezervacijeShouldBeFound("datumDolaska.lessThanOrEqual=" + DEFAULT_DATUM_DOLASKA);

        // Get all the sobeRezervacijeList where datumDolaska is less than or equal to SMALLER_DATUM_DOLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumDolaska.lessThanOrEqual=" + SMALLER_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumDolaskaIsLessThanSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumDolaska is less than DEFAULT_DATUM_DOLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumDolaska.lessThan=" + DEFAULT_DATUM_DOLASKA);

        // Get all the sobeRezervacijeList where datumDolaska is less than UPDATED_DATUM_DOLASKA
        defaultSobeRezervacijeShouldBeFound("datumDolaska.lessThan=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumDolaskaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumDolaska is greater than DEFAULT_DATUM_DOLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumDolaska.greaterThan=" + DEFAULT_DATUM_DOLASKA);

        // Get all the sobeRezervacijeList where datumDolaska is greater than SMALLER_DATUM_DOLASKA
        defaultSobeRezervacijeShouldBeFound("datumDolaska.greaterThan=" + SMALLER_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumOdlaskaIsEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumOdlaska equals to DEFAULT_DATUM_ODLASKA
        defaultSobeRezervacijeShouldBeFound("datumOdlaska.equals=" + DEFAULT_DATUM_ODLASKA);

        // Get all the sobeRezervacijeList where datumOdlaska equals to UPDATED_DATUM_ODLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumOdlaska.equals=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumOdlaskaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumOdlaska not equals to DEFAULT_DATUM_ODLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumOdlaska.notEquals=" + DEFAULT_DATUM_ODLASKA);

        // Get all the sobeRezervacijeList where datumOdlaska not equals to UPDATED_DATUM_ODLASKA
        defaultSobeRezervacijeShouldBeFound("datumOdlaska.notEquals=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumOdlaskaIsInShouldWork() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumOdlaska in DEFAULT_DATUM_ODLASKA or UPDATED_DATUM_ODLASKA
        defaultSobeRezervacijeShouldBeFound("datumOdlaska.in=" + DEFAULT_DATUM_ODLASKA + "," + UPDATED_DATUM_ODLASKA);

        // Get all the sobeRezervacijeList where datumOdlaska equals to UPDATED_DATUM_ODLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumOdlaska.in=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumOdlaskaIsNullOrNotNull() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumOdlaska is not null
        defaultSobeRezervacijeShouldBeFound("datumOdlaska.specified=true");

        // Get all the sobeRezervacijeList where datumOdlaska is null
        defaultSobeRezervacijeShouldNotBeFound("datumOdlaska.specified=false");
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumOdlaskaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumOdlaska is greater than or equal to DEFAULT_DATUM_ODLASKA
        defaultSobeRezervacijeShouldBeFound("datumOdlaska.greaterThanOrEqual=" + DEFAULT_DATUM_ODLASKA);

        // Get all the sobeRezervacijeList where datumOdlaska is greater than or equal to UPDATED_DATUM_ODLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumOdlaska.greaterThanOrEqual=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumOdlaskaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumOdlaska is less than or equal to DEFAULT_DATUM_ODLASKA
        defaultSobeRezervacijeShouldBeFound("datumOdlaska.lessThanOrEqual=" + DEFAULT_DATUM_ODLASKA);

        // Get all the sobeRezervacijeList where datumOdlaska is less than or equal to SMALLER_DATUM_ODLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumOdlaska.lessThanOrEqual=" + SMALLER_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumOdlaskaIsLessThanSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumOdlaska is less than DEFAULT_DATUM_ODLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumOdlaska.lessThan=" + DEFAULT_DATUM_ODLASKA);

        // Get all the sobeRezervacijeList where datumOdlaska is less than UPDATED_DATUM_ODLASKA
        defaultSobeRezervacijeShouldBeFound("datumOdlaska.lessThan=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllSobeRezervacijesByDatumOdlaskaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sobeRezervacijeRepository.saveAndFlush(sobeRezervacije);

        // Get all the sobeRezervacijeList where datumOdlaska is greater than DEFAULT_DATUM_ODLASKA
        defaultSobeRezervacijeShouldNotBeFound("datumOdlaska.greaterThan=" + DEFAULT_DATUM_ODLASKA);

        // Get all the sobeRezervacijeList where datumOdlaska is greater than SMALLER_DATUM_ODLASKA
        defaultSobeRezervacijeShouldBeFound("datumOdlaska.greaterThan=" + SMALLER_DATUM_ODLASKA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSobeRezervacijeShouldBeFound(String filter) throws Exception {
        restSobeRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sobeRezervacije.getId().intValue())))
            .andExpect(jsonPath("$.[*].brojSobe").value(hasItem(DEFAULT_BROJ_SOBE)))
            .andExpect(jsonPath("$.[*].cijena").value(hasItem(DEFAULT_CIJENA.doubleValue())))
            .andExpect(jsonPath("$.[*].datumDolaska").value(hasItem(DEFAULT_DATUM_DOLASKA.toString())))
            .andExpect(jsonPath("$.[*].datumOdlaska").value(hasItem(DEFAULT_DATUM_ODLASKA.toString())));

        // Check, that the count call also returns 1
        restSobeRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSobeRezervacijeShouldNotBeFound(String filter) throws Exception {
        restSobeRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSobeRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSobeRezervacije() throws Exception {
        // Get the sobeRezervacije
        restSobeRezervacijeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
