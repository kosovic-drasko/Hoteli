package hoteli.service;

import hoteli.domain.Sobe;
import hoteli.repository.SobeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sobe}.
 */
@Service
@Transactional
public class SobeService {

    private final Logger log = LoggerFactory.getLogger(SobeService.class);

    private final SobeRepository sobeRepository;

    public SobeService(SobeRepository sobeRepository) {
        this.sobeRepository = sobeRepository;
    }

    /**
     * Save a sobe.
     *
     * @param sobe the entity to save.
     * @return the persisted entity.
     */
    public Sobe save(Sobe sobe) {
        log.debug("Request to save Sobe : {}", sobe);
        return sobeRepository.save(sobe);
    }

    /**
     * Update a sobe.
     *
     * @param sobe the entity to save.
     * @return the persisted entity.
     */
    public Sobe update(Sobe sobe) {
        log.debug("Request to save Sobe : {}", sobe);
        return sobeRepository.save(sobe);
    }

    /**
     * Partially update a sobe.
     *
     * @param sobe the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Sobe> partialUpdate(Sobe sobe) {
        log.debug("Request to partially update Sobe : {}", sobe);

        return sobeRepository
            .findById(sobe.getId())
            .map(existingSobe -> {
                if (sobe.getBrojSobe() != null) {
                    existingSobe.setBrojSobe(sobe.getBrojSobe());
                }
                if (sobe.getCijena() != null) {
                    existingSobe.setCijena(sobe.getCijena());
                }

                return existingSobe;
            })
            .map(sobeRepository::save);
    }

    /**
     * Get all the sobes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Sobe> findAll(Pageable pageable) {
        log.debug("Request to get all Sobes");
        return sobeRepository.findAll(pageable);
    }

    /**
     * Get one sobe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Sobe> findOne(Long id) {
        log.debug("Request to get Sobe : {}", id);
        return sobeRepository.findById(id);
    }

    /**
     * Delete the sobe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sobe : {}", id);
        sobeRepository.deleteById(id);
    }
}
