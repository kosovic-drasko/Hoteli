package hoteli.domain;

import static org.assertj.core.api.Assertions.assertThat;

import hoteli.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SobeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sobe.class);
        Sobe sobe1 = new Sobe();
        sobe1.setId(1L);
        Sobe sobe2 = new Sobe();
        sobe2.setId(sobe1.getId());
        assertThat(sobe1).isEqualTo(sobe2);
        sobe2.setId(2L);
        assertThat(sobe1).isNotEqualTo(sobe2);
        sobe1.setId(null);
        assertThat(sobe1).isNotEqualTo(sobe2);
    }
}
