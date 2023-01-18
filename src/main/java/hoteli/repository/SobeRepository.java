package hoteli.repository;

import hoteli.domain.Sobe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sobe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SobeRepository extends JpaRepository<Sobe, Long>, JpaSpecificationExecutor<Sobe> {}
