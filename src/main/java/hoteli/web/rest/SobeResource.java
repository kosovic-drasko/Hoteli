package hoteli.web.rest;

import hoteli.domain.Sobe;
import hoteli.repository.SobeRepository;
import hoteli.service.SobeQueryService;
import hoteli.service.SobeService;
import hoteli.service.criteria.SobeCriteria;
import hoteli.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link hoteli.domain.Sobe}.
 */
@RestController
@RequestMapping("/api")
public class SobeResource {

    private final Logger log = LoggerFactory.getLogger(SobeResource.class);

    private static final String ENTITY_NAME = "sobe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SobeService sobeService;

    private final SobeRepository sobeRepository;

    private final SobeQueryService sobeQueryService;

    public SobeResource(SobeService sobeService, SobeRepository sobeRepository, SobeQueryService sobeQueryService) {
        this.sobeService = sobeService;
        this.sobeRepository = sobeRepository;
        this.sobeQueryService = sobeQueryService;
    }

    /**
     * {@code POST  /sobes} : Create a new sobe.
     *
     * @param sobe the sobe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sobe, or with status {@code 400 (Bad Request)} if the sobe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sobes")
    public ResponseEntity<Sobe> createSobe(@Valid @RequestBody Sobe sobe) throws URISyntaxException {
        log.debug("REST request to save Sobe : {}", sobe);
        if (sobe.getId() != null) {
            throw new BadRequestAlertException("A new sobe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sobe result = sobeService.save(sobe);
        return ResponseEntity
            .created(new URI("/api/sobes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sobes/:id} : Updates an existing sobe.
     *
     * @param id the id of the sobe to save.
     * @param sobe the sobe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sobe,
     * or with status {@code 400 (Bad Request)} if the sobe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sobe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sobes/{id}")
    public ResponseEntity<Sobe> updateSobe(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Sobe sobe)
        throws URISyntaxException {
        log.debug("REST request to update Sobe : {}, {}", id, sobe);
        if (sobe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sobe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sobeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sobe result = sobeService.update(sobe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sobe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sobes/:id} : Partial updates given fields of an existing sobe, field will ignore if it is null
     *
     * @param id the id of the sobe to save.
     * @param sobe the sobe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sobe,
     * or with status {@code 400 (Bad Request)} if the sobe is not valid,
     * or with status {@code 404 (Not Found)} if the sobe is not found,
     * or with status {@code 500 (Internal Server Error)} if the sobe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sobes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sobe> partialUpdateSobe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Sobe sobe
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sobe partially : {}, {}", id, sobe);
        if (sobe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sobe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sobeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sobe> result = sobeService.partialUpdate(sobe);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sobe.getId().toString())
        );
    }

    /**
     * {@code GET  /sobes} : get all the sobes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sobes in body.
     */
    @GetMapping("/sobes")
    public ResponseEntity<List<Sobe>> getAllSobes(SobeCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Sobes by criteria: {}", criteria);
        Page<Sobe> page = sobeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sobes/count} : count all the sobes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sobes/count")
    public ResponseEntity<Long> countSobes(SobeCriteria criteria) {
        log.debug("REST request to count Sobes by criteria: {}", criteria);
        return ResponseEntity.ok().body(sobeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sobes/:id} : get the "id" sobe.
     *
     * @param id the id of the sobe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sobe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sobes/{id}")
    public ResponseEntity<Sobe> getSobe(@PathVariable Long id) {
        log.debug("REST request to get Sobe : {}", id);
        Optional<Sobe> sobe = sobeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sobe);
    }

    /**
     * {@code DELETE  /sobes/:id} : delete the "id" sobe.
     *
     * @param id the id of the sobe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sobes/{id}")
    public ResponseEntity<Void> deleteSobe(@PathVariable Long id) {
        log.debug("REST request to delete Sobe : {}", id);
        sobeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
