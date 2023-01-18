import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRezervacije, Rezervacije } from '../rezervacije.model';

import { RezervacijeService } from './rezervacije.service';

describe('Rezervacije Service', () => {
  let service: RezervacijeService;
  let httpMock: HttpTestingController;
  let elemDefault: IRezervacije;
  let expectedResult: IRezervacije | IRezervacije[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RezervacijeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      brojSobe: 0,
      datumDolaska: currentDate,
      datumOdlaska: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          datumDolaska: currentDate.format(DATE_FORMAT),
          datumOdlaska: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Rezervacije', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          datumDolaska: currentDate.format(DATE_FORMAT),
          datumOdlaska: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumDolaska: currentDate,
          datumOdlaska: currentDate,
        },
        returnedFromService
      );

      service.create(new Rezervacije()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rezervacije', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          brojSobe: 1,
          datumDolaska: currentDate.format(DATE_FORMAT),
          datumOdlaska: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumDolaska: currentDate,
          datumOdlaska: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Rezervacije', () => {
      const patchObject = Object.assign(
        {
          brojSobe: 1,
          datumDolaska: currentDate.format(DATE_FORMAT),
        },
        new Rezervacije()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          datumDolaska: currentDate,
          datumOdlaska: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Rezervacije', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          brojSobe: 1,
          datumDolaska: currentDate.format(DATE_FORMAT),
          datumOdlaska: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datumDolaska: currentDate,
          datumOdlaska: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Rezervacije', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRezervacijeToCollectionIfMissing', () => {
      it('should add a Rezervacije to an empty array', () => {
        const rezervacije: IRezervacije = { id: 123 };
        expectedResult = service.addRezervacijeToCollectionIfMissing([], rezervacije);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rezervacije);
      });

      it('should not add a Rezervacije to an array that contains it', () => {
        const rezervacije: IRezervacije = { id: 123 };
        const rezervacijeCollection: IRezervacije[] = [
          {
            ...rezervacije,
          },
          { id: 456 },
        ];
        expectedResult = service.addRezervacijeToCollectionIfMissing(rezervacijeCollection, rezervacije);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rezervacije to an array that doesn't contain it", () => {
        const rezervacije: IRezervacije = { id: 123 };
        const rezervacijeCollection: IRezervacije[] = [{ id: 456 }];
        expectedResult = service.addRezervacijeToCollectionIfMissing(rezervacijeCollection, rezervacije);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rezervacije);
      });

      it('should add only unique Rezervacije to an array', () => {
        const rezervacijeArray: IRezervacije[] = [{ id: 123 }, { id: 456 }, { id: 56602 }];
        const rezervacijeCollection: IRezervacije[] = [{ id: 123 }];
        expectedResult = service.addRezervacijeToCollectionIfMissing(rezervacijeCollection, ...rezervacijeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rezervacije: IRezervacije = { id: 123 };
        const rezervacije2: IRezervacije = { id: 456 };
        expectedResult = service.addRezervacijeToCollectionIfMissing([], rezervacije, rezervacije2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rezervacije);
        expect(expectedResult).toContain(rezervacije2);
      });

      it('should accept null and undefined values', () => {
        const rezervacije: IRezervacije = { id: 123 };
        expectedResult = service.addRezervacijeToCollectionIfMissing([], null, rezervacije, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rezervacije);
      });

      it('should return initial array if no Rezervacije is added', () => {
        const rezervacijeCollection: IRezervacije[] = [{ id: 123 }];
        expectedResult = service.addRezervacijeToCollectionIfMissing(rezervacijeCollection, undefined, null);
        expect(expectedResult).toEqual(rezervacijeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
