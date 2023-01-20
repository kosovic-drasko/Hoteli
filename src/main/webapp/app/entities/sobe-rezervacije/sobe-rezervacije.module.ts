import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SobeRezervacijeComponent } from './list/sobe-rezervacije.component';
import { SobeRezervacijeDetailComponent } from './detail/sobe-rezervacije-detail.component';
import { SobeRezervacijeRoutingModule } from './route/sobe-rezervacije-routing.module';

@NgModule({
  imports: [SharedModule, SobeRezervacijeRoutingModule],
  declarations: [SobeRezervacijeComponent, SobeRezervacijeDetailComponent],
})
export class SobeRezervacijeModule {}
