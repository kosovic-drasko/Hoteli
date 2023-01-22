import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITabelaRaspolozivostiSoba } from '../tabela-raspolozivosti-soba.model';
import { TabelaRaspolozivostiSobaService } from '../service/tabela-raspolozivosti-soba.service';

@Component({
  templateUrl: './tabela-raspolozivosti-soba-delete-dialog.component.html',
})
export class TabelaRaspolozivostiSobaDeleteDialogComponent {
  tabelaRaspolozivostiSoba?: ITabelaRaspolozivostiSoba;

  constructor(protected tabelaRaspolozivostiSobaService: TabelaRaspolozivostiSobaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tabelaRaspolozivostiSobaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
