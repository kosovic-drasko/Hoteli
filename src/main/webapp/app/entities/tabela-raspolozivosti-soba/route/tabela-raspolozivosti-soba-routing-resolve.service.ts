import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITabelaRaspolozivostiSoba, TabelaRaspolozivostiSoba } from '../tabela-raspolozivosti-soba.model';
import { TabelaRaspolozivostiSobaService } from '../service/tabela-raspolozivosti-soba.service';

@Injectable({ providedIn: 'root' })
export class TabelaRaspolozivostiSobaRoutingResolveService implements Resolve<ITabelaRaspolozivostiSoba> {
  constructor(protected service: TabelaRaspolozivostiSobaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITabelaRaspolozivostiSoba> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tabelaRaspolozivostiSoba: HttpResponse<TabelaRaspolozivostiSoba>) => {
          if (tabelaRaspolozivostiSoba.body) {
            return of(tabelaRaspolozivostiSoba.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TabelaRaspolozivostiSoba());
  }
}
