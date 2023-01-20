create
    definer = root@localhost procedure rezervacijaDatumDolaskaOdlaska(IN datumDolaska date, IN datumOdlaska date)
BEGIN
    SELECT r.id,r.broj_sobe,r.cijena,r.datum_dolaska,r.datum_odlaska
    FROM sobe_rezervacije r
    WHERE r.broj_sobe
              NOT IN (
              SELECT b.broj_sobe  FROM sobe_rezervacije b
              WHERE NOT (b.datum_odlaska  <
                         datumDolaska
                  OR
                         b.datum_dolaska  >datumOdlaska)

          )
    ORDER BY r.broj_sobe  ;
END;
