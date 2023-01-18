package hoteli.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hoteli.IntegrationTest;
import hoteli.domain.Sobe;
import hoteli.repository.SobeRepository;
import hoteli.service.criteria.SobeCriteria;
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
 * Integration tests for the {@link SobeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SobeResourceIT {

    private static final Integer DEFAULT_BROJ_SOBE = 1;
    private static final Integer UPDATED_BROJ_SOBE = 2;
    private static final Integer SMALLER_BROJ_SOBE = 1 - 1;

    private static final Double DEFAULT_CIJENA = 1D;
    private static final Double UPDATED_CIJENA = 2D;
    private static final Double SMALLER_CIJENA = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/sobes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SobeRepository sobeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSobeMockMvc;

    private Sobe sobe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sobe createEntity(EntityManager em) {
        Sobe sobe = new Sobe().brojSobe(DEFAULT_BROJ_SOBE).cijena(DEFAULT_CIJENA);
        return sobe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sobe createUpdatedEntity(EntityManager em) {
        Sobe sobe = new Sobe().brojSobe(UPDATED_BROJ_SOBE).cijena(UPDATED_CIJENA);
        return sobe;
    }

    @BeforeEach
    public void initTest() {
        sobe = createEntity(em);
    }

    @Test
    @Transactional
    void createSobe() throws Exception {
        int databaseSizeBeforeCreate = sobeRepository.findAll().size();
        // Create the Sobe
        restSobeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sobe)))
            .andExpect(status().isCreated());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeCreate + 1);
        Sobe testSobe = sobeList.get(sobeList.size() - 1);
        assertThat(testSobe.getBrojSobe()).isEqualTo(DEFAULT_BROJ_SOBE);
        assertThat(testSobe.getCijena()).isEqualTo(DEFAULT_CIJENA);
    }

    @Test
    @Transactional
    void createSobeWithExistingId() throws Exception {
        // Create the Sobe with an existing ID
        sobe.setId(1L);

        int databaseSizeBeforeCreate = sobeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSobeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sobe)))
            .andExpect(status().isBadRequest());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrojSobeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sobeRepository.findAll().size();
        // set the field null
        sobe.setBrojSobe(null);

        // Create the Sobe, which fails.

        restSobeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sobe)))
            .andExpect(status().isBadRequest());

        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCijenaIsRequired() throws Exception {
        int databaseSizeBeforeTest = sobeRepository.findAll().size();
        // set the field null
        sobe.setCijena(null);

        // Create the Sobe, which fails.

        restSobeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sobe)))
            .andExpect(status().isBadRequest());

        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSobes() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList
        restSobeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sobe.getId().intValue())))
            .andExpect(jsonPath("$.[*].brojSobe").value(hasItem(DEFAULT_BROJ_SOBE)))
            .andExpect(jsonPath("$.[*].cijena").value(hasItem(DEFAULT_CIJENA.doubleValue())));
    }

    @Test
    @Transactional
    void getSobe() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get the sobe
        restSobeMockMvc
            .perform(get(ENTITY_API_URL_ID, sobe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sobe.getId().intValue()))
            .andExpect(jsonPath("$.brojSobe").value(DEFAULT_BROJ_SOBE))
            .andExpect(jsonPath("$.cijena").value(DEFAULT_CIJENA.doubleValue()));
    }

    @Test
    @Transactional
    void getSobesByIdFiltering() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        Long id = sobe.getId();

        defaultSobeShouldBeFound("id.equals=" + id);
        defaultSobeShouldNotBeFound("id.notEquals=" + id);

        defaultSobeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSobeShouldNotBeFound("id.greaterThan=" + id);

        defaultSobeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSobeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSobesByBrojSobeIsEqualToSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where brojSobe equals to DEFAULT_BROJ_SOBE
        defaultSobeShouldBeFound("brojSobe.equals=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeList where brojSobe equals to UPDATED_BROJ_SOBE
        defaultSobeShouldNotBeFound("brojSobe.equals=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobesByBrojSobeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where brojSobe not equals to DEFAULT_BROJ_SOBE
        defaultSobeShouldNotBeFound("brojSobe.notEquals=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeList where brojSobe not equals to UPDATED_BROJ_SOBE
        defaultSobeShouldBeFound("brojSobe.notEquals=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobesByBrojSobeIsInShouldWork() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where brojSobe in DEFAULT_BROJ_SOBE or UPDATED_BROJ_SOBE
        defaultSobeShouldBeFound("brojSobe.in=" + DEFAULT_BROJ_SOBE + "," + UPDATED_BROJ_SOBE);

        // Get all the sobeList where brojSobe equals to UPDATED_BROJ_SOBE
        defaultSobeShouldNotBeFound("brojSobe.in=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobesByBrojSobeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where brojSobe is not null
        defaultSobeShouldBeFound("brojSobe.specified=true");

        // Get all the sobeList where brojSobe is null
        defaultSobeShouldNotBeFound("brojSobe.specified=false");
    }

    @Test
    @Transactional
    void getAllSobesByBrojSobeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where brojSobe is greater than or equal to DEFAULT_BROJ_SOBE
        defaultSobeShouldBeFound("brojSobe.greaterThanOrEqual=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeList where brojSobe is greater than or equal to UPDATED_BROJ_SOBE
        defaultSobeShouldNotBeFound("brojSobe.greaterThanOrEqual=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobesByBrojSobeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where brojSobe is less than or equal to DEFAULT_BROJ_SOBE
        defaultSobeShouldBeFound("brojSobe.lessThanOrEqual=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeList where brojSobe is less than or equal to SMALLER_BROJ_SOBE
        defaultSobeShouldNotBeFound("brojSobe.lessThanOrEqual=" + SMALLER_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobesByBrojSobeIsLessThanSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where brojSobe is less than DEFAULT_BROJ_SOBE
        defaultSobeShouldNotBeFound("brojSobe.lessThan=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeList where brojSobe is less than UPDATED_BROJ_SOBE
        defaultSobeShouldBeFound("brojSobe.lessThan=" + UPDATED_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobesByBrojSobeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where brojSobe is greater than DEFAULT_BROJ_SOBE
        defaultSobeShouldNotBeFound("brojSobe.greaterThan=" + DEFAULT_BROJ_SOBE);

        // Get all the sobeList where brojSobe is greater than SMALLER_BROJ_SOBE
        defaultSobeShouldBeFound("brojSobe.greaterThan=" + SMALLER_BROJ_SOBE);
    }

    @Test
    @Transactional
    void getAllSobesByCijenaIsEqualToSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where cijena equals to DEFAULT_CIJENA
        defaultSobeShouldBeFound("cijena.equals=" + DEFAULT_CIJENA);

        // Get all the sobeList where cijena equals to UPDATED_CIJENA
        defaultSobeShouldNotBeFound("cijena.equals=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobesByCijenaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where cijena not equals to DEFAULT_CIJENA
        defaultSobeShouldNotBeFound("cijena.notEquals=" + DEFAULT_CIJENA);

        // Get all the sobeList where cijena not equals to UPDATED_CIJENA
        defaultSobeShouldBeFound("cijena.notEquals=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobesByCijenaIsInShouldWork() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where cijena in DEFAULT_CIJENA or UPDATED_CIJENA
        defaultSobeShouldBeFound("cijena.in=" + DEFAULT_CIJENA + "," + UPDATED_CIJENA);

        // Get all the sobeList where cijena equals to UPDATED_CIJENA
        defaultSobeShouldNotBeFound("cijena.in=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobesByCijenaIsNullOrNotNull() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where cijena is not null
        defaultSobeShouldBeFound("cijena.specified=true");

        // Get all the sobeList where cijena is null
        defaultSobeShouldNotBeFound("cijena.specified=false");
    }

    @Test
    @Transactional
    void getAllSobesByCijenaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where cijena is greater than or equal to DEFAULT_CIJENA
        defaultSobeShouldBeFound("cijena.greaterThanOrEqual=" + DEFAULT_CIJENA);

        // Get all the sobeList where cijena is greater than or equal to UPDATED_CIJENA
        defaultSobeShouldNotBeFound("cijena.greaterThanOrEqual=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobesByCijenaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where cijena is less than or equal to DEFAULT_CIJENA
        defaultSobeShouldBeFound("cijena.lessThanOrEqual=" + DEFAULT_CIJENA);

        // Get all the sobeList where cijena is less than or equal to SMALLER_CIJENA
        defaultSobeShouldNotBeFound("cijena.lessThanOrEqual=" + SMALLER_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobesByCijenaIsLessThanSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where cijena is less than DEFAULT_CIJENA
        defaultSobeShouldNotBeFound("cijena.lessThan=" + DEFAULT_CIJENA);

        // Get all the sobeList where cijena is less than UPDATED_CIJENA
        defaultSobeShouldBeFound("cijena.lessThan=" + UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void getAllSobesByCijenaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        // Get all the sobeList where cijena is greater than DEFAULT_CIJENA
        defaultSobeShouldNotBeFound("cijena.greaterThan=" + DEFAULT_CIJENA);

        // Get all the sobeList where cijena is greater than SMALLER_CIJENA
        defaultSobeShouldBeFound("cijena.greaterThan=" + SMALLER_CIJENA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSobeShouldBeFound(String filter) throws Exception {
        restSobeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sobe.getId().intValue())))
            .andExpect(jsonPath("$.[*].brojSobe").value(hasItem(DEFAULT_BROJ_SOBE)))
            .andExpect(jsonPath("$.[*].cijena").value(hasItem(DEFAULT_CIJENA.doubleValue())));

        // Check, that the count call also returns 1
        restSobeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSobeShouldNotBeFound(String filter) throws Exception {
        restSobeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSobeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSobe() throws Exception {
        // Get the sobe
        restSobeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSobe() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();

        // Update the sobe
        Sobe updatedSobe = sobeRepository.findById(sobe.getId()).get();
        // Disconnect from session so that the updates on updatedSobe are not directly saved in db
        em.detach(updatedSobe);
        updatedSobe.brojSobe(UPDATED_BROJ_SOBE).cijena(UPDATED_CIJENA);

        restSobeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSobe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSobe))
            )
            .andExpect(status().isOk());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
        Sobe testSobe = sobeList.get(sobeList.size() - 1);
        assertThat(testSobe.getBrojSobe()).isEqualTo(UPDATED_BROJ_SOBE);
        assertThat(testSobe.getCijena()).isEqualTo(UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void putNonExistingSobe() throws Exception {
        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();
        sobe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSobeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sobe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sobe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSobe() throws Exception {
        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();
        sobe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSobeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sobe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSobe() throws Exception {
        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();
        sobe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSobeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sobe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSobeWithPatch() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();

        // Update the sobe using partial update
        Sobe partialUpdatedSobe = new Sobe();
        partialUpdatedSobe.setId(sobe.getId());

        restSobeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSobe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSobe))
            )
            .andExpect(status().isOk());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
        Sobe testSobe = sobeList.get(sobeList.size() - 1);
        assertThat(testSobe.getBrojSobe()).isEqualTo(DEFAULT_BROJ_SOBE);
        assertThat(testSobe.getCijena()).isEqualTo(DEFAULT_CIJENA);
    }

    @Test
    @Transactional
    void fullUpdateSobeWithPatch() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();

        // Update the sobe using partial update
        Sobe partialUpdatedSobe = new Sobe();
        partialUpdatedSobe.setId(sobe.getId());

        partialUpdatedSobe.brojSobe(UPDATED_BROJ_SOBE).cijena(UPDATED_CIJENA);

        restSobeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSobe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSobe))
            )
            .andExpect(status().isOk());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
        Sobe testSobe = sobeList.get(sobeList.size() - 1);
        assertThat(testSobe.getBrojSobe()).isEqualTo(UPDATED_BROJ_SOBE);
        assertThat(testSobe.getCijena()).isEqualTo(UPDATED_CIJENA);
    }

    @Test
    @Transactional
    void patchNonExistingSobe() throws Exception {
        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();
        sobe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSobeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sobe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sobe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSobe() throws Exception {
        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();
        sobe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSobeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sobe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSobe() throws Exception {
        int databaseSizeBeforeUpdate = sobeRepository.findAll().size();
        sobe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSobeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sobe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sobe in the database
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSobe() throws Exception {
        // Initialize the database
        sobeRepository.saveAndFlush(sobe);

        int databaseSizeBeforeDelete = sobeRepository.findAll().size();

        // Delete the sobe
        restSobeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sobe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sobe> sobeList = sobeRepository.findAll();
        assertThat(sobeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
