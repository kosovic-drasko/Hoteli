import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'sobe',
        data: { pageTitle: 'hoteliApp.sobe.home.title' },
        loadChildren: () => import('./sobe/sobe.module').then(m => m.SobeModule),
      },
      {
        path: 'rezervacije',
        data: { pageTitle: 'hoteliApp.rezervacije.home.title' },
        loadChildren: () => import('./rezervacije/rezervacije.module').then(m => m.RezervacijeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
