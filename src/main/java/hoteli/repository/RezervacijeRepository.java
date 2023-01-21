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
    //    @Procedure(procedureName = "rezervacijeDatumDolaskaOdlaska")
    //    List<Rezervacije> findRezervacije(@Param("dolazak") String dolazak, @Param("odolazak") String odolazak);
    //

    @Query(
        value = "" +
        " SELECT r.id,r.broj_sobe,r.datum_dolaska,r.datum_odlaska\n" +
        "    FROM rezervacije r\n" +
        "    WHERE r.broj_sobe\n" +
        "              NOT IN (\n" +
        "              SELECT b.broj_sobe  FROM rezervacije b\n" +
        "              WHERE NOT (b.datum_odlaska  <:dolazak)\n" +
        "                  OR\n" +
        "                         b.datum_dolaska  >:odlazak)\n" +
        "\n" +
        "    ORDER BY r.broj_sobe " +
        "",
        nativeQuery = true
    )
    List<Rezervacije> findRezervacije(@Param("dolazak") String dolazak, @Param("odlazak") String odlazak);
}
