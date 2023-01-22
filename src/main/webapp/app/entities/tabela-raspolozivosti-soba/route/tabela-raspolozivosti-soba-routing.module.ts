import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TabelaRaspolozivostiSobaComponent } from '../list/tabela-raspolozivosti-soba.component';
import { TabelaRaspolozivostiSobaDetailComponent } from '../detail/tabela-raspolozivosti-soba-detail.component';
import { TabelaRaspolozivostiSobaUpdateComponent } from '../update/tabela-raspolozivosti-soba-update.component';
import { TabelaRaspolozivostiSobaRoutingResolveService } from './tabela-raspolozivosti-soba-routing-resolve.service';

const tabelaRaspolozivostiSobaRoute: Routes = [
  {
    path: '',
    component: TabelaRaspolozivostiSobaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TabelaRaspolozivostiSobaDetailComponent,
    resolve: {
      tabelaRaspolozivostiSoba: TabelaRaspolozivostiSobaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TabelaRaspolozivostiSobaUpdateComponent,
    resolve: {
      tabelaRaspolozivostiSoba: TabelaRaspolozivostiSobaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TabelaRaspolozivostiSobaUpdateComponent,
    resolve: {
      tabelaRaspolozivostiSoba: TabelaRaspolozivostiSobaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tabelaRaspolozivostiSobaRoute)],
  exports: [RouterModule],
})
export class TabelaRaspolozivostiSobaRoutingModule {}
