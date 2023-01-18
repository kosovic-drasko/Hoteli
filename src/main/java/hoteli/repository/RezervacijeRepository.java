package hoteli.repository;

import hoteli.domain.Rezervacije;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rezervacije entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RezervacijeRepository extends JpaRepository<Rezervacije, Long>, JpaSpecificationExecutor<Rezervacije> {}
