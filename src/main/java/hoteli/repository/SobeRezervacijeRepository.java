package hoteli.repository;

import hoteli.domain.Rezervacije;
import hoteli.domain.SobeRezervacije;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SobeRezervacije entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SobeRezervacijeRepository extends JpaRepository<SobeRezervacije, Long>, JpaSpecificationExecutor<SobeRezervacije> {
    @Procedure(procedureName = "rezervacijaDatumDolaskaOdlaska")
    List<SobeRezervacije> findRezervacije(@Param("dolazak") String dolazak, @Param("odolazak") String odolazak);
}
