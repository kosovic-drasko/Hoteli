import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITabelaRaspolozivostiSoba, TabelaRaspolozivostiSoba } from '../tabela-raspolozivosti-soba.model';

import { TabelaRaspolozivostiSobaService } from './tabela-raspolozivosti-soba.service';

describe('TabelaRaspolozivostiSoba Service', () => {
  let service: TabelaRaspolozivostiSobaService;
  let httpMock: HttpTestingController;
  let elemDefault: ITabelaRaspolozivostiSoba;
  let expectedResult: ITabelaRaspolozivostiSoba | ITabelaRaspolozivostiSoba[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TabelaRaspolozivostiSobaService);
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

    it('should create a TabelaRaspolozivostiSoba', () => {
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

      service.create(new TabelaRaspolozivostiSoba()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TabelaRaspolozivostiSoba', () => {
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

    it('should partial update a TabelaRaspolozivostiSoba', () => {
      const patchObject = Object.assign(
        {
          brojSobe: 1,
          datumDolaska: currentDate.format(DATE_FORMAT),
          datumOdlaska: currentDate.format(DATE_FORMAT),
        },
        new TabelaRaspolozivostiSoba()
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

    it('should return a list of TabelaRaspolozivostiSoba', () => {
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

    it('should delete a TabelaRaspolozivostiSoba', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTabelaRaspolozivostiSobaToCollectionIfMissing', () => {
      it('should add a TabelaRaspolozivostiSoba to an empty array', () => {
        const tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba = { id: 123 };
        expectedResult = service.addTabelaRaspolozivostiSobaToCollectionIfMissing([], tabelaRaspolozivostiSoba);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tabelaRaspolozivostiSoba);
      });

      it('should not add a TabelaRaspolozivostiSoba to an array that contains it', () => {
        const tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba = { id: 123 };
        const tabelaRaspolozivostiSobaCollection: ITabelaRaspolozivostiSoba[] = [
          {
            ...tabelaRaspolozivostiSoba,
          },
          { id: 456 },
        ];
        expectedResult = service.addTabelaRaspolozivostiSobaToCollectionIfMissing(
          tabelaRaspolozivostiSobaCollection,
          tabelaRaspolozivostiSoba
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TabelaRaspolozivostiSoba to an array that doesn't contain it", () => {
        const tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba = { id: 123 };
        const tabelaRaspolozivostiSobaCollection: ITabelaRaspolozivostiSoba[] = [{ id: 456 }];
        expectedResult = service.addTabelaRaspolozivostiSobaToCollectionIfMissing(
          tabelaRaspolozivostiSobaCollection,
          tabelaRaspolozivostiSoba
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tabelaRaspolozivostiSoba);
      });

      it('should add only unique TabelaRaspolozivostiSoba to an array', () => {
        const tabelaRaspolozivostiSobaArray: ITabelaRaspolozivostiSoba[] = [{ id: 123 }, { id: 456 }, { id: 41055 }];
        const tabelaRaspolozivostiSobaCollection: ITabelaRaspolozivostiSoba[] = [{ id: 123 }];
        expectedResult = service.addTabelaRaspolozivostiSobaToCollectionIfMissing(
          tabelaRaspolozivostiSobaCollection,
          ...tabelaRaspolozivostiSobaArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba = { id: 123 };
        const tabelaRaspolozivostiSoba2: ITabelaRaspolozivostiSoba = { id: 456 };
        expectedResult = service.addTabelaRaspolozivostiSobaToCollectionIfMissing([], tabelaRaspolozivostiSoba, tabelaRaspolozivostiSoba2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tabelaRaspolozivostiSoba);
        expect(expectedResult).toContain(tabelaRaspolozivostiSoba2);
      });

      it('should accept null and undefined values', () => {
        const tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba = { id: 123 };
        expectedResult = service.addTabelaRaspolozivostiSobaToCollectionIfMissing([], null, tabelaRaspolozivostiSoba, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tabelaRaspolozivostiSoba);
      });

      it('should return initial array if no TabelaRaspolozivostiSoba is added', () => {
        const tabelaRaspolozivostiSobaCollection: ITabelaRaspolozivostiSoba[] = [{ id: 123 }];
        expectedResult = service.addTabelaRaspolozivostiSobaToCollectionIfMissing(tabelaRaspolozivostiSobaCollection, undefined, null);
        expect(expectedResult).toEqual(tabelaRaspolozivostiSobaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
