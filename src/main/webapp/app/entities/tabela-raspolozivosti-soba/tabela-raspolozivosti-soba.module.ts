import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';

import { TabelaRaspolozivostiSobaDetailComponent } from './detail/tabela-raspolozivosti-soba-detail.component';
import { TabelaRaspolozivostiSobaUpdateComponent } from './update/tabela-raspolozivosti-soba-update.component';
import { TabelaRaspolozivostiSobaDeleteDialogComponent } from './delete/tabela-raspolozivosti-soba-delete-dialog.component';
import { TabelaRaspolozivostiSobaRoutingModule } from './route/tabela-raspolozivosti-soba-routing.module';
import { CalendarCommonModule, CalendarModule, CalendarMonthModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'calendar-utils/date-adapters/date-fns';
import { TabelaRaspolozivostiSobaComponent } from './list/tabela-raspolozivosti-soba.component';

@NgModule({
  imports: [SharedModule, TabelaRaspolozivostiSobaRoutingModule, CalendarCommonModule, CalendarMonthModule],
  declarations: [
    TabelaRaspolozivostiSobaComponent,
    TabelaRaspolozivostiSobaDetailComponent,
    TabelaRaspolozivostiSobaUpdateComponent,
    TabelaRaspolozivostiSobaDeleteDialogComponent,
  ],
  entryComponents: [TabelaRaspolozivostiSobaDeleteDialogComponent],
})
export class TabelaRaspolozivostiSobaModule {}
