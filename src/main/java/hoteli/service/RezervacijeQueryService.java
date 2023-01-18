package hoteli.service;

import hoteli.domain.*; // for static metamodels
import hoteli.domain.Rezervacije;
import hoteli.repository.RezervacijeRepository;
import hoteli.service.criteria.RezervacijeCriteria;
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
 * Service for executing complex queries for {@link Rezervacije} entities in the database.
 * The main input is a {@link RezervacijeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Rezervacije} or a {@link Page} of {@link Rezervacije} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RezervacijeQueryService extends QueryService<Rezervacije> {

    private final Logger log = LoggerFactory.getLogger(RezervacijeQueryService.class);

    private final RezervacijeRepository rezervacijeRepository;

    public RezervacijeQueryService(RezervacijeRepository rezervacijeRepository) {
        this.rezervacijeRepository = rezervacijeRepository;
    }

    /**
     * Return a {@link List} of {@link Rezervacije} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Rezervacije> findByCriteria(RezervacijeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rezervacije> specification = createSpecification(criteria);
        return rezervacijeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Rezervacije} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Rezervacije> findByCriteria(RezervacijeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rezervacije> specification = createSpecification(criteria);
        return rezervacijeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RezervacijeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rezervacije> specification = createSpecification(criteria);
        return rezervacijeRepository.count(specification);
    }

    /**
     * Function to convert {@link RezervacijeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rezervacije> createSpecification(RezervacijeCriteria criteria) {
        Specification<Rezervacije> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rezervacije_.id));
            }
            if (criteria.getBrojSobe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBrojSobe(), Rezervacije_.brojSobe));
            }
            if (criteria.getDatumDolaska() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatumDolaska(), Rezervacije_.datumDolaska));
            }
            if (criteria.getDatumOdlaska() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatumOdlaska(), Rezervacije_.datumOdlaska));
            }
        }
        return specification;
    }
}
