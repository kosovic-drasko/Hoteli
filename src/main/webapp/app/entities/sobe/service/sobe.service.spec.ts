import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISobe, Sobe } from '../sobe.model';

import { SobeService } from './sobe.service';

describe('Sobe Service', () => {
  let service: SobeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISobe;
  let expectedResult: ISobe | ISobe[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SobeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      brojSobe: 0,
      cijena: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Sobe', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Sobe()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sobe', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          brojSobe: 1,
          cijena: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sobe', () => {
      const patchObject = Object.assign(
        {
          brojSobe: 1,
          cijena: 1,
        },
        new Sobe()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sobe', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          brojSobe: 1,
          cijena: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Sobe', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSobeToCollectionIfMissing', () => {
      it('should add a Sobe to an empty array', () => {
        const sobe: ISobe = { id: 123 };
        expectedResult = service.addSobeToCollectionIfMissing([], sobe);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sobe);
      });

      it('should not add a Sobe to an array that contains it', () => {
        const sobe: ISobe = { id: 123 };
        const sobeCollection: ISobe[] = [
          {
            ...sobe,
          },
          { id: 456 },
        ];
        expectedResult = service.addSobeToCollectionIfMissing(sobeCollection, sobe);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sobe to an array that doesn't contain it", () => {
        const sobe: ISobe = { id: 123 };
        const sobeCollection: ISobe[] = [{ id: 456 }];
        expectedResult = service.addSobeToCollectionIfMissing(sobeCollection, sobe);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sobe);
      });

      it('should add only unique Sobe to an array', () => {
        const sobeArray: ISobe[] = [{ id: 123 }, { id: 456 }, { id: 51706 }];
        const sobeCollection: ISobe[] = [{ id: 123 }];
        expectedResult = service.addSobeToCollectionIfMissing(sobeCollection, ...sobeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sobe: ISobe = { id: 123 };
        const sobe2: ISobe = { id: 456 };
        expectedResult = service.addSobeToCollectionIfMissing([], sobe, sobe2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sobe);
        expect(expectedResult).toContain(sobe2);
      });

      it('should accept null and undefined values', () => {
        const sobe: ISobe = { id: 123 };
        expectedResult = service.addSobeToCollectionIfMissing([], null, sobe, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sobe);
      });

      it('should return initial array if no Sobe is added', () => {
        const sobeCollection: ISobe[] = [{ id: 123 }];
        expectedResult = service.addSobeToCollectionIfMissing(sobeCollection, undefined, null);
        expect(expectedResult).toEqual(sobeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
