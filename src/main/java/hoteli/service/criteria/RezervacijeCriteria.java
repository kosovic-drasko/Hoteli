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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hoteli.domain.Rezervacije} entity. This class is used
 * in {@link hoteli.web.rest.RezervacijeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rezervacijes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class RezervacijeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter brojSobe;

    private LocalDateFilter datumDolaska;

    private LocalDateFilter datumOdlaska;

    private Boolean distinct;

    public RezervacijeCriteria() {}

    public RezervacijeCriteria(RezervacijeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.brojSobe = other.brojSobe == null ? null : other.brojSobe.copy();
        this.datumDolaska = other.datumDolaska == null ? null : other.datumDolaska.copy();
        this.datumOdlaska = other.datumOdlaska == null ? null : other.datumOdlaska.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RezervacijeCriteria copy() {
        return new RezervacijeCriteria(this);
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

    public LocalDateFilter getDatumDolaska() {
        return datumDolaska;
    }

    public LocalDateFilter datumDolaska() {
        if (datumDolaska == null) {
            datumDolaska = new LocalDateFilter();
        }
        return datumDolaska;
    }

    public void setDatumDolaska(LocalDateFilter datumDolaska) {
        this.datumDolaska = datumDolaska;
    }

    public LocalDateFilter getDatumOdlaska() {
        return datumOdlaska;
    }

    public LocalDateFilter datumOdlaska() {
        if (datumOdlaska == null) {
            datumOdlaska = new LocalDateFilter();
        }
        return datumOdlaska;
    }

    public void setDatumOdlaska(LocalDateFilter datumOdlaska) {
        this.datumOdlaska = datumOdlaska;
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
        final RezervacijeCriteria that = (RezervacijeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(brojSobe, that.brojSobe) &&
            Objects.equals(datumDolaska, that.datumDolaska) &&
            Objects.equals(datumOdlaska, that.datumOdlaska) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brojSobe, datumDolaska, datumOdlaska, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RezervacijeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (brojSobe != null ? "brojSobe=" + brojSobe + ", " : "") +
            (datumDolaska != null ? "datumDolaska=" + datumDolaska + ", " : "") +
            (datumOdlaska != null ? "datumOdlaska=" + datumOdlaska + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
