package hoteli.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hoteli.IntegrationTest;
import hoteli.domain.Rezervacije;
import hoteli.repository.RezervacijeRepository;
import hoteli.service.criteria.RezervacijeCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
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
 * Integration tests for the {@link RezervacijeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RezervacijeResourceIT {

    private static final Integer DEFAULT_BROJ_SOBE = 1;
    private static final Integer UPDATED_BROJ_SOBE = 2;
    private static final Integer SMALLER_BROJ_SOBE = 1 - 1;

    private static final LocalDate DEFAULT_DATUM_DOLASKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_DOLASKA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATUM_DOLASKA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATUM_ODLASKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_ODLASKA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATUM_ODLASKA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/rezervacijes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RezervacijeRepository rezervacijeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRezervacijeMockMvc;

    private Rezervacije rezervacije;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rezervacije createEntity(EntityManager em) {
        Rezervacije rezervacije = new Rezervacije()
            .brojSobe(DEFAULT_BROJ_SOBE)
            .datumDolaska(DEFAULT_DATUM_DOLASKA)
            .datumOdlaska(DEFAULT_DATUM_ODLASKA);
        return rezervacije;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rezervacije createUpdatedEntity(EntityManager em) {
        Rezervacije rezervacije = new Rezervacije()
            .brojSobe(UPDATED_BROJ_SOBE)
            .datumDolaska(UPDATED_DATUM_DOLASKA)
            .datumOdlaska(UPDATED_DATUM_ODLASKA);
        return rezervacije;
    }

    @BeforeEach
    public void initTest() {
        rezervacije = createEntity(em);
    }

    @Test
    @Transactional
    void createRezervacije() throws Exception {
        int databaseSizeBeforeCreate = rezervacijeRepository.findAll().size();
        // Create the Rezervacije
        restRezervacijeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rezervacije)))
            .andExpect(status().isCreated());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeCreate + 1);
        Rezervacije testRezervacije = rezervacijeList.get(rezervacijeList.size() - 1);
        assertThat(testRezervacije.getBrojSobe()).isEqualTo(DEFAULT_BROJ_SOBE);
        assertThat(testRezervacije.getDatumDolaska()).isEqualTo(DEFAULT_DATUM_DOLASKA);
        assertThat(testRezervacije.getDatumOdlaska()).isEqualTo(DEFAULT_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void createRezervacijeWithExistingId() throws Exception {
        // Create the Rezervacije with an existing ID
        rezervacije.setId(1L);

        int databaseSizeBeforeCreate = rezervacijeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRezervacijeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rezervacije)))
            .andExpect(status().isBadRequest());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrojSobeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rezervacijeRepository.findAll().size();
        // set the field null
        rezervacije.setBrojSobe(null);

        // Create the Rezervacije, which fails.

        restRezervacijeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rezervacije)))
            .andExpect(status().isBadRequest());

        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRezervacijes() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList
        restRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rezervacije.getId().intValue())))
            .andExpect(jsonPath("$.[*].brojSobe").value(hasItem(DEFAULT_BROJ_SOBE)))
            .andExpect(jsonPath("$.[*].datumDolaska").value(hasItem(DEFAULT_DATUM_DOLASKA.toString())))
            .andExpect(jsonPath("$.[*].datumOdlaska").value(hasItem(DEFAULT_DATUM_ODLASKA.toString())));
    }

    @Test
    @Transactional
    void getRezervacije() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get the rezervacije
        restRezervacijeMockMvc
            .perform(get(ENTITY_API_URL_ID, rezervacije.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rezervacije.getId().intValue()))
            .andExpect(jsonPath("$.brojSobe").value(DEFAULT_BROJ_SOBE))
            .andExpect(jsonPath("$.datumDolaska").value(DEFAULT_DATUM_DOLASKA.toString()))
            .andExpect(jsonPath("$.datumOdlaska").value(DEFAULT_DATUM_ODLASKA.toString()));
    }

    @Test
    @Transactional
    void getRezervacijesByIdFiltering() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        Long id = rezervacije.getId();

        defaultRezervacijeShouldBeFound("id.equals=" + id);
        defaultRezervacijeShouldNotBeFound("id.notEquals=" + id);

        defaultRezervacijeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRezervacijeShouldNotBeFound("id.greaterThan=" + id);

        defaultRezervacijeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRezervacijeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRezervacijesByBrojSobeIsEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where brojSobe equals to DEFAULT_BROJ_SOBE
        defaultRezervacijeShouldBeFound("brojSobe.equals=" + DEFAULT_BROJ_SOBE);

        // Get all the rezervacijeList where brojSobe equals to UPDATED_BROJ_SOBE
        defaultRezervacijeShouldNotBeFound("brojSobe.equals=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllRezervacijesByBrojSobeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where brojSobe not equals to DEFAULT_BROJ_SOBE
        defaultRezervacijeShouldNotBeFound("brojSobe.notEquals=" + DEFAULT_BROJ_SOBE);

        // Get all the rezervacijeList where brojSobe not equals to UPDATED_BROJ_SOBE
        defaultRezervacijeShouldBeFound("brojSobe.notEquals=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllRezervacijesByBrojSobeIsInShouldWork() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where brojSobe in DEFAULT_BROJ_SOBE or UPDATED_BROJ_SOBE
        defaultRezervacijeShouldBeFound("brojSobe.in=" + DEFAULT_BROJ_SOBE + "," + UPDATED_BROJ_SOBE);

        // Get all the rezervacijeList where brojSobe equals to UPDATED_BROJ_SOBE
        defaultRezervacijeShouldNotBeFound("brojSobe.in=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllRezervacijesByBrojSobeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where brojSobe is not null
        defaultRezervacijeShouldBeFound("brojSobe.specified=true");

        // Get all the rezervacijeList where brojSobe is null
        defaultRezervacijeShouldNotBeFound("brojSobe.specified=false");
    }

    @Test
    @Transactional
    void getAllRezervacijesByBrojSobeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where brojSobe is greater than or equal to DEFAULT_BROJ_SOBE
        defaultRezervacijeShouldBeFound("brojSobe.greaterThanOrEqual=" + DEFAULT_BROJ_SOBE);

        // Get all the rezervacijeList where brojSobe is greater than or equal to UPDATED_BROJ_SOBE
        defaultRezervacijeShouldNotBeFound("brojSobe.greaterThanOrEqual=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllRezervacijesByBrojSobeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where brojSobe is less than or equal to DEFAULT_BROJ_SOBE
        defaultRezervacijeShouldBeFound("brojSobe.lessThanOrEqual=" + DEFAULT_BROJ_SOBE);

        // Get all the rezervacijeList where brojSobe is less than or equal to SMALLER_BROJ_SOBE
        defaultRezervacijeShouldNotBeFound("brojSobe.lessThanOrEqual=" + SMALLER_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllRezervacijesByBrojSobeIsLessThanSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where brojSobe is less than DEFAULT_BROJ_SOBE
        defaultRezervacijeShouldNotBeFound("brojSobe.lessThan=" + DEFAULT_BROJ_SOBE);

        // Get all the rezervacijeList where brojSobe is less than UPDATED_BROJ_SOBE
        defaultRezervacijeShouldBeFound("brojSobe.lessThan=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllRezervacijesByBrojSobeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where brojSobe is greater than DEFAULT_BROJ_SOBE
        defaultRezervacijeShouldNotBeFound("brojSobe.greaterThan=" + DEFAULT_BROJ_SOBE);

        // Get all the rezervacijeList where brojSobe is greater than SMALLER_BROJ_SOBE
        defaultRezervacijeShouldBeFound("brojSobe.greaterThan=" + SMALLER_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumDolaskaIsEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumDolaska equals to DEFAULT_DATUM_DOLASKA
        defaultRezervacijeShouldBeFound("datumDolaska.equals=" + DEFAULT_DATUM_DOLASKA);

        // Get all the rezervacijeList where datumDolaska equals to UPDATED_DATUM_DOLASKA
        defaultRezervacijeShouldNotBeFound("datumDolaska.equals=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumDolaskaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumDolaska not equals to DEFAULT_DATUM_DOLASKA
        defaultRezervacijeShouldNotBeFound("datumDolaska.notEquals=" + DEFAULT_DATUM_DOLASKA);

        // Get all the rezervacijeList where datumDolaska not equals to UPDATED_DATUM_DOLASKA
        defaultRezervacijeShouldBeFound("datumDolaska.notEquals=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumDolaskaIsInShouldWork() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumDolaska in DEFAULT_DATUM_DOLASKA or UPDATED_DATUM_DOLASKA
        defaultRezervacijeShouldBeFound("datumDolaska.in=" + DEFAULT_DATUM_DOLASKA + "," + UPDATED_DATUM_DOLASKA);

        // Get all the rezervacijeList where datumDolaska equals to UPDATED_DATUM_DOLASKA
        defaultRezervacijeShouldNotBeFound("datumDolaska.in=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumDolaskaIsNullOrNotNull() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumDolaska is not null
        defaultRezervacijeShouldBeFound("datumDolaska.specified=true");

        // Get all the rezervacijeList where datumDolaska is null
        defaultRezervacijeShouldNotBeFound("datumDolaska.specified=false");
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumDolaskaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumDolaska is greater than or equal to DEFAULT_DATUM_DOLASKA
        defaultRezervacijeShouldBeFound("datumDolaska.greaterThanOrEqual=" + DEFAULT_DATUM_DOLASKA);

        // Get all the rezervacijeList where datumDolaska is greater than or equal to UPDATED_DATUM_DOLASKA
        defaultRezervacijeShouldNotBeFound("datumDolaska.greaterThanOrEqual=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumDolaskaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumDolaska is less than or equal to DEFAULT_DATUM_DOLASKA
        defaultRezervacijeShouldBeFound("datumDolaska.lessThanOrEqual=" + DEFAULT_DATUM_DOLASKA);

        // Get all the rezervacijeList where datumDolaska is less than or equal to SMALLER_DATUM_DOLASKA
        defaultRezervacijeShouldNotBeFound("datumDolaska.lessThanOrEqual=" + SMALLER_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumDolaskaIsLessThanSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumDolaska is less than DEFAULT_DATUM_DOLASKA
        defaultRezervacijeShouldNotBeFound("datumDolaska.lessThan=" + DEFAULT_DATUM_DOLASKA);

        // Get all the rezervacijeList where datumDolaska is less than UPDATED_DATUM_DOLASKA
        defaultRezervacijeShouldBeFound("datumDolaska.lessThan=" + UPDATED_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumDolaskaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumDolaska is greater than DEFAULT_DATUM_DOLASKA
        defaultRezervacijeShouldNotBeFound("datumDolaska.greaterThan=" + DEFAULT_DATUM_DOLASKA);

        // Get all the rezervacijeList where datumDolaska is greater than SMALLER_DATUM_DOLASKA
        defaultRezervacijeShouldBeFound("datumDolaska.greaterThan=" + SMALLER_DATUM_DOLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumOdlaskaIsEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumOdlaska equals to DEFAULT_DATUM_ODLASKA
        defaultRezervacijeShouldBeFound("datumOdlaska.equals=" + DEFAULT_DATUM_ODLASKA);

        // Get all the rezervacijeList where datumOdlaska equals to UPDATED_DATUM_ODLASKA
        defaultRezervacijeShouldNotBeFound("datumOdlaska.equals=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumOdlaskaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumOdlaska not equals to DEFAULT_DATUM_ODLASKA
        defaultRezervacijeShouldNotBeFound("datumOdlaska.notEquals=" + DEFAULT_DATUM_ODLASKA);

        // Get all the rezervacijeList where datumOdlaska not equals to UPDATED_DATUM_ODLASKA
        defaultRezervacijeShouldBeFound("datumOdlaska.notEquals=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumOdlaskaIsInShouldWork() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumOdlaska in DEFAULT_DATUM_ODLASKA or UPDATED_DATUM_ODLASKA
        defaultRezervacijeShouldBeFound("datumOdlaska.in=" + DEFAULT_DATUM_ODLASKA + "," + UPDATED_DATUM_ODLASKA);

        // Get all the rezervacijeList where datumOdlaska equals to UPDATED_DATUM_ODLASKA
        defaultRezervacijeShouldNotBeFound("datumOdlaska.in=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumOdlaskaIsNullOrNotNull() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumOdlaska is not null
        defaultRezervacijeShouldBeFound("datumOdlaska.specified=true");

        // Get all the rezervacijeList where datumOdlaska is null
        defaultRezervacijeShouldNotBeFound("datumOdlaska.specified=false");
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumOdlaskaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumOdlaska is greater than or equal to DEFAULT_DATUM_ODLASKA
        defaultRezervacijeShouldBeFound("datumOdlaska.greaterThanOrEqual=" + DEFAULT_DATUM_ODLASKA);

        // Get all the rezervacijeList where datumOdlaska is greater than or equal to UPDATED_DATUM_ODLASKA
        defaultRezervacijeShouldNotBeFound("datumOdlaska.greaterThanOrEqual=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumOdlaskaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumOdlaska is less than or equal to DEFAULT_DATUM_ODLASKA
        defaultRezervacijeShouldBeFound("datumOdlaska.lessThanOrEqual=" + DEFAULT_DATUM_ODLASKA);

        // Get all the rezervacijeList where datumOdlaska is less than or equal to SMALLER_DATUM_ODLASKA
        defaultRezervacijeShouldNotBeFound("datumOdlaska.lessThanOrEqual=" + SMALLER_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumOdlaskaIsLessThanSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumOdlaska is less than DEFAULT_DATUM_ODLASKA
        defaultRezervacijeShouldNotBeFound("datumOdlaska.lessThan=" + DEFAULT_DATUM_ODLASKA);

        // Get all the rezervacijeList where datumOdlaska is less than UPDATED_DATUM_ODLASKA
        defaultRezervacijeShouldBeFound("datumOdlaska.lessThan=" + UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void getAllRezervacijesByDatumOdlaskaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        // Get all the rezervacijeList where datumOdlaska is greater than DEFAULT_DATUM_ODLASKA
        defaultRezervacijeShouldNotBeFound("datumOdlaska.greaterThan=" + DEFAULT_DATUM_ODLASKA);

        // Get all the rezervacijeList where datumOdlaska is greater than SMALLER_DATUM_ODLASKA
        defaultRezervacijeShouldBeFound("datumOdlaska.greaterThan=" + SMALLER_DATUM_ODLASKA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRezervacijeShouldBeFound(String filter) throws Exception {
        restRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rezervacije.getId().intValue())))
            .andExpect(jsonPath("$.[*].brojSobe").value(hasItem(DEFAULT_BROJ_SOBE)))
            .andExpect(jsonPath("$.[*].datumDolaska").value(hasItem(DEFAULT_DATUM_DOLASKA.toString())))
            .andExpect(jsonPath("$.[*].datumOdlaska").value(hasItem(DEFAULT_DATUM_ODLASKA.toString())));

        // Check, that the count call also returns 1
        restRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRezervacijeShouldNotBeFound(String filter) throws Exception {
        restRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRezervacijeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRezervacije() throws Exception {
        // Get the rezervacije
        restRezervacijeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRezervacije() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();

        // Update the rezervacije
        Rezervacije updatedRezervacije = rezervacijeRepository.findById(rezervacije.getId()).get();
        // Disconnect from session so that the updates on updatedRezervacije are not directly saved in db
        em.detach(updatedRezervacije);
        updatedRezervacije.brojSobe(UPDATED_BROJ_SOBE).datumDolaska(UPDATED_DATUM_DOLASKA).datumOdlaska(UPDATED_DATUM_ODLASKA);

        restRezervacijeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRezervacije.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRezervacije))
            )
            .andExpect(status().isOk());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
        Rezervacije testRezervacije = rezervacijeList.get(rezervacijeList.size() - 1);
        assertThat(testRezervacije.getBrojSobe()).isEqualTo(UPDATED_BROJ_SOBE);
        assertThat(testRezervacije.getDatumDolaska()).isEqualTo(UPDATED_DATUM_DOLASKA);
        assertThat(testRezervacije.getDatumOdlaska()).isEqualTo(UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void putNonExistingRezervacije() throws Exception {
        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();
        rezervacije.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRezervacijeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rezervacije.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rezervacije))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRezervacije() throws Exception {
        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();
        rezervacije.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRezervacijeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rezervacije))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRezervacije() throws Exception {
        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();
        rezervacije.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRezervacijeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rezervacije)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRezervacijeWithPatch() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();

        // Update the rezervacije using partial update
        Rezervacije partialUpdatedRezervacije = new Rezervacije();
        partialUpdatedRezervacije.setId(rezervacije.getId());

        partialUpdatedRezervacije.brojSobe(UPDATED_BROJ_SOBE).datumDolaska(UPDATED_DATUM_DOLASKA);

        restRezervacijeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRezervacije.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRezervacije))
            )
            .andExpect(status().isOk());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
        Rezervacije testRezervacije = rezervacijeList.get(rezervacijeList.size() - 1);
        assertThat(testRezervacije.getBrojSobe()).isEqualTo(UPDATED_BROJ_SOBE);
        assertThat(testRezervacije.getDatumDolaska()).isEqualTo(UPDATED_DATUM_DOLASKA);
        assertThat(testRezervacije.getDatumOdlaska()).isEqualTo(DEFAULT_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void fullUpdateRezervacijeWithPatch() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();

        // Update the rezervacije using partial update
        Rezervacije partialUpdatedRezervacije = new Rezervacije();
        partialUpdatedRezervacije.setId(rezervacije.getId());

        partialUpdatedRezervacije.brojSobe(UPDATED_BROJ_SOBE).datumDolaska(UPDATED_DATUM_DOLASKA).datumOdlaska(UPDATED_DATUM_ODLASKA);

        restRezervacijeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRezervacije.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRezervacije))
            )
            .andExpect(status().isOk());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
        Rezervacije testRezervacije = rezervacijeList.get(rezervacijeList.size() - 1);
        assertThat(testRezervacije.getBrojSobe()).isEqualTo(UPDATED_BROJ_SOBE);
        assertThat(testRezervacije.getDatumDolaska()).isEqualTo(UPDATED_DATUM_DOLASKA);
        assertThat(testRezervacije.getDatumOdlaska()).isEqualTo(UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void patchNonExistingRezervacije() throws Exception {
        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();
        rezervacije.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRezervacijeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rezervacije.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rezervacije))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRezervacije() throws Exception {
        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();
        rezervacije.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRezervacijeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rezervacije))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRezervacije() throws Exception {
        int databaseSizeBeforeUpdate = rezervacijeRepository.findAll().size();
        rezervacije.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRezervacijeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rezervacije))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rezervacije in the database
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRezervacije() throws Exception {
        // Initialize the database
        rezervacijeRepository.saveAndFlush(rezervacije);

        int databaseSizeBeforeDelete = rezervacijeRepository.findAll().size();

        // Delete the rezervacije
        restRezervacijeMockMvc
            .perform(delete(ENTITY_API_URL_ID, rezervacije.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rezervacije> rezervacijeList = rezervacijeRepository.findAll();
        assertThat(rezervacijeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
