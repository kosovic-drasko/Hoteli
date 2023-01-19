package hoteli.repository;

import hoteli.domain.Rezervacije;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rezervacije entity.
 */
@SuppressWarnings("unused")
@Transactional
@Repository
public interface RezervacijeRepository extends JpaRepository<Rezervacije, Long>, JpaSpecificationExecutor<Rezervacije> {
    @Procedure(procedureName = "rezervacijeDatumDolaskaOdlaska")
    List<Rezervacije> findRezervacije(@Param("dolazak") String dolazak, @Param("odolazak") String odolazak);
}
