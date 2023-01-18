import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISobe } from '../sobe.model';
import { SobeService } from '../service/sobe.service';

@Component({
  templateUrl: './sobe-delete-dialog.component.html',
})
export class SobeDeleteDialogComponent {
  sobe?: ISobe;

  constructor(protected sobeService: SobeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sobeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
