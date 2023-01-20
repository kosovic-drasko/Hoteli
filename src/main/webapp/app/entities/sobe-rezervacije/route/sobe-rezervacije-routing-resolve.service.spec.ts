import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISobeRezervacije, SobeRezervacije } from '../sobe-rezervacije.model';
import { SobeRezervacijeService } from '../service/sobe-rezervacije.service';

import { SobeRezervacijeRoutingResolveService } from './sobe-rezervacije-routing-resolve.service';

describe('SobeRezervacije routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SobeRezervacijeRoutingResolveService;
  let service: SobeRezervacijeService;
  let resultSobeRezervacije: ISobeRezervacije | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(SobeRezervacijeRoutingResolveService);
    service = TestBed.inject(SobeRezervacijeService);
    resultSobeRezervacije = undefined;
  });

  describe('resolve', () => {
    it('should return ISobeRezervacije returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSobeRezervacije = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSobeRezervacije).toEqual({ id: 123 });
    });

    it('should return new ISobeRezervacije if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSobeRezervacije = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSobeRezervacije).toEqual(new SobeRezervacije());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SobeRezervacije })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSobeRezervacije = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSobeRezervacije).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
