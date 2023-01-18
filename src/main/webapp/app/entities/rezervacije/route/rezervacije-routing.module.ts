import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RezervacijeComponent } from '../list/rezervacije.component';
import { RezervacijeDetailComponent } from '../detail/rezervacije-detail.component';
import { RezervacijeUpdateComponent } from '../update/rezervacije-update.component';
import { RezervacijeRoutingResolveService } from './rezervacije-routing-resolve.service';

const rezervacijeRoute: Routes = [
  {
    path: '',
    component: RezervacijeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RezervacijeDetailComponent,
    resolve: {
      rezervacije: RezervacijeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RezervacijeUpdateComponent,
    resolve: {
      rezervacije: RezervacijeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RezervacijeUpdateComponent,
    resolve: {
      rezervacije: RezervacijeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rezervacijeRoute)],
  exports: [RouterModule],
})
export class RezervacijeRoutingModule {}
