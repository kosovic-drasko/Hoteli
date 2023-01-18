import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRezervacije, Rezervacije } from '../rezervacije.model';
import { RezervacijeService } from '../service/rezervacije.service';

@Injectable({ providedIn: 'root' })
export class RezervacijeRoutingResolveService implements Resolve<IRezervacije> {
  constructor(protected service: RezervacijeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRezervacije> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rezervacije: HttpResponse<Rezervacije>) => {
          if (rezervacije.body) {
            return of(rezervacije.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Rezervacije());
  }
}
