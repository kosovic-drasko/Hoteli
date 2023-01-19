package hoteli.web.rest;

import hoteli.domain.Rezervacije;
import hoteli.repository.RezervacijeRepository;
import hoteli.service.RezervacijeQueryService;
import hoteli.service.RezervacijeService;
import hoteli.service.criteria.RezervacijeCriteria;
import hoteli.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link hoteli.domain.Rezervacije}.
 */
@RestController
@RequestMapping("/api")
public class RezervacijeResource {

    private final Logger log = LoggerFactory.getLogger(RezervacijeResource.class);

    private static final String ENTITY_NAME = "rezervacije";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RezervacijeService rezervacijeService;

    private final RezervacijeRepository rezervacijeRepository;

    private final RezervacijeQueryService rezervacijeQueryService;

    public RezervacijeResource(
        RezervacijeService rezervacijeService,
        RezervacijeRepository rezervacijeRepository,
        RezervacijeQueryService rezervacijeQueryService
    ) {
        this.rezervacijeService = rezervacijeService;
        this.rezervacijeRepository = rezervacijeRepository;
        this.rezervacijeQueryService = rezervacijeQueryService;
    }

    /**
     * {@code POST  /rezervacijes} : Create a new rezervacije.
     *
     * @param rezervacije the rezervacije to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rezervacije, or with status {@code 400 (Bad Request)} if the rezervacije has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rezervacijes")
    public ResponseEntity<Rezervacije> createRezervacije(@Valid @RequestBody Rezervacije rezervacije) throws URISyntaxException {
        log.debug("REST request to save Rezervacije : {}", rezervacije);
        if (rezervacije.getId() != null) {
            throw new BadRequestAlertException("A new rezervacije cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rezervacije result = rezervacijeService.save(rezervacije);
        return ResponseEntity
            .created(new URI("/api/rezervacijes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rezervacijes/:id} : Updates an existing rezervacije.
     *
     * @param id          the id of the rezervacije to save.
     * @param rezervacije the rezervacije to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rezervacije,
     * or with status {@code 400 (Bad Request)} if the rezervacije is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rezervacije couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rezervacijes/{id}")
    public ResponseEntity<Rezervacije> updateRezervacije(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Rezervacije rezervacije
    ) throws URISyntaxException {
        log.debug("REST request to update Rezervacije : {}, {}", id, rezervacije);
        if (rezervacije.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rezervacije.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rezervacijeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rezervacije result = rezervacijeService.update(rezervacije);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rezervacije.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rezervacijes/:id} : Partial updates given fields of an existing rezervacije, field will ignore if it is null
     *
     * @param id          the id of the rezervacije to save.
     * @param rezervacije the rezervacije to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rezervacije,
     * or with status {@code 400 (Bad Request)} if the rezervacije is not valid,
     * or with status {@code 404 (Not Found)} if the rezervacije is not found,
     * or with status {@code 500 (Internal Server Error)} if the rezervacije couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rezervacijes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rezervacije> partialUpdateRezervacije(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rezervacije rezervacije
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rezervacije partially : {}, {}", id, rezervacije);
        if (rezervacije.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rezervacije.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rezervacijeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rezervacije> result = rezervacijeService.partialUpdate(rezervacije);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rezervacije.getId().toString())
        );
    }

    /**
     * {@code GET  /rezervacijes} : get all the rezervacijes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rezervacijes in body.
     */
    @GetMapping("/rezervacijes")
    public ResponseEntity<List<Rezervacije>> getAllRezervacijes(
        RezervacijeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Rezervacijes by criteria: {}", criteria);
        Page<Rezervacije> page = rezervacijeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rezervacijes/count} : count all the rezervacijes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rezervacijes/count")
    public ResponseEntity<Long> countRezervacijes(RezervacijeCriteria criteria) {
        log.debug("REST request to count Rezervacijes by criteria: {}", criteria);
        return ResponseEntity.ok().body(rezervacijeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rezervacijes/:id} : get the "id" rezervacije.
     *
     * @param id the id of the rezervacije to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rezervacije, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rezervacijes/{id}")
    public ResponseEntity<Rezervacije> getRezervacije(@PathVariable Long id) {
        log.debug("REST request to get Rezervacije : {}", id);
        Optional<Rezervacije> rezervacije = rezervacijeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rezervacije);
    }

    /**
     * {@code DELETE  /rezervacijes/:id} : delete the "id" rezervacije.
     *
     * @param id the id of the rezervacije to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rezervacijes/{id}")
    public ResponseEntity<Void> deleteRezervacije(@PathVariable Long id) {
        log.debug("REST request to delete Rezervacije : {}", id);
        rezervacijeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/bookings/date/{startDate}/to/{endDate}")
    public List<Rezervacije> showRezervacijesByDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        log.debug("Inside showRezervacijesByDate() method of RezervacijeController");
        if ((startDate.compareTo(endDate)) > 0) {
            return null;
        } else {
            return rezervacijeRepository
                .findAll()
                .stream()
                .filter(bookings ->
                    //                    (bookings) -> bookings.getCheckin().isAfter(startDate.minusDays(1)) && bookings.getCheckout().isBefore(endDate.plusDays(1)))
                    bookings.getDatumOdlaska().isAfter(startDate.minusDays(1)) && bookings.getDatumDolaska().isBefore(endDate.plusDays(1))
                )
                .collect(Collectors.toList());
        }
    }
}
