import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RezervacijeComponent } from './list/rezervacije.component';
import { RezervacijeDetailComponent } from './detail/rezervacije-detail.component';
import { RezervacijeUpdateComponent } from './update/rezervacije-update.component';
import { RezervacijeDeleteDialogComponent } from './delete/rezervacije-delete-dialog.component';
import { RezervacijeRoutingModule } from './route/rezervacije-routing.module';

@NgModule({
  imports: [SharedModule, RezervacijeRoutingModule],
  declarations: [RezervacijeComponent, RezervacijeDetailComponent, RezervacijeUpdateComponent, RezervacijeDeleteDialogComponent],
  entryComponents: [RezervacijeDeleteDialogComponent],
})
export class RezervacijeModule {}
