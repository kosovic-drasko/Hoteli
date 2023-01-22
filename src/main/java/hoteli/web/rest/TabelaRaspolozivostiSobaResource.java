package hoteli.web.rest;

import hoteli.domain.TabelaRaspolozivostiSoba;
import hoteli.repository.TabelaRaspolozivostiSobaRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link hoteli.domain.TabelaRaspolozivostiSoba}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TabelaRaspolozivostiSobaResource {

    private final Logger log = LoggerFactory.getLogger(TabelaRaspolozivostiSobaResource.class);

    private static final String ENTITY_NAME = "tabelaRaspolozivostiSoba";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TabelaRaspolozivostiSobaRepository tabelaRaspolozivostiSobaRepository;

    public TabelaRaspolozivostiSobaResource(TabelaRaspolozivostiSobaRepository tabelaRaspolozivostiSobaRepository) {
        this.tabelaRaspolozivostiSobaRepository = tabelaRaspolozivostiSobaRepository;
    }

    /**
     * {@code POST  /tabela-raspolozivosti-sobas} : Create a new tabelaRaspolozivostiSoba.
     *
     * @param tabelaRaspolozivostiSoba the tabelaRaspolozivostiSoba to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tabelaRaspolozivostiSoba, or with status {@code 400 (Bad Request)} if the tabelaRaspolozivostiSoba has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tabela-raspolozivosti-sobas")
    public ResponseEntity<TabelaRaspolozivostiSoba> createTabelaRaspolozivostiSoba(
        @Valid @RequestBody TabelaRaspolozivostiSoba tabelaRaspolozivostiSoba
    ) throws URISyntaxException {
        log.debug("REST request to save TabelaRaspolozivostiSoba : {}", tabelaRaspolozivostiSoba);
        if (tabelaRaspolozivostiSoba.getId() != null) {
            throw new BadRequestAlertException("A new tabelaRaspolozivostiSoba cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TabelaRaspolozivostiSoba result = tabelaRaspolozivostiSobaRepository.save(tabelaRaspolozivostiSoba);
        return ResponseEntity
            .created(new URI("/api/tabela-raspolozivosti-sobas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tabela-raspolozivosti-sobas/:id} : Updates an existing tabelaRaspolozivostiSoba.
     *
     * @param id the id of the tabelaRaspolozivostiSoba to save.
     * @param tabelaRaspolozivostiSoba the tabelaRaspolozivostiSoba to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabelaRaspolozivostiSoba,
     * or with status {@code 400 (Bad Request)} if the tabelaRaspolozivostiSoba is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tabelaRaspolozivostiSoba couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tabela-raspolozivosti-sobas/{id}")
    public ResponseEntity<TabelaRaspolozivostiSoba> updateTabelaRaspolozivostiSoba(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TabelaRaspolozivostiSoba tabelaRaspolozivostiSoba
    ) throws URISyntaxException {
        log.debug("REST request to update TabelaRaspolozivostiSoba : {}, {}", id, tabelaRaspolozivostiSoba);
        if (tabelaRaspolozivostiSoba.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tabelaRaspolozivostiSoba.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tabelaRaspolozivostiSobaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TabelaRaspolozivostiSoba result = tabelaRaspolozivostiSobaRepository.save(tabelaRaspolozivostiSoba);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tabelaRaspolozivostiSoba.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tabela-raspolozivosti-sobas/:id} : Partial updates given fields of an existing tabelaRaspolozivostiSoba, field will ignore if it is null
     *
     * @param id the id of the tabelaRaspolozivostiSoba to save.
     * @param tabelaRaspolozivostiSoba the tabelaRaspolozivostiSoba to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabelaRaspolozivostiSoba,
     * or with status {@code 400 (Bad Request)} if the tabelaRaspolozivostiSoba is not valid,
     * or with status {@code 404 (Not Found)} if the tabelaRaspolozivostiSoba is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabelaRaspolozivostiSoba couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tabela-raspolozivosti-sobas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TabelaRaspolozivostiSoba> partialUpdateTabelaRaspolozivostiSoba(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TabelaRaspolozivostiSoba tabelaRaspolozivostiSoba
    ) throws URISyntaxException {
        log.debug("REST request to partial update TabelaRaspolozivostiSoba partially : {}, {}", id, tabelaRaspolozivostiSoba);
        if (tabelaRaspolozivostiSoba.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tabelaRaspolozivostiSoba.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tabelaRaspolozivostiSobaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TabelaRaspolozivostiSoba> result = tabelaRaspolozivostiSobaRepository
            .findById(tabelaRaspolozivostiSoba.getId())
            .map(existingTabelaRaspolozivostiSoba -> {
                if (tabelaRaspolozivostiSoba.getBrojSobe() != null) {
                    existingTabelaRaspolozivostiSoba.setBrojSobe(tabelaRaspolozivostiSoba.getBrojSobe());
                }
                if (tabelaRaspolozivostiSoba.getDatumDolaska() != null) {
                    existingTabelaRaspolozivostiSoba.setDatumDolaska(tabelaRaspolozivostiSoba.getDatumDolaska());
                }
                if (tabelaRaspolozivostiSoba.getDatumOdlaska() != null) {
                    existingTabelaRaspolozivostiSoba.setDatumOdlaska(tabelaRaspolozivostiSoba.getDatumOdlaska());
                }

                return existingTabelaRaspolozivostiSoba;
            })
            .map(tabelaRaspolozivostiSobaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tabelaRaspolozivostiSoba.getId().toString())
        );
    }

    /**
     * {@code GET  /tabela-raspolozivosti-sobas} : get all the tabelaRaspolozivostiSobas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tabelaRaspolozivostiSobas in body.
     */
    @GetMapping("/tabela-raspolozivosti-sobas")
    public List<TabelaRaspolozivostiSoba> getAllTabelaRaspolozivostiSobas() {
        log.debug("REST request to get all TabelaRaspolozivostiSobas");
        return tabelaRaspolozivostiSobaRepository.findAll();
    }

    /**
     * {@code GET  /tabela-raspolozivosti-sobas/:id} : get the "id" tabelaRaspolozivostiSoba.
     *
     * @param id the id of the tabelaRaspolozivostiSoba to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tabelaRaspolozivostiSoba, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tabela-raspolozivosti-sobas/{id}")
    public ResponseEntity<TabelaRaspolozivostiSoba> getTabelaRaspolozivostiSoba(@PathVariable Long id) {
        log.debug("REST request to get TabelaRaspolozivostiSoba : {}", id);
        Optional<TabelaRaspolozivostiSoba> tabelaRaspolozivostiSoba = tabelaRaspolozivostiSobaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tabelaRaspolozivostiSoba);
    }

    /**
     * {@code DELETE  /tabela-raspolozivosti-sobas/:id} : delete the "id" tabelaRaspolozivostiSoba.
     *
     * @param id the id of the tabelaRaspolozivostiSoba to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tabela-raspolozivosti-sobas/{id}")
    public ResponseEntity<Void> deleteTabelaRaspolozivostiSoba(@PathVariable Long id) {
        log.debug("REST request to delete TabelaRaspolozivostiSoba : {}", id);
        tabelaRaspolozivostiSobaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
