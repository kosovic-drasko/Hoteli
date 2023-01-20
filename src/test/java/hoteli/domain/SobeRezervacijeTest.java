package hoteli.domain;

import static org.assertj.core.api.Assertions.assertThat;

import hoteli.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SobeRezervacijeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SobeRezervacije.class);
        SobeRezervacije sobeRezervacije1 = new SobeRezervacije();
        sobeRezervacije1.setId(1L);
        SobeRezervacije sobeRezervacije2 = new SobeRezervacije();
        sobeRezervacije2.setId(sobeRezervacije1.getId());
        assertThat(sobeRezervacije1).isEqualTo(sobeRezervacije2);
        sobeRezervacije2.setId(2L);
        assertThat(sobeRezervacije1).isNotEqualTo(sobeRezervacije2);
        sobeRezervacije1.setId(null);
        assertThat(sobeRezervacije1).isNotEqualTo(sobeRezervacije2);
    }
}
