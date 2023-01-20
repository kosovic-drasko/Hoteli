package hoteli.repository;

import hoteli.domain.SobeRezervacije;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SobeRezervacije entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SobeRezervacijeRepository extends JpaRepository<SobeRezervacije, Long>, JpaSpecificationExecutor<SobeRezervacije> {}
