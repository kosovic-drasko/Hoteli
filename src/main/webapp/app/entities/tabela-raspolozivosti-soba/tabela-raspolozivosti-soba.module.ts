import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TabelaRaspolozivostiSobaComponent } from './list/tabela-raspolozivosti-soba.component';
import { TabelaRaspolozivostiSobaDetailComponent } from './detail/tabela-raspolozivosti-soba-detail.component';
import { TabelaRaspolozivostiSobaUpdateComponent } from './update/tabela-raspolozivosti-soba-update.component';
import { TabelaRaspolozivostiSobaDeleteDialogComponent } from './delete/tabela-raspolozivosti-soba-delete-dialog.component';
import { TabelaRaspolozivostiSobaRoutingModule } from './route/tabela-raspolozivosti-soba-routing.module';

@NgModule({
  imports: [SharedModule, TabelaRaspolozivostiSobaRoutingModule],
  declarations: [
    TabelaRaspolozivostiSobaComponent,
    TabelaRaspolozivostiSobaDetailComponent,
    TabelaRaspolozivostiSobaUpdateComponent,
    TabelaRaspolozivostiSobaDeleteDialogComponent,
  ],
  entryComponents: [TabelaRaspolozivostiSobaDeleteDialogComponent],
})
export class TabelaRaspolozivostiSobaModule {}
