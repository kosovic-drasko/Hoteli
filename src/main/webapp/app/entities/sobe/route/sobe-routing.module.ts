import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SobeComponent } from '../list/sobe.component';
import { SobeDetailComponent } from '../detail/sobe-detail.component';
import { SobeUpdateComponent } from '../update/sobe-update.component';
import { SobeRoutingResolveService } from './sobe-routing-resolve.service';

const sobeRoute: Routes = [
  {
    path: '',
    component: SobeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SobeDetailComponent,
    resolve: {
      sobe: SobeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SobeUpdateComponent,
    resolve: {
      sobe: SobeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SobeUpdateComponent,
    resolve: {
      sobe: SobeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sobeRoute)],
  exports: [RouterModule],
})
export class SobeRoutingModule {}
