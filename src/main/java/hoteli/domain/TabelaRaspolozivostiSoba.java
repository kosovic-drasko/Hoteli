package hoteli.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TabelaRaspolozivostiSoba.
 */
@Entity
@Table(name = "tabela_raspolozivosti_soba")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TabelaRaspolozivostiSoba implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "broj_sobe", nullable = false, unique = true)
    private Integer brojSobe;

    @Column(name = "datum_dolaska")
    private LocalDate datumDolaska;

    @Column(name = "datum_odlaska")
    private LocalDate datumOdlaska;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TabelaRaspolozivostiSoba id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBrojSobe() {
        return this.brojSobe;
    }

    public TabelaRaspolozivostiSoba brojSobe(Integer brojSobe) {
        this.setBrojSobe(brojSobe);
        return this;
    }

    public void setBrojSobe(Integer brojSobe) {
        this.brojSobe = brojSobe;
    }

    public LocalDate getDatumDolaska() {
        return this.datumDolaska;
    }

    public TabelaRaspolozivostiSoba datumDolaska(LocalDate datumDolaska) {
        this.setDatumDolaska(datumDolaska);
        return this;
    }

    public void setDatumDolaska(LocalDate datumDolaska) {
        this.datumDolaska = datumDolaska;
    }

    public LocalDate getDatumOdlaska() {
        return this.datumOdlaska;
    }

    public TabelaRaspolozivostiSoba datumOdlaska(LocalDate datumOdlaska) {
        this.setDatumOdlaska(datumOdlaska);
        return this;
    }

    public void setDatumOdlaska(LocalDate datumOdlaska) {
        this.datumOdlaska = datumOdlaska;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TabelaRaspolozivostiSoba)) {
            return false;
        }
        return id != null && id.equals(((TabelaRaspolozivostiSoba) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TabelaRaspolozivostiSoba{" +
            "id=" + getId() +
            ", brojSobe=" + getBrojSobe() +
            ", datumDolaska='" + getDatumDolaska() + "'" +
            ", datumOdlaska='" + getDatumOdlaska() + "'" +
            "}";
    }
}
