package hoteli.repository;

import hoteli.domain.TabelaRaspolozivostiSoba;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TabelaRaspolozivostiSoba entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TabelaRaspolozivostiSobaRepository extends JpaRepository<TabelaRaspolozivostiSoba, Long> {}
