import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SobeRezervacijeComponent } from '../list/sobe-rezervacije.component';
import { SobeRezervacijeDetailComponent } from '../detail/sobe-rezervacije-detail.component';
import { SobeRezervacijeRoutingResolveService } from './sobe-rezervacije-routing-resolve.service';

const sobeRezervacijeRoute: Routes = [
  {
    path: '',
    component: SobeRezervacijeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SobeRezervacijeDetailComponent,
    resolve: {
      sobeRezervacije: SobeRezervacijeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sobeRezervacijeRoute)],
  exports: [RouterModule],
})
export class SobeRezervacijeRoutingModule {}
