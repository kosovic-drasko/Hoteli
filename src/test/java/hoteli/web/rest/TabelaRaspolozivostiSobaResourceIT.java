package hoteli.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hoteli.IntegrationTest;
import hoteli.domain.TabelaRaspolozivostiSoba;
import hoteli.repository.TabelaRaspolozivostiSobaRepository;
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
 * Integration tests for the {@link TabelaRaspolozivostiSobaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TabelaRaspolozivostiSobaResourceIT {

    private static final Integer DEFAULT_BROJ_SOBE = 1;
    private static final Integer UPDATED_BROJ_SOBE = 2;

    private static final LocalDate DEFAULT_DATUM_DOLASKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_DOLASKA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATUM_ODLASKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_ODLASKA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/tabela-raspolozivosti-sobas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TabelaRaspolozivostiSobaRepository tabelaRaspolozivostiSobaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTabelaRaspolozivostiSobaMockMvc;

    private TabelaRaspolozivostiSoba tabelaRaspolozivostiSoba;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TabelaRaspolozivostiSoba createEntity(EntityManager em) {
        TabelaRaspolozivostiSoba tabelaRaspolozivostiSoba = new TabelaRaspolozivostiSoba()
            .brojSobe(DEFAULT_BROJ_SOBE)
            .datumDolaska(DEFAULT_DATUM_DOLASKA)
            .datumOdlaska(DEFAULT_DATUM_ODLASKA);
        return tabelaRaspolozivostiSoba;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TabelaRaspolozivostiSoba createUpdatedEntity(EntityManager em) {
        TabelaRaspolozivostiSoba tabelaRaspolozivostiSoba = new TabelaRaspolozivostiSoba()
            .brojSobe(UPDATED_BROJ_SOBE)
            .datumDolaska(UPDATED_DATUM_DOLASKA)
            .datumOdlaska(UPDATED_DATUM_ODLASKA);
        return tabelaRaspolozivostiSoba;
    }

    @BeforeEach
    public void initTest() {
        tabelaRaspolozivostiSoba = createEntity(em);
    }

    @Test
    @Transactional
    void createTabelaRaspolozivostiSoba() throws Exception {
        int databaseSizeBeforeCreate = tabelaRaspolozivostiSobaRepository.findAll().size();
        // Create the TabelaRaspolozivostiSoba
        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isCreated());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeCreate + 1);
        TabelaRaspolozivostiSoba testTabelaRaspolozivostiSoba = tabelaRaspolozivostiSobaList.get(tabelaRaspolozivostiSobaList.size() - 1);
        assertThat(testTabelaRaspolozivostiSoba.getBrojSobe()).isEqualTo(DEFAULT_BROJ_SOBE);
        assertThat(testTabelaRaspolozivostiSoba.getDatumDolaska()).isEqualTo(DEFAULT_DATUM_DOLASKA);
        assertThat(testTabelaRaspolozivostiSoba.getDatumOdlaska()).isEqualTo(DEFAULT_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void createTabelaRaspolozivostiSobaWithExistingId() throws Exception {
        // Create the TabelaRaspolozivostiSoba with an existing ID
        tabelaRaspolozivostiSoba.setId(1L);

        int databaseSizeBeforeCreate = tabelaRaspolozivostiSobaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrojSobeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tabelaRaspolozivostiSobaRepository.findAll().size();
        // set the field null
        tabelaRaspolozivostiSoba.setBrojSobe(null);

        // Create the TabelaRaspolozivostiSoba, which fails.

        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isBadRequest());

        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTabelaRaspolozivostiSobas() throws Exception {
        // Initialize the database
        tabelaRaspolozivostiSobaRepository.saveAndFlush(tabelaRaspolozivostiSoba);

        // Get all the tabelaRaspolozivostiSobaList
        restTabelaRaspolozivostiSobaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tabelaRaspolozivostiSoba.getId().intValue())))
            .andExpect(jsonPath("$.[*].brojSobe").value(hasItem(DEFAULT_BROJ_SOBE)))
            .andExpect(jsonPath("$.[*].datumDolaska").value(hasItem(DEFAULT_DATUM_DOLASKA.toString())))
            .andExpect(jsonPath("$.[*].datumOdlaska").value(hasItem(DEFAULT_DATUM_ODLASKA.toString())));
    }

    @Test
    @Transactional
    void getTabelaRaspolozivostiSoba() throws Exception {
        // Initialize the database
        tabelaRaspolozivostiSobaRepository.saveAndFlush(tabelaRaspolozivostiSoba);

        // Get the tabelaRaspolozivostiSoba
        restTabelaRaspolozivostiSobaMockMvc
            .perform(get(ENTITY_API_URL_ID, tabelaRaspolozivostiSoba.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tabelaRaspolozivostiSoba.getId().intValue()))
            .andExpect(jsonPath("$.brojSobe").value(DEFAULT_BROJ_SOBE))
            .andExpect(jsonPath("$.datumDolaska").value(DEFAULT_DATUM_DOLASKA.toString()))
            .andExpect(jsonPath("$.datumOdlaska").value(DEFAULT_DATUM_ODLASKA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTabelaRaspolozivostiSoba() throws Exception {
        // Get the tabelaRaspolozivostiSoba
        restTabelaRaspolozivostiSobaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTabelaRaspolozivostiSoba() throws Exception {
        // Initialize the database
        tabelaRaspolozivostiSobaRepository.saveAndFlush(tabelaRaspolozivostiSoba);

        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();

        // Update the tabelaRaspolozivostiSoba
        TabelaRaspolozivostiSoba updatedTabelaRaspolozivostiSoba = tabelaRaspolozivostiSobaRepository
            .findById(tabelaRaspolozivostiSoba.getId())
            .get();
        // Disconnect from session so that the updates on updatedTabelaRaspolozivostiSoba are not directly saved in db
        em.detach(updatedTabelaRaspolozivostiSoba);
        updatedTabelaRaspolozivostiSoba.brojSobe(UPDATED_BROJ_SOBE).datumDolaska(UPDATED_DATUM_DOLASKA).datumOdlaska(UPDATED_DATUM_ODLASKA);

        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTabelaRaspolozivostiSoba.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTabelaRaspolozivostiSoba))
            )
            .andExpect(status().isOk());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
        TabelaRaspolozivostiSoba testTabelaRaspolozivostiSoba = tabelaRaspolozivostiSobaList.get(tabelaRaspolozivostiSobaList.size() - 1);
        assertThat(testTabelaRaspolozivostiSoba.getBrojSobe()).isEqualTo(UPDATED_BROJ_SOBE);
        assertThat(testTabelaRaspolozivostiSoba.getDatumDolaska()).isEqualTo(UPDATED_DATUM_DOLASKA);
        assertThat(testTabelaRaspolozivostiSoba.getDatumOdlaska()).isEqualTo(UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void putNonExistingTabelaRaspolozivostiSoba() throws Exception {
        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();
        tabelaRaspolozivostiSoba.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tabelaRaspolozivostiSoba.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTabelaRaspolozivostiSoba() throws Exception {
        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();
        tabelaRaspolozivostiSoba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTabelaRaspolozivostiSoba() throws Exception {
        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();
        tabelaRaspolozivostiSoba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTabelaRaspolozivostiSobaWithPatch() throws Exception {
        // Initialize the database
        tabelaRaspolozivostiSobaRepository.saveAndFlush(tabelaRaspolozivostiSoba);

        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();

        // Update the tabelaRaspolozivostiSoba using partial update
        TabelaRaspolozivostiSoba partialUpdatedTabelaRaspolozivostiSoba = new TabelaRaspolozivostiSoba();
        partialUpdatedTabelaRaspolozivostiSoba.setId(tabelaRaspolozivostiSoba.getId());

        partialUpdatedTabelaRaspolozivostiSoba.brojSobe(UPDATED_BROJ_SOBE).datumDolaska(UPDATED_DATUM_DOLASKA);

        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTabelaRaspolozivostiSoba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTabelaRaspolozivostiSoba))
            )
            .andExpect(status().isOk());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
        TabelaRaspolozivostiSoba testTabelaRaspolozivostiSoba = tabelaRaspolozivostiSobaList.get(tabelaRaspolozivostiSobaList.size() - 1);
        assertThat(testTabelaRaspolozivostiSoba.getBrojSobe()).isEqualTo(UPDATED_BROJ_SOBE);
        assertThat(testTabelaRaspolozivostiSoba.getDatumDolaska()).isEqualTo(UPDATED_DATUM_DOLASKA);
        assertThat(testTabelaRaspolozivostiSoba.getDatumOdlaska()).isEqualTo(DEFAULT_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void fullUpdateTabelaRaspolozivostiSobaWithPatch() throws Exception {
        // Initialize the database
        tabelaRaspolozivostiSobaRepository.saveAndFlush(tabelaRaspolozivostiSoba);

        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();

        // Update the tabelaRaspolozivostiSoba using partial update
        TabelaRaspolozivostiSoba partialUpdatedTabelaRaspolozivostiSoba = new TabelaRaspolozivostiSoba();
        partialUpdatedTabelaRaspolozivostiSoba.setId(tabelaRaspolozivostiSoba.getId());

        partialUpdatedTabelaRaspolozivostiSoba
            .brojSobe(UPDATED_BROJ_SOBE)
            .datumDolaska(UPDATED_DATUM_DOLASKA)
            .datumOdlaska(UPDATED_DATUM_ODLASKA);

        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTabelaRaspolozivostiSoba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTabelaRaspolozivostiSoba))
            )
            .andExpect(status().isOk());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
        TabelaRaspolozivostiSoba testTabelaRaspolozivostiSoba = tabelaRaspolozivostiSobaList.get(tabelaRaspolozivostiSobaList.size() - 1);
        assertThat(testTabelaRaspolozivostiSoba.getBrojSobe()).isEqualTo(UPDATED_BROJ_SOBE);
        assertThat(testTabelaRaspolozivostiSoba.getDatumDolaska()).isEqualTo(UPDATED_DATUM_DOLASKA);
        assertThat(testTabelaRaspolozivostiSoba.getDatumOdlaska()).isEqualTo(UPDATED_DATUM_ODLASKA);
    }

    @Test
    @Transactional
    void patchNonExistingTabelaRaspolozivostiSoba() throws Exception {
        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();
        tabelaRaspolozivostiSoba.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tabelaRaspolozivostiSoba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTabelaRaspolozivostiSoba() throws Exception {
        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();
        tabelaRaspolozivostiSoba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTabelaRaspolozivostiSoba() throws Exception {
        int databaseSizeBeforeUpdate = tabelaRaspolozivostiSobaRepository.findAll().size();
        tabelaRaspolozivostiSoba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabelaRaspolozivostiSobaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tabelaRaspolozivostiSoba))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TabelaRaspolozivostiSoba in the database
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTabelaRaspolozivostiSoba() throws Exception {
        // Initialize the database
        tabelaRaspolozivostiSobaRepository.saveAndFlush(tabelaRaspolozivostiSoba);

        int databaseSizeBeforeDelete = tabelaRaspolozivostiSobaRepository.findAll().size();

        // Delete the tabelaRaspolozivostiSoba
        restTabelaRaspolozivostiSobaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tabelaRaspolozivostiSoba.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSobaList = tabelaRaspolozivostiSobaRepository.findAll();
        assertThat(tabelaRaspolozivostiSobaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
