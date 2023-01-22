import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITabelaRaspolozivostiSoba } from '../tabela-raspolozivosti-soba.model';

@Component({
  selector: 'jhi-tabela-raspolozivosti-soba-detail',
  templateUrl: './tabela-raspolozivosti-soba-detail.component.html',
})
export class TabelaRaspolozivostiSobaDetailComponent implements OnInit {
  tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tabelaRaspolozivostiSoba }) => {
      this.tabelaRaspolozivostiSoba = tabelaRaspolozivostiSoba;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
