import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRezervacije } from '../rezervacije.model';
import { RezervacijeService } from '../service/rezervacije.service';

@Component({
  templateUrl: './rezervacije-delete-dialog.component.html',
})
export class RezervacijeDeleteDialogComponent {
  rezervacije?: IRezervacije;

  constructor(protected rezervacijeService: RezervacijeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rezervacijeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
