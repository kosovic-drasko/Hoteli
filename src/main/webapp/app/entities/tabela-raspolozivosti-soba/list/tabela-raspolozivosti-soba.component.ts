import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITabelaRaspolozivostiSoba } from '../tabela-raspolozivosti-soba.model';
import { TabelaRaspolozivostiSobaService } from '../service/tabela-raspolozivosti-soba.service';
import { TabelaRaspolozivostiSobaDeleteDialogComponent } from '../delete/tabela-raspolozivosti-soba-delete-dialog.component';

@Component({
  selector: 'jhi-tabela-raspolozivosti-soba',
  templateUrl: './tabela-raspolozivosti-soba.component.html',
})
export class TabelaRaspolozivostiSobaComponent implements OnInit {
  tabelaRaspolozivostiSobas?: ITabelaRaspolozivostiSoba[];
  isLoading = false;

  constructor(protected tabelaRaspolozivostiSobaService: TabelaRaspolozivostiSobaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tabelaRaspolozivostiSobaService.query().subscribe({
      next: (res: HttpResponse<ITabelaRaspolozivostiSoba[]>) => {
        this.isLoading = false;
        this.tabelaRaspolozivostiSobas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITabelaRaspolozivostiSoba): number {
    return item.id!;
  }

  delete(tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba): void {
    const modalRef = this.modalService.open(TabelaRaspolozivostiSobaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tabelaRaspolozivostiSoba = tabelaRaspolozivostiSoba;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
