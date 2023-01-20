package hoteli.service;

import hoteli.domain.SobeRezervacije;
import hoteli.repository.SobeRezervacijeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SobeRezervacije}.
 */
@Service
@Transactional
public class SobeRezervacijeService {

    private final Logger log = LoggerFactory.getLogger(SobeRezervacijeService.class);

    private final SobeRezervacijeRepository sobeRezervacijeRepository;

    public SobeRezervacijeService(SobeRezervacijeRepository sobeRezervacijeRepository) {
        this.sobeRezervacijeRepository = sobeRezervacijeRepository;
    }

    /**
     * Save a sobeRezervacije.
     *
     * @param sobeRezervacije the entity to save.
     * @return the persisted entity.
     */
    public SobeRezervacije save(SobeRezervacije sobeRezervacije) {
        log.debug("Request to save SobeRezervacije : {}", sobeRezervacije);
        return sobeRezervacijeRepository.save(sobeRezervacije);
    }

    /**
     * Update a sobeRezervacije.
     *
     * @param sobeRezervacije the entity to save.
     * @return the persisted entity.
     */
    public SobeRezervacije update(SobeRezervacije sobeRezervacije) {
        log.debug("Request to save SobeRezervacije : {}", sobeRezervacije);
        return sobeRezervacijeRepository.save(sobeRezervacije);
    }

    /**
     * Partially update a sobeRezervacije.
     *
     * @param sobeRezervacije the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SobeRezervacije> partialUpdate(SobeRezervacije sobeRezervacije) {
        log.debug("Request to partially update SobeRezervacije : {}", sobeRezervacije);

        return sobeRezervacijeRepository
            .findById(sobeRezervacije.getId())
            .map(existingSobeRezervacije -> {
                if (sobeRezervacije.getBrojSobe() != null) {
                    existingSobeRezervacije.setBrojSobe(sobeRezervacije.getBrojSobe());
                }
                if (sobeRezervacije.getCijena() != null) {
                    existingSobeRezervacije.setCijena(sobeRezervacije.getCijena());
                }
                if (sobeRezervacije.getDatumDolaska() != null) {
                    existingSobeRezervacije.setDatumDolaska(sobeRezervacije.getDatumDolaska());
                }
                if (sobeRezervacije.getDatumOdlaska() != null) {
                    existingSobeRezervacije.setDatumOdlaska(sobeRezervacije.getDatumOdlaska());
                }

                return existingSobeRezervacije;
            })
            .map(sobeRezervacijeRepository::save);
    }

    /**
     * Get all the sobeRezervacijes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SobeRezervacije> findAll(Pageable pageable) {
        log.debug("Request to get all SobeRezervacijes");
        return sobeRezervacijeRepository.findAll(pageable);
    }

    /**
     * Get one sobeRezervacije by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SobeRezervacije> findOne(Long id) {
        log.debug("Request to get SobeRezervacije : {}", id);
        return sobeRezervacijeRepository.findById(id);
    }

    /**
     * Delete the sobeRezervacije by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SobeRezervacije : {}", id);
        sobeRezervacijeRepository.deleteById(id);
    }
}
