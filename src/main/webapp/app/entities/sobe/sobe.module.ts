import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SobeComponent } from './list/sobe.component';
import { SobeDetailComponent } from './detail/sobe-detail.component';
import { SobeUpdateComponent } from './update/sobe-update.component';
import { SobeDeleteDialogComponent } from './delete/sobe-delete-dialog.component';
import { SobeRoutingModule } from './route/sobe-routing.module';

@NgModule({
  imports: [SharedModule, SobeRoutingModule],
  declarations: [SobeComponent, SobeDetailComponent, SobeUpdateComponent, SobeDeleteDialogComponent],
  entryComponents: [SobeDeleteDialogComponent],
})
export class SobeModule {}
