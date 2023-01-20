import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISobeRezervacije, SobeRezervacije } from '../sobe-rezervacije.model';
import { SobeRezervacijeService } from '../service/sobe-rezervacije.service';

@Injectable({ providedIn: 'root' })
export class SobeRezervacijeRoutingResolveService implements Resolve<ISobeRezervacije> {
  constructor(protected service: SobeRezervacijeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISobeRezervacije> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sobeRezervacije: HttpResponse<SobeRezervacije>) => {
          if (sobeRezervacije.body) {
            return of(sobeRezervacije.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SobeRezervacije());
  }
}
