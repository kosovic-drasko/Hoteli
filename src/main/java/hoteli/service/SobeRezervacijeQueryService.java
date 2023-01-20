package hoteli.service;

import hoteli.domain.*; // for static metamodels
import hoteli.domain.SobeRezervacije;
import hoteli.repository.SobeRezervacijeRepository;
import hoteli.service.criteria.SobeRezervacijeCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SobeRezervacije} entities in the database.
 * The main input is a {@link SobeRezervacijeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SobeRezervacije} or a {@link Page} of {@link SobeRezervacije} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SobeRezervacijeQueryService extends QueryService<SobeRezervacije> {

    private final Logger log = LoggerFactory.getLogger(SobeRezervacijeQueryService.class);

    private final SobeRezervacijeRepository sobeRezervacijeRepository;

    public SobeRezervacijeQueryService(SobeRezervacijeRepository sobeRezervacijeRepository) {
        this.sobeRezervacijeRepository = sobeRezervacijeRepository;
    }

    /**
     * Return a {@link List} of {@link SobeRezervacije} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SobeRezervacije> findByCriteria(SobeRezervacijeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SobeRezervacije> specification = createSpecification(criteria);
        return sobeRezervacijeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SobeRezervacije} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SobeRezervacije> findByCriteria(SobeRezervacijeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SobeRezervacije> specification = createSpecification(criteria);
        return sobeRezervacijeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SobeRezervacijeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SobeRezervacije> specification = createSpecification(criteria);
        return sobeRezervacijeRepository.count(specification);
    }

    /**
     * Function to convert {@link SobeRezervacijeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SobeRezervacije> createSpecification(SobeRezervacijeCriteria criteria) {
        Specification<SobeRezervacije> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SobeRezervacije_.id));
            }
            if (criteria.getBrojSobe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBrojSobe(), SobeRezervacije_.brojSobe));
            }
            if (criteria.getCijena() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCijena(), SobeRezervacije_.cijena));
            }
            if (criteria.getDatumDolaska() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatumDolaska(), SobeRezervacije_.datumDolaska));
            }
            if (criteria.getDatumOdlaska() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatumOdlaska(), SobeRezervacije_.datumOdlaska));
            }
        }
        return specification;
    }
}
