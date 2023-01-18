package hoteli.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sobe.
 */
@Entity
@Table(name = "sobe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sobe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "broj_sobe", nullable = false)
    private Integer brojSobe;

    @NotNull
    @Column(name = "cijena", nullable = false)
    private Double cijena;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sobe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBrojSobe() {
        return this.brojSobe;
    }

    public Sobe brojSobe(Integer brojSobe) {
        this.setBrojSobe(brojSobe);
        return this;
    }

    public void setBrojSobe(Integer brojSobe) {
        this.brojSobe = brojSobe;
    }

    public Double getCijena() {
        return this.cijena;
    }

    public Sobe cijena(Double cijena) {
        this.setCijena(cijena);
        return this;
    }

    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sobe)) {
            return false;
        }
        return id != null && id.equals(((Sobe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sobe{" +
            "id=" + getId() +
            ", brojSobe=" + getBrojSobe() +
            ", cijena=" + getCijena() +
            "}";
    }
}
