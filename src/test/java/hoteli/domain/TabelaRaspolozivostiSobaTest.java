package hoteli.domain;

import static org.assertj.core.api.Assertions.assertThat;

import hoteli.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TabelaRaspolozivostiSobaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TabelaRaspolozivostiSoba.class);
        TabelaRaspolozivostiSoba tabelaRaspolozivostiSoba1 = new TabelaRaspolozivostiSoba();
        tabelaRaspolozivostiSoba1.setId(1L);
        TabelaRaspolozivostiSoba tabelaRaspolozivostiSoba2 = new TabelaRaspolozivostiSoba();
        tabelaRaspolozivostiSoba2.setId(tabelaRaspolozivostiSoba1.getId());
        assertThat(tabelaRaspolozivostiSoba1).isEqualTo(tabelaRaspolozivostiSoba2);
        tabelaRaspolozivostiSoba2.setId(2L);
        assertThat(tabelaRaspolozivostiSoba1).isNotEqualTo(tabelaRaspolozivostiSoba2);
        tabelaRaspolozivostiSoba1.setId(null);
        assertThat(tabelaRaspolozivostiSoba1).isNotEqualTo(tabelaRaspolozivostiSoba2);
    }
}
