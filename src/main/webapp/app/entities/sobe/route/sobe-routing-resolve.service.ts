import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISobe, Sobe } from '../sobe.model';
import { SobeService } from '../service/sobe.service';

@Injectable({ providedIn: 'root' })
export class SobeRoutingResolveService implements Resolve<ISobe> {
  constructor(protected service: SobeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISobe> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sobe: HttpResponse<Sobe>) => {
          if (sobe.body) {
            return of(sobe.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sobe());
  }
}
