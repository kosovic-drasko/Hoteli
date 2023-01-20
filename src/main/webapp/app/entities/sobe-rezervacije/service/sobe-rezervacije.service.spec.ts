import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISobeRezervacije } from '../sobe-rezervacije.model';

import { SobeRezervacijeService } from './sobe-rezervacije.service';

describe('SobeRezervacije Service', () => {
  let service: SobeRezervacijeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISobeRezervacije;
  let expectedResult: ISobeRezervacije | ISobeRezervacije[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SobeRezervacijeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      brojSobe: 0,
      cijena: 0,
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

    it('should return a list of SobeRezervacije', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          brojSobe: 1,
          cijena: 1,
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

    describe('addSobeRezervacijeToCollectionIfMissing', () => {
      it('should add a SobeRezervacije to an empty array', () => {
        const sobeRezervacije: ISobeRezervacije = { id: 123 };
        expectedResult = service.addSobeRezervacijeToCollectionIfMissing([], sobeRezervacije);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sobeRezervacije);
      });

      it('should not add a SobeRezervacije to an array that contains it', () => {
        const sobeRezervacije: ISobeRezervacije = { id: 123 };
        const sobeRezervacijeCollection: ISobeRezervacije[] = [
          {
            ...sobeRezervacije,
          },
          { id: 456 },
        ];
        expectedResult = service.addSobeRezervacijeToCollectionIfMissing(sobeRezervacijeCollection, sobeRezervacije);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SobeRezervacije to an array that doesn't contain it", () => {
        const sobeRezervacije: ISobeRezervacije = { id: 123 };
        const sobeRezervacijeCollection: ISobeRezervacije[] = [{ id: 456 }];
        expectedResult = service.addSobeRezervacijeToCollectionIfMissing(sobeRezervacijeCollection, sobeRezervacije);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sobeRezervacije);
      });

      it('should add only unique SobeRezervacije to an array', () => {
        const sobeRezervacijeArray: ISobeRezervacije[] = [{ id: 123 }, { id: 456 }, { id: 16678 }];
        const sobeRezervacijeCollection: ISobeRezervacije[] = [{ id: 123 }];
        expectedResult = service.addSobeRezervacijeToCollectionIfMissing(sobeRezervacijeCollection, ...sobeRezervacijeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sobeRezervacije: ISobeRezervacije = { id: 123 };
        const sobeRezervacije2: ISobeRezervacije = { id: 456 };
        expectedResult = service.addSobeRezervacijeToCollectionIfMissing([], sobeRezervacije, sobeRezervacije2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sobeRezervacije);
        expect(expectedResult).toContain(sobeRezervacije2);
      });

      it('should accept null and undefined values', () => {
        const sobeRezervacije: ISobeRezervacije = { id: 123 };
        expectedResult = service.addSobeRezervacijeToCollectionIfMissing([], null, sobeRezervacije, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sobeRezervacije);
      });

      it('should return initial array if no SobeRezervacije is added', () => {
        const sobeRezervacijeCollection: ISobeRezervacije[] = [{ id: 123 }];
        expectedResult = service.addSobeRezervacijeToCollectionIfMissing(sobeRezervacijeCollection, undefined, null);
        expect(expectedResult).toEqual(sobeRezervacijeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
