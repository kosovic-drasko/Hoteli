package hoteli.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hoteli.domain.Sobe} entity. This class is used
 * in {@link hoteli.web.rest.SobeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sobes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SobeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter brojSobe;

    private DoubleFilter cijena;

    private Boolean distinct;

    public SobeCriteria() {}

    public SobeCriteria(SobeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.brojSobe = other.brojSobe == null ? null : other.brojSobe.copy();
        this.cijena = other.cijena == null ? null : other.cijena.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SobeCriteria copy() {
        return new SobeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getBrojSobe() {
        return brojSobe;
    }

    public IntegerFilter brojSobe() {
        if (brojSobe == null) {
            brojSobe = new IntegerFilter();
        }
        return brojSobe;
    }

    public void setBrojSobe(IntegerFilter brojSobe) {
        this.brojSobe = brojSobe;
    }

    public DoubleFilter getCijena() {
        return cijena;
    }

    public DoubleFilter cijena() {
        if (cijena == null) {
            cijena = new DoubleFilter();
        }
        return cijena;
    }

    public void setCijena(DoubleFilter cijena) {
        this.cijena = cijena;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SobeCriteria that = (SobeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(brojSobe, that.brojSobe) &&
            Objects.equals(cijena, that.cijena) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brojSobe, cijena, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SobeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (brojSobe != null ? "brojSobe=" + brojSobe + ", " : "") +
            (cijena != null ? "cijena=" + cijena + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
