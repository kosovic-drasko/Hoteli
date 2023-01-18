package hoteli.service;

import hoteli.domain.Rezervacije;
import hoteli.repository.RezervacijeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rezervacije}.
 */
@Service
@Transactional
public class RezervacijeService {

    private final Logger log = LoggerFactory.getLogger(RezervacijeService.class);

    private final RezervacijeRepository rezervacijeRepository;

    public RezervacijeService(RezervacijeRepository rezervacijeRepository) {
        this.rezervacijeRepository = rezervacijeRepository;
    }

    /**
     * Save a rezervacije.
     *
     * @param rezervacije the entity to save.
     * @return the persisted entity.
     */
    public Rezervacije save(Rezervacije rezervacije) {
        log.debug("Request to save Rezervacije : {}", rezervacije);
        return rezervacijeRepository.save(rezervacije);
    }

    /**
     * Update a rezervacije.
     *
     * @param rezervacije the entity to save.
     * @return the persisted entity.
     */
    public Rezervacije update(Rezervacije rezervacije) {
        log.debug("Request to save Rezervacije : {}", rezervacije);
        return rezervacijeRepository.save(rezervacije);
    }

    /**
     * Partially update a rezervacije.
     *
     * @param rezervacije the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Rezervacije> partialUpdate(Rezervacije rezervacije) {
        log.debug("Request to partially update Rezervacije : {}", rezervacije);

        return rezervacijeRepository
            .findById(rezervacije.getId())
            .map(existingRezervacije -> {
                if (rezervacije.getBrojSobe() != null) {
                    existingRezervacije.setBrojSobe(rezervacije.getBrojSobe());
                }
                if (rezervacije.getDatumDolaska() != null) {
                    existingRezervacije.setDatumDolaska(rezervacije.getDatumDolaska());
                }
                if (rezervacije.getDatumOdlaska() != null) {
                    existingRezervacije.setDatumOdlaska(rezervacije.getDatumOdlaska());
                }

                return existingRezervacije;
            })
            .map(rezervacijeRepository::save);
    }

    /**
     * Get all the rezervacijes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Rezervacije> findAll(Pageable pageable) {
        log.debug("Request to get all Rezervacijes");
        return rezervacijeRepository.findAll(pageable);
    }

    /**
     * Get one rezervacije by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rezervacije> findOne(Long id) {
        log.debug("Request to get Rezervacije : {}", id);
        return rezervacijeRepository.findById(id);
    }

    /**
     * Delete the rezervacije by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rezervacije : {}", id);
        rezervacijeRepository.deleteById(id);
    }
}
