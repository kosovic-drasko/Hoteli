package hoteli.domain;

import static org.assertj.core.api.Assertions.assertThat;

import hoteli.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RezervacijeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rezervacije.class);
        Rezervacije rezervacije1 = new Rezervacije();
        rezervacije1.setId(1L);
        Rezervacije rezervacije2 = new Rezervacije();
        rezervacije2.setId(rezervacije1.getId());
        assertThat(rezervacije1).isEqualTo(rezervacije2);
        rezervacije2.setId(2L);
        assertThat(rezervacije1).isNotEqualTo(rezervacije2);
        rezervacije1.setId(null);
        assertThat(rezervacije1).isNotEqualTo(rezervacije2);
    }
}
