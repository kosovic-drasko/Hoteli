package hoteli.web.rest;

import hoteli.domain.Rezervacije;
import hoteli.domain.SobeRezervacije;
import hoteli.repository.SobeRezervacijeRepository;
import hoteli.service.SobeRezervacijeQueryService;
import hoteli.service.SobeRezervacijeService;
import hoteli.service.criteria.SobeRezervacijeCriteria;
import hoteli.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link hoteli.domain.SobeRezervacije}.
 */
@RestController
@RequestMapping("/api")
public class SobeRezervacijeResource {

    private final Logger log = LoggerFactory.getLogger(SobeRezervacijeResource.class);

    private final SobeRezervacijeService sobeRezervacijeService;

    private final SobeRezervacijeRepository sobeRezervacijeRepository;

    private final SobeRezervacijeQueryService sobeRezervacijeQueryService;

    public SobeRezervacijeResource(
        SobeRezervacijeService sobeRezervacijeService,
        SobeRezervacijeRepository sobeRezervacijeRepository,
        SobeRezervacijeQueryService sobeRezervacijeQueryService
    ) {
        this.sobeRezervacijeService = sobeRezervacijeService;
        this.sobeRezervacijeRepository = sobeRezervacijeRepository;
        this.sobeRezervacijeQueryService = sobeRezervacijeQueryService;
    }

    /**
     * {@code GET  /sobe-rezervacijes} : get all the sobeRezervacijes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sobeRezervacijes in body.
     */
    @GetMapping("/sobe-rezervacijes")
    public ResponseEntity<List<SobeRezervacije>> getAllSobeRezervacijes(
        SobeRezervacijeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SobeRezervacijes by criteria: {}", criteria);
        Page<SobeRezervacije> page = sobeRezervacijeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sobe-rezervacijes/count} : count all the sobeRezervacijes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sobe-rezervacijes/count")
    public ResponseEntity<Long> countSobeRezervacijes(SobeRezervacijeCriteria criteria) {
        log.debug("REST request to count SobeRezervacijes by criteria: {}", criteria);
        return ResponseEntity.ok().body(sobeRezervacijeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sobe-rezervacijes/:id} : get the "id" sobeRezervacije.
     *
     * @param id the id of the sobeRezervacije to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sobeRezervacije, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sobe-rezervacijes/{id}")
    public ResponseEntity<SobeRezervacije> getSobeRezervacije(@PathVariable Long id) {
        log.debug("REST request to get SobeRezervacije : {}", id);
        Optional<SobeRezervacije> sobeRezervacije = sobeRezervacijeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sobeRezervacije);
    }

    @GetMapping("/sobe-rezervacije/{dolazak}/{odlazak}")
    @Transactional
    public List<SobeRezervacije> getNadjiRezervaciju(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String dolazak,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String odlazak
    ) {
        List<SobeRezervacije> sobeRezervacije = sobeRezervacijeRepository.findRezervacije(dolazak, odlazak);
        return sobeRezervacije;
    }
}
