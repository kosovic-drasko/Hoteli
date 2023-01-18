import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRezervacije, Rezervacije } from '../rezervacije.model';
import { RezervacijeService } from '../service/rezervacije.service';

import { RezervacijeRoutingResolveService } from './rezervacije-routing-resolve.service';

describe('Rezervacije routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RezervacijeRoutingResolveService;
  let service: RezervacijeService;
  let resultRezervacije: IRezervacije | undefined;

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
    routingResolveService = TestBed.inject(RezervacijeRoutingResolveService);
    service = TestBed.inject(RezervacijeService);
    resultRezervacije = undefined;
  });

  describe('resolve', () => {
    it('should return IRezervacije returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRezervacije = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRezervacije).toEqual({ id: 123 });
    });

    it('should return new IRezervacije if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRezervacije = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRezervacije).toEqual(new Rezervacije());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Rezervacije })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRezervacije = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRezervacije).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
